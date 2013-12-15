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
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class DeveloperActivity extends Activity {
	private static final String TAG = DeveloperActivity.class.getSimpleName();
	private boolean motionServiceRunning = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_developer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.developer, menu);
		return true;
	}

	// Controls the service
	public void buttonStartMotionDetectionService(View view){

		/* Test if service running */
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for(RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
			if(MotionDetectionService.class.getName().equals(service.service.getClassName())){
				motionServiceRunning = true;
			}
		}
		// Stop if running
		if(motionServiceRunning){
			stopService(new Intent(this, MotionDetectionService.class));
			motionServiceRunning = false;
		}
		// Start if not running
		else{
			MotionDetectionStatusReceiver br = new MotionDetectionStatusReceiver();
			registerReceiver(br, new IntentFilter(MotionDetectionService.MOTION_DETECTION_ACTION));
			startService(new Intent(this, MotionDetectionService.class));
		}
	}

	// TODO: Brodcast receiver in Main GUI einfügen. 
	public class MotionDetectionStatusReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(MotionDetectionService.MOTION_DETECTION_ACTION)){
				Log.d(TAG, "MotionDetection Broadcast received!!!");
				Bundle bundle = intent.getExtras();
				if (bundle != null){
					String textMsg = "Status: " + (String) bundle.get("TIMER_RUNNING_STR") + " Time left: " + (String) bundle.get("TIME_LEFT");

					TextView timeLeftTextView = (TextView) findViewById(R.id.timeLeftTextView); 
					timeLeftTextView.setText(textMsg);
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

}
