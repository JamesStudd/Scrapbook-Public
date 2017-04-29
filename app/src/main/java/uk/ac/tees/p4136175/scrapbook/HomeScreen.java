package uk.ac.tees.p4136175.scrapbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity implements android.view.View.OnClickListener{

    Button btnAdd, btnList, btnSearch, btnFind;
    final Context context = this;
    CalendarView cv;
    boolean calendarShown = false;
    TextView leftArrow, rightArrow;
    EditText noteSearch;
    int currentSearch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_screen);

        btnAdd = (Button) findViewById(R.id.addButton);
        btnAdd.setOnClickListener(this);

        btnList = (Button) findViewById(R.id.listButton);
        btnList.setOnClickListener(this);


        btnSearch = (Button) findViewById(R.id.searchButton);
        btnSearch.setOnClickListener(this);

        btnFind = (Button) findViewById(R.id.findButton);
        btnFind.setVisibility(View.INVISIBLE);
        btnFind.setOnClickListener(this);

        cv = (CalendarView) findViewById(R.id.calendarView3);
        cv.setVisibility(View.INVISIBLE);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String[] monthNames = {"Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec"};

                String date = dayOfMonth + " " + monthNames[month] + " " + year;
                Intent intent = new Intent(context, AdventureList.class);
                Bundle b = new Bundle();
                b.putString("date", date);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        leftArrow = (TextView) findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(this);
        leftArrow.setVisibility(View.INVISIBLE);
        rightArrow = (TextView) findViewById(R.id.rightArrow);
        rightArrow.setOnClickListener(this);
        rightArrow.setVisibility(View.INVISIBLE);

        noteSearch = (EditText) findViewById(R.id.noteSearch);
        noteSearch.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.addButton)) {
            Intent intent = new Intent(context, MakeAdventure.class);
            startActivity(intent);
        } else if (v == findViewById(R.id.listButton)){
            Intent intent = new Intent(context, AdventureList.class);
            startActivity(intent);
        } else if (v == findViewById(R.id.searchButton)){
            startAnimation("calendar");
        } else if (v == findViewById(R.id.findButton)){
            Intent intent = new Intent(context, AdventureList.class);
            Bundle b = new Bundle();
            b.putString("note", noteSearch.getText().toString());
            intent.putExtras(b);
            startActivity(intent);
        }

        if(v == findViewById(R.id.leftArrow) || v==findViewById(R.id.rightArrow)){
            if (v == findViewById(R.id.rightArrow)){
                if(currentSearch != 1){
                    currentSearch++;
                }
            } else if (v == findViewById(R.id.leftArrow)){
                if(currentSearch != 0){
                    currentSearch--;
                }
            }

            switch(currentSearch){
                case 0:
                    showWayOfSearching("calendar");
                    btnFind.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    showWayOfSearching("note");
                    btnFind.setVisibility(View.VISIBLE);
                    break;
            }

        }

    }

    public void startAnimation(String animationType){
        if (animationType == "calendar"){
            if(calendarShown){
                btnAdd.animate().scaleX(1f).start();
                btnAdd.animate().scaleY(1f).start();
                btnAdd.animate().translationY(0).start();

                cv.setVisibility(View.INVISIBLE);
                leftArrow.setVisibility(View.INVISIBLE);
                rightArrow.setVisibility(View.INVISIBLE);
            } else {
                btnAdd.animate().scaleX(0.5f).start();
                btnAdd.animate().scaleY(0.5f).start();
                btnAdd.animate().translationY(-300).start();
                cv.setVisibility(View.VISIBLE);
                leftArrow.setVisibility(View.VISIBLE);
                rightArrow.setVisibility(View.VISIBLE);
            }
            calendarShown = !calendarShown;
            btnFind.setVisibility(View.INVISIBLE);
            noteSearch.setVisibility(View.INVISIBLE);
        }

    }

    public void showWayOfSearching(String state){
        if(state == "calendar"){
            cv.setVisibility(View.VISIBLE);
            noteSearch.setVisibility(View.INVISIBLE);
        } else if (state == "note"){
            cv.setVisibility(View.INVISIBLE);
            noteSearch.setVisibility(View.VISIBLE);
        }
    }

}
