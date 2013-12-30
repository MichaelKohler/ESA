// Author: Mario Aloise (MAS-Student)


package ch.ffhs.esa.bewegungsmelder;

//import ch.ffhs.esa.bewegungsmelder.KontaktDBContract.KontaktTabelle;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ch.ffhs.esa.bewegungsmelder.KontaktDBContract.KontaktTabelle;


public class KontaktManuellHinzu extends Activity {
	private static final String TAG = KontaktManuellHinzu.class.getSimpleName();



	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kontakt_manuell_hinzu);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kontakt_manuell_hinzu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}




	public void speichern (View view){
		Log.d(TAG, "Speichern Methode");
		KontaktDBHelper mDbHelper = new KontaktDBHelper(this);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		//------------------------------------------!!!!QUICK & DIRTY!!!!-----------------------------------------------------//
		//mDbHelper.onCreate(db);  // Die Methode wird sonst nicht gestartet! // 2013-12-29 WiR
		//------------------------------------------!!!!QUICK & DIRTY!!!!-----------------------------------------------------//

		EditText t_name = (EditText)findViewById(R.id.name);
		EditText t_telnummer = (EditText)findViewById(R.id.telnummer);
		String name = t_name.getText().toString();

		String telefonnummer = t_telnummer.getText().toString();
		
		ContentValues values = new ContentValues();

		values.put(KontaktTabelle.COLUMN_NAME_NAME, name);
		values.put(KontaktTabelle.COLUMN_NAME_NUMBER, telefonnummer);

		
		String countQuery = "SELECT  * FROM " + KontaktTabelle.TABLE_NAME;
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
	    cursor.close();
		
		
		if (count < 5){
		
		db.insert(
				KontaktTabelle.TABLE_NAME,
				null,
				values);

		db.close();

		Intent intent = new Intent(this, AddContact.class);
		startActivity(intent);
		
		Toast toast = Toast.makeText(getApplicationContext(), "Kontakt: " + name + " Telefonnummer: " + telefonnummer + " wurde hinzugefügt", Toast.LENGTH_SHORT);
		toast.show();

		

		}
		
		
		else {
			
			Toast.makeText(this, "Sie haben bereits 5 Kontakte eingefügt- bitte zuerst Kontakt löschen" , Toast.LENGTH_SHORT).show();	
			
			Intent intent = new Intent(this, AddContact.class);
			startActivity(intent);
			
		}
		
		
	}


	
		
}



