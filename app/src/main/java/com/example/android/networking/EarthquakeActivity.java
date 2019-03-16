package com.example.android.networking;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&&minfelt=50&minmagnitude=4";
    private static final int EARTHQUAKE_LOADER_ID = 0;
    ListView earthquakeListView;
    private EarthquakeAdapter adapter;
    TextView textView;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(LOG_TAG, "TEST: Earthquake Activity OnCreate Called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);


        earthquakeListView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        textView = (TextView) findViewById(R.id.emptyView);
        earthquakeListView.setEmptyView(textView);

        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake currentEarthquake = adapter.getItem(position);

                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                startActivity(websiteIntent
                );
            }
        });

        LoaderManager loaderManager = getSupportLoaderManager();

        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i(LOG_TAG, "TEST: calling initLoader...");
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            textView.setText("No internet Connection");
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST: oncreateLoader called...");
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.i(LOG_TAG, "TEST: onLoadFinished called...");
        //clear the adapter of previous earthquakes
        progressBar.setVisibility(View.GONE);
        adapter.clear();
        //update listView
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes);
        } else {
            textView.setText("No earthquakes found");
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG, "TEST: onLoaderReset called...");

        //clear out existing data
        adapter.clear();
    }

}
