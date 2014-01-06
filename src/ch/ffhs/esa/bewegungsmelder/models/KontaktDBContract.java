
	/**
	 * Diese 'Contract' Klasse definiert das Layout der SQL lite DD. 
	  * @author Mario Aloise
	 */

package ch.ffhs.esa.bewegungsmelder.models;

import android.provider.BaseColumns;

    public final class KontaktDBContract {
	
	public KontaktDBContract() {}
	
	public static abstract class KontaktTabelle implements BaseColumns {
        public static final String TABLE_NAME = "Kontakte";

        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_NUMBER = "Telefonnummer";
    }
	
	private static final String TEXT_TYPE = " TEXT";
private static final String COMMA_SEP = ",";
private static final String SQL_CREATE_ENTRIES =
    "CREATE TABLE " + KontaktTabelle.TABLE_NAME + " (" +
    KontaktTabelle._ID + " INTEGER PRIMARY KEY," +
     KontaktTabelle.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
	KontaktTabelle.COLUMN_NAME_NUMBER + TEXT_TYPE + COMMA_SEP +
    " )";

private static final String SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS " + KontaktTabelle.TABLE_NAME;	
	



}
