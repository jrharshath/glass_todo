package com.jugaad.glasstodo.model;

import com.jugaad.glasstodo.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskItemListAdapter extends ArrayAdapter<TaskItem> {

	private final Context context;
	private final TaskItem[] values;

	public TaskItemListAdapter(Context context, TaskItem[] values) {
		super(context, R.layout.task_list_item , values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.task_list_item, parent, false);

		TaskItem taskItem = values[position];
		
		TextView tv = (TextView) rowView.findViewById(R.id.taskDescription);		    
		tv.setText(taskItem.getTaskDescription());
		if(taskItem.isDone()) {
			tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
		}
		
		if(taskItem.grabbed) {
			rowView.setBackgroundColor(Color.parseColor("#77aa77"));
		}

		return rowView;
	}
}
