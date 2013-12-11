package com.example.bewegungsmeldermain;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Author: Ralf Wittich <bullit@gmx.ch>
 * Contributors:
 * 	- Ralf Wittich <bullit@gmx.ch> 
 *  - Mario Aloise <aloise.mario@gmail.com> 
 *  - Michael Kohler <mkohler@picobudget.com>
 *  */

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	
	private static final int RESULT_SETTINGS = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.id.action_settings:
            Intent i = new Intent(this, UserSettingActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            break;
 
        }
 
        return true;
    }
	
	public void onContactButtonClick() {
		
	}
	
	public void onModeButtonClick() {
		
	}
	
	public void onSupervisionButtonClick() {
		
	}
	
	private void enableSupervision() {
		
	}
	
	private void disableSupervision() {
		
	}
	
	private void enableMode(int mode) {
		
	}
	
	private void recalculateTicker() {
		
	}
	
	private void setProgressBar(int progressValue) {
		
	}
	
	private void recalculateGPSPosition() {
		
	}
	
	public void onEmergencyButtonClicked() {
		// TODO: get primary contact's number
		String phoneNumber = "000";
		// TODO: get current location
		float currentLocationLat = 0;
		float currentLocationLong = 0;
		String message = "Notruf! Koordinaten, Lat: " + Float.toString(currentLocationLat) + ", Long: " + Float.toString(currentLocationLong) + ".. Bitte mit leerer SMS bestätigen.";
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phoneNumber, null, message, null, null);
	}
	
	//Developer Debugging Activity call / 2013-11-13 i3ullit
	public void onDeveloperButtonClicked(View view){
		Intent i = new Intent(this, DeveloperActivity.class);
		startActivity(i);
	}

}
