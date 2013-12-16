package ch.ffhs.esa.bewegungsmelder;

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

import ch.ffhs.esa.bewegungsmelder.DeveloperActivity.MotionDetectionStatusReceiver;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	boolean motionServiceRunning = false;
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
	
	public void onContactButtonClick(View view) {
		Intent intent = new Intent(this, AddContact.class);
		startActivity(intent);
	
	}
	
	public void onModeButtonClick() {
			
	}
	
/* ----------------------- Start of Timer Service / WiR 2013-12-16 ------------ */
	
	public void onSupervisionButtonClicked(View view) {		

		/* Test if service running */
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for(RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
			if(MotionDetectionService.class.getName().equals(service.service.getClassName())){
				motionServiceRunning = true;
			}
		}
		// Stop if running
		if(motionServiceRunning){
			Log.d(TAG, "Stopping service!");
			stopService(new Intent(this, MotionDetectionService.class));
			motionServiceRunning = false;
			TextView textViewTimeLeft = (TextView) findViewById(R.id.textViewTimeLeft); 
			textViewTimeLeft.setText("not running"); // TODO: Sauber implementieren
		}
		// Start if not running
		else{
			Log.d(TAG, "Starting service!");
			MotionDetectionStatusReceiver br = new MotionDetectionStatusReceiver();
			registerReceiver(br, new IntentFilter(MotionDetectionService.MOTION_DETECTION_ACTION));
			startService(new Intent(this, MotionDetectionService.class));
		}
	}
	// Receiver des MotionDetection Services
	public class MotionDetectionStatusReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(MotionDetectionService.MOTION_DETECTION_ACTION)){
				Log.d(TAG, "MotionDetection Broadcast received!!!");
				Bundle bundle = intent.getExtras();
				Log.d(TAG, "Extras read!!!");
				if (bundle != null){
					Log.d(TAG, "bundle != null");
					String textMsg = "Status: " + (String) bundle.get("TIMER_RUNNING_STR") + " Time left: " + (String) bundle.get("TIME_LEFT");

					TextView textViewTimeLeft = (TextView) findViewById(R.id.textViewTimeLeft); 
					textViewTimeLeft.setText(textMsg);
					Log.d(TAG, textMsg);

					// stop service, unregister Broadcast receiver
					if(!(Boolean)bundle.get("TIMER_RUNNING_BOOL")){
						context.stopService(new Intent(context, MotionDetectionService.class));
						context.unregisterReceiver(MotionDetectionStatusReceiver.this);
						Log.d(TAG, "Broadcast receiver unregistered, service stopped!");
					}
				}
			}
		}
	}
	
	/* ----------------------- End of Timer Service / WiR 2013-12-16 ------------ */

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

    /**
     * facilitates the emergency button click
     * TODO: add check for Setting (call or SMS)
     * @author Michael Kohler
     * @param View view the View from which this function is called
     */
	public void onEmergencyButtonClicked(View view) {
        // TODO: get primary contact's number
        String phoneNumber = "5556";

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String emergencyHandlingPref = sharedPref.getString("pref_direktruf_aktion", "SMS");
        if (emergencyHandlingPref.equals("CALL")) {
            Helper.call(phoneNumber, MainActivity.this);
        }
        else {
            handleEmergencySMS(phoneNumber);
        }
	}

    /**
     * handles the emergency SMS case
     *
     * @author Michael Kohler
     * @param String aPhoneNumber phone number of the recipient
     */
    private void handleEmergencySMS(String aPhoneNumber) {
        // TODO: get current location
        float currentLocationLat = 0;
        float currentLocationLong = 0;
        String message = "Notruf! Koordinaten, Lat: " + Float.toString(currentLocationLat) + ", Long: " + Float.toString(currentLocationLong) + ".. Bitte mit leerer SMS bestätigen.";
        Helper.sendEmergencySMS(aPhoneNumber, message);
        Context context = getApplicationContext();
        Toast.makeText(context, "Message sent!", Toast.LENGTH_LONG).show();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }
	
	//Developer Debugging Activity call / 2013-11-13 i3ullit
	public void onDeveloperButtonClicked(View view){
		Intent i = new Intent(this, DeveloperActivity.class);
		startActivity(i);
	}

}
