package com.erik.erikdroid.connection;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

public class VolleyBasedConnection {

    private static RequestQueue requestQueue;
    public static final String TAG = "ErikDroid";

    public RequestQueue getRequestQueue(Context context) {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }

    public <T> void addToRequestQueue(Context context, Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue(context).add(req);
    }


    public <T> void addToRequestQueue(Context context, Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue(context).add(req);
    }


    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
