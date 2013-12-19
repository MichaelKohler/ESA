package ch.ffhs.esa.bewegungsmelder;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class KontaktListeTestActivity extends ListActivity {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		displayContacts();
		
		
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
		getMenuInflater().inflate(R.menu.kontakt_liste_test, menu);
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
	
	
	private void displayContacts() {
	     
		 ArrayList<String> data = new ArrayList<String>();
		
		
	      ContentResolver cr = getContentResolver();
	        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
	                null, null, null, null);
	        if (cur.getCount() > 0) {
	            while (cur.moveToNext()) {
	                  String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
	                  String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	                
	                  if (Integer.parseInt(cur.getString(
	                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
	                     Cursor pCur = cr.query(
	                               ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	                               null,
	                               ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
	                               new String[]{id}, null);
	                     while (pCur.moveToNext()) {
	                         String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	                         data.add (phoneNo);
	                        
	                     }
	                    pCur.close();
	                }
	            }
	        }
	        setListAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,data));
	    }
	
	

	
	

}
