package ch.ffhs.esa.bewegungsmelder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KontaktDBHelper extends SQLiteOpenHelper {
	
	
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "KontaktDB.db";
	private static final String SQL_CREATE_ENTRIES = null;
	private static final String SQL_DELETE_ENTRIES = null;
    public static final String COLUMN_NAME_ENTRY_ID = "entryid";
    public static final String COLUMN_NAME_VORNAME = "Vorname";
    public static final String COLUMN_NAME_NACHNAME = "Nachname";
    public static final String COLUMN_NAME_NUMBER = "Telefonnummer";
	
	
	
	
	
	
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
