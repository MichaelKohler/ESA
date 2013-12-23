package ch.ffhs.esa.bewegungsmelder;

//import ch.ffhs.esa.bewegungsmelder.KontaktDBContract.KontaktTabelle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ch.ffhs.esa.bewegungsmelder.KontaktDBContract.KontaktTabelle;


public class KontaktManuellHinzu extends Activity {

	
	
	
	@Override
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
		
		KontaktDBHelper mDbHelper = new KontaktDBHelper(this);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();


		EditText t_vorname = (EditText)findViewById(R.id.vorname);
		EditText t_nachname = (EditText)findViewById(R.id.nachname);
		EditText t_telnummer = (EditText)findViewById(R.id.telnummer);
		String vorname = t_vorname.getText().toString();
		String nachname = t_nachname.getText().toString();
		String telefonnummer = t_telnummer.getText().toString();
		Toast toast = Toast.makeText(getApplicationContext(), "Vorname: " + vorname + " Nachname: " + nachname + " Telefonnummer: " + telefonnummer, Toast.LENGTH_SHORT);
		toast.show();
		
		ContentValues values = new ContentValues();
		values.put(KontaktTabelle.COLUMN_NAME_VORNAME, vorname);
		values.put(KontaktTabelle.COLUMN_NAME_NACHNAME, nachname);
		values.put(KontaktTabelle.COLUMN_NAME_NUMBER, telefonnummer);

		db.insert(
				KontaktTabelle.TABLE_NAME,
				null,
		         values);


		//updateList();
		}

	
		
		
		
	}
	
	

