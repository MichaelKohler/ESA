package com.example.bewegungsmeldermain;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Author: Mario Aloise <aloise.mario@gmail.com> 
 * Contributors:
 * 	- Ralf Wittich <bullit@gmx.ch> 
 *  - Mario Aloise <aloise.mario@gmail.com> 
 *  - Michael Kohler <mkohler@picobudget.com>
 *  */

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