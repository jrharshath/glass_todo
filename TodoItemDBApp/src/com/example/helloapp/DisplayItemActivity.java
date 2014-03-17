package com.example.helloapp;

import java.util.ArrayList;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayItemActivity extends Activity {
	public final static String ITEM = "com.example.myfirstapp.ITEM";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_item);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		String prodName = intent.getStringExtra(DisplayMessageActivity.PROD_NAME);
		TextView txtView1 = (TextView) findViewById(R.id.txtView1);
		TextView txtView2 = (TextView) findViewById(R.id.txtView2);
		TextView txtView3 = (TextView) findViewById(R.id.txtView3);
		
		 MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
	     Product product = dbHandler.findProduct(prodName);

	     if (product != null) {
	       txtView1.setText(prodName);
		   txtView2.setText(String.valueOf(product.getID()));
		   txtView3.setText(String.valueOf(product.getQuantity()));
	     } else {
	       txtView1.setText("No Match Found");
	     }
		
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_item, menu);
		return true;
	}*/
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			Intent intent = getIntent();
			setResult(RESULT_OK, intent);
			//NavUtils.navigateUpFromSameTask(this);
			super.finish();
			//return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
