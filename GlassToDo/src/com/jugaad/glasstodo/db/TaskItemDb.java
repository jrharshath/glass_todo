package com.jugaad.glasstodo.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jugaad.glasstodo.model.TaskItem;

public class TaskItemDb extends SQLiteOpenHelper{
	
	private static String TAG = "TaskItemDb";

	private static final int DATABASE_VERSION = 4;
	
	private static final String DATABASE_NAME = "taskDB.db";
	private static final String TABLE_TASKS = "tasks";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TASKDESCRIPTION = "taskdescription";
	public static final String COLUMN_DONE = "done";
	public static final String COLUMN_ORDER = "ord";
	
	public static final String COLUMN_URGENCY = "urgency";
	public static final String COLUMN_IMPORTANCE = "importance";
	public static final String COLUMN_DEADLINE = "deadline";
	public static final String COLUMN_TAGS = "tags";
	
	public TaskItemDb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
	             TABLE_TASKS + "("
				 + COLUMN_ID + " INTEGER PRIMARY KEY," 
				 + COLUMN_TASKDESCRIPTION + " TEXT,"
				 + COLUMN_DONE + " INTEGER,"
				 + COLUMN_ORDER + " INTEGER,"
				 
				 + COLUMN_URGENCY + " INTEGER," 
				 + COLUMN_IMPORTANCE + " INTEGER," 
				 + COLUMN_DEADLINE + " TEXT," 
	             + COLUMN_TAGS + " TEXT" + ")";
	      db.execSQL(CREATE_PRODUCTS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
	    onCreate(db);
	}
	
	public void saveTaskItem(TaskItem item) {
		String query = "Select MAX("+COLUMN_ORDER+") FROM " + TABLE_TASKS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		int max_order;		
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			max_order = cursor.getInt(0) + 1;
			cursor.close();
		} else {
			max_order = 1;
		}
		
		ContentValues values = new ContentValues();
        values.put(COLUMN_TASKDESCRIPTION, item.getTaskDescription());
        values.put(COLUMN_DONE, item.isDone() ? 1 : 0);
        values.put(COLUMN_ORDER, max_order);
        
        values.put(COLUMN_URGENCY, item.getUrgency());
        values.put(COLUMN_IMPORTANCE, item.getImportance());
        values.put(COLUMN_DEADLINE, item.getDeadline());
        values.put(COLUMN_TAGS, item.getTags());

        db.insert(TABLE_TASKS, null, values);
        db.close();
	}	
	
	public TaskItem getTaskItemById(int tid) {
		String query = "Select "
				+ COLUMN_ID + ","
				+ COLUMN_TASKDESCRIPTION + ","
				+ COLUMN_DONE + ","
				+ COLUMN_ORDER + " FROM " + TABLE_TASKS 
				+ " WHERE " + COLUMN_ID + " =  \"" + tid + "\"";
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		TaskItem taskItem;
		
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			
			int id = cursor.getInt(0);
			String taskDescription = cursor.getString(1);
			int done = cursor.getInt(2);
			int order = cursor.getInt(3);
			taskItem = new TaskItem(id, taskDescription, done==0 ? false : true, order);
			
			cursor.close();
		} else {
			taskItem = null;
		}
		
		db.close();
		return taskItem;
	}
	
	public void updateTaskItem(TaskItem newTask){
		ContentValues values = new ContentValues();
        values.put(COLUMN_TASKDESCRIPTION, newTask.getTaskDescription());
        values.put(COLUMN_DONE, newTask.isDone() ? 1 : 0);
        values.put(COLUMN_ORDER, newTask.getOrder());
        
        values.put(COLUMN_URGENCY, newTask.getUrgency());
        values.put(COLUMN_IMPORTANCE, newTask.getImportance());
        values.put(COLUMN_DEADLINE, newTask.getDeadline());
        values.put(COLUMN_TAGS, newTask.getTags());
		
		SQLiteDatabase db = this.getWritableDatabase();
		String tempStr = String.valueOf(newTask.getID());
		db.update(TABLE_TASKS, values, COLUMN_ID + "=" +tempStr ,null);
		db.close();
		
	}
	
	public List<TaskItem> getAllTaskItems() {
		String query = "Select " 
				+ COLUMN_ID + "," 
				+ COLUMN_TASKDESCRIPTION + "," 
				+ COLUMN_DONE + ","
				+ COLUMN_ORDER + " FROM " + TABLE_TASKS
				+ " ORDER BY " + COLUMN_ORDER;
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ArrayList<TaskItem> allTaskItems = new ArrayList<TaskItem>();
		
		Cursor cursor = db.rawQuery(query, null);
		
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				int id = cursor.getInt(0);
				String taskDescription = cursor.getString(1);
				int done = cursor.getInt(2);
				int order = cursor.getInt(3);
				
				allTaskItems.add(new TaskItem(id, taskDescription, done==0 ? false : true, order));
				cursor.moveToNext();
			}
			cursor.close();
		}

		db.close();
		return allTaskItems;
	}
	
	public void deleteTaskItem(TaskItem item) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASKS, COLUMN_ID + " = " + item.getID(), null);
	}
}
