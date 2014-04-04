package com.jugaad.glasstodo.activity;

import android.app.ListActivity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.glass.media.Sounds;
import com.jugaad.glasstodo.R;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.task_list_screen);
		
		mListView = (ReorderableListView) findViewById(android.R.id.list);
		
		mListView.addReorderListener(this);
		
		mValues = new TaskItem[] {
				new TaskItem("task 1"),
				new TaskItem("task 2"),
				new TaskItem("task 3"),
				new TaskItem("task 4"),
				new TaskItem("task 5"),
				new TaskItem("task 6"),
				new TaskItem("task 7"),
				new TaskItem("task 8")
		};
		
		mAudio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		mAdapter = new TaskItemListAdapter(this, mValues);
		setListAdapter(mAdapter);
	}

	@Override
	public void move(int index, boolean up) {
		Log.i(TAG, "moving item " + index + " " + (up?"up":"down"));
		
		int i = index;
		
		if(i<=0 && up) { return; }
		if(i>=mValues.length-1 && !up) { return; }
		
		mAudio.playSoundEffect(Sounds.SELECTED);
		
		int j = i + (up ? -1 : 1);
		
		TaskItem vi = mValues[i];
		TaskItem vj = mValues[j];
		
		mValues[i] = vj;
		mValues[j] = vi;
		
		mAdapter.notifyDataSetChanged();
		
		mListView.setSelection(j);
	}

	@Override
	public void doneDragging() {
		mAudio.playSoundEffect(Sounds.TAP);
	}

	@Override
	public void startDragging() {
		mAudio.playSoundEffect(Sounds.TAP);
	}
}
