package com.example.maheshbabugorantla.googlemapspractice;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshbabugorantla.googlemapspractice.ZomatoAPI.ZomatoHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String LOG_TAG = "MainActivity";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private TextView CoOrdinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().show();

        System.out.println(new ZomatoHelper().execute("Blah Blah"));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        CoOrdinates = (TextView) findViewById(R.id.gps_coordinates); // Here we display the GPS Co-Ordinates
    }

    // Here we will connect to the GoogleAPIClient
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    // Here we will disconnect the GoogleAPIClient
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This adds items to the actionBar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle ActionBar item clicks here.
        // The action bar will automatically handle clicks on the
        // Home/Up button, so long as you specify a parent activity
        // in AndroidManifest.xml

        int id = item.getItemId();

        switch (id) {

            case R.id.action_settings:
            {
                startActivity(new Intent(this, SettingsActivity.class));
            }

            case R.id.action_gps:
            {
                
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(3000); // Getting location updates every 3 seconds
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            Toast.makeText(this, "Cannot access GPS", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Waiting for the connection", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed Please Check your connection", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        CoOrdinates.setText(String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
        //CoOrdinates.setText();
    }

    // Here need to implement a onStop/onPause to stop listening for the Location updates when the device is no longer in focus
}