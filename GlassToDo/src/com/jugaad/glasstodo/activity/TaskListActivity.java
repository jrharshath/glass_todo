package com.jugaad.glasstodo.activity;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jugaad.glasstodo.R;
import com.jugaad.glasstodo.db.TaskItemDb;
import com.jugaad.glasstodo.model.TaskItem;
import com.jugaad.glasstodo.model.TaskItemListAdapter;
import com.jugaad.glasstodo.view.ReorderListener;
import com.jugaad.glasstodo.view.ReorderableListView;

public class TaskListActivity extends ListActivity implements ReorderListener {
	private static final String TAG = "HelloWorldActivity";
	private TaskItem[] mValues;
	private TaskItemListAdapter mAdapter;
	private ReorderableListView mListView;
	private AudioManager mAudio;
	private TaskItemDb db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.task_list_screen);
		mAudio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);		
		mListView = (ReorderableListView) findViewById(android.R.id.list);		
		mListView.addReorderListener(this);
		mListView.setAudio(mAudio);
	}
	
	@Override
	protected void onResume() {
		db = new TaskItemDb(this);		
		List<TaskItem> allTasks = db.getAllTaskItems();
		mValues = allTasks.toArray(new TaskItem[allTasks.size()]);
		
		mAdapter = new TaskItemListAdapter(this, mValues);
		setListAdapter(mAdapter);		

		if(mValues.length > 1) {
			Toast.makeText(this, "Long press to reorder", Toast.LENGTH_LONG).show();
		}
		
		super.onResume();
	}

	@Override
	public void move(int index, boolean up) {
		Log.i(TAG, "moving item " + index + " " + (up?"up":"down"));

		int i = index;
		int j = index + (up ? -1 : 1);
		
		TaskItem vi = mValues[i];
		TaskItem vj = mValues[j];
		
		int order_i = vi.getOrder();
		int order_j = vj.getOrder();
		
		vi.setOrder(order_j);
		vj.setOrder(order_i);
		
		db.updateTaskItem(vi);
		db.updateTaskItem(vj);
		
		mValues[i] = vj;
		mValues[j] = vi;
		
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void selectItem(int index) {
		Intent intent = new Intent(this, SingleTaskActivity.class);
		intent.putExtra("taskItemId", mValues[mListView.getSelectionIndex()].getID());
		startActivity(intent);
	}

	@Override
	public void startDragging(int index) {
		mValues[index].grabbed = true;
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void stopDragging(int index) {
		mValues[index].grabbed = false;
		mAdapter.notifyDataSetChanged();
	}
}
