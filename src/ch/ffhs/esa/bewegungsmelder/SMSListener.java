package ch.ffhs.esa.bewegungsmelder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Author: Michael Kohler <mkohler@picobudget.com>
 * Contributors:
 * 	- YOUR NAME HERE <email@email.ch>
 *  */
public class SMSListener extends BroadcastReceiver {
    private static final String TAG = SMSListener.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "SMS RECEIVED.. processing..");
        Bundle bundle = intent.getExtras();
        if (bundle != null && Helper.emergencyOngoing) {
            try {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < messages.length; i++)
                {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    byte[] data = messages[i].getUserData();
                    String receivedMessage = new String(data);
                    // if we received an empty message, we have a confirmation
                    if (receivedMessage.equals("")) {
                        Helper.emergencyConfirmed = true;
                        Helper.emergencyOngoing = false;
                        // TODO: display "help on the way" or similar notification to the user?
                        Toast.makeText(context, "SMS ERHALTEN!!!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            } catch(Exception e){
                // let's ignore that for now..
                Log.d(TAG, "Error while receiving SMS..");
            }
        }
    }
}
