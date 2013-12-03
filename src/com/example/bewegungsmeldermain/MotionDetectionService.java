package com.example.bewegungsmeldermain;

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

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

public class MotionDetectionService extends Service implements SensorEventListener{
	
	 private SensorManager mSensorManager;
     private Sensor mAccelerometer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void onCreate() {

		Toast.makeText(this, "MotionDetection Service created", Toast.LENGTH_LONG).show();
		
		// System Service zuweisen
		 mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
         mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		super.onCreate();
	}

	
	@Override
	public void onDestroy() {

		Toast.makeText(this, "MotionDetection: onDestroy", Toast.LENGTH_LONG).show();
		super.onDestroy();
	}

	/* 
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Toast.makeText(this, "Sensor changed", Toast.LENGTH_SHORT).show();
		
	}

	
}
