package com.example.lockdown;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lockdown.service.LocationMonitoring_Service;
import com.example.lockdown.network.DownloadCallback;
import com.example.lockdown.network.NetworkRequestHandler;
import com.example.lockdown.tool.CommonBroadCastReceiver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.List;
import java.util.Locale;

public class Maps_Activity extends AppCompatActivity
        implements DownloadCallback, OnMapReadyCallback, Addfootprint_Fragment.OnFragmentInteractionListener {

    // Map
    private GoogleMap mMap;
    private static final String TAG = "Maps_Activity";
    public Marker currentMarker = null;
    public Addfootprint_Fragment currentFragment;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_CODE = 1337;
    private float zoom = 15;

    // Spotify
    private SpotifyAppRemote mSpotifyAppRemote;
    private static final String CLIENT_ID = "954bf3f438c04897af73f0209a741c8a";
    private static final String REDIRECT_URI = "testschema://callback";

    // BroadCastReceiver for receiving location info
    private CommonBroadCastReceiver commonBroadCastReceiver;

    //  Networking
    private Network_Fragment networkFragment; // singleton for networkFragment
    private boolean downloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The correct order to call functions below is:
        // requestWindowFeature -> setContentView -> ..._init(basic UI elements)
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_maps);

        customizedTitle_init();
        bottom_navigation_init();

        setGoogleMapFragment();

        AuthenticationForSpotify();
        ConnectToSpotify();

        LocationMonitoringService_init(Maps_Activity.this);
        broadCastReciverInit( LocalBroadcastManager.getInstance(this) );

        networkFragment = Network_Fragment.getInstance(getSupportFragmentManager(), "http://3.87.45.142:8080/");

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near College Station, Texas
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String TAG = "MapReady";

        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        // Add a marker in LockDown and move the camera
        LatLng LockDown = new LatLng(30.58757, -96.33633);
        mMap.addMarker(new MarkerOptions().position(LockDown).title("Hometown"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LockDown, zoom));
        setMapLongClick(mMap);
        setPoiClick(mMap);
        enableMyLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) { }

    public void LocationMonitoringService_init(Context context) {
        if(isServiceRunning("LocationMonitoringThread")) {
            Log.i("Service is running!","return");
            return;
        }
        Intent location_monitoring_intent = new Intent(context, LocationMonitoring_Service.class);
        startService(location_monitoring_intent);
    }

    public void customizedTitle_init() {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText("Map");
    }

    private void setGoogleMapFragment(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void AuthenticationForSpotify() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    private void ConnectToSpotify() {
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("SpotifyRemoteConnection", "Connected! Yay!");
                        // Do what we want spotify to do after connecting
                        // Write code in connected()
                        connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("SpotifyRemoteConnection", throwable.getMessage(), throwable);
                    }
                });
    }

    private void bottom_navigation_init() {
        Intent intent_toInd = new Intent(Maps_Activity.this, Yourfootprint_Index.class);
        Intent intentExplore = new Intent(Maps_Activity.this, ShowFootprintTest.class);
        BottomNavigationView bottomNV = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch ( menuItem.getItemId()) {
                    case R.id.navigation_map:
                        return true;
                    case R.id.navigation_explore:
                        startActivity(intentExplore);
                        return true;
                    case R.id.navigation_yourfootprint:
                        startActivity(intent_toInd);
                        return true;
                }
                return false;
            }
        });
        bottomNV.setSelectedItemId(R.id.navigation_map);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    private void connected() {
        // Play a playlist
        //mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX7K31D69s4M1");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("yourfootprint_index", track.name + " by " + track.artist.name);
                    }
                });
    }

    private void setMapLongClick(final GoogleMap map) {
//        FetchAddressTask mFetchAddressTask = new FetchAddressTask(Maps_Activity.this);
//        mFusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            mFetchAddressTask.execute(location);
//                            }
//                        }
//                    });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if( currentMarker != null) {
                    currentMarker.remove();
                    currentMarker = null;
                }
                if( currentFragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                    currentFragment = null;
                }
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //String snippet = mFetchAddressTask.getAddress();
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);

                if( currentMarker != null) {
                    currentMarker.remove();
                    currentMarker = null;
                }
                currentMarker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.dropped_pin))
                        .snippet(snippet));

                if( currentFragment != null ) {
                    getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                    currentFragment = null;
                }
                currentFragment = Addfootprint_Fragment.newInstance(snippet);
                getSupportFragmentManager().beginTransaction().add(R.id.map, currentFragment).commit();
                startDownload();

                StringRequest stringRequest = new StringRequest
                        (Request.Method.GET, "http://3.87.45.142:8080/", new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.d("NetRequest", response);
                                Toast.makeText(Maps_Activity.this, response, Toast.LENGTH_SHORT );
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Log.d("NetRequest", error.toString());
                            }
                        });
                NetworkRequestHandler.getInstance(Maps_Activity.this).addToRequestQueue(stringRequest);
                //startActivity(intent);
            }
        });
    }

    private void setPoiClick(final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) {
                Marker poiMarker = mMap.addMarker(new MarkerOptions()
                        .position(poi.latLng)
                        .title(poi.name));
                poiMarker.showInfoWindow();
            }
        });
    }

    private boolean isServiceRunning(final String className) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }

    private void broadCastReciverInit( LocalBroadcastManager localBroadcastManager) {
        commonBroadCastReceiver = new CommonBroadCastReceiver();
        IntentFilter filter = new IntentFilter("Music auto-play");
        //filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        localBroadcastManager.registerReceiver(commonBroadCastReceiver, filter);
    }


    // Trigger to start download
    private void startDownload() {
        if (!downloading && networkFragment != null) {
            // Execute the async download
            networkFragment.startDownload();
            downloading = true;
        }
    }

    @Override
    public void updateFromDownload(String result) {
        // Update your UI here based on result of download.
        Log.d("Message", result);
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                break;
            case Progress.CONNECT_SUCCESS:
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                break;
        }
    }

    @Override
    public void finishDownloading() {
        downloading = false;
        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }

    /**
     * These two methods are used to set map style select menu
     */
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */
}
