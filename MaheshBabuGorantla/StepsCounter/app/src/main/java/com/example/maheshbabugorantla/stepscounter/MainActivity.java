package com.example.maheshbabugorantla.stepscounter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.location.Location;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshbabugorantla.stepscounter.CustomViews.CircularProgressBar;
import com.example.maheshbabugorantla.stepscounter.HelperClasses.Utility;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Date;

/**
 *  This Activity will be launching screen for the Android Application
 *  Date Created: 2/10/2017
 *  Last Date Updated: 3/4/2017
 * */

public class MainActivity extends AppCompatActivity implements
        SensorEventListener,
        ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult>,
        OnMapReadyCallback {

    protected static final String TAG = "MainActivity";

    // Variables for the SensorManager
    SensorManager mSensorManager;
    Sensor mStepsCounter = null;

//    boolean locationServicesEnabled = false;
    boolean mRunning = false;

    RunTimePermissions runTimePermissions; // USed to ask the User for the RunTime Permissions

    CircularProgressBar circularProgressBar;

    // Steps Count
    int oldSteps = 0;
    int newSteps = 0;

    static final String OLD_STEPS = "old steps";

    /**
     *  Constant used in the location settings dialog
     * */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     *  The desired interval for location updates. Inexact. Updates may be more or less frequent
     * */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     *  The fastest rate for active location updates. Exact. Updates will never be more frequent
     *  than this value.
     * */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     *  Provides the entry point to Google Play Services
     * */
    protected GoogleApiClient mGoogleApiClient;

    /**
     *  Stores parameters for requests to the FusedLocationProviderApi
     * */
    protected LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings
     * */
    protected LocationSettingsRequest mLocationSettingsRequest;


    /**
     * Represents a geographical location.
     * */
    protected Location mCurrentLocation;

    /**
     *  UI Widgets
     * */
    protected Button mStopUpdatesButton;

    /**
     *  Tracks the status of the location updates request. Value changes when the user presses the
     *  Start Updates and Stop Updates buttons.
     * */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String
     * */
    protected String mLastUpdateTime;


    /**
     * Object to Access the Google Map Fragment in the application
     * */
    protected GoogleMap googleMap;
    boolean mapReady = false;
    protected Marker currentLocMarker;
    LatLng Home_CoOrdinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        runTimePermissions = new RunTimePermissions(getApplicationContext(), this);

        // Asking the user for all runtime permissions
        runTimePermissions.checkInternetAccess();
        runTimePermissions.checkLocationAccess();
        runTimePermissions.checkNetworkStateAccess();
        runTimePermissions.checkWriteExternalStorage();

        try {
            getSupportActionBar().show();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        // Checking for the presence of the StepsCounter Sensor on the User's Device.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepsCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        circularProgressBar = (CircularProgressBar) findViewById(R.id.progressBar);
        circularProgressBar.setMax(10000);

        // Associated all the views to specific properties
        mStopUpdatesButton = (Button) findViewById(R.id.stop_updates_button);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle
        updateValuesFromBundle(savedInstanceState);

        /**
         *  Setting up the MapFragment
         * */
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);

        /**
         *  Kickoff the process of building the GoogleApiClient, LocationRequest, and
         *  LocationSettingsRequest objects.
         * */
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingRequest();
    }

    /**
     *  Updates fields based on data stored in the bundle.
     *
     *  @param savedInstanceState: The activity state saved in the Bundle
     * */
    private void updateValuesFromBundle(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates button are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and Update the UI to show the
            // correct latitude and longitude
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of the mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            updateUI();
        }
    }

    /**
     *  Builds a GoogleApiClient. Uses the method to request the LocationServices API.
     * */
    protected synchronized void buildGoogleApiClient() {

        Log.i(TAG, "Building GoogleApiClient");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     *  This method sets up the location request. Android has two location request settings.
     *  One is ACCESS_FINE_LOCATION and one is ACCESS_COARSE_LOCATION. These setting control the accuracy
     *  of the current location. This ample uses ACCESS_FINE_LOCATION, as defined in the AndroidManiffest.xml
     *  <p/>
     *  When the ACCESS_FINE_LOCATION setting is specified combines with a fast update interval (5 secs)
     *  the Fused Location Provider API returns location updates that are accurate to within a few feet.
     *  <p/>
     *  These settings are appropriate for mapping applications that show real-time location
     *  updates.
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setSmallestDisplacement(5);
    }

    /**
     *  Building Location Services Settings Checker that ensures that the device has tbe needed
     *  location settings
     * */
    protected void buildLocationSettingRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // This adds items to the action Bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Created by: Mahesh Babu Gorantla
     * Created on: Feb 09, 2017
     * Last Updated on: Mar 05, 2017
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle ActionBar item clicks here
        // The action bar will automatically handle clicks on the Home/Up button
        // so long as you specify a parent Activity in AndroidManifest.xml

        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings: {
                startActivity(new Intent(this, SettingsActivity.class));
            }
            case R.id.action_current_location: {
                checkLocationSettings();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *  This function is called immediately after onCreate
     *  Author: Mahesh Babu Gorantla , Date: Feb 09, 2017
     */
    @Override
    protected void onStart() {

        super.onStart();

        // Connecting to the Google API Client
        mGoogleApiClient.connect();

        // Fetching the User Settings
        circularProgressBar.setMax(new Utility().getMaxStepsCount(this));

        mRunning = true;

        Toast.makeText(this, "Inside onStart", Toast.LENGTH_SHORT).show();

        oldSteps = newSteps;

        // Registering the StepCounter Listener to listen for the changes
        if(mStepsCounter != null) {
            // Here the sampling frequency is set to the rate that is suitable for the user interface which has a value of 2
            Toast.makeText(this, "Registering StepsCount Listener", Toast.LENGTH_SHORT);
            mSensorManager.registerListener(this, mStepsCounter, SensorManager.SENSOR_DELAY_UI);
        }
        else {
            Toast.makeText(this, "Step Counter not available!", Toast.LENGTH_LONG).show();

            // Else May be implement our own custom Algorithm to count the steps using Accelerometer and Gyroscopes

            //googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(Home_CoOrdinates, 15, 65, 112)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        Log.i(TAG, "Inside onPause");
        if(mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Inside onStop");
        mGoogleApiClient.disconnect(); // Stop listening to updates when app goes into the background
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        System.out.println("New Steps: " + newSteps);
        outState.putString(OLD_STEPS, String.valueOf(newSteps));

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    /**
     * This function is called when the System shuts down the app to free up the resources
     * Author: Mahesh Babu Gorantla , Date: Feb 09, 2017
     * **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRunning = false;
        mSensorManager.unregisterListener(this, mStepsCounter); // UnRegistering the Sensor when the Application is not in Focus
    }

    /**
     * This function is called whenever there is a new sensor Event
     * Author: Mahesh Babu Gorantla , Date: Feb 09, 2017
     * **/
    @Override
    public void onSensorChanged(SensorEvent event) {

        if(mRunning) {
            newSteps = Math.round(event.values[0]);
            ((TextView) findViewById(R.id.stepsCount)).setText(String.valueOf(newSteps - oldSteps));
            circularProgressBar.setProgress(Math.round(newSteps - oldSteps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * This is used to determine if a run-time permission has been granted by the user/not
     * Author: Mahesh Babu Gorantla , Date: Feb 10, 2017
     **/
     @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        runTimePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Requesting Location Access");
            runTimePermissions.checkLocationAccess();
        }
        else {
            if(mCurrentLocation == null) {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient); // Need to handle this later

                // Using android.icu.text.SimpleDateFormat makes it useful only from API 24 instead use java.text.SimpleDateFormat
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    /**
     *  The callback invoked when
     *  {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     *  LocationSettingsRequest)} is called. Examines the
     *  {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     *  location settings are adequate. If they are not, begins the process of presenting a location
     *  settings dialog to the user.
     * */
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();

        switch (status.getStatusCode()) {

            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All Location Settings are satisfied");
                startLocationUpdates();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: // This case is used to show the user the dialog
                Log.i(TAG, "Location Settings not satisfied. Showing the Location Services " +
                        "settings dialog to the user");

                // Asking the user for the settings
                try {
                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "Pending Intent unable to execute the request");
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. " +
                        "Dialog not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK: // Need to clarify this
                        Log.i(TAG, "User agreed to make required location settings changes");
                        if(mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
                            Log.i(TAG, "Started Location Updates");
                            startLocationUpdates();
                        }
                        break;

                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes");
                        break;
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * This is callback function that fires when a location change is noticed by the device
     *
     * TO DO
     *
     * */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateLocationUI();
        Toast.makeText(this, getResources().getString(R.string.location_updated_message),
                Toast.LENGTH_SHORT).show();
    }

    /**
     *  Checking if the device's location settings are adequate for the app's needs using the
     *  {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     *  LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     * */
    protected void checkLocationSettings() {

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );

        result.setResultCallback(this);
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates
     * */
    public void stopUpdatesButtonHandler(View view) {
        Log.i(TAG, "Stopped Location Updates");
        stopLocationUpdates();
    }

    /**
     * This method requests location updates from the FusedLocationApi
     * */
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Requesting the Location Access");
            runTimePermissions.checkLocationAccess();
        }
        else {
            Log.i(TAG, "StartedLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    this
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.i(TAG, "StartedLocationUpdates: inside onResult");
                    mRequestingLocationUpdates = true;
                }
            });
        }
    }

    /**
     * Updating all the UI Fields
     * */
    private void updateUI() {
        updateLocationUI();
    }

    /**
     *  Sets the value of the UI fields for the location latitude, longitude and last update time.
     *  And update the location the Google Maps
     * */
    private void updateLocationUI() {

        if(currentLocMarker != null) {
            currentLocMarker.remove();
        }

        if(mCurrentLocation != null && mapReady) { // It only updates the Map when the map is Ready
            //Log.i(TAG, "Inside mCurrentLocation not Null");
            Home_CoOrdinates = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(Home_CoOrdinates, 15, 65, 112)));

            // Adding the Google Marker to the current location
            MarkerOptions markerOptions = new MarkerOptions();

            // This places a marker on the GPS Co-Ordinates of the current Location
            markerOptions.position(Home_CoOrdinates);
            markerOptions.title("Here"); // This is title of the Google Maps Marker
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            currentLocMarker = googleMap.addMarker(markerOptions);
        }
    }

    /**
     * Removes location Updates from the FusedLocationApi.
     * */
    protected void stopLocationUpdates() {

        // It is a good practice to remove location requests when the activity is in a paused
        // or stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap mMap) {

        /** Clearing any existing Marker at the current Device Location */
        if(currentLocMarker != null) {
            currentLocMarker.remove();
        }

        mapReady = true;
        googleMap = mMap;

        updateLocationUI();
    }
}