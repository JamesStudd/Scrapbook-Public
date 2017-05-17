package uk.ac.tees.p4136175.scrapbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is the longest, it allows for new adventures to be created but also
 * has other methods to get the current location, allow access to the image gallery
 */
public class MakeAdventure extends AppCompatActivity implements View.OnClickListener{

    // Instance Variables
    EditText makeEntry; // The note the user can enter
    TextView toolbarDate, toolbarSave; // The current date / chosen date and the save button
    CalendarView calendarView; // The calendar to change the date of the adventure
    String selectedDate; // The date (if the user chooses a different date)
    List<View> listOfComponents = new ArrayList<>(); // All of the components (to make invis)
    DateFormat dateFormat; // The date formatted
    Date date; // The current date
    WebView attributionText;
    boolean imageChanged; // Has the image been updated

    LocationManager locationManager;
    public static TextView locationText;
    String currentLocation = "none";

    private int _Adventure_Id=0;

    // Image Stuff
    private ImageView mImageView;

    private final static int MY_PERMISSION_FINE_LOCATION = 104;
    private final static int PLACE_PICKER_REQUEST = 107;

    private final int SELECT_PHOTO = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;

    BottomNavigationView bottomNav;

    AdventureEntry adv;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_adventure);

        imageChanged = false;

        makeEntry = (EditText) findViewById(R.id.adventureEntry);
        listOfComponents.add(makeEntry);

        toolbarSave = (TextView) findViewById(R.id.toolbar_save);
        toolbarSave.setOnClickListener(this);

        locationText = (TextView) findViewById(R.id.locationText);
        listOfComponents.add(locationText);

        mImageView = (ImageView) findViewById(R.id.imageView);
        listOfComponents.add(mImageView);

        toolbarDate = (TextView) findViewById(R.id.toolbarDate);

        dateFormat = new SimpleDateFormat("dd MMM yyyy");
        date = new Date();
        selectedDate = dateFormat.format(date);

        attributionText = (WebView) findViewById(R.id.wvAttribution);
        attributionText.setVisibility(View.INVISIBLE);


        // This section is to check if the activity is to EDIT or make a NEW activity,
        // if any of the advenure attributes aren't null, the TextView's will be changed to
        // show the current values
        _Adventure_Id =0;
        Intent intent = getIntent();
        _Adventure_Id =intent.getIntExtra("adventure_Id", 0);
        AdventureRepo repo = new AdventureRepo(this);

        adv = repo.getAdventureById(_Adventure_Id);

        if(adv.note_text != null){
            makeEntry.setText(String.valueOf(adv.note_text));
        }

        // If the adventure has an image attached to it, set the imageview bitmap to the image
        if(adv.image != null){
            mImageView.setImageBitmap(getImage(adv.image));
        }


        calendarView = (CalendarView) findViewById(R.id.calendarViewDate);
        calendarView.setVisibility(View.INVISIBLE);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String[] monthNames = {"Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec"};

                selectedDate = dayOfMonth + " " + monthNames[month] + " " + year;
                changeComponents(View.VISIBLE);
                toolbarDate.setText(selectedDate);
            }
        });

        // If the adventure has a date attached to it
        if(adv.datetime != null) selectedDate = adv.datetime;
        toolbarDate.setText(selectedDate);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        setSupportActionBar(myToolbar);

        bottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            // If the user selects the map button, they will be taken to the location picker
                            case R.id.make_map:
                                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                                try {
                                    Intent intent = builder.build(MakeAdventure.this);
                                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                                    e.printStackTrace();
                                }
                                break;

                            // If the user selects the camera button, they will be taken to the gallery
                            case R.id.make_camera:
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, SELECT_PHOTO);


                                break;
                        }
                        return false;
                    }
                });

        if(adv.loc_name != null){
            currentLocation = adv.loc_name;
            locationText.setText(currentLocation);
        } else {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MakeAdventure.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            } else {
                if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Location currentLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    System.out.println(currentLoc);
                    getLocation(currentLoc);


                    //UPDATE LOCATION EVERY 10 MINUTES OR 20 METRES WALKED - saves battery
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600000, 20, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            getLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                }
                else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 20, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            getLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                }
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                // If the user clicks the calendar, display the calendar to select a new date
                case R.id.make_calendar_button:
                    calendarView.setVisibility(View.VISIBLE);
                    changeComponents(View.INVISIBLE);
                    return true;

                // If the user wants to delete the current entry
                case R.id.make_delete_opt:
                    // Use the repo delete method
                    AdventureRepo repo = new AdventureRepo(this);
                    repo.delete(_Adventure_Id);
                    Toast.makeText(this, "Adventure Deleted", Toast.LENGTH_SHORT);
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();

            }
        return super.onOptionsItemSelected(item);
        }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }

            case MY_PERMISSION_FINE_LOCATION: {
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    askForStoragePermission();
                }
            }
        }
    }

    private void askForStoragePermission(){
        if (ContextCompat.checkSelfPermission(MakeAdventure.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MakeAdventure.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    /**
     * When an activity has a result
     * @param requestCode The request code
     * @param resultCode THe result code
     * @param imageReturnedIntent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        // Switch case for the request code
        switch(requestCode) {
            // if the request is to select a photo
            case SELECT_PHOTO: {

                // if the result code is successful
                if (resultCode == RESULT_OK) {
                    final Uri imageUri = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    mImageView.setImageBitmap(selectedImage);
                    imageChanged = true;

                }
                break;
            }
            case PLACE_PICKER_REQUEST: {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(MakeAdventure.this, imageReturnedIntent);

                    currentLocation = String.valueOf(place.getAddress());
                    locationText.setText(place.getAddress());

                    if (place.getAttributions() == null) {
                        attributionText.loadData("no attribution", "text/html; charset=utf-8", "UFT-8");
                        attributionText.setVisibility(View.INVISIBLE);
                    }else {
                        attributionText.loadData(place.getAttributions().toString(), "text/html; charset=utf-8", "UFT-8");
                        attributionText.setVisibility(View.INVISIBLE);
                    }
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_make_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * When a component is clicked
     * @param v THe component
     */
    @Override
    public void onClick(View v) {


        // if the component is the save button
        if (v == findViewById(R.id.toolbar_save)) {



            // Create a local repo and a blank adventure
            AdventureRepo repo = new AdventureRepo(this);
            AdventureEntry adv = new AdventureEntry();

            // Set the ID to the adventure Id made earlier
            adv.ID = _Adventure_Id;
            adv.note_text = makeEntry.getText().toString();

            Bitmap image = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            adv.image = getBytes(image);
            adv.datetime = selectedDate;
            adv.loc_name = currentLocation;

            // If the adventure is new, insert it into the DB
            if (_Adventure_Id == 0) {

                if(!imageChanged) {
                    Toast.makeText(this, "Please Choose An Image", Toast.LENGTH_SHORT).show();
                } else if (makeEntry.getText().toString().equals("")){
                    Toast.makeText(this, "Please Add A Note", Toast.LENGTH_SHORT).show();
                } else {
                    _Adventure_Id = repo.insert(adv);
                    Toast.makeText(this, "New Adventure Created", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }


                // If the adventure is exisiting already, just update it
            } else {
                repo.update(adv);
                Toast.makeText(this, "Adventure Entry Updated", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
            // If the component is the delete button

        }

    }


    /**
     * THis converts a bitmap to a byte[] array
     * @param bitmap The bitmap
     * @return A byte[] array
     */
    public static byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    /**
     * This converts a byte[] array to a bitmap
     * @param image The byte[] array
     * @return Bitmap
     */
    public static Bitmap getImage(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    /**
     * Take each component on the make adventure page and set them visible, or invisible
     * @param state View.Visible or View.Invisible
     */
    private void changeComponents(int state){
        for (View v : listOfComponents){
            v.setVisibility(state);
        }
        if(state == View.VISIBLE){
            calendarView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Sets the locationText and currentLocation variable to the location of the user
     * @param location
     */
    public void getLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            //Loads of methods using .get to get different info about the location
            String str = addressList.get(0).getLocality()+", ";
            str += addressList.get(0).getCountryName();
            currentLocation = str;
            locationText.setText(str);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
