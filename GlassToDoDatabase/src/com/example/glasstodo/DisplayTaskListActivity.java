package com.example.glasstodo;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.content.Intent;

public class DisplayTaskListActivity extends Activity implements OnItemClickListener {
	
	public final static String TASK_ITEM = "com.example.myfirstapp.TASK_ITEM";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_displaytasklist_message);
		
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		ArrayList<String> stringList = intent.getStringArrayListExtra(DatabaseActivity.TASK_LIST);
		System.out.println(stringList);
		ArrayList<TaskItem> completeList = new ArrayList<TaskItem> ();
		
		for (String strTask : stringList){
			String [] eachItem = strTask.split("[|]");
			TaskItem tempItem;
			if (eachItem.length == 1){
				tempItem = new TaskItem (Integer.valueOf(eachItem[0]),"blah");	
			}else{
				tempItem = new TaskItem (Integer.valueOf(eachItem[0]),eachItem[1]);
			}
			completeList.add(tempItem);
		}
		
		ListView listView = (ListView) findViewById(R.id.list);
		ArrayAdapter <TaskItem>adapter = new ArrayAdapter<TaskItem>(this, android.R.layout.simple_list_item_1, completeList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
		//TextView txtView = new TextView(this);
		//txtView.setTextSize(40);
	    //txtView.setText(msg);
	    //setContentView(txtView);
	}
	
	
	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		TaskItem taskItem = (TaskItem) parent.getItemAtPosition(position);
		System.out.println(taskItem.getID());
		int item = taskItem.getID();
 	    Intent intent = new Intent(this, DisplayItemActivity.class);
 	    //intent.putExtra(TASK_ITEM, productName);
 	    intent.putExtra(TASK_ITEM, item);
 	    //startActivity(intent);
 	    startActivityForResult(intent, 1);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
		return true;
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
