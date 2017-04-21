package uk.ac.tees.p4136175.scrapbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity implements android.view.View.OnClickListener{

    Button btnAdd, btnList, btnGrid;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        btnAdd = (Button) findViewById(R.id.addButton);
        btnAdd.setOnClickListener(this);

        btnList = (Button) findViewById(R.id.listButton);
        btnList.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.addButton)) {
            Intent intent = new Intent(context, MakeAdventure.class);
            startActivity(intent);
        } else if (v == findViewById(R.id.listButton)){
            Intent intent = new Intent(context, AdventureList.class);
            startActivity(intent);
        }
    }
}
