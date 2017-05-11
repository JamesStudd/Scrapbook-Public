package uk.ac.tees.p4136175.scrapbook;

/**
 * Created by p4136175 on 11/05/2017.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final String[] date;
    private final Bitmap[] img;

    public CustomArrayAdapter(Activity context, String[] itemname, Bitmap[] img, String[] date) {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.img=img;
        this.date = date;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView txtDate = (TextView) rowView.findViewById(R.id.date);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.location);

        txtDate.setText(date[position]);
        txtTitle.setText(itemname[position]);
        imageView.setImageBitmap(img[position]);
        extratxt.setText("Location: ");
        return rowView;

    };
}
