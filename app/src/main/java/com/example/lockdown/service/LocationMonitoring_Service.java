package com.example.lockdown.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import com.example.lockdown.tool.location;

import java.util.concurrent.Executor;


public class LocationMonitoring_Service extends IntentService implements Executor {
    static location testpoint = new location(30.6217049, -96.3405336);
    private int dist;
    private LocalBroadcastManager localBroadcastManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String TAG = "LocationMoni_Service";

    public LocationMonitoring_Service(  ) {
        super("LocationMonitoringThread");
    }

    public LocationMonitoring_Service(FusedLocationProviderClient providerclient ) {
        super("LocationMonitoringThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            localBroadcastManager = LocalBroadcastManager.getInstance(LocationMonitoring_Service.this);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(LocationMonitoring_Service.this);
            while(true) {
                Thread.sleep(5000);

                if(mFusedLocationClient!=null) {
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(LocationMonitoring_Service.this, new OnSuccessListener<Location>() {
                                @Override

                                public void onSuccess(Location newlocation) {
                                    if (newlocation != null) {
                                        Log.d(TAG, "Lat:" + newlocation.getLatitude() + ", Lng" + newlocation.getLongitude() );
                                        location cur_location = new location(newlocation.getLatitude(), newlocation.getLongitude());
                                        dist = cur_location.computeDistanceTo(testpoint);
                                        Log.d(TAG, "Current distance is: " + dist + ".");
                                        if(dist < 0)
                                            sendPlayMusicMessage();

                                    }
                                }
                            });
                }
            }
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

    public void sendPlayMusicMessage() {
        Intent localIntent = new Intent();
        localIntent.setAction("Music auto-play");
        localIntent.putExtra("Distance","Distance to Zach282B is: " + dist + " .");

        localBroadcastManager.sendBroadcast(localIntent);
        Log.d(TAG, "Auto-play!");
    }

    public void execute(Runnable r) {
        r.run();
    }
}
