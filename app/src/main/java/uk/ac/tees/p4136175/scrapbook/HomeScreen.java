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
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity implements android.view.View.OnClickListener{

    Button btnAdd, btnList, showHide, btnSearch;
    final Context context = this;
    Animation slideUpAnimation, slideDownAnimation;
    boolean menuShow = false;
    CalendarView cv;
    boolean calendarShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_screen);

        btnAdd = (Button) findViewById(R.id.addButton);
        btnAdd.setOnClickListener(this);

        btnList = (Button) findViewById(R.id.listButton);
        btnList.setOnClickListener(this);

        showHide = (Button) findViewById(R.id.showHideButton);
        showHide.setOnClickListener(this);

        btnSearch = (Button) findViewById(R.id.searchButton);
        btnSearch.setOnClickListener(this);

        cv = (CalendarView) findViewById(R.id.calendarView3);
        cv.setVisibility(View.INVISIBLE);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String[] monthNames = {"Jan","Feb","Mar","Apr","May","June",
                        "Jul","Aug","Sep","Oct","Nov","Dec"};

                String date = dayOfMonth + " " + monthNames[month] + " " + year;
                Intent intent = new Intent(context, AdventureList.class);
                Bundle b = new Bundle();
                b.putString("date", date);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up_animation);
        slideUpAnimation.setFillAfter(true);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down_animation);
        slideDownAnimation.setFillAfter(true);

    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.addButton)) {
            Intent intent = new Intent(context, MakeAdventure.class);
            startActivity(intent);
        } else if (v == findViewById(R.id.listButton)){
            Intent intent = new Intent(context, AdventureList.class);
            startActivity(intent);
        } else if (v == findViewById(R.id.showHideButton)){
            startAnimation();
            if(calendarShown){
                cv.setVisibility(View.INVISIBLE);
                btnAdd.animate().scaleX(1f).start();
                btnAdd.animate().scaleY(1f).start();
                btnAdd.animate().translationY(0).start();
                calendarShown = false;
            }
        } else if (v == findViewById(R.id.searchButton)){
            if(calendarShown){
                btnAdd.animate().scaleX(1f).start();
                btnAdd.animate().scaleY(1f).start();
                btnAdd.animate().translationY(0).start();

                cv.setVisibility(View.INVISIBLE);
            } else {
                btnAdd.animate().scaleX(0.5f).start();
                btnAdd.animate().scaleY(0.5f).start();
                btnAdd.animate().translationY(-300).start();
                cv.setVisibility(View.VISIBLE);
            }
            calendarShown = !calendarShown;

        }
    }

    public void startAnimation(){
        if(menuShow) {
            btnSearch.animate().translationY(0).start();
            btnList.animate().translationY(0).start();
            showHide.animate().translationY(0).start();
            showHide.setText("Show");
        } else {
            btnSearch.animate().translationY(-160).start();
            btnList.animate().translationY(-160).start();
            showHide.animate().translationY(-160).start();
            showHide.setText("Hide");
        }
        menuShow = !menuShow;
    }

}
