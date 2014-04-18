package com.jugaad.glasstodo.auidiorecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class AudioRecorder {

	private static final int RECORDER_SAMPLERATE = 8000;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private static final int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
	private static final int BytesPerElement = 2; // 2 bytes in 16bit format
	
	private AudioRecord recorder = null;
	private Thread recordingThread = null;
	private boolean isRecording = false;
	
	private final String FILE_PATH;

	public AudioRecorder() {		
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
				RECORDER_SAMPLERATE, RECORDER_CHANNELS,
				RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);
		
		FILE_PATH = "/sdcard/voice8K16bitmono.pcm";
	}

	public void startRecording() {
		recorder.startRecording();
		isRecording = true;
		
		recordingThread = new Thread(new Runnable() {
			public void run() {
				writeAudioDataToFile();
			}
		}, "AudioRecorder Thread");
		recordingThread.start();
	}
	
	public File stopRecordingAndgetFile() {
		if (null != recorder) {
			isRecording = false;
			recorder.stop();
			recorder.release();
			recorder = null;
			recordingThread = null;
		}
		
		return new File(FILE_PATH);
	}
	
	private void writeAudioDataToFile() {
		
		short sData[] = new short[BufferElements2Rec];

		FileOutputStream os = null;
		try {
			os = new FileOutputStream(FILE_PATH);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (isRecording) {
			recorder.read(sData, 0, BufferElements2Rec);
			System.out.println("Short wirting to file" + sData.toString());
			try {
				byte bData[] = short2byte(sData);
				os.write(bData, 0, BufferElements2Rec * BytesPerElement);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] short2byte(short[] sData) {
		int shortArrsize = sData.length;
		byte[] bytes = new byte[shortArrsize * 2];
		for (int i = 0; i < shortArrsize; i++) {
			bytes[i * 2] = (byte) (sData[i] & 0x00FF);
			bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
			sData[i] = 0;
		}
		return bytes;
	}

	public boolean isRecording() {
		return this.isRecording;
	}	
}
