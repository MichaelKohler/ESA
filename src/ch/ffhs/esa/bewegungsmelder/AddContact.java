// Author: Mario Aloise (MAS-Student)

package ch.ffhs.esa.bewegungsmelder;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import ch.ffhs.esa.bewegungsmelder.KontaktDBContract.KontaktTabelle;

public class AddContact extends Activity implements
OnItemClickListener {

	private static final int ADD_KONTAKT_DIALOG = 1;
	private static final String TAG = AddContact.class.getSimpleName();
	private ListView listView;
	private  int geklicktesItem;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		// Show the Up button in the action bar.
		setupActionBar();
		updateList();
		listView = (ListView) findViewById(R.id.updateList);
		listView.setOnItemClickListener(this);
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
		getMenuInflater().inflate(R.menu.add_contact, menu);
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

	
	
	/**
	 * Diese Methode öffnet den Dialog mit der Wahlmöglichkeit für das hinzufügen der Kontakte. 
	 * @author Mario Aloise
	 */
	
	public void contactAdd (View view){
		
		
		FragmentManager manager = getFragmentManager();
		KontaktDialogFragment d = new KontaktDialogFragment ();
		d.show(manager, "KontaktDialogFragment");
			
		
	}

	/**
	 * Diese Methode wird jedesmal aufgerufen, wenn die Activity erstellt wird und sorgt dafür, dass die Kontaktliste
	 * immer aktualisiert wird. 
	 * @author Mario Aloise
	 */	
	
	
	public void updateList(){
		Log.d(TAG, "Updating List");
		
		
		List<Kontakt> updatelist = new ArrayList<Kontakt>();
		
		
		KontaktDBHelper mDbHelper = new KontaktDBHelper(this);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		String[] projection = {

				KontaktTabelle._ID,
				KontaktTabelle.COLUMN_NAME_NAME,
				KontaktTabelle.COLUMN_NAME_NUMBER,

		};

		Cursor c = null;

		try {
			c = db.query(KontaktTabelle.TABLE_NAME, projection, null, null, null, null, null);
		} catch (Exception e) {
			c = db.rawQuery("SELECT  * FROM " + KontaktTabelle.TABLE_NAME, null);
			for (int i = 0; i < c.getColumnCount();i++){
				Log.d(TAG, "Catched an exception on query!!!! Raw Query of column " +i + ":" + c.getColumnName(i) );
			}
			e.printStackTrace();
		}

		Log.d(TAG, "Cursor: " + c.toString());

		
	
		if (c.moveToFirst()){

			do {
			
			String ID = c.getString(0);	
			String name = c.getString(1);
			String phoneNumber = c.getString(2);
			
			Kontakt objContact = new Kontakt();
			objContact.setName(name);
			objContact.setPhoneNo(phoneNumber);
			objContact.setObjektID(ID);
			
			updatelist.add(objContact);
		
			}	 while (c.moveToNext());
		}

		
		KontaktAdapter objAdapter = new KontaktAdapter(this, R.layout.alluser_row, updatelist);
		setContentView(R.layout.activity_add_contact);
		ListView list = (ListView) findViewById(R.id.updateList);
		list.setAdapter(objAdapter);
		
		

		
	}

	
	/**
	 * Diese Methode öffnet den Dialog mit der Wahlmöglichkeit für löschen, verändern und setzen des 
	 * Primary Flags eines gewählten Kontaktes (primary Flag funktioniert noch nicht). 
	 * @author Mario Aloise
	 */	
	
	@Override
	public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
		
		
	
		
		FragmentManager manager = getFragmentManager();
		KontaktManageDialogFragment d = new KontaktManageDialogFragment ();
		d.show(manager, "KontaktManageDialogFragment");
		
		geklicktesItem = position;
		
	
		
	}
	

	/**
	 * Diese Methode erlaubt das löschen eines Kontaktes.
	 * @author Mario Aloise
	 */
	
	
public void deleteKontakt(){
		
		ListView list = (ListView) findViewById(R.id.updateList);
		Object o = list.getItemAtPosition(geklicktesItem);
		Kontakt k = (Kontakt)o;
		String s = k.getObjektID();
		KontaktDBHelper mDbHelper = new KontaktDBHelper(this);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		String selection = "_ID ='"+s+"'";
		db.delete(KontaktTabelle.TABLE_NAME, selection, null);
		
		 
		
	}
	
/**
 * Diese Methode erlaubt das editieren eines Kontaktes.
 * @author Mario Aloise
 */
	
	public void modifyKontakt(){
		
		ListView list = (ListView) findViewById(R.id.updateList);
		Object o = list.getItemAtPosition(geklicktesItem);
		Kontakt k = (Kontakt)o;
		String tel = k.getPhoneNo();
		String name = k.getName();
		String id = k.getObjektID();
		
		Intent intent = new Intent(this, KontaktUpdate.class);
		intent.putExtra("Telefonnummer", tel);
		intent.putExtra("Name", name);
		intent.putExtra("ID", id);
		startActivity(intent);
		
	}	
	


	
	};
	
	

