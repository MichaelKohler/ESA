package ch.ffhs.esa.bewegungsmelder;

import android.provider.BaseColumns;

public final class KontaktDBContract {
	
	public KontaktDBContract() {}
	
	public static abstract class KontaktTabelle implements BaseColumns {
        public static final String TABLE_NAME = "Kontakte";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_VORNAME = "Vorname";
        public static final String COLUMN_NAME_NACHNAME = "Nachname";
        public static final String COLUMN_NAME_NUMBER = "Telefonnummer";
    }
	
	private static final String TEXT_TYPE = "TEXT";
private static final String COMMA_SEP = ",";
private static final String SQL_CREATE_ENTRIES =
    "CREATE TABLE " + KontaktTabelle.TABLE_NAME + " (" +
    KontaktTabelle._ID + " INTEGER PRIMARY KEY," +
    KontaktTabelle.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
    KontaktTabelle.COLUMN_NAME_VORNAME + TEXT_TYPE + COMMA_SEP +
    KontaktTabelle.COLUMN_NAME_NACHNAME + TEXT_TYPE + COMMA_SEP +
	KontaktTabelle.COLUMN_NAME_NUMBER + TEXT_TYPE + COMMA_SEP +
    " )";

private static final String SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS " + KontaktTabelle.TABLE_NAME;	
	
	

}
