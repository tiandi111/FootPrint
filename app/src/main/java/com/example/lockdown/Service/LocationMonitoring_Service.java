package com.example.lockdown.Service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;
import java.util.concurrent.Executor;

public class LocationMonitoringService extends IntentService implements Executor {

    private String TAG = "LocationMonitoringService";
    FusedLocationProviderClient mFusedLocationClient;
    int cnt = 0;

    public LocationMonitoringService(  ) {
        super("LocationMonitoringThread");
    }

    public LocationMonitoringService( FusedLocationProviderClient providerclient ) {
        super("LocationMonitoringThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(LocationMonitoringService.this);
            while(true) {
                Thread.sleep(5000);
                if(mFusedLocationClient!=null) {
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(LocationMonitoringService.this, new OnSuccessListener<Location>() {
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
