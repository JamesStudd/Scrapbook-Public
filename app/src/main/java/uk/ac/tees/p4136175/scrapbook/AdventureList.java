package uk.ac.tees.p4136175.scrapbook;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
public class AdventureList extends AppCompatActivity implements View.OnClickListener {

    // Components on the adventure_list activity
    TextView adventure_id;
    GridView gridView;

    // Custom image adapter used to list each image from adventures
    ImageAdapter imageAdapter;

    // Local repo to access the adventures
    AdventureRepo repo;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    //Toolbar
    Toolbar myToolbar;

    NavigationView nv;

    // This class
    final Context context = this;

    /**
     * When the activity is called
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure_list);

        nv = (NavigationView) findViewById(R.id.nv3);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_adventure_list);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar3);
        setSupportActionBar(myToolbar);
        myToolbar.setLogo(R.drawable.snippetgreen);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_adventures:
                        Intent intent3 = new Intent(context, HomeScreen.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_Atlas:
                        Intent intent = new Intent(context, AtlasBackup.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        Intent intent2 = new Intent(context, CalendarActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_images:
                        break;
                }
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Initialise repo
        repo = new AdventureRepo(this);

        // Use repo to initialise the imageAdapter
        imageAdapter = new ImageAdapter(this, repo);
        gridView = (GridView) findViewById(R.id.gridView1);

        // Update imageadapter images then refresh the list
        imageAdapter.getImages();
        refreshList();


    }


    /**
     * This will create a list of the current adventure images
     */
    private void refreshList() {
        // Initialise the repo
        AdventureRepo repo = new AdventureRepo(this);

        // Create an ArrayList of HashMaps using the repo method "getAdventureEntryGrid()"
        // This returns the ID and Image of each adventure
        final ArrayList<HashMap<String, Object>> adventureList = repo.getAdventureEntryGrid();
        if (adventureList.size() != 0) {
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
                    Intent objIndent = new Intent(getApplicationContext(), MakeAdventure.class);
                    objIndent.putExtra("adventure_Id", Integer.parseInt(adventureId));
                    startActivity(objIndent);
                }
            });
            // Set the activity of the gridview to the imageAdapter
            gv.setAdapter(imageAdapter);
        } else {
            // Otherwise, print "no adventures"
            Toast.makeText(this, "No adventures!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View v) {
        //
    }
}
