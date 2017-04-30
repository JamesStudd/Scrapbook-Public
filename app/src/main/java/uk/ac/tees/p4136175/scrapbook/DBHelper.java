package uk.ac.tees.p4136175.scrapbook;

/**
 * Created by p4136175 on 10/03/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * THis class allows access to the database
 */
public class DBHelper extends SQLiteOpenHelper {

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "crud.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_JOURNAL = "CREATE TABLE " + AdventureEntry.TABLE  + "("
                + AdventureEntry.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + AdventureEntry.KEY_note + " TEXT, "
                + AdventureEntry.KEY_image + " BLOB, "
                + AdventureEntry.KEY_datetime + " REAL, "
                + AdventureEntry.KEY_loc_long + " REAL, "
                + AdventureEntry.KEY_loc_lat + " REAL )";

        db.execSQL(CREATE_TABLE_JOURNAL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + AdventureEntry.TABLE);

        // Create tables again
        onCreate(db);

    }

}
