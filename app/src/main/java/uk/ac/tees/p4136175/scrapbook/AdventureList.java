package uk.ac.tees.p4136175.scrapbook;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class AdventureList extends ListActivity implements View.OnClickListener{

    Button backBtn, refreshBtn;
    TextView adventure_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure_list);

        backBtn = (Button) findViewById(R.id.backButton);
        backBtn.setOnClickListener(this);

        refreshBtn = (Button) findViewById(R.id.refreshButton);
        refreshBtn.setOnClickListener(this);

        refreshList();
    }

    private void refreshList(){
        AdventureRepo repo = new AdventureRepo(this);
        ArrayList<HashMap<String, String>> adventureList =  repo.getAdventureEntryList();
        if(adventureList.size()!=0) {
            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adventure_id = (TextView) view.findViewById(R.id.adventure_Id);
                    String adventureId = adventure_id.getText().toString();
                    System.out.println("The adventure id : " + adventureId);
                    Intent objIndent = new Intent(getApplicationContext(),MakeAdventure.class);
                    objIndent.putExtra("adventure_Id", Integer.parseInt( adventureId));
                    startActivity(objIndent);
                }
            });
            ListAdapter adapter = new SimpleAdapter( AdventureList.this,adventureList, R.layout.activity_view_adventure_entry, new String[] { "id","note_text","datetime"}, new int[] {R.id.adventure_Id, R.id.adventure_note, R.id.adventure_datetime});
            setListAdapter(adapter);
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
}
