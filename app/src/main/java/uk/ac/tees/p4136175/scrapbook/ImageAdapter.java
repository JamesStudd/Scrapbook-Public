package uk.ac.tees.p4136175.scrapbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by p4136175 on 24/03/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private AdventureRepo adventureRepo;
    private List<Bitmap> images;

    public ImageAdapter(Context c,  AdventureRepo repo) {
        mContext = c;
        adventureRepo = repo;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    public void getImages(){
        ArrayList<HashMap<String, Object>> adventureList =  adventureRepo.getAdventureEntryGrid();
        for(HashMap<String, Object> h : adventureList){
            System.out.println(h);
        }

        AdventureEntry adv = new AdventureEntry();

        System.out.println("Adventure List : " + adventureList);
        System.out.println(adventureList.size() + " is the size of the adventure list");

        for (int i = 1; i <= adventureList.size(); i++){
            adv = adventureRepo.getAdventureById(i);
            if(adv.image != null){
                System.out.println(adv.image);
                //images.add(getImage(adv.image));
            }
        }

    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.dogo, R.drawable.dogo2,
            R.drawable.dogo3, R.drawable.dogo4,
            R.drawable.dogo5, R.drawable.dogo6

    };

    public List getImageList(){
        return images;
    }

    public static Bitmap getImage(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}