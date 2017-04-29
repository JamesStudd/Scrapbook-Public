package uk.ac.tees.p4136175.scrapbook;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class AdventureList extends ListActivity implements View.OnClickListener{

    TextView adventure_id;
    Switch changeView;
    ImageAdapter ia;
    TextView noteView, imageView;

    Boolean switchState = false;

    ListView lv;
    GridView gv;
    AdventureRepo repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adventure_list);

        noteView = (TextView) findViewById(R.id.noteViewText);
        noteView.setVisibility(View.VISIBLE);
        imageView = (TextView) findViewById(R.id.imageViewText);
        imageView.setVisibility(View.VISIBLE);

        changeView = (Switch) findViewById(R.id.switch1);
        changeView.setVisibility(View.VISIBLE);
        changeView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                switchState = changeView.isChecked();
                refreshList();
            }
        });

        repo = new AdventureRepo(this);
        ia = new ImageAdapter(this, repo);
        gv = (GridView) findViewById(R.id.gridView1);

        Bundle b = getIntent().getExtras();
        if (b!=null){
            //System.out.println(b.getString("date"));
            displayEntriesByDate(b.getString("date"));
        } else {
            refreshList();
        }

        ia.getImages();
        System.out.println("ia.getImageList() : " + ia.getImageList());




    }

    private void refreshList(){
        AdventureRepo repo = new AdventureRepo(this);

        if(switchState){
            gv.setVisibility(View.VISIBLE);
            ListView lv = getListView();
            lv.setVisibility(View.INVISIBLE);
            final ArrayList<HashMap<String, Object>> adventureList =  repo.getAdventureEntryGrid();
            if(adventureList.size()!=0) {
                GridView gv = (GridView) findViewById(R.id.gridView1);
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adventure_id = (TextView) view.findViewById(R.id.adventure_Id_grid);
                        String adventureId = String.valueOf(adventureList.get(Integer.parseInt(String.valueOf(position))).get("id"));
                        Intent objIndent = new Intent(getApplicationContext(),MakeAdventure.class);
                        objIndent.putExtra("adventure_Id", Integer.parseInt( adventureId));
                        startActivity(objIndent);
                    }
                });
                gv.setAdapter(ia);
            }else{
                Toast.makeText(this,"No adventures!",Toast.LENGTH_SHORT).show();
            }
        } else {
            gv.setVisibility(View.INVISIBLE);
            ListView lv = getListView();
            lv.setVisibility(View.VISIBLE);
            ArrayList<HashMap<String, String>> adventureList =  repo.getAdventureEntryList();
            if(adventureList.size()!=0) {
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adventure_id = (TextView) view.findViewById(R.id.adventure_Id);
                        String adventureId = adventure_id.getText().toString();
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


    }


    public void displayEntriesByDate(String date){

        // Set the top widgets to invisible - Text views and switch
        changeView.setVisibility(View.INVISIBLE);
        noteView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);

        ListView lv = getListView();
        lv.setVisibility(View.VISIBLE);


        // adventureList is all the current entries
        ArrayList<HashMap<String, String>> adventureList =  repo.getAdventureEntryList();

        // adventureListWithCorrectDates is empty to start but will be populated with only entries
        // on a certain date
        ArrayList<HashMap<String, String>> adventureListWithCorrectDates = new ArrayList<>();

        // Loop through each hashmap in adventureList, if the entry contains the date
        // passed into the function, the entry will be added to adventurListWithCorrectDates
        int i = 0;
        for (HashMap<String, String> entry : adventureList){
            if(entry.containsValue(date)){
                adventureListWithCorrectDates.add(adventureList.get(i));
            }
            i++;
        }

        if(adventureListWithCorrectDates.size()!=0) {
            //System.out.println("There is entries");
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adventure_id = (TextView) view.findViewById(R.id.adventure_Id);
                    String adventureId = adventure_id.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(),MakeAdventure.class);
                    objIndent.putExtra("adventure_Id", Integer.parseInt( adventureId));
                    startActivity(objIndent);
                }
            });
            ListAdapter adapter = new SimpleAdapter( AdventureList.this,adventureListWithCorrectDates, R.layout.activity_view_adventure_entry, new String[] { "id","note_text","datetime"}, new int[] {R.id.adventure_Id, R.id.adventure_note, R.id.adventure_datetime});
            setListAdapter(adapter);
        }else{
            Toast.makeText(this,"No adventures!",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View v) {
       //
    }
}
