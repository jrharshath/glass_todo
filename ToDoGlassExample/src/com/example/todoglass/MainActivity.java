package com.example.todoglass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

	private String sample = "Sample";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// Card firstCard = new Card(this);
		// firstCard.setText("Hello World");
		// firstCard.setFootnote("My ToDo List");
		// setContentView(firstCard.toView());

		setContentView(R.layout.custom_layout);

		TextView Heading = (TextView) findViewById(R.id.TV_Heading);
		Heading.setText(sample);
	}

	public boolean onKeyDown(int keycode, KeyEvent event) {
		if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
			Intent newIntent = new Intent(getApplicationContext(), SecondActivity.class);
			startActivity(newIntent);
			return true;
		}
		return false;
	}
}
