package uk.ac.tees.p4136175.scrapbook;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * HomeScreen will be the first screen that the user views, it will hold:
 * 'Make Adventure' button
 * 'Search' button
 * 'List' button
 * Navigation bar
 */
public class HomeScreen extends AppCompatActivity implements android.view.View.OnClickListener {

    // btnAdd = Make Adventure
    ImageButton btnAdd;

    // List view and ID for the adventure
    ListView listView;
    TextView adventure_id;

    // These variables will be for the custom array adapter
    AdventureRepo tempRepo = new AdventureRepo(this);
    String[] adventureNote;
    Bitmap[] adventureImage;
    String[] adventureDate;
    String[] adventureLocation;
    int[] adventureIdArray;
    ImageAdapter imageAdapter;
    AdventureRepo repo;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    NavigationView nv;


    // This class
    final Context context = this;

    // The calendar view, used for searching
    CalendarView calendarView;

    //Toolbar
    Toolbar myToolbar;

    // Is the calendar currently shown?
    boolean calendarShown = false;

    // Arrows are used to change between calendar and note view
    TextView leftArrow, rightArrow;

    // Allow the user to enter a search string
    EditText noteSearch;

    // Current search type in use, 0 = calendar, 1 = note
    int currentSearch = 0;

    /**
     * When the activity starts
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove the title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_screen);


        // Initialise repo
        repo = new AdventureRepo(this);

        // Use repo to initialise the imageAdapter
        imageAdapter = new ImageAdapter(this, repo);
        imageAdapter.getImages();

        // This part of the code initializes 3 different arrays
        // adventureNote - holds each adventure note
        // adventureImage - holds each adventure image
        // adventureIdArray - holds each adventure ID
        // This way they are all the same index.



        listView = (ListView) findViewById(R.id.listView);

        setArrays("");

        // Initialise the components, each button etc.
        btnAdd = (ImageButton) findViewById(R.id.addButton);
        btnAdd.setOnClickListener(this);

        nv = (NavigationView) findViewById(R.id.nv1);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_screen);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        calendarView = (CalendarView) findViewById(R.id.calendarView3);
        calendarView.setVisibility(View.INVISIBLE);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            /**
             * If the date is selected on a callender
             * @param view Calendar in use
             * @param year Year selected
             * @param month Month selected
             * @param dayOfMonth Day selected
             */
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Used to convert the month to a 3 letter representation to store in the DB
                String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

                // Concatenate a string using the day, month and year
                String date = dayOfMonth + " " + monthNames[month] + " " + year;

                // Start the adventurelist class and pass in the date that the user selects
                Intent intent = new Intent(context, AdventureList.class);
                Bundle b = new Bundle();
                b.putString("date", date);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        // Add a click listener to the arrows so that different types of searches can be used
        leftArrow = (TextView) findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(this);
        leftArrow.setVisibility(View.INVISIBLE);
        rightArrow = (TextView) findViewById(R.id.rightArrow);
        rightArrow.setOnClickListener(this);
        rightArrow.setVisibility(View.INVISIBLE);

        noteSearch = (EditText) findViewById(R.id.noteSearch);
        noteSearch.setVisibility(View.INVISIBLE);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setLogo(R.drawable.snippetgreen);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_adventures:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.nav_Atlas:
                        Intent intent = new Intent(context, AtlasBackup.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.nav_calendar:
                        Intent intent2 = new Intent(context, CalendarActivity.class);
                        startActivity(intent2);
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                }
                return true;
            }
        });

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            switch (item.getItemId()) {
                case R.id.settings_id:
                    Toast.makeText(getApplicationContext(), "Settings selected", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.search_id:
                    if(noteSearch.getVisibility() == View.INVISIBLE) {
                        noteSearch.setVisibility(View.VISIBLE);
                        myToolbar.setLogo(null);
                    } else {
                        noteSearch.setVisibility(View.INVISIBLE);
                        myToolbar.setLogo(R.drawable.snippetgreen);

                        if(!((noteSearch.getText().toString()).matches(""))) {
                            System.out.println("Search-"+noteSearch.getText().toString()+"-");
                            setArrays(noteSearch.getText().toString());
//                            Intent intent = new Intent(context, AdventureList.class);
//                            Bundle b = new Bundle();
//                            b.putString("note", noteSearch.getText().toString());
//                            intent.putExtras(b);
//                            noteSearch.setText("");
//                            startActivity(intent);
                        } else {
                            setArrays("");
                        }

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

                    }
                    return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Anytime a component with an action listener is clicked, actions
     * will be performed
     *
     * @param v The component that has had an action performed on it
     */
    @Override
    public void onClick(View v) {
        // If the 'Make Adventure' button is selected, the MakeAdventure activity is called
        if (v == findViewById(R.id.addButton)) {
            System.out.println("Clicked");
            Intent intent = new Intent(context, MakeAdventure.class);
            startActivityForResult(intent,0);
            // If the find button is selected, the AdventureList activity is called whilst passing
            // in the current noteSearch string (search)
        } else if (v == findViewById(R.id.findButton)) {
            Intent intent = new Intent(context, AdventureList.class);
            Bundle b = new Bundle();
            b.putString("note", noteSearch.getText().toString());
            intent.putExtras(b);
            startActivity(intent);
        }
        // If either of the arrows is clicked, this is here just so code wouldn't be repeated too much
//        if (v == findViewById(R.id.leftArrow) || v == findViewById(R.id.rightArrow)) {
//            // If the right arrow is clicked and the current search is not already at the max
//            // increment currentSearch
//            if (v == findViewById(R.id.rightArrow) && currentSearch != 1) {
//                currentSearch++;
//                // Otherwise, if the left arrow is clicked and the current search isn't already at
//                // the minimum, decrement currentSearch
//            } else if (v == findViewById(R.id.leftArrow) && currentSearch != 0) {
//                currentSearch--;
//            }
//
//            // Show or Hide some components based on the currentSearch variable
//            // 0 is calendar, 1 is note
//            showWayOfSearching(currentSearch);
//
//        }

    }

//    /**
//     * Animates the 'Make Adventure' button as well as removing
//     * the left arrow, right arrow and the calendar
//     */
//
//
//    /**
//     * Sets the calender and noteSearch components invisible or visible based on the parameter
//     *
//     * @param state will either be '0' or '1', 0 = calendar, 1 = note
//     */

    private void updateList() {

        CustomArrayAdapter adapter = new CustomArrayAdapter(this, adventureNote, adventureImage, adventureDate,adventureLocation);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    Intent objIndent = new Intent(getApplicationContext(), MakeAdventure.class);
                    objIndent.putExtra("adventure_Id", adventureIdArray[position]);
                    startActivity(objIndent);
                }
            });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        setArrays("");
    }

    private void setArrays(String searchCriteria){
            AdventureRepo repo = new AdventureRepo(this);
            ArrayList<HashMap<String, String>> adventureList = repo.getAdventureEntryList();
            if(searchCriteria != ""){
                ArrayList<HashMap<String, String>> adventureListWithSearchCritera = new ArrayList<>();
                for (HashMap<String, String> h : adventureList){
                    String s = h.get("note_text");
                    if (s.toLowerCase().indexOf(searchCriteria.toLowerCase()) != -1){
                        adventureListWithSearchCritera.add(h);
                    }
                }

                if(adventureListWithSearchCritera.size() == 0){
                    Toast.makeText(this,"No adventures containing: " + searchCriteria,Toast.LENGTH_SHORT).show();
                } else {
                    adventureList.clear();
                    adventureList = adventureListWithSearchCritera;
                }


            }

            adventureNote = new String[adventureList.size()];
            adventureImage = new Bitmap[adventureList.size()];
            adventureDate = new String[adventureList.size()];
            adventureLocation = new String[adventureList.size()];
            // Create an array the same size as the current adventure list size
            adventureIdArray = new int[adventureList.size()];
            int count = 0;
            // For each hashmap, get the ID of the entry and save it into the array just created
            for(HashMap<String, String> h : adventureList){
                adventureIdArray[count] = Integer.parseInt(String.valueOf(h.get("id")));
                count++;
            }


            imageAdapter.getImages();
            List<Bitmap> images = imageAdapter.getImageList();
            for (int i = 0; i < adventureImage.length; i++){
                adventureImage[i] = images.get(i);
            }


            for (int i = 0; i <adventureNote.length ; i ++){
                HashMap<String, String> t = adventureList.get(i);
                adventureNote[i] = t.get("note_text");
                adventureDate[i] = t.get("datetime");
                adventureLocation[i] = t.get("location");
            }


            updateList();
    }


}
