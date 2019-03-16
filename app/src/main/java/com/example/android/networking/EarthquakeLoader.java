package com.example.android.networking;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.networking.QueryUtils.LOG_TAG;

/**
 * Created by Kevin Kimaru Chege on 6/25/2017.
 */

public class EarthquakeLoader extends android.support.v4.content.AsyncTaskLoader<List<Earthquake>> {

    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "onStartLoading called...... ");
        forceLoad();
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackgroundCalled() called...... ");

        if (mUrl == null) {
            return null;
        }

        // Perform the HTTP request for earthquake data and process the response.
        ArrayList<Earthquake> result = QueryUtils.fetchEarthquakeData(mUrl);
        return result;
    }


}
