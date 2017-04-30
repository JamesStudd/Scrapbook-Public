package uk.ac.tees.p4136175.scrapbook;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * AdventureList will be how the other classes retrieve a list of all the current adventures
 * it also has a few different methods to get a different type of list:
 * The IDs and Note Text (used in the classic view of adventures)
 * The IDs and Images (used in the grid view of adventures)
 */
public class AdventureList extends ListActivity implements View.OnClickListener{

    // Components on the adventure_list activity
    TextView adventure_id;
    Switch changeView;
    TextView noteView, imageView;
    ListView listView;
    GridView gridView;

    // Boolean just to keep track of state
    Boolean switchState = false;

    // Custom image adapter used to list each image from adventures
    ImageAdapter imageAdapter;

    // Local repo to access the adventures
    AdventureRepo repo;

    /**
     * When the activity is called
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure_list);

        // Initialise some components from the activity
        noteView = (TextView) findViewById(R.id.noteViewText);
        noteView.setVisibility(View.VISIBLE);
        imageView = (TextView) findViewById(R.id.imageViewText);
        imageView.setVisibility(View.VISIBLE);

        // If the user uses the switch, the switchstate will change, and the list will be updated
        // to show the new type of list (gridview or listview)
        changeView = (Switch) findViewById(R.id.switch1);
        changeView.setVisibility(View.VISIBLE);
        changeView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                switchState = changeView.isChecked();
                refreshList();
            }
        });

        // Initialise repo
        repo = new AdventureRepo(this);

        // Use repo to initialise the imageAdapter
        imageAdapter = new ImageAdapter(this, repo);
        gridView = (GridView) findViewById(R.id.gridView1);

        // This gets any extras when the activity is called, the extras will be passed in from
        // the HomeScreen page. If the user searches a date, the date will be passed in, if the
        // user searches a note, that will be passed in instead.
        Bundle b = getIntent().getExtras();
        if (b!=null){
            //System.out.println(b.getString("date"));
            if(b.containsKey("date")){
                // Calls the method using the searched date
                displayEntriesByDate(b.getString("date"));
            } else if (b.containsKey("note")){
                // Calls the method using the searched note
                displayEntriesByNote(b.getString("note"));
            }

        } else {
            // If there are no extras, the refreshList will just be called instead
            refreshList();
        }

        // imageAdapter will call the method getImages() to load all the images from current advs
        imageAdapter.getImages();


    }

    /**
     * This will create a list of the current adventures
     * If switchstate is true, the list will be a gridview of the images from each adventure
     * If switchstate is false, the list will be a normal listview of the notes and dates
     */
    private void refreshList(){
        // Initialise the repo
        AdventureRepo repo = new AdventureRepo(this);

        // If the switchstate is true
        if(switchState){
            // Set the grid view visibility to false
            gridView.setVisibility(View.VISIBLE);

            // Get the list view and then set it invisible
            ListView lv = getListView();
            lv.setVisibility(View.INVISIBLE);

            // Create an ArrayList of HashMaps using the repo method "getAdventureEntryGrid()"
            // This returns the ID and Image of each adventure
            final ArrayList<HashMap<String, Object>> adventureList =  repo.getAdventureEntryGrid();
            if(adventureList.size()!=0) {
                // Intiailise the grid view
                GridView gv = (GridView) findViewById(R.id.gridView1);
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    /**
                     * If an item in the grid is clicked
                     * @param parent Parent component
                     * @param view View will be the component
                     * @param position Position returns the index the item is on the grid
                     * @param id
                     */
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the adventure ID from the viw_grid_entry activity
                        adventure_id = (TextView) view.findViewById(R.id.adventure_Id_grid);
                        // Get the current position, convert it to an integer, use it to get the ID from adventure list
                        // then pass that into the String adventureId
                        String adventureId = String.valueOf(adventureList.get(Integer.parseInt(String.valueOf(position))).get("id"));
                        // Call the makeAdventure activity, passing in the adventureId so values can be set
                        Intent objIndent = new Intent(getApplicationContext(),MakeAdventure.class);
                        objIndent.putExtra("adventure_Id", Integer.parseInt( adventureId));
                        startActivity(objIndent);
                    }
                });
                // Set the activity of the gridview to the imageAdapter
                gv.setAdapter(imageAdapter);
            }else{
                // Otherwise, print "no adventures"
                Toast.makeText(this,"No adventures!",Toast.LENGTH_SHORT).show();
            }
        } else {
            // If the switchstate is false, set the gridview to invisible
            gridView.setVisibility(View.INVISIBLE);

            // Initiallise a listview, and set the visibility to visible
            ListView lv = getListView();
            lv.setVisibility(View.VISIBLE);

            // Create an array list of hashmaps from the return of getAdventureEntryList,
            // this returns the adventure Id and note text
            ArrayList<HashMap<String, String>> adventureList =  repo.getAdventureEntryList();
            if(adventureList.size()!=0) {
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    /**
                     * If an item is the listview is clicked
                     * @param parent Parent component
                     * @param view Item clicked
                     * @param position Index of the item
                     * @param id Id of the item
                     */
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // Get the adventureId from the adventureId component in view_adventure_entry
                        adventure_id = (TextView) view.findViewById(R.id.adventure_Id);
                        String adventureId = adventure_id.getText().toString();
                        Intent objIndent = new Intent(getApplicationContext(),MakeAdventure.class);
                        objIndent.putExtra("adventure_Id", Integer.parseInt( adventureId));
                        startActivity(objIndent);
                    }
                });
                ListAdapter adapter = new SimpleAdapter( AdventureList.this,adventureList, R.layout.activity_view_adventure_entry, new String[] { "id","note_text","datetime"}, new int[] {R.id.adventure_Id, R.id.adventure_note, R.id.adventure_datetime});
                setListAdapter(adapter);
            }else{
                Toast.makeText(this,"No adventures!",Toast.LENGTH_SHORT).show();
            }

        }


    }


    public void displayEntriesByDate(String date){

        // Set the top widgets to invisible - Text views and switch
        changeView.setVisibility(View.INVISIBLE);
        noteView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);

        ListView lv = getListView();
        lv.setVisibility(View.VISIBLE);


        // adventureList is all the current entries
        ArrayList<HashMap<String, String>> adventureList =  repo.getAdventureEntryList();

        // adventureListWithCorrectDates is empty to start but will be populated with only entries
        // on a certain date
        ArrayList<HashMap<String, String>> adventureListWithCorrectDates = new ArrayList<>();

        // Loop through each hashmap in adventureList, if the entry contains the date
        // passed into the function, the entry will be added to adventurListWithCorrectDates
        int i = 0;
        for (HashMap<String, String> entry : adventureList){
            if(entry.containsValue(date)){
                adventureListWithCorrectDates.add(adventureList.get(i));
            }
            i++;
        }

        if(adventureListWithCorrectDates.size()!=0) {
            //System.out.println("There is entries");
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adventure_id = (TextView) view.findViewById(R.id.adventure_Id);
                    String adventureId = adventure_id.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(),MakeAdventure.class);
                    objIndent.putExtra("adventure_Id", Integer.parseInt( adventureId));
                    startActivity(objIndent);
                }
            });
            ListAdapter adapter = new SimpleAdapter( AdventureList.this,adventureListWithCorrectDates, R.layout.activity_view_adventure_entry, new String[] { "id","note_text","datetime"}, new int[] {R.id.adventure_Id, R.id.adventure_note, R.id.adventure_datetime});
            setListAdapter(adapter);
        }else{
            Toast.makeText(this,"No adventures!",Toast.LENGTH_SHORT).show();
        }


    }

    public void displayEntriesByNote(String note){

        // Set the top widgets to invisible - Text views and switch
        changeView.setVisibility(View.INVISIBLE);
        noteView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);

        ListView lv = getListView();
        lv.setVisibility(View.VISIBLE);


        // adventureList is all the current entries
        ArrayList<HashMap<String, String>> adventureList =  repo.getAdventureEntryList();

        // adventureListWithCorrectNotes will be populated with only entries
        // that contain any of the note text
        ArrayList<HashMap<String, String>> adventureListWithCorrectNotes =  new ArrayList<>();

        int i = 0;
        for (HashMap<String, String> entry : adventureList){
            String s = entry.get("note_text");

            if (s.toLowerCase().indexOf(note.toLowerCase()) != -1){
                adventureListWithCorrectNotes.add(adventureList.get(i));
            }

            i++;
        }

        if(adventureListWithCorrectNotes.size()!=0) {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adventure_id = (TextView) view.findViewById(R.id.adventure_Id);
                    String adventureId = adventure_id.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(),MakeAdventure.class);
                    objIndent.putExtra("adventure_Id", Integer.parseInt( adventureId));
                    startActivity(objIndent);
                }
            });
            ListAdapter adapter = new SimpleAdapter( AdventureList.this,adventureListWithCorrectNotes, R.layout.activity_view_adventure_entry, new String[] { "id","note_text","datetime"}, new int[] {R.id.adventure_Id, R.id.adventure_note, R.id.adventure_datetime});
            setListAdapter(adapter);
        }else{
            Toast.makeText(this,"No adventures!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
       //
    }
}
