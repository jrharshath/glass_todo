package com.jugaad.glasstodo.model;

import com.jugaad.glasstodo.R;

import android.content.Context;
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

		TextView textView = (TextView) rowView.findViewById(R.id.taskDescription);		    
		textView.setText(values[position].getTaskDescription());

		return rowView;
	}
}
