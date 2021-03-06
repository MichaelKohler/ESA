package ch.ffhs.esa.bewegungsmelder.activities;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import ch.ffhs.esa.bewegungsmelder.R;
import ch.ffhs.esa.bewegungsmelder.helpers.Helper;
import ch.ffhs.esa.bewegungsmelder.helpers.KontaktDBHelper;
import ch.ffhs.esa.bewegungsmelder.receivers.SMSReceiver;
import ch.ffhs.esa.bewegungsmelder.services.LocationService;
import ch.ffhs.esa.bewegungsmelder.services.MotionDetectionService;
import ch.ffhs.esa.bewegungsmelder.services.SMSSenderTimerService;
import ch.ffhs.esa.bewegungsmelder.services.ServerService;

import java.util.ArrayList;

public class MainActivity extends Activity {
	public static final String MODE = "ch.ffhs.esa.bewegungsmelder.MODE";
	private static final String TAG = MainActivity.class.getSimpleName();
	boolean motionServiceRunning = false;
	private static final int RESULT_SETTINGS = 1;
	private float latitude = 0;
	private float longitude = 0;
	private float accuracy = 0;
	private boolean dayMode = true;
    private SMSReceiver smsReceiver = new SMSReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Method: onCreate");
        // Starting the SMS Receiver // KoM 2013-12-23
        Log.d(TAG, "Registering SMS Receiver!");
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        // Starting the Server Service // KoM 2013-12-23
        Log.d(TAG, "Starting the Server Service!");
        Intent i = new Intent(this, ServerService.class);
        startService(i);

        // Starting the Resend Service // KoM 2013-01-04
        Log.d(TAG, "Starting the SMSSenderTimerService!");
        Intent j = new Intent(this, SMSSenderTimerService.class);
        startService(j);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

    @Override
    /**
     * stop the services before destroying
     *
     * @author Michael Kohler
     */
    public void onDestroy() {
    	Log.d(TAG, "Method: onDestroy");
        MainActivity.this.stopService(new Intent(MainActivity.this, MotionDetectionService.class));
        MainActivity.this.stopService(new Intent(MainActivity.this, ServerService.class));
        MainActivity.this.stopService(new Intent(MainActivity.this, SMSSenderTimerService.class));
        unregisterReceiver(smsReceiver);
        super.onDestroy();
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

/* ----------------------- Start of Button Clicks Handlers ------------ */

	public void onContactButtonClick(View view) {
		Intent intent = new Intent(this, AddContact.class);
		startActivity(intent);
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

    /**
     *
     * @author Ralf Wittich
     */
    public void onSupervisionButtonClicked(View view) {
        // check if there is any contact // 2014-01-06 Michael Kohler
        ArrayList<String> phoneNumbers = new KontaktDBHelper(MainActivity.this).getAllContactsNumbers();
        if (phoneNumbers.size() == 0) {
            Toast.makeText(this, "KEIN KONTAKT ERFASST -> bitte erfassen", Toast.LENGTH_SHORT).show();
            return;
        }
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
	
/* ----------------------- Start of Timer Service / WiR 2013-12-16 ------------ */

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

					// set progress bar accordingly
					ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
					progressBar.setProgress((Integer) bundle.get("TIMER_PROGRESS_LEVEL"));
					Log.d(TAG, "Progress Bar Set");
					// stop service
					if(!(Boolean)bundle.get("TIMER_RUNNING_BOOL")){
                        // we need to set the Label to "Start Supervision" once the timer is at 0 // 2014-01-06 Michael Kohler
                        Button supervisionButton = (Button) findViewById(R.id.buttonToggleSupervision);
                        supervisionButton.setText(R.string.start_supervision);

						context.stopService(new Intent(context, MotionDetectionService.class));
						context.unregisterReceiver(MotionDetectionStatusReceiver.this);
						Log.d(TAG, "Broadcast receiver unregistered, service stopped!");
						runLocationService();
					}
				}
				else{
					Log.d(TAG, "Timer_Stopped");
					context.unregisterReceiver(MotionDetectionStatusReceiver.this);
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
        Log.d(TAG, "Method: enableSupervision");
        ((TextView) findViewById(R.id.labelTopLeft)).setText(R.string.supervision_running);
        ((Button) findViewById(R.id.buttonToggleSupervision)).setText(R.string.stop_supervision);
		Log.d(TAG, "Starting motion service!");
		 
		// Register MotionDetectionReceiver
		MotionDetectionStatusReceiver br = new MotionDetectionStatusReceiver();
		registerReceiver(br, new IntentFilter(MotionDetectionService.MOTION_DETECTION_ACTION));
		
		Intent i = new Intent(this, MotionDetectionService.class);
		i.putExtra("MODE", dayMode); // Day or Night Mode
		startService(i);
	}
	
	private void disableSupervision() {
		Log.d(TAG, "Method: disableSupervision");
        ((TextView) findViewById(R.id.labelTopLeft)).setText(R.string.supervision_stopped);
        ((Button) findViewById(R.id.buttonToggleSupervision)).setText(R.string.start_supervision);
		Log.d(TAG, "Stopping motion service!");
		stopService(new Intent(this, MotionDetectionService.class));
		motionServiceRunning = false;
		TextView textViewTimeLeft = (TextView) findViewById(R.id.textViewTimeLeft); 
		textViewTimeLeft.setText("-");
	}
	
	/* ----------------------- End of Timer Service / WiR 2013-12-16 ------------ */


    /* ----------------------- supporting methods ------------ */

	// Setzt die Positionsdaten, wird aufgerufen, wenn die Accuracy ok ist.
	private void setPositionData(float lat, float lon, float acc){
		latitude = lat;
		longitude = lon;
		accuracy = acc;
		Log.d(TAG, "Location Set: Lat: " + latitude + " Long: " + longitude + " Accuracy: " +accuracy);
	}
	
	private void runLocationService() {		
		Log.d(TAG, "Getting Position...");
		// Register LocationReceiver
		LocationReceiver lr = new LocationReceiver();
		registerReceiver(lr, new IntentFilter(LocationService.LOCATION_ACTION));
		
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
						// 9999 if no accurate position can be found
						if(mAcc < 20 || mAcc == 9999){
							 mLat = bundle.getFloat("LATITUDE");
							 mLong = bundle.getFloat("LONGITUDE");
							setPositionData(mLat, mLong, mAcc);

                            ArrayList<String> phoneNumbers = new KontaktDBHelper(MainActivity.this).getAllContactsNumbers();
                            if (phoneNumbers.size() > 0) {
                                handleEmergencySMS(phoneNumbers.get(0));
                            }
                            else {
                                Toast.makeText(context, "KEIN KONTAKT ERFASST -> KEINE SMS GESENDET!", Toast.LENGTH_SHORT).show();
                            }

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
        ArrayList<String> phoneNumbers = new KontaktDBHelper(MainActivity.this).getAllContactsNumbers();
        if (phoneNumbers.size() > 0) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String emergencyHandlingPref = sharedPref.getString("pref_direktruf_aktion", "SMS");
            if (emergencyHandlingPref.equals("CALL")) {
                Helper.call(phoneNumbers.get(0), MainActivity.this);
            }
            else {
                runLocationService();
            }
        }
        else {
            Toast.makeText(this, "KEIN KONTAKT ERFASST -> KEINE SMS GESENDET!", Toast.LENGTH_SHORT).show();
        }
	}

    /**
     * handles the emergency SMS case
     *
     * @author Michael Kohler
     * @param String aPhoneNumber phone number of the recipient
     */
    private void handleEmergencySMS(String aPhoneNumber) {
        // accuracy 9999 is set after 20 position changes w/o reaching acc < 20
        String acc = "";
        if(accuracy == 9999){
            acc = "Position ungenau!!!";
        };

        String emergencyMessage = "Notruf! " + acc + " Position: https://maps.google.com/maps?q=" + Float.toString(latitude) +","+ Float.toString(longitude) + ".. Bitte mit einer SMS mit OK als Text bestaetigen.";
        Helper.setEmergencyMessage(emergencyMessage, this); // save emergencyMessage for later
        Helper.setEmergencyOngoing(true, this);
        Helper.setEmergencyConfirmed(false, this);
        Log.d(TAG, "Sending SMS: Number: " + aPhoneNumber + " Content: " + emergencyMessage);
        Helper.sendEmergencySMS(aPhoneNumber, emergencyMessage);
        Context context = getApplicationContext();
        Toast.makeText(context, "Message sent!", Toast.LENGTH_LONG).show();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }
}
