package com.example.glasstodo;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.ActivityNotFoundException;
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

      public void newTaskItem (View view) {
    	   MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
    	   handleSpeech();
    	   TaskItem item = new TaskItem(taskDescription.getText().toString());
    	   dbHandler.addTaskItem(item);
    	   taskDescription.setText("");
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
    
      public void lookupTask (View view) {
    	     MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
    	
    	     TaskItem task = dbHandler.findTaskItem(Integer.parseInt(idView.getText().toString()));
    
    	     if (task != null) {
    		   taskDescription.setText(task.getTasktext());
           } else {
        	   taskDescription.setText("No Match Found");
           }        	
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
}
