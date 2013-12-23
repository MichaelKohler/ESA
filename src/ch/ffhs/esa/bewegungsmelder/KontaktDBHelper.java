// Author: Mario Aloise (MAS-Student)

package ch.ffhs.esa.bewegungsmelder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ch.ffhs.esa.bewegungsmelder.KontaktDBContract.KontaktTabelle;

public class KontaktDBHelper extends SQLiteOpenHelper {
	
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "KontaktDB.db";

    
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	
	
	private static final String SQL_CREATE_ENTRIES =
		    "CREATE TABLE " + KontaktTabelle.TABLE_NAME + " (" +
		    KontaktTabelle._ID + " INTEGER PRIMARY KEY," +
		    KontaktTabelle.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
		    KontaktTabelle.COLUMN_NAME_VORNAME + TEXT_TYPE + COMMA_SEP +
		    KontaktTabelle.COLUMN_NAME_NACHNAME + TEXT_TYPE + COMMA_SEP +
			KontaktTabelle.COLUMN_NAME_NUMBER + TEXT_TYPE + 
		    " )"; 
   
	private static final String SQL_DELETE_ENTRIES =
		    "DROP TABLE IF EXISTS " + KontaktTabelle.TABLE_NAME;		
	
	
    public KontaktDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    
    
    

}
