package ch.ffhs.esa.bewegungsmelder;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Author: Ralf Wittich <bullit@gmx.ch>
 * Contributors:
 * 	- Ralf Wittich <bullit@gmx.ch> 
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

public class MotionDetectionService extends Service implements SensorEventListener{
	public static final String MOTION_DETECTION_ACTION = "ch.ffhs.esa.bewegungsmelder.MOTION_DETECTION_ACTION";
	private static final String TAG = MotionDetectionService.class.getSimpleName();

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Timer motionTimer = null;
	private BroadcastMotionDetectionStatus sendStatus = null;
	private int delayTime = 10000; //in ms (300000 == 5 min)	//TODO: delayTime auslesen
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

			//	Log.d(TAG, "Broadcasting! timer: " + timer + "ms to go" + " counter: " +counter + " refreshTime: " +refreshTime  );

			String toastMsg;

			if(timer > 0){
				timerRunning = true;
				toastMsg = "running";
			}
			else {
				timerRunning= false;
				toastMsg = "stopped";
			};

			i.putExtra("TIME_LEFT", Integer.toString(timer/1000)+" seconds"); // Timer in seconds
			i.putExtra("TIMER_RUNNING_BOOL", timerRunning); // Boolean
			i.putExtra("TIMER_RUNNING_STR", toastMsg);
			sendBroadcast(i);	 

			Log.d(TAG, "Broadcast! Timer: " + timer /1000 + "s until alarm. Timer Status: " + toastMsg);

			if(!timerRunning){
				this.cancel();
				Log.d(TAG, "timer stopped");
			};
		}			
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "created");
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		// Read Settings
		SharedPreferences preferences = getSharedPreferences("activity_settings", MODE_PRIVATE);
		delayTime = 1000 * preferences.getInt("pref_intervall_timer_day", 1);
	}

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
		startTimer();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float thres = 0.5f; // Event values groesser thres werden als Bewegung gewertet. TODO: Thres aus Settings lesen	
		float axisX = event.values[0];
		float axisY = event.values[1];
		float axisZ = event.values[2];

		if(axisX > thres || axisY > thres || axisZ > thres){
			stopTimer();	
			startTimer();
			Log.d(TAG, "Sensor changed, restarting timer" );
		}
	};

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
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
