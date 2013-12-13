package ch.ffhs.esa.bewegungsmelder;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Author: Ralf Wittich <bullit@gmx.ch>
 * Contributors:
 * 	- Ralf Wittich <bullit@gmx.ch> 
 * 
 * Retourniert ein Event wenn eine Bewegung festgestellt wurde.
 * 
 */

import java.util.Timer;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class MotionDetectionService extends Service implements SensorEventListener{
	private static final String TAG = MotionDetectionService.class.getSimpleName();
	 private SensorManager mSensorManager;
     private Sensor mAccelerometer;
     private CountDownTimer Timer;

     // Wird fuer IPC benoetigt (inter-process communication")
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "created");
		 mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
         mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mSensorManager.unregisterListener(this);
		Log.d(TAG, "destroyed");	
	}

	/* 
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Timer = new CountDownTimer(30000, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Timer Tick");
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				Log.d(TAG, "Timer Finished");
			}
		}.start();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float thres = 0.5f; // Event values groesser thres werden als Bewegung gewertet.
		
		float axisX = event.values[0];
		float axisY = event.values[1];
		float axisZ = event.values[2];
		
		if(axisX > thres){
			Log.d(TAG, "Sensor changed: X = " + axisX );
		};
		if(axisY > thres){
			Log.d(TAG, "Sensor changed: Y = " + axisY );
		};
		if(axisZ > thres){
			Log.d(TAG, "Sensor changed: Z = " + axisZ );
		};
	}
}
