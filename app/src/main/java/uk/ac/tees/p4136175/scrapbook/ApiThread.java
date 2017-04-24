package uk.ac.tees.p4136175.scrapbook;

import android.os.Build;
import android.support.annotation.RequiresApi;

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

public class ApiThread implements Runnable {

    private float longi1, lati1;

    public ApiThread(float longi, float lati){
        longi1 = longi;
        lati1 = lati;
        System.out.println("longitude: " + longi + " latitude: " + lati);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        try  {
            try {
                URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + longi1 + "," + lati1 + "&sensor=false");
                System.out.println(url);
                JSONObject json = new JSONObject();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
                    JSONParser parser = new JSONParser();
                    for (String line; (line = reader.readLine()) != null;) {
                        System.out.println(line);

                        json = (JSONObject) parser.parse(line);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GPS_Service.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println(json);
                JSONObject j = (JSONObject) json.get("results");
                System.out.println(j);
                String s = String.valueOf(j.get("formatted_address"));
                System.out.println(s);


            } catch (MalformedURLException ex) {
                Logger.getLogger(GPS_Service.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
