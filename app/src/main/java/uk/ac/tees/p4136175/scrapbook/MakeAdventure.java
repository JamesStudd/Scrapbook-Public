package uk.ac.tees.p4136175.scrapbook;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import android.view.Menu;


public class MakeAdventure extends AppCompatActivity implements View.OnClickListener{

    private GPS_Service gps_service;
    private BroadcastReceiver broadcastReceiver;

    private void enable_buttons() {
                Intent i = new Intent(getApplicationContext(),GPS_Service.class);
                startService(i);
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                    ,100);
            return true;
        }
        return false;
    }


    //----------------------------------------------------------------------------------------------------------------------------------

    Button btnSave, btnCancel, btnDelete;
    EditText makeEntry;
    TextView dateTextView, location, dateStatic, locationStatic;
    String formattedDate;
    CalendarView calendarView;
    String selectedDate;
    List<View> listOfComponents = new ArrayList<>();
    DateFormat dateFormat;
    Date date;

    private int _Adventure_Id=0;

    // Image Stuff
    private Uri mImageCaptureUri;
    private ImageView mImageView;


    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;

    private final int SELECT_PHOTO = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_adventure);

        // Get all the components of the UI
        btnSave = (Button) findViewById(R.id.saveButton);
        btnSave.setOnClickListener(this);
        listOfComponents.add(btnSave);

        btnDelete = (Button) findViewById(R.id.deleteButton);
        btnDelete.setOnClickListener(this);
        btnDelete.setEnabled(false);
        listOfComponents.add(btnDelete);

        btnCancel = (Button) findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(this);
        listOfComponents.add(btnCancel);

        makeEntry = (EditText) findViewById(R.id.adventureEntry);
        listOfComponents.add(makeEntry);

        dateStatic = (TextView) findViewById(R.id.dateTextStatic);
        listOfComponents.add(dateStatic);

        locationStatic = (TextView) findViewById(R.id.locationTextStatic);
        listOfComponents.add(locationStatic);

        location = (TextView) findViewById(R.id.locationText);
        listOfComponents.add(location);

        mImageView = (ImageView) findViewById(R.id.imageView);
        listOfComponents.add(mImageView);

        dateFormat = new SimpleDateFormat("dd MMM yyyy");
        date = new Date();
        selectedDate = dateFormat.format(date);

        calendarView = (CalendarView) findViewById(R.id.calendarViewDate);
        calendarView.setVisibility(View.INVISIBLE);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String[] monthNames = {"Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec"};

                selectedDate = dayOfMonth + " " + monthNames[month] + " " + year;
                changeComponents(View.VISIBLE);

                if(!Objects.equals(selectedDate, dateFormat.format(date))){
                    dateStatic.setText("Chosen Date:");
                } else {
                    dateStatic.setText("Current Date:");
                }
                dateTextView.setText(selectedDate);
            }
        });



        dateTextView = (TextView) findViewById(R.id.dateText);
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setVisibility(View.VISIBLE);
                changeComponents(View.INVISIBLE);
            }
        });
        listOfComponents.add(dateTextView);

        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    location.setText(""+intent.getExtras().get("coordinates"));
                    setFormattedAddress();

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));

        _Adventure_Id =0;
        Intent intent = getIntent();
        _Adventure_Id =intent.getIntExtra("adventure_Id", 0);
        AdventureRepo repo = new AdventureRepo(this);
        AdventureEntry adv;
        adv = repo.getAdventureById(_Adventure_Id);

        if(adv.note_text != null){
            btnDelete.setEnabled(true);
            makeEntry.setText(String.valueOf(adv.note_text));
        }

        if(adv.image != null){
            System.out.println(adv.image + " is the image");
            System.out.println(getImage(adv.image));
            mImageView.setImageBitmap(getImage(adv.image));
        } else {
            System.out.println("adv image was null.");
        }

        dateTextView.setText(selectedDate);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        Button pickImage = (Button) findViewById(R.id.imageButton);
        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        listOfComponents.add(pickImage);

        if(!runtime_permissions())
            enable_buttons();

        setFormattedAddress();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 100: {
                if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    enable_buttons();
                } else {
                    runtime_permissions();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    final Uri imageUri = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    mImageView.setImageBitmap(selectedImage);

                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj      = {MediaStore.Images.Media.DATA};
        Cursor cursor       = getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor == null) return null;

        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.saveButton)){
            AdventureRepo repo = new AdventureRepo(this);
            AdventureEntry adv = new AdventureEntry();
            adv.ID = _Adventure_Id;
            adv.note_text = makeEntry.getText().toString();

            Bitmap image = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            adv.image = getBytes(image);
            adv.datetime = formattedDate;
            adv.loc_lang = "tester";
            adv.loc_lat = "tester";

            if(_Adventure_Id == 0){
                _Adventure_Id = repo.insert(adv);
                Toast.makeText(this, "New Adventure Created", Toast.LENGTH_SHORT).show();
            } else {
                repo.update(adv);
                Toast.makeText(this, "Adventure Entry Updated", Toast.LENGTH_SHORT).show();
            }
            unregisterReceiver(broadcastReceiver);
            finish();
        } else if (v == findViewById(R.id.deleteButton)){
            AdventureRepo repo = new AdventureRepo(this);
            repo.delete(_Adventure_Id);
            Toast.makeText(this, "Adventure Deleted", Toast.LENGTH_SHORT);
            finish();
        } else if (v == findViewById(R.id.cancelButton)){
            unregisterReceiver(broadcastReceiver);
            finish();
        }
    }

    // This converts from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // This converts from byte array to bitmap
    public static Bitmap getImage(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void setFormattedAddress(){
        if(LocationHelper.getlInstance().currentLocation != null){
            location.setText(LocationHelper.getlInstance().currentLocation);
        } else {
            System.out.println("is null");
        }

    }

    private void changeComponents(int state){
        for (View v : listOfComponents){
            v.setVisibility(state);
        }
        if(state == View.VISIBLE){
            calendarView.setVisibility(View.INVISIBLE);
        }
    }

}
