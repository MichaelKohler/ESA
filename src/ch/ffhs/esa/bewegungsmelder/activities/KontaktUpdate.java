package ch.ffhs.esa.bewegungsmelder.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ch.ffhs.esa.bewegungsmelder.models.KontaktDBContract.KontaktTabelle;
import ch.ffhs.esa.bewegungsmelder.R;
import ch.ffhs.esa.bewegungsmelder.helpers.KontaktDBHelper;

public class KontaktUpdate extends Activity {

	public String update_name;
	public String update_telefonnummer;
	public String ObjektID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kontakt_update);
		// Show the Up button in the action bar.
		setupActionBar();
		
		setfields();	
		
		
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
		getMenuInflater().inflate(R.menu.kontakt_update, menu);
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
	



	
	
public String getObjektID() {

		Intent intent = getIntent();
			String ObjektID = intent.getExtras().getString("ID"); 
		        return ObjektID;
		    }
	
	
public String getupdate_telefonnummer() {

Intent intent = getIntent();
	String update_telefonnummer = intent.getExtras().getString("Telefonnummer"); 
        return update_telefonnummer;
    }
	
	
	
public String getupdate_name() {

Intent intent = getIntent();
	String update_name = intent.getExtras().getString("Name"); 
        return update_name;
    }
	
	
	
	
	
	
public void setfields(){
	
	
	EditText t_update_name = (EditText)findViewById(R.id.update_name);
	EditText t_update_telnummer = (EditText)findViewById(R.id.update_telnummer);
	
	String n = getupdate_name();
	String t = getupdate_telefonnummer();
	t_update_name.setText(n);
	t_update_telnummer.setText(getupdate_telefonnummer());
	
}	
	




public void updateSpeichern (View view){
	
	
	
	
		KontaktDBHelper mDbHelper = new KontaktDBHelper(this);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		
		EditText t_update_name = (EditText)findViewById(R.id.update_name);
		EditText t_update_telnummer = (EditText)findViewById(R.id.update_telnummer);
	
			
		String name_new = t_update_name.getText().toString();
		String telefonnummer_new = t_update_telnummer.getText().toString();
		
		ContentValues values = new ContentValues();

		values.put(KontaktTabelle.COLUMN_NAME_NAME, name_new);
		values.put(KontaktTabelle.COLUMN_NAME_NUMBER, telefonnummer_new);
		
		
		
	/*	if(update_name.matches("") || update_telefonnummer.matches ("")){
			
			Toast toast = Toast.makeText(getApplicationContext(), "Bitte beide Felder vollst�ndig ausf�llen", Toast.LENGTH_SHORT);
			toast.show();	
		}
		else{    */
			
		String whereID = getObjektID();
		
			String selection = "_ID ='"+whereID+"'"; ;
			
		
		db.update(
				KontaktTabelle.TABLE_NAME,
				 values,
				    selection,
				    null);

		db.close();

		Intent intent_b = new Intent(this, AddContact.class);
		startActivity(intent_b);
		
		Toast toast = Toast.makeText(getApplicationContext(), "Kontakt: " + name_new + " Telefonnummer: " + telefonnummer_new + " wurde upgedated", Toast.LENGTH_SHORT);
		toast.show();

		
		
	//	}
		
		
	}



protected void onStart() {
    super.onStart();
//setfields();

}



	public void updateAbbrechen (View view){

		EditText t_update_name_clear = (EditText)findViewById(R.id.update_name);
		EditText t_update_telnummer_clear = (EditText)findViewById(R.id.update_telnummer);	
		t_update_name_clear.setText("");	
		t_update_telnummer_clear.setText("");
		
		Intent intent = new Intent(this, AddContact.class);
		startActivity(intent);

		}
	
	
	
	
		
}

	
	
	
	
	


