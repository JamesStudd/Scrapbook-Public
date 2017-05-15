package uk.ac.tees.p4136175.scrapbook;

/**
 * Created by p4136175 on 07/03/2017.
 */

/**
 * This will be used for each 'adventure' that the user adds
 */
public class AdventureEntry {

    // Name of the table that will be used
    public static final String TABLE = "AdventureEntryNew";

    // Labels Table Column Names
    public static final String KEY_ID = "_id";
    public static final String KEY_note = "notetext";
    public static final String KEY_image = "image";
    public static final String KEY_datetime = "datetime";
    public static final String KEY_loc_name = "loc_name";

    // property help us to keep data
    public int ID;
    public String note_text;
    public byte[] image;
    public String datetime;
    public String loc_name;
}
