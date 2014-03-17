package com.example.helloapp;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class DatabaseActivity extends Activity {
	public final static String PROD_LIST = "com.example.myfirstapp.PROD_LIST";
	
	TextView idView;
	EditText productBox;
	EditText quantityBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
		
        idView = (TextView) findViewById(R.id.productID);
        productBox = (EditText) findViewById(R.id.product_name);
        quantityBox = (EditText) findViewById(R.id.productQuantity);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

      public void newProduct (View view) {
    	     MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
    	
    	   int quantity = 
              Integer.parseInt(quantityBox.getText().toString());
    	
    	   Product product = 
              new Product(productBox.getText().toString(), quantity);
    	
    	   dbHandler.addProduct(product);
    	   productBox.setText("");
    	   quantityBox.setText("");
      }
      
      public void showToDo (View view){
    	  MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
    	  ArrayList<String> productNameList = dbHandler.showProducts();
    	  
    	  Intent intent = new Intent(this, DisplayMessageActivity.class);
  		  intent.putStringArrayListExtra(PROD_LIST, productNameList);
  		  //intent.putExtra(PROD_LIST, productNameList.toString());
  		  startActivity(intent);
      }
    
      public void lookupProduct (View view) {
    	     MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
    	
    	     Product product = 
               dbHandler.findProduct(productBox.getText().toString());
    
    	     if (product != null) {
    		   idView.setText(String.valueOf(product.getID()));
    		   quantityBox.setText(String.valueOf(product.getQuantity()));
           } else {
    	         idView.setText("No Match Found");
           }        	
       }
   
       public void removeProduct (View view) {
    	     MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
    	
    	     boolean result = dbHandler.deleteProduct(
                productBox.getText().toString());
    
    	     if (result)
	     {
    		     idView.setText("Record Deleted");
		     productBox.setText("");
    		     quantityBox.setText("");
	     }
           else
    	           idView.setText("No Match Found");        	
       }
}
