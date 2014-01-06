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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

public class Helper {
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
     * Sends the emergency SMS
     *
     * @author Michael Kohler
     * @param String aPhoneNumber phone number of recipient
     */
    public static void sendEmergencySMS(String aPhoneNumber, Context aContext) {
        String message = getEmergencyMessage(aContext);
        sendEmergencySMS(aPhoneNumber, message);
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
      Log.d("Helper", "SENDING NETWORK REQUEST!!");
      new POSTTask().execute(aServerURL, aServerPort);
    }

    /**
     * resends the SMS if necessary
     *
     * @author Michael Kohler
     * @param counter to indicate which round we're on
     * @param aContext context
     */
    public static void checkAndResendSMS(int counter, Context aContext) {
        boolean emergencyOngoing = isEmergencyOngoing(aContext);
        Log.d("Helper", "Is emergency ongoing: " + emergencyOngoing);
        boolean emergencyConfirmed = isEmergencyConfirmed(aContext);
        Log.d("Helper", "Is emergency confirmed: " + emergencyConfirmed);
        if (emergencyOngoing && !emergencyConfirmed) {
            ResendSMSTask task = new ResendSMSTask(aContext);
            task.execute(counter);
        }
    }

    /**
     * returns whether an emergency is ongoing or not
     *
     * @author Michael Kohler
     * @param aContext context
     * @return whether an emergency is ongoing or not
     */
    public static boolean isEmergencyOngoing(Context aContext) {
        return PreferenceManager.getDefaultSharedPreferences(aContext)
                .getBoolean("pref_emergencyongoing", false);
    }

    /**
     * returns whether an emergency is confirmed or not
     *
     * @author Michael Kohler
     * @param aContext context
     * @return whether an emergency is confirmed or not
     *
     */
    public static boolean isEmergencyConfirmed(Context aContext) {
        return PreferenceManager.getDefaultSharedPreferences(aContext)
                .getBoolean("pref_emergencyconfirmed", false);
    }

    /**
     * sets the emergency message for later use
     *
     * @author Michael Kohler
     * @param aMessage - message to be stored
     * @param aContext context
     */
    public static void setEmergencyMessage(String aMessage, Context aContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(aContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("pref_emergencymessage", aMessage);
        editor.commit();
    }

    /**
     * returns the stored emergency message
     *
     * @author Michael Kohler
     *
     * @param aContext context
     * @return emergency message
     */
    public static String getEmergencyMessage(Context aContext) {
        return PreferenceManager.getDefaultSharedPreferences(aContext)
                .getString("pref_emergencymessage", "");
    }

    /**
     * sets whether an emergency is ongoing or not
     *
     * @author Michael Kohler
     * @param aOngoing - whether emergency is ongoing or not
     * @param aContext context
     */
    public static void setEmergencyOngoing(boolean aOngoing, Context aContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(aContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("pref_emergencyongoing", aOngoing);
        editor.commit();
    }

    /**
     * sets whether an emergency is confirmed or not
     *
     * @author Michael Kohler
     * @param aConfirmed - whether emergency is confirmed or not
     * @param aContext context
     */
    public static void setEmergencyConfirmed(boolean aConfirmed, Context aContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(aContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("pref_emergencyconfirmed", aConfirmed);
        editor.commit();
    }
}
