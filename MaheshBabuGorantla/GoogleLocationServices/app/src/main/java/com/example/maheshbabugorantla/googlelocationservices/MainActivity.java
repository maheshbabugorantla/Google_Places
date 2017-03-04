package com.example.maheshbabugorantla.googlelocationservices;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult> {

    protected static final String TAG = "MainActivity";

    /**
     *  Created by Mahesh Babu Gorantla.
     *  Created: 2/27/2017
     *  Last Updated: 3/01/2017
     * */

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
    protected Button mStartUpdatesButton;
    protected Button mStopUpdatesButton;
    protected TextView mLastUpdatedTimeTextView;
    protected TextView mLatitudeTextView;
    protected TextView mLongitudeTextView;

    /**
     * Labels
     * */
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdatedTimeLabel;

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
     *  Used to ask the user for the RunTime Permissions
     * */
    RunTimePermissions runTimePermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runTimePermissions = new RunTimePermissions(getApplicationContext(), this);

        // Asking the user for Internet Access Permissions
        runTimePermissions.checkInternetAccess();
        runTimePermissions.checkWriteExternalStorage();
        runTimePermissions.checkLocationAccess();
        runTimePermissions.checkNetworkStateAccess();

        // Associated all the views to specific properties
        mLatitudeTextView = (TextView) findViewById(R.id.latitude_text);
        mLongitudeTextView = (TextView) findViewById(R.id.longitude_text);
        mStartUpdatesButton = (Button) findViewById(R.id.start_updates_button);
        mStopUpdatesButton = (Button) findViewById(R.id.stop_updates_button);
        mLastUpdatedTimeTextView = (TextView) findViewById(R.id.last_update_time_text);

        // Setting the Labels
        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLastUpdatedTimeLabel = getResources().getString(R.string.last_update_time_label);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle
        updateValuesFromBundle(savedInstanceState);

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
     *  When the ACCESS_FINE_LOCATION setting is specified combines with a fast update interval (5 mins)
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
     * This methods gets called when the Google API Client successfully gets connected
     * */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
     * This is callback function that fires when a location change is noticed by the device
     * */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateLocationUI();
        Toast.makeText(this, getResources().getString(R.string.location_updated_message),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
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

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
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
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes");
                        startLocationUpdates();
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
     * Handles the Start Updates button and requests start of location updates. Does nothing ig
     * updates have already been requested.
     * */
    public void startUpdatesButtonHandler(View view) {
        Log.i(TAG, "Started Location Updates");
        checkLocationSettings();
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
                    setButtonsEnabledState();
                }
            });
        }
    }

    /**
     * Updating all the UI Fields
     * */
    private void updateUI() {
        setButtonsEnabledState();
        updateLocationUI();
    }

    /**
     * Disables both buttons when functionality is disabled due to insufficient location settings.
     * Otherwise this ensures that only button is enabled at any time. The Start Updates button is
     * enabled if the user is not requesting location updates. The Stop Updates button is enabled
     * if the user is requesting location updates.
     * */
   private void setButtonsEnabledState() {

       if(mRequestingLocationUpdates) {
           mStartUpdatesButton.setEnabled(false);
           mStopUpdatesButton.setEnabled(true);
       } else {
           mStopUpdatesButton.setEnabled(false);
           mStartUpdatesButton.setEnabled(true);
       }
   }

    /**
     *  Sets the value of the UI fields for the location latitude, longitude and last update time.
     * */
    private void updateLocationUI() {

        if(mCurrentLocation != null) {

            mLatitudeTextView.setText(String.format(Locale.getDefault(), "%s: %f", mLatitudeLabel,
                    mCurrentLocation.getLatitude()));

            mLongitudeTextView.setText(String.format(Locale.getDefault(), "%s: %f", mLongitudeLabel,
                    mCurrentLocation.getLongitude()));

            mLastUpdatedTimeTextView.setText(String.format(Locale.getDefault(),"%s: %s", mLastUpdatedTimeLabel,
                    mLastUpdateTime));
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
                mRequestingLocationUpdates = false;
                setButtonsEnabledState();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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
        if(mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "Inside onRequestPermissionsResult");
        runTimePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
