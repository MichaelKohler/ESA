package ch.ffhs.esa.bewegungsmelder.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import ch.ffhs.esa.bewegungsmelder.models.KontaktAdapter;
import ch.ffhs.esa.bewegungsmelder.models.KontaktDBContract.KontaktTabelle;
import ch.ffhs.esa.bewegungsmelder.R;
import ch.ffhs.esa.bewegungsmelder.helpers.KontaktDBHelper;
import ch.ffhs.esa.bewegungsmelder.models.Kontakt;

public class Kontaktliste extends Activity implements
OnItemClickListener {

private ListView listView;
private List<Kontakt> list = new ArrayList<Kontakt>();

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_kontaktliste);
setupActionBar();


listView = (ListView) findViewById(R.id.kontaktliste);
listView.setOnItemClickListener(this);

Cursor phones = getContentResolver().query(
		ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
		null, null);
while (phones.moveToNext()) {

	String name = phones
			.getString(phones
					.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

	String phoneNumber = phones
			.getString(phones
					.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

	Kontakt objContact = new Kontakt();
	objContact.setName(name);
	objContact.setPhoneNo(phoneNumber);
	list.add(objContact);

}
phones.close();

KontaktAdapter objAdapter = new KontaktAdapter(
		Kontaktliste.this, R.layout.alluser_row, list);
listView.setAdapter(objAdapter);


}

/**
 * Durch diese Methode wird ein, vom User gew�hlter Kontakt in die DB geschrieben. If-statement sorgt daf�r, dass h�chstens 5 Kontakte hinzugef�gt werden k�nnen.
  * @author Mario Aloise
 */

@Override
public void onItemClick(AdapterView<?> listview, View v, int position,
	long id) {
Kontakt bean = (Kontakt) listview.getItemAtPosition(position);
 String ausgabe_name = bean.getName();
 String ausgabe_tel = bean.getPhoneNo();
 

 KontaktDBHelper mDbHelper = new KontaktDBHelper(this);
	SQLiteDatabase db = mDbHelper.getWritableDatabase();
	
	ContentValues values = new ContentValues();
	values.put(KontaktTabelle.COLUMN_NAME_NAME, ausgabe_name);
		values.put(KontaktTabelle.COLUMN_NAME_NUMBER, ausgabe_tel);

	
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
	
	Toast.makeText(this, "Kontakt: " + ausgabe_name + " Telefonnummer: " + ausgabe_tel  + " wurde hinzugefuegt", Toast.LENGTH_SHORT).show();
	
	Intent intent = new Intent(this, AddContact.class);
	startActivity(intent);

	}
		
		
else {

			Toast.makeText(this, "Sie haben bereits 5 Kontakte eingefuegt- bitte zuerst Kontakt loeschen" , Toast.LENGTH_SHORT).show();
			
			Intent intent = new Intent(this, AddContact.class);
			startActivity(intent);
			
		}	
		
		
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
		getMenuInflater().inflate(R.menu.kontaktliste, menu);
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

}
