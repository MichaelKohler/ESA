package ch.ffhs.esa.bewegungsmelder.services;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Author: Michael Kohler <mkohler@picobudget.com>
 * Contributors:
 * 
 *  Prüft periodisch, ob das SMS an eine andere Person gesendet werden muss.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import ch.ffhs.esa.bewegungsmelder.helpers.Helper;

public class SMSSenderTimerService extends Service {
    Handler handler = new Handler();
    int interval = 30 * 1000;
    int counter = 0;
    Context context = this;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d("Helper", "Checking if SMS necessary");
            Helper.checkAndResendSMS(counter, context);
            counter++;
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
        handler.postDelayed(runnable, interval);
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