package uk.ac.tees.p4136175.scrapbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity implements android.view.View.OnClickListener{

    Button btnAdd, btnList, showHide, btnSearch;
    final Context context = this;
    Animation slideUpAnimation, slideDownAnimation;
    boolean menuShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        btnAdd = (Button) findViewById(R.id.addButton);
        btnAdd.setOnClickListener(this);

        btnList = (Button) findViewById(R.id.listButton);
        btnList.setOnClickListener(this);

        showHide = (Button) findViewById(R.id.showHideButton);
        showHide.setOnClickListener(this);

        btnSearch = (Button) findViewById(R.id.searchButton);
        btnSearch.setOnClickListener(this);

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
        } else if (v == findViewById(R.id.searchButton)){
            // not done yet
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
            btnAdd.animate().translationY(-160).start();
            showHide.setText("Hide");
        }
        menuShow = !menuShow;
    }

}
