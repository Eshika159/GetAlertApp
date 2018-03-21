package com.example.eshika.getalert;

/**
 * Created by Eshika on 04-Feb-18.
 */

public class CustomList {

    private String placename;
    private double latitude;
    private double longitude;


    public CustomList(String placename, double latitude, double longitude) {
        this.placename = placename;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlacename() {
        return placename;
    }



    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }


}
