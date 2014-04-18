package com.jugaad.glasstodo.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.jugaad.glasstodo.R;
import com.jugaad.glasstodo.db.TaskItemDb;
import com.jugaad.glasstodo.model.TaskItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class NewTaskActivity extends Activity {

	private TaskItem createdTask = null;
	private TaskItemDb db;
	private Timer saveTimer;
	
	private final int SPEECH = 10284;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new TaskItemDb(this);

		setContentView(R.layout.new_task_screen);

		recordTask();
	}

	private void recordTask() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, SPEECH);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == SPEECH && resultCode == RESULT_OK) {
			List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String spokenText = results.get(0);
			createdTask = new TaskItem(spokenText);

			showTaskOnView(createdTask);
			
			saveTimer = new Timer();
			final int[] c = new int[]{3};
			saveTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					if(c[0]==0) {
						saveTimer.cancel();
						saveTimer = null;
						saveHandler.sendEmptyMessage(0);
					} else {
						updateHandler.sendEmptyMessage(c[0]--);						
					}
				}				
			}, 1000, 1000);
		} else {
			((TextView) findViewById(R.id.newTaskLabel)).setVisibility(View.INVISIBLE);
			((TextView) findViewById(R.id.taskDescription)).setVisibility(View.INVISIBLE);
			
			((TextView) findViewById(R.id.messageTextView)).setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.messageTextView)).setText("An error occurred! Try again later.");
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	((TextView) findViewById(R.id.messageTextView)).setText("Saving in "+msg.what+" seconds\ntap for options");
        }
    };
    
    private Handler saveHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	saveTask();
        }
    };


	private void showTaskOnView(TaskItem task) {
		((TextView) findViewById(R.id.newTaskLabel)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.taskDescription)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.taskDescription)).setText(task.getTaskDescription());
		
		((TextView) findViewById(R.id.messageTextView)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.messageTextView)).setText("tap for options");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.new_task_menu, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			if(saveTimer  != null) {
				saveTimer.cancel();
				saveTimer = null;
				((TextView) findViewById(R.id.messageTextView)).setText("tap to options");
			}
			openOptionsMenu();
			return true; 
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_menu_item:
			saveTask();
			return true;
		case R.id.record_again_menu_item:
			recordTask();
			return true;
		case R.id.cancel_menu_item:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void saveTask() {
		db.saveTaskItem(createdTask);
		((TextView) findViewById(R.id.newTaskLabel)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.taskDescription)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.messageTextView)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.messageTextView)).setText("Saved.");

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				finish();				
			} }, 1000);
	}
}
