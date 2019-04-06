package com.example.lockdown.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/*
This class is used to handle netwrok request.
 */
public class NetworkRequestHandler {
    private static NetworkRequestHandler singleton;
    private static Context ctx;
    private RequestQueue requestQueue;

    private NetworkRequestHandler(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized NetworkRequestHandler getInstance(Context context) {
        if( singleton == null ) {
            singleton = new NetworkRequestHandler(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if( requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    private String url = "http://my-json-feed";

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
//                    textView.setText("Response: " + response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error

                }
            });
//    singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
}

