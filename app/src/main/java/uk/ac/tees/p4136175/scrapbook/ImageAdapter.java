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

/**
 * This class is a custom adapter used to change each image into a grid view cell
 *
 */
public class ImageAdapter extends BaseAdapter {
    // Private instances of context and the adventure repo
    private Context mContext;
    private AdventureRepo adventureRepo;

    // Currently empty list that will eventually be populated with images from adventureList
    private List<Bitmap> images = new ArrayList<>();

    /**
     * Constructor for the image adapter
     * @param c Activity called from
     * @param repo Repository to use
     */
    public ImageAdapter(Context c,  AdventureRepo repo) {
        mContext = c;
        adventureRepo = repo;
    }

    /**
     * Returns the size of the images list
     * @return int of images size
     */
    public int getCount() {
        return images.size();
    }

    /**
     * THis method has to be implemented but doesn't serve a function in our program, but it would
     * return a specific item after passing in the index
     * @param position The index
     * @return The object on the grid
     */
    public Object getItem(int position) {
        return null;
    }

    /**
     * Get the item ID of one of the objects from the grid
     * @param position Index of the grid item
     * @return ID
     */
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

        // Set the tag (varable) to the index of the item
        imageView.setTag(Integer.valueOf(position));

        // Set the image bitmap of the item to images[position]
        imageView.setImageBitmap(images.get(position));
        return imageView;
    }

    /**
     * Gets all the images from the current adventures and saves them into the list<Bitmap>
     */
    public void getImages(){
        // Gets an arraylist of hashmaps from the return of the repo method "getAdventureEntryGrid"
        // The hashmap will be populated with ID and Images
        ArrayList<HashMap<String, Object>> adventureList =  adventureRepo.getAdventureEntryGrid();
        AdventureEntry adv = new AdventureEntry();
        images.clear();

        // Create an array the same size as the current adventure list size
        int[] imageArray = new int[adventureList.size()];
        int count = 0;
        // For each hashmap, get the ID of the entry and save it into the array just created
        for(HashMap<String, Object> h : adventureList){
            imageArray[count] = Integer.parseInt(String.valueOf(h.get("id")));
            count++;
        }

        // For each item in the adventure list, get the adventure by passing in the ID from array
        // This is done so that the images will be arranged in the correct order
        for (int i = 0; i < adventureList.size(); i++){
            adv = adventureRepo.getAdventureById(imageArray[i]);
            if(adv.image != null){
                // if the image isn't null, add the image to the images list
                images.add(getImage(adv.image));
            }
        }

    }


    /**
     * Returns the images list
     * @return the images list
     */
    public List getImageList(){
        return images;
    }

    /**
     * Convert a byte[] array to a bitmap image so that the grid item image can be set
     * @param image The byte array
     * @return the bitmap
     */
    public static Bitmap getImage(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}