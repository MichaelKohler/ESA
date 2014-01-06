package ch.ffhs.esa.bewegungsmelder.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import ch.ffhs.esa.bewegungsmelder.helpers.Helper;
import ch.ffhs.esa.bewegungsmelder.helpers.KontaktDBHelper;

import java.util.ArrayList;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Author: Michael Kohler <mkohler@picobudget.com>
 * Contributors:
 *  - Michael Kohler <mkohler@picobudget.com>
 *  */
public class ResendSMSTask extends AsyncTask<Integer, Integer, Integer> {
    private Context context;

    public ResendSMSTask(Context aContext){
        context = aContext;
    }

    protected Integer doInBackground(Integer... params) {
        Log.d("ResendSMSTask", "Running async sms sending..");
        ArrayList<String> phoneNumbers = new KontaktDBHelper(context).getAllContactsNumbers();
        String currentPhoneNumber = phoneNumbers.get(params[0] % phoneNumbers.size());
        Helper.sendEmergencySMS(currentPhoneNumber, context);
        return 1;
    }
}