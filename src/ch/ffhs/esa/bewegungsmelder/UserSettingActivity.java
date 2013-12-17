package ch.ffhs.esa.bewegungsmelder;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Author: Michael Kohler <mkohler@picobudget.com>
 * Contributors:
 * 	- your name <your@mail.com> */

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

public class UserSettingActivity extends PreferenceActivity {
	 
	public final static String PREFS_NAME = "activity_settings";
	private static final String TAG = UserSettingActivity.class.getSimpleName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.layout.activity_settings);
        String preferencesName = this.getPreferenceManager().getSharedPreferencesName();
        Log.d(TAG, "Default Shared Name: "+preferencesName);
    }
}