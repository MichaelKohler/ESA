package ch.ffhs.esa.bewegungsmelder;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Author: Ralf Wittich <bullit@gmx.ch>
 * Contributors:
 * 	- Ralf Wittich <bullit@gmx.ch>
 *  - Michael Kohler <mkohler@picobudget.com> (Implementation)
 * 
 *  Kommuniziert mit dem Ueberwachungsserver.
 */

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class ServerService extends Service {
    Handler handler = new Handler();
    String server = "";
    String port = "";
    int interval = 0;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Helper.sendPOSTRequest(server, port);
            handler.postDelayed(this, interval);
        }
    };

    /* (non-Javadoc)
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
    @Override
    public void onStart(Intent intent, int startId) {
        // Wird nach onCreate ausgefuehrt
        super.onStart(intent, startId);
        getSettingsForServer();
        handler.postDelayed(runnable, interval);
    }

    /**
     * reads and sets the settings from the preferences
     */
    private void getSettingsForServer() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        server = sharedPref.getString("pref_server_address", "");
        port = sharedPref.getString("pref_server_port", "3001");
        interval = Integer.parseInt(sharedPref.getString("pref_server_ping_intervall", "1")) * 60 * 1000;
        Log.d("ServerService", "Interval: " + interval);
    }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
