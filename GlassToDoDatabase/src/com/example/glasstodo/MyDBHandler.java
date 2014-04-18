package com.example.glasstodo;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDBHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "taskDB.db";
	private static final String TABLE_TASKS = "tasks";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TASK_DESCRIPTION = "taskdescription";
	//really a URI to get recording object from filesystem
	public static final String COLUMN_TASK_RECORDING = "taskrecording";
	public static final String COLUMN_URGENCY = "urgency";
	public static final String COLUMN_IMPORTANCE = "importance";
	public static final String COLUMN_DEADLINE = "deadline";
	public static final String COLUMN_TAGS = "tags";
	
	public MyDBHandler(Context context, String name, CursorFactory factory, int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String CREATE_TASKS_TABLE = "CREATE TABLE " +
	             TABLE_TASKS + "("+ COLUMN_ID + " INTEGER PRIMARY KEY," 
				 + COLUMN_TASK_DESCRIPTION + " TEXT," + COLUMN_TASK_RECORDING + " TEST," + COLUMN_URGENCY + " INTEGER," + 
				 COLUMN_IMPORTANCE + " INTEGER," + COLUMN_DEADLINE + " TEXT," + 
	             COLUMN_TAGS + " TEXT" + ")";
	      db.execSQL(CREATE_TASKS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
	    onCreate(db);
	}
	
	public void addTaskItem(TaskItem item){
		ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_DESCRIPTION, item.getTasktext());
        values.put(COLUMN_TASK_RECORDING, item.getTaskrecodingString());
        values.put(COLUMN_URGENCY, item.getUrgency());
        values.put(COLUMN_IMPORTANCE, item.getImportance());
        values.put(COLUMN_DEADLINE, item.getDeadline());
        values.put(COLUMN_TAGS, item.getTags());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_TASKS, null, values);
        db.close();
	}
	
	
	
	public TaskItem findTaskItem(int tid) {
		String query = "Select * FROM " + TABLE_TASKS + " WHERE " + COLUMN_ID + " =  \"" + tid + "\"";
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
		
		TaskItem taskItem = new TaskItem();
		
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			//System.out.println(cursor.getString(1));
			taskItem.setID(Integer.parseInt(cursor.getString(0)));
			taskItem.setTasktext(cursor.getString(1));
			taskItem.setTaskrecording(cursor.getString(2));
			taskItem.setUrgency(Integer.parseInt(cursor.getString(3)));
			taskItem.setImportance(Integer.parseInt(cursor.getString(4)));
			taskItem.setDeadline(cursor.getString(5));
			taskItem.setTagsBundle(cursor.getString(6));
			cursor.close();
		} else {
			taskItem = null;
		}
	        db.close();
		return taskItem;
	}
	
	public void updateTaskItem(TaskItem newTask){
		ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_DESCRIPTION, newTask.getTasktext());
        values.put(COLUMN_TASK_RECORDING, newTask.getTaskrecodingString());
        values.put(COLUMN_URGENCY, newTask.getUrgency());
        values.put(COLUMN_IMPORTANCE, newTask.getImportance());
        values.put(COLUMN_DEADLINE, newTask.getDeadline());
        values.put(COLUMN_TAGS, newTask.getTags());
		
		SQLiteDatabase db = this.getWritableDatabase();
		String tempStr = String.valueOf(newTask.getID());
		db.update(TABLE_TASKS, values, COLUMN_ID + "=" +tempStr ,null);
		db.close();
		
	}
	
	public ArrayList<TaskItem> showSimpleTasks() {
		String query = "Select " + COLUMN_ID + "," + COLUMN_TASK_DESCRIPTION + " FROM " + TABLE_TASKS;
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ArrayList<TaskItem> listOfTasks = new ArrayList<TaskItem>();
		
		Cursor cursor = db.rawQuery(query, null);
		
		TaskItem _task;
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				_task = new TaskItem();
				_task.setID(Integer.parseInt(cursor.getString(0)));
				_task.setTasktext(cursor.getString(1));
				listOfTasks.add(_task);
				cursor.moveToNext();
			}
			cursor.close();
		} else {
			listOfTasks = null;
		}
	        db.close();
		return listOfTasks;
	}
	
	public ArrayList<String> showTasks() {
		String query = "Select " + COLUMN_ID + "," + COLUMN_TASK_DESCRIPTION + " FROM " + TABLE_TASKS;
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ArrayList<String> listOfTasks = new ArrayList<String>();
		
		Cursor cursor = db.rawQuery(query, null);
		
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				String tmpStr = cursor.getString(0);
				tmpStr = tmpStr.concat("|");
				tmpStr = tmpStr.concat(cursor.getString(1));
				System.out.println(tmpStr);
				listOfTasks.add(tmpStr);
				cursor.moveToNext();
			}
			cursor.close();
		} else {
			listOfTasks = null;
		}
	        db.close();
		return listOfTasks;
	}
	
	/*public boolean deleteProduct(String productname) {
		
		boolean result = false;
		
		String query = "Select * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASKDESCRIPTION + " =  \"" + productname + "\"";

		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
		
		Product product = new Product();
		
		if (cursor.moveToFirst()) {
			product.setID(Integer.parseInt(cursor.getString(0)));
			db.delete(TABLE_TASKS, COLUMN_ID + " = ?",
		            new String[] { String.valueOf(product.getID()) });
			cursor.close();
			result = true;
		}
	        db.close();
		return result;
	}
	
	public Product findProduct(String productname) {
		String query = "Select * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASKDESCRIPTION + " =  \"" + productname + "\"";
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
		
		Product product = new Product();
		
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			product.setID(Integer.parseInt(cursor.getString(0)));
			product.setProductName(cursor.getString(1));
			product.setQuantity(Integer.parseInt(cursor.getString(2)));
			cursor.close();
		} else {
			product = null;
		}
	        db.close();
		return product;
	}
	
	
	
	public void addProduct(Product product) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_TASKDESCRIPTION, product.getProductName());
        values.put(COLUMN_URGENCY, product.getQuantity());
 
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.insert(TABLE_TASKS, null, values);
        db.close();
	}*/

}
