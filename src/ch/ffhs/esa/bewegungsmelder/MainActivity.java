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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String MODE = "ch.ffhs.esa.bewegungsmelder.MODE";
	private static final String TAG = MainActivity.class.getSimpleName();
	boolean motionServiceRunning = false;
	private static final int RESULT_SETTINGS = 1;
	private float latitude = 0;
	private float longitude = 0;
	private float accuracy = 0;
	private boolean dayMode = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Registering SMS Receiver!");
        SMSListener smsListener = new SMSListener();
        registerReceiver(smsListener, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

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
	
	
	
/* ----------------------- Start of Timer Service / WiR 2013-12-16 ------------ */
	
	public void onSupervisionButtonClicked(View view) {		

		/* Test if service running */
		motionServiceRunning = isServiceRunning();
		// Stop if running
		if(motionServiceRunning){
			disableSupervision();
		}
		// Start if not running
		else{
			enableSupervision();
		}
	}
	// Receiver des MotionDetection Services
	public class MotionDetectionStatusReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(MotionDetectionService.MOTION_DETECTION_ACTION)){
				Log.d(TAG, "MotionDetection Broadcast received!!!");
				Bundle bundle = intent.getExtras();
				if (bundle != null){
					Log.d(TAG, "bundle != null");
					String timeLeft = (String) bundle.get("TIME_LEFT");
					String textMsg = "Status: " + (String) bundle.get("TIMER_RUNNING_STR") + " Time left: " + timeLeft;
					
					TextView textViewTimeLeft = (TextView) findViewById(R.id.textViewTimeLeft); 
					textViewTimeLeft.setText(timeLeft);
					Log.d(TAG, textMsg);

					// stop service, unregister Broadcast receiver
					if(!(Boolean)bundle.get("TIMER_RUNNING_BOOL")){
						context.stopService(new Intent(context, MotionDetectionService.class));
						context.unregisterReceiver(MotionDetectionStatusReceiver.this);
						Log.d(TAG, "Broadcast receiver unregistered, service stopped!");
						runLocationService();
					}
				}
			}
		}
	}
	
	private boolean isServiceRunning(){
		boolean rs = false;
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for(RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
			if(MotionDetectionService.class.getName().equals(service.service.getClassName())){
				rs = true;
			}
		}
		return rs;
	}
	
	private void enableSupervision() {
		Log.d(TAG, "Starting motion service!");
		MotionDetectionStatusReceiver br = new MotionDetectionStatusReceiver();
		registerReceiver(br, new IntentFilter(MotionDetectionService.MOTION_DETECTION_ACTION));
		Intent i = new Intent(this, MotionDetectionService.class);
		i.putExtra("MODE", dayMode); // Day or Night Mode
		startService(i);
	}
	
	private void disableSupervision() {
		Log.d(TAG, "Stopping motion service!");
		stopService(new Intent(this, MotionDetectionService.class));
		motionServiceRunning = false;
		TextView textViewTimeLeft = (TextView) findViewById(R.id.textViewTimeLeft); 
		textViewTimeLeft.setText("-"); 
	}
	
	/* ----------------------- End of Timer Service / WiR 2013-12-16 ------------ */
	
	private void enableMode(int mode) {
		
	}
	
	private void recalculateTicker() {
		
	}
	
	private void setProgressBar(int progressValue) {
		
	}
	
	public void onModeButtonClicked(View view){
		String buttonModeLabel;
		Button buttonMode = (Button) findViewById(R.id.buttonMode); 
		
		if(dayMode){
			dayMode = false;
			buttonModeLabel = "Nachtmodus";
		}
		else{
			dayMode = true;
			buttonModeLabel = "Tagmodus";
		}
		buttonMode.setText(buttonModeLabel);
	}
	
	// Setzt die Positionsdaten, wird aufgerufen, wenn die Accuracy ok ist.
	private void setPositionData(float lat, float lon, float acc){
		latitude = lat;
		longitude = lon;
		accuracy = acc;
		Log.d(TAG, "Location Set: Lat: " + latitude + " Long: " + longitude + " Accuracy: " +accuracy);

	}
	
	private void runLocationService() {		
		Log.d(TAG, "Getting Position...");
		LocationReceiver br = new LocationReceiver();
		registerReceiver(br, new IntentFilter(LocationService.LOCATION_ACTION));
		startService(new Intent(this, LocationService.class));
	}
	// Receiver des Location Services
		public class LocationReceiver extends BroadcastReceiver{
			float mLat = 0.0f;
			float mLong = 0.0f;
			float mAcc = 0.0f;
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(LocationService.LOCATION_ACTION)){
					Log.d(TAG, "Location Broadcast received!!!");
					Bundle bundle = intent.getExtras();
					if (bundle != null){
						Log.d(TAG, "bundle != null");					
						 mAcc = bundle.getFloat("ACCURACY");
						
						Log.d(TAG, "Location: Lat: " + mLat + " Long: " + mLong + " Accuracy: " + mAcc);

						// wait for accurate signal, setPosition, stop service, unregister Broadcast receiver
						if(mAcc < 20){
							 mLat = bundle.getFloat("LATITUDE");
							 mLong = bundle.getFloat("LONGITUDE");
							setPositionData(mLat, mLong, mAcc);

                            // TODO: act if no response is coming back
                            // TODO: get phone number
                            String phoneNumber = "5556";
                            handleEmergencySMS(phoneNumber);

							context.stopService(new Intent(context, LocationService.class));
							context.unregisterReceiver(LocationReceiver.this);
							Log.d(TAG, "Broadcast receiver unregistered, service stopped! Accuracy: " +mAcc);
						}
					}
				}
			}
		}

    /**
     * facilitates the emergency button click
     *
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
            runLocationService();
        }
	}

    /**
     * handles the emergency SMS case
     *
     * @author Michael Kohler
     * @param String aPhoneNumber phone number of the recipient
     */
    private void handleEmergencySMS(String aPhoneNumber) {
        Helper.emergencyOngoing = true;
        String message = "Notruf! Koordinaten, Lat: " + Float.toString(latitude) + ", Long: " + Float.toString(longitude) + ".. Bitte mit leerer SMS bestätigen.";
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
