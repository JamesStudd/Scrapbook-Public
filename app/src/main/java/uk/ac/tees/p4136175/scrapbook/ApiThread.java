package uk.ac.tees.p4136175.scrapbook;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Sean on 24/04/2017.
 */

/**
 * This is a custom thread that can have longitude and latitude passed in
 * so that the API can be used.
 *
 * This class was constructed as I don't think it was possible to do an API call using
 * the main thread
 */
public class ApiThread implements Runnable {

    // Local instnaces of longitude and latitude
    private float longitude, latitude;

    // The formatted address
    String formattedAddress;

    /**
     * Constructor for the thread
     * @param longi longtitude to use in the api
     * @param lati latitude to use in the api
     */
    public ApiThread(float longi, float lati){
        longitude = longi;
        latitude = lati;
    }

    /**
     * This gets called when the thread starts
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        try  {
            try {
                // API call to return a JSON object consiting of various details about the location
                URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + longitude + "," + latitude + "&sensor=false");

                // Initialise a JSONObject and a stringtojson just for the return from the api
                JSONObject json = new JSONObject();
                String stringToJson="";

                // Read each line of the API call
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
                    JSONParser parser = new JSONParser();
                    for (String line; (line = reader.readLine()) != null;) {
                        // Add each line to a string
                        stringToJson+=line;
                    }
                    // Convert the string to a json file
                    json = (JSONObject) parser.parse(stringToJson);
                } catch (IOException ex) {
                    Logger.getLogger(GPS_Service.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Go through each layer of the JSON file to get the "formatted_address" this will
                // be like so: 16 Example Drive, Exampletown, Examplecounty
                JSONArray j = (JSONArray) json.get("results");
                JSONObject j2 = (JSONObject) j.get(0);
                formattedAddress = String.valueOf(j2.get("formatted_address"));


            } catch (MalformedURLException ex) {
                Logger.getLogger(GPS_Service.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the formatted address
     * @return the formatted address
     */
    public String getFormattedAddress(){
        return formattedAddress;
    }
}
