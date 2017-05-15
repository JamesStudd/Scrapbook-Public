package uk.ac.tees.p4136175.scrapbook;

/**
 * Created by p4136175 on 10/03/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class gets a repository of all the adventure entries
 */
public class AdventureRepo {

    // Local instance of DBHelper, used to access the database
    private DBHelper dbHelper;

    /**
     * Constructor
     * @param context Activity
     */
    public AdventureRepo(Context context) { dbHelper = new DBHelper(context);}

    /**
     * Insert an entry into the DB
     * @param adv The entrty
     * @return Whether or not the insert was successful
     */
    public int insert(AdventureEntry adv){

        // Create a database entry passing in all the variables of adventure
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(adv.KEY_note, adv.note_text);
        values.put(adv.KEY_image, adv.image);
        values.put(adv.KEY_datetime, adv.datetime);
        values.put(adv.KEY_loc_name, adv.loc_name);

        long adv_Id = db.insert(adv.TABLE, null, values);
        db.close();
        return (int) adv_Id;
    }

    /**
     * Deletes an adventure entry from the database
     * @param adv_Id The ID of the adventure wanting to be deleted
     */
    public void delete(int adv_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(AdventureEntry.TABLE, AdventureEntry.KEY_ID + "= ?", new String[] { String.valueOf(adv_Id) });
        db.close(); // Closing database connection
    }

    /**
     * Update an entry in the database
     * @param adv The adventure to be updated
     */
    public void update(AdventureEntry adv) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Updates each variable in the database
        values.put(adv.KEY_note, adv.note_text);
        values.put(adv.KEY_image, adv.image);
        values.put(adv.KEY_datetime, adv.datetime);
        values.put(adv.KEY_loc_name, adv.loc_name);


        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(adv.TABLE, values, adv.KEY_ID + "= ?", new String[] { String.valueOf(adv.ID) });
        db.close(); // Closing database connection
    }

    /**
     * Gets a list of all the current adventure entries, the hashmap will consist of:
     * The note text of the entry
     * The date of the entry
     * @return
     */
    public ArrayList<HashMap<String, String>>  getAdventureEntryList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                AdventureEntry.KEY_ID + "," +
                AdventureEntry.KEY_note + "," +
                AdventureEntry.KEY_image + "," +
                AdventureEntry.KEY_datetime + "," +
                AdventureEntry.KEY_loc_name +
                " FROM " + AdventureEntry.TABLE;

        // Initiallise the list to be returned
        ArrayList<HashMap<String, String>> adventureEntryList = new ArrayList<HashMap<String, String>>();

        // Intiailise a cursor, this can select entries in the table
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        // If there are entries in the db
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> adventureEntry = new HashMap<String, String>();
                adventureEntry.put("id", cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_ID)));

                // The temp variable is used so that if the note text is above 50 characters, the
                // last 3 characters will be changed to "..." so that it doesn't take up too much space
                String temp = "";
                if (cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_note)).length() > 50){
                    temp = cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_note)).substring(0, 50) + "...";
                } else {
                    temp = cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_note));
                }
                adventureEntry.put("note_text", temp);
                adventureEntry.put("datetime", cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_datetime)));
                adventureEntry.put("location", cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_loc_name)));
                adventureEntryList.add(adventureEntry);

            } while (cursor.moveToNext()); // whilst there are still entries to go to
        }

        cursor.close(); // These are used for safety
        db.close(); //       ^    ^    ^   ^   ^
        return adventureEntryList;

    }

    /**
     * Returns a list of all the current adventure entries, this hashmap will consist of:
     * Id of the current adventure
     * Image of the current adventure
     * @return
     */
    public ArrayList<HashMap<String, Object>> getAdventureEntryGrid() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                AdventureEntry.KEY_ID + "," +
                AdventureEntry.KEY_note + "," +
                AdventureEntry.KEY_image + "," +
                AdventureEntry.KEY_datetime + "," +
                AdventureEntry.KEY_loc_name +
                " FROM " + AdventureEntry.TABLE;

        ArrayList<HashMap<String, Object>> adventureEntryGrid = new ArrayList<HashMap<String, Object>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor != null && cursor.moveToFirst()) {
            do {
                HashMap<String, Object> adventureEntry = new HashMap<String, Object>();
                adventureEntry.put("id", cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_ID)));
                adventureEntry.put("image", cursor.getBlob(cursor.getColumnIndex(AdventureEntry.KEY_image)));
                adventureEntryGrid.add(adventureEntry);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return adventureEntryGrid;
    }

    /**
     * Return a single adventure by passing in the requested ID
     * @param Id The requested adventure
     * @return the adventure entry
     */
    public AdventureEntry getAdventureById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                AdventureEntry.KEY_ID + "," +
                AdventureEntry.KEY_note + "," +
                AdventureEntry.KEY_image + "," +
                AdventureEntry.KEY_datetime + "," +
                AdventureEntry.KEY_loc_name +
                " FROM " + AdventureEntry.TABLE
                + " WHERE " +
                AdventureEntry.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        // Create a blank entry for variables to be passed into
        AdventureEntry aEntry = new AdventureEntry();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {

            do { // Basically casts each variable to the correct type

                aEntry.ID =cursor.getInt(cursor.getColumnIndex(AdventureEntry.KEY_ID));
                //System.out.println("id: " + aEntry.ID);

                aEntry.note_text = cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_note));
                //System.out.println("note text: " + aEntry.note_text);

                aEntry.image  = cursor.getBlob(cursor.getColumnIndex(AdventureEntry.KEY_image));
                //System.out.println("image: " + aEntry.image);

                aEntry.datetime = cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_datetime));
                //System.out.println("datetime: " + aEntry.datetime);

                aEntry.loc_name = cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_loc_name));
                //System.out.println("loc lat: " + aEntry.loc_lat);


            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return aEntry;
    }

}
