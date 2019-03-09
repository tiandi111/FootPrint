package com.example.lockdown.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;

public class CommonBroadCastReceiver extends BroadcastReceiver {
    private SpotifyAppRemote mSpotifyAppRemote;
    private static final String TAG = "MyBroadcastReceiver";
    private static final String CLIENT_ID = "954bf3f438c04897af73f0209a741c8a";
    private static final String REDIRECT_URI = "testschema://callback";

    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
        Log.d(TAG, log);
        ConnectToSpotify(context);
        if(mSpotifyAppRemote!=null) {
            mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX7K31D69s4M1");
            Log.d(TAG, "broadcast Music");
        }
        else
            Log.e(TAG, "SpotifyRemote is null");
        //Toast.makeText(context, log, Toast.LENGTH_LONG).show();
    }

    private void ConnectToSpotify(Context context) {
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(context, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("SpotifyRemoteConnection", "Connected! Yay!");
                        // Do what we want spotify to do after connecting
                        //connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("SpotifyRemoteConnection", throwable.getMessage()+"错误", throwable);
                    }
                });
    }
}
