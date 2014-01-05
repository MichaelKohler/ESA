// Author: Mario Aloise (MAS-Student)

package ch.ffhs.esa.bewegungsmelder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ch.ffhs.esa.bewegungsmelder.KontaktDBContract.KontaktTabelle;

import java.util.ArrayList;

public class KontaktDBHelper extends SQLiteOpenHelper {
	private static final String TAG = KontaktDBHelper.class.getSimpleName();
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "KontaktDB.db";

    
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	
	
	private static final String SQL_CREATE_ENTRIES =
		    "CREATE TABLE " + KontaktTabelle.TABLE_NAME + " (" +
		    KontaktTabelle._ID + " INTEGER PRIMARY KEY," +
		   
		    KontaktTabelle.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
			KontaktTabelle.COLUMN_NAME_NUMBER + TEXT_TYPE + 
		    " )"; 
   
	private static final String SQL_DELETE_ENTRIES =
		    "DROP TABLE IF EXISTS " + KontaktTabelle.TABLE_NAME;		
	
	
    public KontaktDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);    
    }

    public void onCreate(SQLiteDatabase db) {
    	Log.d(TAG, "Table creation executed!");
    	//db.execSQL(SQL_DELETE_ENTRIES); // Benoetigt um falsche DB zu l�schen :-)
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.d(TAG, "onUpgrade!");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Log.d(TAG, "onDowngrade!");
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * returns all contact numbers
     *
     * @author Michael Kohler
     * @return ArrayList with all the contact numbers
     */
    public ArrayList<String> getAllContactsNumbers() {
        ArrayList<String> contactList = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + KontaktTabelle.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all results
        if (cursor.moveToFirst()) {
            do {
                contactList.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

}
