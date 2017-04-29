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
    private List<Bitmap> images = new ArrayList<>();

    public ImageAdapter(Context c,  AdventureRepo repo) {
        mContext = c;
        adventureRepo = repo;
    }

    public int getCount() {
        return images.size();
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

        imageView.setTag(Integer.valueOf(position));
        imageView.setImageBitmap(images.get(position));
        return imageView;
    }

    public void getImages(){
        // Code from MakeAdventure
//        AdventureRepo repo = new AdventureRepo(this);
//        AdventureEntry adv;
//        adv = repo.getAdventureById(_Adventure_Id);
//
//        if(adv.note_text != null){
//            btnDelete.setEnabled(true);
//            makeEntry.setText(String.valueOf(adv.note_text));
//        }
//
//        if(adv.image != null){
//            System.out.println(adv.image + " is the image");
//            System.out.println(getImage(adv.image));
//            mImageView.setImageBitmap(getImage(adv.image));
//        } else {
//            System.out.println("adv image was null.");
//        }


        ArrayList<HashMap<String, Object>> adventureList =  adventureRepo.getAdventureEntryGrid();
        AdventureEntry adv = new AdventureEntry();


        int[] imageArray = new int[adventureList.size()];
        int count = 0;
        for(HashMap<String, Object> h : adventureList){
            imageArray[count] = Integer.parseInt(String.valueOf(h.get("id")));
            count++;
        }


        for (int i = 0; i < adventureList.size(); i++){
            adv = adventureRepo.getAdventureById(imageArray[i]);
            if(adv.image != null){
                System.out.println("image = " + adv.image);
                System.out.println("bitmap thing = " + getImage(adv.image));
                images.add(getImage(adv.image));
            }
        }

    }


    public List getImageList(){
        return images;
    }

    public static Bitmap getImage(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}