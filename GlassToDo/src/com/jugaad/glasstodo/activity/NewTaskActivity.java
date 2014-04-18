package com.jugaad.glasstodo.activity;

import java.util.List;

import com.jugaad.glasstodo.R;
import com.jugaad.glasstodo.model.TaskItem;
import com.jugaad.glasstodo.model.MyDBHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class NewTaskActivity extends Activity {
	
	private TaskItem createdTask = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_task_screen);

		recordTask();
	}

	private void recordTask() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK) {
			List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String spokenText = results.get(0);
			createdTask = new TaskItem(spokenText);

			showTaskOnView(createdTask);
		} else {
			// TODO: show a "recording failed, try again" message
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showTaskOnView(TaskItem task) {
		// populate all fields of the taskItem into the view
		((TextView) findViewById(R.id.taskDescription)).setText(task.getTaskDescription());
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_task_menu, menu);
        return true;
    }
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && createdTask != null) {
              openOptionsMenu();
              return true; 
          }
          return false;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu_item:
                // DONE: x-save the "createdTask" object to database
            	MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
            	TextView tv = (TextView) findViewById(R.id.taskDescription);
         	   	TaskItem task = new TaskItem(tv.getText().toString());
         	   	dbHandler.addTaskItem(task);
            	// TODO: show "saved" for 2 sec, then...
            	finish();
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


}
