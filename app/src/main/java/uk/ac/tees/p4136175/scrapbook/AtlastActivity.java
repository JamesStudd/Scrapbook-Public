package uk.ac.tees.p4136175.scrapbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AtlastActivity extends FragmentActivity implements OnMapReadyCallback {

    Toolbar myToolbar;

    NavigationView nv;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    // This class
    final Context context = this;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atlast);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        myToolbar = (Toolbar) findViewById(R.id.my_toolbar3);
////        setSupportActionBar(myToolbar);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        nv = (NavigationView) findViewById(R.id.nv2);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.calendar_activity);
//        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
//
//        mDrawerLayout.addDrawerListener(mToggle);
//        mToggle.syncState();
//
//        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.nav_adventures:
//                        finish();
//                        break;
//                    case R.id.nav_Atlas:
//                        mDrawerLayout.closeDrawer(Gravity.LEFT);
//                        break;
//                    case R.id.nav_calendar:
//                        Intent intent2 = new Intent(context, CalendarActivity.class);
//                        startActivity(intent2);
//                        mDrawerLayout.closeDrawer(Gravity.LEFT);
//                        break;
//                }
//                return true;
//            }
//        });
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        AdventureRepo repo = new AdventureRepo(this);
        mMap = googleMap;
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(this);

        ArrayList<HashMap<String, String>> adventureList = repo.getAdventureEntryList();
        String[] adventureLocation = new String[adventureList.size()];
        for (int i = 0; i <adventureLocation.length ; i ++){
            HashMap<String, String> t = adventureList.get(i);
            adventureLocation[i] = t.get("location");
        }

        for (String s : adventureLocation){
            try {
                addressList = geocoder.getFromLocationName(s, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(address.getLocality()));
        }
    }
}
