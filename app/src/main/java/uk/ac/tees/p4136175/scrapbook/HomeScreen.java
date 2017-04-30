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

/**
 * HomeScreen will be the first screen that the user views, it will hold:
 * 'Make Adventure' button
 * 'Search' button
 * 'List' button
 * Navigation bar
 */
public class HomeScreen extends AppCompatActivity implements android.view.View.OnClickListener{

    // btnAdd = Make Adventure
    // btnList = List adventures
    // btnSearch = Bring up the calendar/noteSearch
    // btnFind = Search when a note is typed in
    Button btnAdd, btnList, btnSearch, btnFind;

    // This class
    final Context context = this;

    // The calendar view, used for searching
    CalendarView calendarView;

    // Is the calendar currently shown?
    boolean calendarShown = false;

    // Arrows are used to change between calendar and note view
    TextView leftArrow, rightArrow;

    // Allow the user to enter a search string
    EditText noteSearch;

    // Current search type in use, 0 = calendar, 1 = note
    int currentSearch = 0;

    /**
     * When the activity starts
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove the title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_screen);

        // Initialise the components, each button etc.
        btnAdd = (Button) findViewById(R.id.addButton);
        btnAdd.setOnClickListener(this);

        btnList = (Button) findViewById(R.id.listButton);
        btnList.setOnClickListener(this);

        btnSearch = (Button) findViewById(R.id.searchButton);
        btnSearch.setOnClickListener(this);

        btnFind = (Button) findViewById(R.id.findButton);
        btnFind.setVisibility(View.INVISIBLE);
        btnFind.setOnClickListener(this);

        calendarView = (CalendarView) findViewById(R.id.calendarView3);
        calendarView.setVisibility(View.INVISIBLE);
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
                String[] monthNames = {"Jan","Feb","Mar","Apr","May","Jun",
                        "Jul","Aug","Sep","Oct","Nov","Dec"};

                // Concatenate a string using the day, month and year
                String date = dayOfMonth + " " + monthNames[month] + " " + year;

                // Start the adventurelist class and pass in the date that the user selects
                Intent intent = new Intent(context, AdventureList.class);
                Bundle b = new Bundle();
                b.putString("date", date);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        // Add a click listener to the arrows so that different types of searches can be used
        leftArrow = (TextView) findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(this);
        leftArrow.setVisibility(View.INVISIBLE);
        rightArrow = (TextView) findViewById(R.id.rightArrow);
        rightArrow.setOnClickListener(this);
        rightArrow.setVisibility(View.INVISIBLE);

        noteSearch = (EditText) findViewById(R.id.noteSearch);
        noteSearch.setVisibility(View.INVISIBLE);

    }

    /**
     * Anytime a component with an action listener is clicked, actions
     * will be performed
     * @param v The component that has had an action performed on it
     */
    @Override
    public void onClick(View v) {
        // If the 'Make Adventure' button is selected, the MakeAdventure activity is called
        if (v == findViewById(R.id.addButton)) {
            Intent intent = new Intent(context, MakeAdventure.class);
            startActivity(intent);
        // If the list button is selected, the AdventureList activity is called
        } else if (v == findViewById(R.id.listButton)){
            Intent intent = new Intent(context, AdventureList.class);
            startActivity(intent);
        // If the search button is selected, the calendar will go invisible or visible based on
        // what the current state is
        } else if (v == findViewById(R.id.searchButton)){
            startAnimation("calendar");
        // If the find button is selected, the AdventureList activity is called whilst passing
        // in the current noteSearch string (search)
        } else if (v == findViewById(R.id.findButton)){
            Intent intent = new Intent(context, AdventureList.class);
            Bundle b = new Bundle();
            b.putString("note", noteSearch.getText().toString());
            intent.putExtras(b);
            startActivity(intent);
        }
        // If either of the arrows is clicked, this is here just so code wouldn't be repeated too much
        if(v == findViewById(R.id.leftArrow) || v==findViewById(R.id.rightArrow)){
            // If the right arrow is clicked and the current search is not already at the max
            // increment currentSearch
            if (v == findViewById(R.id.rightArrow) && currentSearch != 1){
                currentSearch++;
            // Otherwise, if the left arrow is clicked and the current search isn't already at
            // the minimum, decrement currentSearch
            } else if (v == findViewById(R.id.leftArrow) && currentSearch != 0){
                currentSearch--;
            }

            // Switch case for currentSearch, 0 is calendar, 1 is note
            switch(currentSearch){
                case 0:
                    // Make the: calendar visible, editText & find button invisible
                    showWayOfSearching("calendar");
                    break;
                case 1:
                    // Make the calendar invisible, editText & find button visible
                    showWayOfSearching("note");
                    break;
            }

        }

    }

    /**
     * Animates the 'Make Adventure' button as well as removing
     * the left arrow, right arrow and the calendar
     * @param animationType String 'calendar' will make the calendar invisible
     */
    public void startAnimation(String animationType){
        if (animationType == "calendar"){
            if(calendarShown){
                // If the calendar is already shown, make the 'Make Adventure' button bigger
                // and move it back down, as well as making the calendar and each arrow invisible
                btnAdd.animate().scaleX(1f).start();
                btnAdd.animate().scaleY(1f).start();
                btnAdd.animate().translationY(0).start();

                calendarView.setVisibility(View.INVISIBLE);
                leftArrow.setVisibility(View.INVISIBLE);
                rightArrow.setVisibility(View.INVISIBLE);
            } else {
                // If the calendar isn't shown, make the 'Make Adventure' button smaller and move
                // it up out of the way as well as making the calendar and each arrow visible
                btnAdd.animate().scaleX(0.5f).start();
                btnAdd.animate().scaleY(0.5f).start();
                btnAdd.animate().translationY(-300).start();
                calendarView.setVisibility(View.VISIBLE);
                leftArrow.setVisibility(View.VISIBLE);
                rightArrow.setVisibility(View.VISIBLE);
            }
            // Set calendarShown = not calendarShown (true to false, false to true)
            calendarShown = !calendarShown;
            // Make the find button and note search invisible
            btnFind.setVisibility(View.INVISIBLE);
            noteSearch.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Sets the calender and noteSearch components invisible or visible based on the parameter
     * @param state will either be 'calendar' or 'note'
     */
    public void showWayOfSearching(String state){
        if(state == "calendar"){
            calendarView.setVisibility(View.VISIBLE);
            noteSearch.setVisibility(View.INVISIBLE);
            btnFind.setVisibility(View.INVISIBLE);
        } else if (state == "note"){
            calendarView.setVisibility(View.INVISIBLE);
            noteSearch.setVisibility(View.VISIBLE);
            btnFind.setVisibility(View.VISIBLE);
        }
    }

}
