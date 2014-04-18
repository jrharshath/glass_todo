package com.jugaad.glasstodo.db;

import java.util.ArrayList;
import java.util.List;

import com.jugaad.glasstodo.model.TaskItem;

public class TodoItemDb {

	private List<TaskItem> tasks = new ArrayList<TaskItem>();
	
	public void saveItem(TaskItem task) {
		tasks.add(task);
	}

}
