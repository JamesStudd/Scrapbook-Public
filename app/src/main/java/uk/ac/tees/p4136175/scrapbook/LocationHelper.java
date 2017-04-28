package uk.ac.tees.p4136175.scrapbook;

/**
 * Created by p4136175 on 28/04/2017.
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
