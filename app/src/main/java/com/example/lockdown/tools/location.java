package com.example.lockdown.tools;

import android.location.Location;

import java.lang.Math;

public class location {
    private double EARTH_RADIUS = 6378.137; // Km
    private double Lng;
    private double Lat;
    private String Address;
    protected Location mLastKnownLocation;

    public location() {};

    public location(double lat, double lng) { setLatLng(lat, lng); }

    public void setLatLng(double lat, double lng) {
        Lat = lat;
        Lng = lng;
    }

    public int computeDistanceTo(location footprint) {
        // Get latitude and longitude of the footprint
        double lat_start = rad(Lat);
        double lat_end = rad( footprint.getLat() );
        double lat_diff = lat_start - lat_end;
        double lng_diff = rad(Lng) - rad(footprint.getLng());

        double dis = 2 * Math.asin ( Math.sqrt(Math.pow(Math.sin(lat_diff/2),2) +
                Math.cos(lat_start) * Math.cos(lat_end) * Math.pow(Math.sin(lng_diff/2),2)) );
        dis *= EARTH_RADIUS * 1000;

        return (int) dis;
    }

    private static double rad(double d) { return d * Math.PI / 180.0; }

    public double getLat() { return Lat; }
    public double getLng() { return Lng; }
    public String getAddress() { return Address; }

    // Test
    public static void main(String[] args) {
        double[][] testData = {{30.600179, -96.375236, 30.411157, -96.523966}, {64.3432, 107.333, 30.411157, 96.523966},
                {64.3432, 107.333, 64.3231, 107.3331}, {30.62158, -96.34063, 30.62084, -96.34914}};
        double[] correctResult = {25, 3845, 2, 0.1};

        location S = new location();
        location E = new location();

        for(int i=0; i<testData.length; i++) {
            double[] point = testData[i];
            S.setLatLng(point[0], point[1]);
            E.setLatLng(point[2], point[3]);
            System.out.println("correct:" +correctResult[i]*1000 +", your method:" +S.computeDistanceTo(E));
        }

    }
}

