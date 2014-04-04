package com.jugaad.glasstodo.activity;

import java.util.List;

import com.jugaad.glasstodo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import android.widget.TextView;

public class NewTaskActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_task_screen);

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK) {
			List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String spokenText = results.get(0);

			((TextView) findViewById(R.id.taskDescription)).setText(spokenText);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
