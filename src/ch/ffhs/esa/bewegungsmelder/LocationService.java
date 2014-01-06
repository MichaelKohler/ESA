package ch.ffhs.esa.bewegungsmelder;

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Author: Ralf Wittich <bullit@gmx.ch>
 * Contributors:
 * 	- Ralf Wittich <bullit@gmx.ch> 
 * 
 * Ermittelt den aktuellen Standort mit Genauigkeitsangabe
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service implements LocationListener{

	private static final String TAG = LocationService.class.getSimpleName();
	public static final String LOCATION_ACTION = "ch.ffhs.esa.bewegungsmelder.LOCATION_ACTION";
	int count = 0;	// Counts the locationChanged events

	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();

	}

	/* LocationManager updates entfernen
	 * 
	 * (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		LocationManager locationMgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationMgr.removeUpdates(this);
		super.onDestroy();
	}

	/* SystemService definieren und updates anfordern.
	 * 
	 * (non-Javadoc)
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		LocationManager locationMgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		count = 0;
	}

	/* Methode die aufgerufen wird wenn die Position sich veraendert. 
	 * Ein Intent wird mit den Positionsdaten und dem Genauigkeitsindikator als Broadcast versendet. 
	 * 
	 * (non-Javadoc)
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		float mLat = (float) location.getLatitude();
		float mLong = (float) location.getLongitude();
		float mAcc = location.getAccuracy();

		Log.d(TAG, "Location: Lat: " + mLat + " Long: " + mLong + " Accuracy: " + mAcc + "LocChanged: " +count);
		Intent i = new Intent(LOCATION_ACTION);
		
		// Send 9999 after 20 locationChanged events 
		if(count >= 20){
			mAcc = 9999;
		}
		
		i.putExtra("LATITUDE", mLat);
		i.putExtra("LONGITUDE", mLong);
		i.putExtra("ACCURACY", mAcc);
		sendBroadcast(i);
		count++;
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}


}
