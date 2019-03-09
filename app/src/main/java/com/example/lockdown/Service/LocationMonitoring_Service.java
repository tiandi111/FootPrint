package com.example.lockdown.Service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.List;
import java.util.concurrent.Executor;


public class LocationMonitoring_Service extends IntentService implements Executor {

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
                sendPlayMusicMessage();
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

    public void sendPlayMusicMessage() {
        Intent localIntent = new Intent();
        localIntent.setAction("Music auto-play");
        //localIntent.putExtra("data","Notice me senpai!");
        localBroadcastManager.sendBroadcast(localIntent);
        Log.d(TAG, "Auto-play!");
    }

    public void execute(Runnable r) {
        r.run();
    }
}
