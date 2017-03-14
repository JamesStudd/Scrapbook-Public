package uk.ac.tees.p4136175.scrapbook;

/**
 * Created by p4136175 on 10/03/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class AdventureRepo {

    private DBHelper dbHelper;

    public AdventureRepo(Context context) { dbHelper = new DBHelper(context);}

    public int insert(AdventureEntry adv){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(adv.KEY_note, adv.note_text);
        values.put(adv.KEY_image, adv.image);
        values.put(adv.KEY_datetime, adv.datetime);
        values.put(adv.KEY_loc_lat, adv.loc_lat);
        values.put(adv.KEY_loc_long, adv.loc_lang);

        long adv_Id = db.insert(adv.TABLE, null, values);
        db.close();
        return (int) adv_Id;
    }

    public void delete(int adv_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(AdventureEntry.TABLE, AdventureEntry.KEY_ID + "= ?", new String[] { String.valueOf(adv_Id) });
        db.close(); // Closing database connection
    }

    public void update(AdventureEntry adv) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(adv.KEY_note, adv.note_text);
        values.put(adv.KEY_image, adv.image);
        values.put(adv.KEY_datetime, adv.datetime);
        values.put(adv.KEY_loc_lat, adv.loc_lat);
        values.put(adv.KEY_loc_long, adv.loc_lang);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(adv.TABLE, values, adv.KEY_ID + "= ?", new String[] { String.valueOf(adv.ID) });
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>>  getAdventureEntryList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                AdventureEntry.KEY_ID + "," +
                AdventureEntry.KEY_note + "," +
                AdventureEntry.KEY_image + "," +
                AdventureEntry.KEY_datetime + "," +
                AdventureEntry.KEY_loc_long + "," +
                AdventureEntry.KEY_loc_lat +
                " FROM " + AdventureEntry.TABLE;

        ArrayList<HashMap<String, String>> adventureEntryList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> adventureEntry = new HashMap<String, String>();
                adventureEntry.put("id", cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_ID)));
                adventureEntry.put("note_text", cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_note)));
                adventureEntryList.add(adventureEntry);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return adventureEntryList;

    }

    public AdventureEntry getAdventureById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                AdventureEntry.KEY_ID + "," +
                AdventureEntry.KEY_note + "," +
                AdventureEntry.KEY_image + "," +
                AdventureEntry.KEY_datetime + "," +
                AdventureEntry.KEY_loc_long + "," +
                AdventureEntry.KEY_loc_lat +
                " FROM " + AdventureEntry.TABLE
                + " WHERE " +
                AdventureEntry.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        AdventureEntry aEntry = new AdventureEntry();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {

            do {

                aEntry.ID =cursor.getInt(cursor.getColumnIndex(AdventureEntry.KEY_ID));
                System.out.println("id: " + aEntry.ID);

                aEntry.note_text = cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_note));
                System.out.println("note text: " + aEntry.note_text);

                aEntry.image  = cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_image));
                System.out.println("image: " + aEntry.image);

                aEntry.datetime = cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_datetime));
                System.out.println("datetime: " + aEntry.datetime);

                aEntry.loc_lat = cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_loc_lat));
                System.out.println("loc lat: " + aEntry.loc_lat);

                aEntry.loc_lang = cursor.getString(cursor.getColumnIndex(AdventureEntry.KEY_loc_long));
                System.out.println("loc lang: " + aEntry.loc_lang);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return aEntry;
    }

}
