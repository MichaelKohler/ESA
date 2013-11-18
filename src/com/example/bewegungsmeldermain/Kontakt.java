package com.example.bewegungsmeldermain;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Kontakt extends Activity {
	public String vorName;
	public String nachName;
	public String phoneNumber;
	public boolean primary; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kontakt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kontakt, menu);
		return true;
	}


	public void save (){};
	
	
}

