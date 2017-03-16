package uk.ac.tees.p4136175.scrapbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

public class MakeAdventure extends AppCompatActivity implements View.OnClickListener{

    Button btnSave, btnCancel, btnDelete;
    EditText makeEntry;
    TextView date, location;
    String formattedDate;
    private int _Adventure_Id=0;

    // Image Stuff
    private Uri mImageCaptureUri;
    private ImageView mImageView;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_adventure);

        // Get all the components of the UI
        btnSave = (Button) findViewById(R.id.saveButton);
        btnSave.setOnClickListener(this);

        btnDelete = (Button) findViewById(R.id.deleteButton);
        btnDelete.setOnClickListener(this);

        btnCancel = (Button) findViewById(R.id.cancelButton);
        btnCancel.setOnClickListener(this);

        makeEntry = (EditText) findViewById(R.id.adventureEntry);

        date = (TextView) findViewById(R.id.dateText);
        location = (TextView) findViewById(R.id.locationText);

        _Adventure_Id =0;
        Intent intent = getIntent();
        _Adventure_Id =intent.getIntExtra("adventure_Id", 0);
        AdventureRepo repo = new AdventureRepo(this);
        AdventureEntry adv = new AdventureEntry();
        adv = repo.getAdventureById(_Adventure_Id);

        if(adv.note_text != null){
            makeEntry.setText(String.valueOf(adv.note_text));
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df  = new SimpleDateFormat("dd MMM yyyy");
        formattedDate = df.format(c.getTime());

        date.setText(formattedDate);


        // Image Stuff
        final String [] items           = new String [] {"From Camera", "From Files"};
        ArrayAdapter<String> adapter  = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder     = new AlertDialog.Builder(this);

        builder.setTitle("Select Image");
        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int item ) {
                if (item == 0) {
                    Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file        = new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                    mImageCaptureUri = Uri.fromFile(file);

                    try {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();
                } else {
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        } );

        final AlertDialog dialog = builder.create();

        mImageView = (ImageView) findViewById(R.id.imageView);

        ((Button) findViewById(R.id.imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        Bitmap bitmap   = null;
        String path     = "";

        if (requestCode == PICK_FROM_FILE) {
            mImageCaptureUri = data.getData();
            path = getRealPathFromURI(mImageCaptureUri); //from Gallery

            if (path == null)
                path = mImageCaptureUri.getPath(); //from File Manager

            if (path != null)
                bitmap  = BitmapFactory.decodeFile(path);
        } else {
            path    = mImageCaptureUri.getPath();
            bitmap  = BitmapFactory.decodeFile(path);
        }

        mImageView.setImageBitmap(bitmap);
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
            adv.image = "tester";
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
        } else if (v == findViewById(R.id.deleteButton)){
            AdventureRepo repo = new AdventureRepo(this);
            repo.delete(_Adventure_Id);
            Toast.makeText(this, "Adventure Deleted", Toast.LENGTH_SHORT);
            finish();
        } else if (v == findViewById(R.id.cancelButton)){
            finish();
        }
    }
}
