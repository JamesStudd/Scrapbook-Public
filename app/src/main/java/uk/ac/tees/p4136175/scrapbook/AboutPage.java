package uk.ac.tees.p4136175.scrapbook;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

public class AboutPage extends AppCompatActivity {

    Toolbar myToolbar;

    NavigationView nv; // NavigationView is on each page, it's how to navigate through the app

    private DrawerLayout mDrawerLayout; // To extend the navigation bar
    private ActionBarDrawerToggle mToggle; // The button to open the bar / close the bar


    final Context context = this; // This class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar5);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv5);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_about_page);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    // Home page
                    case R.id.nav_adventures:
                        Intent intent3 = new Intent(context, HomeScreen.class);
                        startActivity(intent3);
                        break;
                    // Atlas page, shows the user where they have visited
                    case R.id.nav_Atlas:
                        Intent intent = new Intent(context, AtlasBackup.class);
                        startActivity(intent);
                        break;
                    // Calendar page, filter adventures by date
                    case R.id.nav_calendar:
                        Intent intent1 = new Intent(context, CalendarActivity.class);
                        startActivity(intent1);
                        break;
                    // Images grid, filter adventures by image
                    case R.id.nav_images:
                        Intent intent4 = new Intent(context, AdventureList.class);
                        startActivity(intent4);
                        break;
                    // Help page, basic how to use
                    case R.id.nav_help:
                        Intent intent5 = new Intent(context, HelpPage.class);
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
}
