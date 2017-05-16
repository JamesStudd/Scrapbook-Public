package uk.ac.tees.p4136175.scrapbook;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(HomeScreen.class)
                .withSplashTimeOut(5000)
                .withBackgroundColor(android.graphics.Color.WHITE)
                .withLogo(R.drawable.snippetgreen);
                //.withHeaderText("SNIPPET");

        //SET TEXT COLOR
        //config.getHeaderTextView().setTextColor(android.graphics.Color.WHITE);

        //SET TO VIEW
        View view = config.create();

        //SET VIEW TO CONTENT VIEW
        setContentView(view);
    }
}
