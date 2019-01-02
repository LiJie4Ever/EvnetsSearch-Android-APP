package com.example.lijie.csci571hw9.Entity;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lijie.csci571hw9.Utils.NetworkUtility;

public class AutoCompleteApiCall {

    private static AutoCompleteApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public AutoCompleteApiCall ( Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized AutoCompleteApiCall getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AutoCompleteApiCall(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String params = "keyword=" + query;
        String url = NetworkUtility.GET_AUTO_COMPLETE + params;
        url = url.replaceAll("\\s+", "+");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        AutoCompleteApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }
}
