package com.example.bewegungsmeldermain;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class KontaktMain extends Activity {

	public Kontakt kontakt; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kontakt_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kontakt_main, menu);
		return true;
	}

	
	public void ContactAdd(){
	};


	public void ContactDelete(){
	};


	public void setPrimary(){

	};

	
}