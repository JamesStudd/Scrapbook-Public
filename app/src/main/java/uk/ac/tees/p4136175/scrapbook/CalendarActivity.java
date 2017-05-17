package uk.ac.tees.p4136175.scrapbook;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
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
    AdventureRepo repo;



    // This class
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        repo = new AdventureRepo(this);

        imageAdapter = new ImageAdapter(this, repo);
        imageAdapter.getImages();

        listView2 = (ListView) findViewById(R.id.listView2);

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
                        Intent intent = new Intent(context, AtlastActivity.class);
                        startActivity(intent);
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                    case R.id.nav_calendar:
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                }
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
