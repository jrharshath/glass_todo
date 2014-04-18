package com.example.glasstodo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.File;
import java.util.ArrayList;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class DatabaseActivity extends Activity {
	public final static String TASK_LIST = "com.example.myfirstapp.TASK_LIST";
	protected static final int RESULT_SPEECH = 1;
	static final int SAMPLE_RATE = 8000;
	public final static String AUDIO_RECORDER_FOLDER = "RecordedAudio";
	private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".txt";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
	
	TextView idView;
	EditText taskDescription;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
		
        idView = (TextView) findViewById(R.id.taskID);
        taskDescription = (EditText) findViewById(R.id.task_description);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void textOrRecording(View view){
		char connected = 5;
		if (connected==1){
			newOnlineTaskItem(view);
		}else if (connected==0){
			//do recording
			//create content service
			//store recording in content service
			//create db entry using URI of stored item 
		}else{
			newPhoneTaskItem(view);
		}
	}
	
	public void newPhoneTaskItem (View view) {
	 	   MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
	 	   TaskItem item = new TaskItem(taskDescription.getText().toString());
	 	   String fname = this.getFilename();
	 	   item.setTaskrecording(fname);
	 	   dbHandler.addTaskItem(item);
	 	   this.saveFile(view, fname);
	 	   taskDescription.setText("");
	}
	
	public void viewSaved(View view){
		//File directory = Environment.getExternalStorageDirectory();
		  // assumes that a file article.rss is available on the SD card
	 	  MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
	      TaskItem task = dbHandler.findTaskItem(Integer.parseInt(idView.getText().toString()));  
		  String fileName = task.getTaskrecodingString(); 
		  System.out.println("Testing Starting to read");
		  try {
			  FileInputStream fis = openFileInput(fileName);
			  StringBuilder builder = new StringBuilder();
			  int content;
			  char item;
			  while ((content = fis.read()) != -1) {
				  item = (char) content; 
				  builder.append(item);
			  }
			  String line = builder.toString();
			  Toast t = Toast.makeText(getApplicationContext(),line,Toast.LENGTH_SHORT);
              t.show();
              fis.close();
		  }catch (Exception e) {
		    e.printStackTrace();
		  }
	}
	
	private void saveFile(View view, String fileName){

		  String eol = System.getProperty("line.separator");
		  BufferedWriter writer = null;
		  //String fileName = this.getFilename();
		  try {
		    writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE)));
		    writer.write(taskDescription.getText().toString() + eol);
		  } catch (Exception e) {
		      e.printStackTrace();
		  } finally {
		    if (writer != null) {
		    	try {
		    		writer.close();
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    	}
		    }
		  }
		
	}
	
	private String getFilename(){
        //String filepath = Environment.getExternalStorageDirectory().getPath();
        //File file = new File(filepath,AUDIO_RECORDER_FOLDER);
        
        //if(!file.exists()){
        //        file.mkdirs();
        //}
        return ("msg_" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_WAV);
        //return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_WAV);
	}
	
	public void showToDo (View view){
    	  MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
    	  ArrayList<TaskItem> taskList = dbHandler.showSimpleTasks();
    	  ArrayList<String> stringTaskList = new ArrayList<String> ();
    	  for (TaskItem e : taskList){
    		  String temp = e.getID() + "|" + e.getTasktext();
    		  stringTaskList.add(temp);
    	  }
    	   
    	  
    	  Intent intent = new Intent(this, DisplayTaskListActivity.class);
  		  intent.putStringArrayListExtra(TASK_LIST, stringTaskList);
  		  //intent.putExtra(PROD_LIST, productNameList.toString());
  		  startActivity(intent);
	}
	
	public void newOnlineTaskItem (View view) {
 	   MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
 	   handleSpeech();
 	   TaskItem item = new TaskItem(taskDescription.getText().toString());
 	   dbHandler.addTaskItem(item);
 	   taskDescription.setText("");
	}
	
	public void generateRecording(){
		
		int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		byte[] buffer = new byte[bufferSize];
		AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
		
	}
	public void handleSpeech(){
          Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

          intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

          try {
              startActivityForResult(intent, RESULT_SPEECH);
              taskDescription.setText("");
          } catch (ActivityNotFoundException a) {
              Toast t = Toast.makeText(getApplicationContext(),
                      "Opps! Your device doesn't support Speech to Text",
                      Toast.LENGTH_SHORT);
              t.show();
          }
      
	}
	/*public void removeProduct (View view) {
    MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

    boolean result = dbHandler.deleteProduct(
       productBox.getText().toString());

    if (result)
{
	     idView.setText("Record Deleted");
    productBox.setText("");
	     quantityBox.setText("");
}
  else
          idView.setText("No Match Found");        	
}*/
	public void lookupTask (View view) {
	     MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
	
	     TaskItem task = dbHandler.findTaskItem(Integer.parseInt(idView.getText().toString()));

	     if (task != null) {
	    	taskDescription.setText(task.getTasktext());
	     } else {
   	   		taskDescription.setText("No Match Found");
	     }        	
	}
      
     // @Override
     /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);
   
          switch (requestCode) {
          case RESULT_SPEECH: {
              if (resultCode == RESULT_OK && null != data) {
   
                  ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
   
                  taskDescription.setText(text.get(0));
              }
              break;
          }
   
          }
      }*/
   
       
}
