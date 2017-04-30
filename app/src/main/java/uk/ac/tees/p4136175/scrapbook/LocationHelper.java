package uk.ac.tees.p4136175.scrapbook;

/**
 * Created by p4136175 on 28/04/2017.
 */

/**
 * THis class is used as a global variable, so that the currentLocation can be saved and used
 * in other classes, for example in the MakeAdventure page so the location can be shown
 */
public class LocationHelper {
    private static LocationHelper lInstance = null;

    public String currentLocation;
    protected LocationHelper(){}

    public static synchronized LocationHelper getlInstance(){
        if(null == lInstance){
            lInstance = new LocationHelper();
        }
        return lInstance;
    }

}
