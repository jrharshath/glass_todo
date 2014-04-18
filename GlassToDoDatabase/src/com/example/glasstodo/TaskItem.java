package com.example.glasstodo;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskItem{
	private int _id;
	private String _taskdescription;
	private URI _taskrecording;
	private int _urgency; //Now, Tomorrow, Later (0, 1, 2)
	private int _importance; //high, medium, low (2, 1, 0)
	private String _deadline;
	private String _tags; //comma-separate list of tags

	public TaskItem() {
		
	}
	
	public TaskItem(int id, String taskdescription) {
		this._id = id;
		this._taskdescription = taskdescription;
	}
	
	public TaskItem(int id, URI taskrecording) {
		this._id = id;
		this._taskrecording = taskrecording;
	}
	
	public TaskItem(String string) {
		// TODO Auto-generated constructor stub
		this._taskdescription = string;
	}

	public int getID(){
		return this._id;
	}
	
	public void setID(int newID){
		this._id = newID;
	}
	
	public String getTasktext(){
		return this._taskdescription;
	}
	
	public void setTasktext(String newText){
		this._taskdescription = newText;
	}
	
	public URI getTaskrecoding(){
		return this._taskrecording;
	}
	
	public String getTaskrecodingString(){
		return this._taskrecording.toString();
	}
	
	public void setTaskrecording(URI newTaskrecording){
		this._taskrecording = newTaskrecording;
	}
	
	public void setTaskrecording(String newTaskrecordingString){
		URI tempURI;
		try {
			tempURI = new URI(newTaskrecordingString);
			this._taskrecording = tempURI;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int getUrgency(){
		return _urgency;
	}
	
	public void setUrgency(int newUrgency){
		this._urgency = newUrgency;
	}
	
	public int getImportance(){
		return _importance;
	}
	
	public void setImportance(int newImportance){
		this._importance = newImportance;
	}
	
	public String getDeadline(){
		return _deadline;
	}
	
	public void setDeadline(String newDeadline){
		this._deadline = newDeadline;
	}
	
	public Date getDeadlineAsDateFormat(){
		SimpleDateFormat sdf = new SimpleDateFormat();
		Date d = new Date();
		try {
			d = sdf.parse(_deadline);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}
	
	
	
	public String getTags(){
		return _tags;
	}
	
	public void setTagsBundle(String newTagString){
		this._tags = newTagString;
	}
	
	public void addTag(String newTag){
		String comma = ",";
		String tempStr = comma.concat(newTag);
		this._tags = this._tags.concat(tempStr);
	}
	
	public String toString(TaskItem item){
		return item._taskdescription;
		
	}
	
	
}
