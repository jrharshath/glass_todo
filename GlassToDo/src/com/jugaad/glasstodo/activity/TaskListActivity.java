package com.jugaad.glasstodo.activity;

import android.app.ListActivity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
				new TaskItem("finish ubicomp project"),
				new TaskItem("stop wasting time"),
				new TaskItem("finish presentation"),
				new TaskItem("buy groceries"),
				new TaskItem("clear room"),
				new TaskItem("learn to play the guitar"),
				new TaskItem("play more badminton"),
				new TaskItem("win raquetball tourney")
		};
		
		mAudio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		mAdapter = new TaskItemListAdapter(this, mValues);
		setListAdapter(mAdapter);
		mListView.setAudio(mAudio);
	}

	@Override
	public void move(int index, boolean up) {
		Log.i(TAG, "moving item " + index + " " + (up?"up":"down"));

		int i = index;
		int j = index + (up ? -1 : 1);
		
		TaskItem vi = mValues[i];
		TaskItem vj = mValues[j];
		
		mValues[i] = vj;
		mValues[j] = vi;
		
		mAdapter.notifyDataSetChanged(); // TODO: move to view later
	}

	@Override
	public void selectItem(int index) {
		// TODO launch single task activity 		
	}
}
