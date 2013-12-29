// Author: Mario Aloise (MAS-Student)

package ch.ffhs.esa.bewegungsmelder;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AddContact extends Activity {

	private static final int ADD_KONTAKT_DIALOG = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
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

	
	public void contactAdd (View view){
		
		/*	
					Intent intent = new Intent(this, KontaktManuellHinzu.class);
		startActivity(intent);
		
	*/
		
		
		FragmentManager manager = getFragmentManager();
		KontaktDialogFragment d = new KontaktDialogFragment ();
		d.show(manager, "KontaktDialogFragment");
		
		
		
	}

	
	
	
	
	
		
	};
	
	

