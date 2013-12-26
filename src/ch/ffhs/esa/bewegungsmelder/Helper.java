package ch.ffhs.esa.bewegungsmelder;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Author: Michael Kohler <mkohler@picobudget.com>
 * Contributors:
 *  - Michael Kohler <mkohler@picobudget.com>
 *  */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

public class Helper {
    public static boolean emergencyOngoing = false;
    public static boolean emergencyConfirmed = false;
    private static final String TAG = Helper.class.getSimpleName();

    /**
     * Sends the emergency SMS
     *
     * @author Michael Kohler
     * @param String aPhoneNumber phone number of recipient
     * @param String aMessage message to send
     */
    public static void sendEmergencySMS(String aPhoneNumber, String aMessage) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(aPhoneNumber, null, aMessage, null, null);
        Log.d(TAG, "SMS Sent: Nr: " + aPhoneNumber + " Message: " +aMessage);
    }

    /**
     * Calls a phone number
     *
     * @author Michael Kohler
     * @param String aPhoneNumber phone number to call
     * @param Activity aActivity activity needed for startActivity()
     */
    public static void call(String aPhoneNumber, Activity aActivity) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + aPhoneNumber));
        aActivity.startActivity(callIntent);
    }

    /**
     * sends a POST request to the server specified
     *
     * @author Michael Kohler
     * @param aServerURL
     * @param aServerPort
     */
    public static void sendPOSTRequest(String aServerURL, String aServerPort) {
      Log.d("Helper", "SENDING REQUEST!!");
      //new POSTTask().execute(aServerURL, aServerPort);
    }

}
