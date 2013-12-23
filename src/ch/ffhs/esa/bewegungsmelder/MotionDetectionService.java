package ch.ffhs.esa.bewegungsmelder;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Author: Ralf Wittich <bullit@gmx.ch>
 * Contributors:
 * 	- Ralf Wittich <bullit@gmx.ch>
 *  - Michael Kohler <mkohler@picbudget.com>
 * 
 * Sends a broadcast with the remaining time and if the timer is expired.
 * 
 */


import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class MotionDetectionService extends Service implements SensorEventListener {
	public static final String MOTION_DETECTION_ACTION = "ch.ffhs.esa.bewegungsmelder.MOTION_DETECTION_ACTION";
	private static final String TAG = MotionDetectionService.class.getSimpleName();

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Timer motionTimer = null;
	private BroadcastMotionDetectionStatus sendStatus = null;
	private int delayTime = 10000; // in ms
	private int refreshTime = 1000; // in ms

	// Inner Class: Definition des Broadcasters
	private class BroadcastMotionDetectionStatus extends TimerTask{

		int counter = 0;
		boolean timerRunning = false;
		@Override
		public void run() {	
			Intent i = new Intent(MOTION_DETECTION_ACTION);				
			int timer = delayTime - counter * refreshTime;		// timer calculation
			counter++;

			String timerRunningStr;
			if(timer > 0){
				timerRunning = true;
				timerRunningStr = "running";
			}
			else {
				timerRunning= false;
				timerRunningStr = "stopped";
			};
			
			// Umwandeln in Minuten / Sekunden 
			int seconds = timer / 1000;
			int minutesLeft = seconds / 60;
			int secondsLeft = seconds % 60;

            // we need to format the string if seconds is less than 10 // KoM 2013-12-22
            String timeLeftDisplay = Integer.toString(minutesLeft) + ":";
            if (secondsLeft < 10) {
                 timeLeftDisplay += "0";
            }
            timeLeftDisplay += Integer.toString(secondsLeft);

            // set the progressLevel appropriately
            // we need to use double because of division limitations with integers
            // KoM 2013-12-22
            double total = delayTime/1000.0;
            double progressLevel = seconds / total * 100;
            Log.d(TAG, "Progressbar regressing: " + progressLevel + "%");

			i.putExtra("TIME_LEFT", timeLeftDisplay); // Timestring [mm:ss]
			i.putExtra("TIMER_RUNNING_BOOL", timerRunning); // Boolean
			i.putExtra("TIMER_RUNNING_STR", timerRunningStr);
            i.putExtra("TIMER_PROGRESS_LEVEL", (int)progressLevel);
			sendBroadcast(i);	 

			Log.d(TAG, "Broadcast! Timer: " + timer /1000 + "s until alarm. Timer Status: " + timerRunningStr);

			if(!timerRunning){
				this.cancel();
				Log.d(TAG, "timer stopped");
			};
		}			
	}

	/* Sensor Service zuordnen, Art und Abtastrate definieren
	 * (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "created");
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	/* Aufraeumen
	 * 
	 * (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mSensorManager.unregisterListener(this);	
		sendStatus.cancel();
		stopTimer();
		Log.d(TAG, "destroyed");	
	}

	/* 
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		boolean dayMode = intent.getBooleanExtra(("MODE"),false);

		// Read Settings
		SharedPreferences preferences = getSharedPreferences("ch.ffhs.esa.bewegungsmelder_preferences", MODE_MULTI_PROCESS);	
		String intTmr;
		if(dayMode){
			Log.d(TAG,"Daymode");	
			intTmr = preferences.getString("pref_intervall_timer_day", "1");
		}else{
			intTmr = preferences.getString("pref_intervall_timer_night", "1");
			Log.d(TAG,"Nightmode");
		}

        // TODO: habe noch keinen Weg gefunden, die Preference als Integer zu speichern, schaue das
        // TODO: noch an. ist aber vorerst nicht schlimm, da der User nur Nummern eingeben kann / KoM 2013-12-22
		delayTime = 60000 * Integer.valueOf(intTmr);
		Log.d(TAG, "Delay Time Set to: " + Integer.toString(delayTime) + " Read Timer: " + intTmr);
		startTimer();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float thres = 0.5f; // Event values groesser thres werden als Bewegung gewertet. // TODO: Threshold in Settings
		float axisX = event.values[0];
		float axisY = event.values[1];
		float axisZ = event.values[2];

		if(axisX > thres || axisY > thres || axisZ > thres){
			stopTimer();	
			startTimer();
			Log.d(TAG, "Sensor changed, restarting timer" );
		}
	};
	/*
	 * Timer starten.
	 */
	private void startTimer(){
		motionTimer = new Timer(true);	// Create new Timer as deamon
		sendStatus = new BroadcastMotionDetectionStatus();
		Log.d(TAG, "BroadcastMotionDetectionStatus created");
		motionTimer.scheduleAtFixedRate(sendStatus, 0, refreshTime);
	}

	/**
	 * Stops the timer, cancel and purge of all tasks.
	 */
	private void stopTimer(){
		motionTimer.cancel();			// Cancel Tasks
		motionTimer.purge();			// Remove Tasks	
	}

	// Wird fuer IPC benoetigt (inter-process communication")
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
