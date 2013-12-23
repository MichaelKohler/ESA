package ch.ffhs.esa.bewegungsmelder;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Author: Michael Kohler <mkohler@picobudget.com>
 * Contributors:
 *  - Michael Kohler <mkohler@picobudget.com>
 *
 *  runs the Network stuff in the background
 *  */

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

class POSTTask extends AsyncTask<String, String, String> {
    protected String doInBackground(String... params) {
        Log.d("POSTTask", "Running async request..");
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://" + params[0] + ":" + params[1] + "/ping");

        try {
            List<NameValuePair> data = new ArrayList<NameValuePair>(0);
            httpPost.setEntity(new UrlEncodedFormEntity(data));
            httpClient.execute(httpPost);
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return null;
    }
}
