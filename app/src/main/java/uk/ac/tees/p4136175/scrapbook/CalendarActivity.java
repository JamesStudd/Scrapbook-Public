package uk.ac.tees.p4136175.scrapbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    Toolbar myToolbar;

    NavigationView nv;

    ListView listView2;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    String[] adventureNote;
    Bitmap[] adventureImage;
    String[] adventureDate;
    String[] adventureLocation;
    int[] adventureIdArray;
    ImageAdapter imageAdapter;
    CalendarView calendarView;
    AdventureRepo repo;
    Date date;
    String chosenDate;



    // This class
    final Context context = this;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        repo = new AdventureRepo(this);

        imageAdapter = new ImageAdapter(this, repo);
        imageAdapter.getImages();

        listView2 = (ListView) findViewById(R.id.listView2);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        date = new Date();
        chosenDate = dateFormat.format(date);

        calendarView = (CalendarView) findViewById(R.id.calendarView4);
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
                chosenDate = dayOfMonth + " " + monthNames[month] + " " + year;

                // Update arrays and list
                setArrays();

            }
        });

        setArrays();

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv2);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.calendar_activity);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_adventures:
                        finish();
                        break;
                    case R.id.nav_Atlas:
                        Intent intent = new Intent(context, AtlasBackup.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        break;
                    case R.id.nav_images:
                        Intent intent3 = new Intent(context, AdventureList.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_help:
                        Intent intent4 = new Intent(context, HelpPage.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_info:
                        Intent intent5 = new Intent(context, AboutPage.class);
                        startActivity(intent5);
                        break;
                }
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.empty_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void updateList() {

        CustomArrayAdapter adapter = new CustomArrayAdapter(this, adventureNote, adventureImage, adventureDate,adventureLocation);
        listView2.setAdapter(adapter);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private void setArrays(){
        AdventureRepo repo = new AdventureRepo(this);
        ArrayList<HashMap<String, String>> adventureList = repo.getAdventureEntryList();
        ArrayList<HashMap<String, String>> adventureListWithCorrectDates = new ArrayList<HashMap<String, String>>();

        for (HashMap<String, String> h : adventureList){
            String s = h.get("datetime");
            if(chosenDate.equals(s)){
                adventureListWithCorrectDates.add(h);
            }
        }

        int sizeOfArray = adventureListWithCorrectDates.size();

        adventureNote = new String[sizeOfArray];
        adventureImage = new Bitmap[sizeOfArray];
        adventureDate = new String[sizeOfArray];
        adventureLocation = new String[sizeOfArray];
        // Create an array the same size as the current adventure list size
        adventureIdArray = new int[sizeOfArray];
        int count = 0;
        // For each hashmap, get the ID of the entry and save it into the array just created
        for(HashMap<String, String> h : adventureListWithCorrectDates){
            adventureIdArray[count] = Integer.parseInt(String.valueOf(h.get("id")));
            count++;
        }

        imageAdapter.getImages();
        List<Bitmap> images = imageAdapter.getImageList();
        for (int i = 0; i < adventureImage.length; i++){
            adventureImage[i] = images.get(i);
        }


        for (int i = 0; i <adventureNote.length ; i ++){
            HashMap<String, String> t = adventureListWithCorrectDates.get(i);
            adventureNote[i] = t.get("note_text");
            adventureDate[i] = t.get("datetime");
            adventureLocation[i] = t.get("location");
        }


        updateList();
    }


}
