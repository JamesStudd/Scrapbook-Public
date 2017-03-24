package uk.ac.tees.p4136175.scrapbook;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class GridList extends AppCompatActivity implements View.OnClickListener {

    Button backBtn, refreshBtn;
    TextView adventure_id;
    ImageAdapter ia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_list);

        backBtn = (Button) findViewById(R.id.backButton);
        backBtn.setOnClickListener(this);

        refreshBtn = (Button) findViewById(R.id.refreshButton);
        refreshBtn.setOnClickListener(this);

        ia = new ImageAdapter(this);

        refreshList();

    }

    private void refreshList() {
        AdventureRepo repo = new AdventureRepo(this);
        ArrayList<HashMap<String, Object>> adventureList =  repo.getAdventureEntryGrid();
        if(adventureList.size()!=0) {
            GridView gv = (GridView) findViewById(R.id.gridView);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adventure_id = (TextView) view.findViewById(R.id.adventure_Id);
                    String adventureId = adventure_id.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(),MakeAdventure.class);
                    objIndent.putExtra("adventure_Id", Integer.parseInt( adventureId));
                    startActivity(objIndent);
                }
            });
            gv.setAdapter(ia);
        }else{
            Toast.makeText(this,"No adventures!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.backButton)){
            finish();
        } else {
            refreshList();
        }
    }

    public ImageAdapter getImageAdapater(){
        return ia;
    }
}
