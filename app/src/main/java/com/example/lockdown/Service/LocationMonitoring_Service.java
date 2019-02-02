package com.example.lockdown.Service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

public class LocationMonitoring_Service extends IntentService implements Executor {

    private String TAG = "LocationMonitoring_Service";
    FusedLocationProviderClient mFusedLocationClient;
    int cnt = 0;

    public LocationMonitoring_Service(  ) {
        super("LocationMonitoringThread");
    }

    public LocationMonitoring_Service(FusedLocationProviderClient providerclient ) {
        super("LocationMonitoringThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(LocationMonitoring_Service.this);
            while(true) {
                Thread.sleep(5000);
                if(mFusedLocationClient!=null) {
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(LocationMonitoring_Service.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        Log.d(TAG, "Lat:" + location.getLatitude() + ", Lng" + location.getLongitude() );
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

    public void execute(Runnable r) {
        r.run();
    }
}
