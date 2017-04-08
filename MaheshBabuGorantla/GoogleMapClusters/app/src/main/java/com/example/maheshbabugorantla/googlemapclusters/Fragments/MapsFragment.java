package com.example.maheshbabugorantla.googlemapclusters.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.maheshbabugorantla.googlemapclusters.HelperClasses.RunTimePermissions;
import com.example.maheshbabugorantla.googlemapclusters.MarkerCluster.MyClusterItem;
import com.example.maheshbabugorantla.googlemapclusters.MarkerCluster.MyClusterRenderer;
import com.example.maheshbabugorantla.googlemapclusters.R;
import com.example.maheshbabugorantla.googlemapclusters.SettingsActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.text.DateFormat;
import java.util.Date;

/**
 * DESCRIPTION: MapsFragment class
 *              This Fragment has displays the google Maps
 * Created by MaheshBabuGorantla
 * First Update On Mar 27, 2017 .
 * Last Update On Apr 06, 2017.
 */

public class MapsFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult>,
        OnMapReadyCallback {


    private static final String LOG_TAG = "MapsFragment";

    /**
     *  Constant used in the location settings dialog
     * */
    protected static final int REQUEST_CHECK_SETTINGS = 0x02;

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

    /**
     *  Provides the entry point to Google Play Services
     * */
    protected GoogleApiClient mGoogleApiClient;

    /**
     *  Stores parameters for requests to the FusedLocationProviderApi
     * */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     * */
    protected Location mCurrentLocation;

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

    RunTimePermissions runTimePermissions;

    /**
     *  Cluster Manager to manage marker Clustering feature
     * */
    private ClusterManager<MyClusterItem> mClusterManager;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings
     * */
    protected LocationSettingsRequest mLocationSettingsRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        runTimePermissions = new RunTimePermissions(getActivity().getApplicationContext(), getActivity());

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    /**
     *  Building the GoogleApiClient to request the Google Location Updates
     * */
    private synchronized void buildGoogleApiClient() {

        Log.i(LOG_TAG, "Building the GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();


         //  Setting up the MapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.googleMap);

        if(mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.googleMap, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);


        /*
            Kickoff the process of building the GoogleApiClient, LocationRequest, and
            LocationSettingsRequest objects.
        */
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingRequest();
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
     *  Creating the Location Request
     * */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);
    }

    @Override
    public void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
    }

    /**
     *  This method starts to request the location updates from FusedLocationApi
     * */
    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(LOG_TAG, "Requesting the Location Access");
            runTimePermissions.checkLocationAccess();
        }
        else {
            Log.i(LOG_TAG, "StartedLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    this
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.i(LOG_TAG, "StartedLocationUpdates: inside onResult");
                    mRequestingLocationUpdates = true;
                }
            });
        }
    }

    /**
     *  Removes the Location Updates from FusedLocationServices Api
     * */
    private void stopLocationUpdates() {

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
    public void onConnected(@Nullable Bundle bundle) {

        // Checking if the user has given all the permissions for the app to access all the location services from the device
        if(ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(LOG_TAG, "Requesting the Location Services Access");
            runTimePermissions.checkLocationAccess();
        }

        else {
            if(mCurrentLocation == null) {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        }
    }

    /**
     *  This method is used to update the current Location Marker
     * */
    private void updateLocationUI() {

        if(currentLocMarker != null) {
            currentLocMarker.remove();
        }

        if(mCurrentLocation != null && mapReady) { // It only updates the Map when the map is Ready

            Home_CoOrdinates = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());

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

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "Connection Failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    /**
     *  This is the method call that gets fired when the device notices a Location Change
     * */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateLocationUI();
        //Toast.makeText(getActivity(), "Location Updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap mMap) {

        if(currentLocMarker != null) {
            currentLocMarker.remove();
        }

        mapReady = true;
        googleMap = mMap;

        setUpMarkerClusterer();

        insertGoogleMarkers();

        updateLocationUI();
    }

    private void insertGoogleMarkers() {

        String[] coOrdinates = {"40.41841214,-86.84202029", "40.36269908,-86.86760934", "40.53977904,-86.89005129", "40.50634612,-87.05256397",
                "40.43827456,-87.08932359", "40.48504711,-86.8012231", "40.33759611,-86.88391314", "40.50411696,-86.94593462",
                "40.43712711,-87.09235671", "40.32522856,-86.96993341", "40.32522856,-86.96993341", "40.38315806,-86.82923481",
                "40.35238126,-86.83377204", "40.50869955,-86.81338996", "40.52436521,-86.83615741", "40.52436521,-86.83615741",
                "40.46613464,-87.07873649", "40.35731712,-86.79558138", "40.33753228,-86.90681964", "40.41725732,-86.83707097",
                "40.4402483,-86.77396881", "40.41377802,-87.06603136", "40.34406513,-86.88301737", "40.47755658,-86.81723276",
                "40.51675625,-87.01240428", "40.41556367,-86.84654062", "40.54840218,-86.86115356", "40.51639931,-86.84640918",
                "40.37008616,-86.9167623", "40.49004583,-86.87159726", "40.45660114,-86.83153949", "40.45317153,-87.10016559",
                "40.37263074,-87.08512738", "40.30463727,-86.92870525", "40.48096817,-87.04038773", "40.30979276,-86.92767156",
                "40.41757992,-86.87271634", "40.33304984,-86.96281284", "40.30934259,-86.84469059", "40.37467623,-86.97825626",
                "40.46997948,-87.00214496", "40.38142492,-86.80650344"};

        int index = 0;

        for(String coordinate: coOrdinates) {
            String[] coord_values = coordinate.split(",");
            double Latitude = Double.parseDouble(coord_values[0]);
            double Longitude =  Double.parseDouble(coord_values[1]);
            MarkerOptions markerOptions = new MarkerOptions()
                                              .position(new LatLng(Latitude, Longitude))
                                              .title("LocationS" + Integer.toString(index))
                                              .icon(BitmapDescriptorFactory.fromResource(R.drawable.thumbnail_marker));
            //MyClusterItem myClusterItem = new MyClusterItem(Latitude, Longitude, "LocationT " + Integer.toString(index), "LocationS " + Integer.toString(index));
            MyClusterItem myClusterItem = new MyClusterItem(markerOptions);
            mClusterManager.addItem(myClusterItem);

            index += 1;
        }
    }

    /**
     *  All the below methods to add the capability to add the Marker Clustering Feature
     * */
    private void setUpMarkerClusterer() {

        // Initialize the cluster manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.
        mClusterManager = new ClusterManager<>(getActivity(), googleMap);

        // Point the map's listeners at the listeners implemented by the cluster manager
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setAnimation(true);

        // Initializing the Custom Cluster Renderer with context, map and clusterManager
        // Setting the ClusterRenderer to the Cluster Manager
        mClusterManager.setRenderer(new MyClusterRenderer(getActivity(), googleMap, mClusterManager));
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

        final Status status = locationSettingsResult.getStatus();

        switch (status.getStatusCode()) {

            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(LOG_TAG, "All Location Settings are satisfied");
                startLocationUpdates();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: // This case is used to show the user the dialog
                Log.i(LOG_TAG, "Location Settings not satisfied. Showing the Location Services " +
                        "settings dialog to the user");

                // Asking the user for the settings
                try {
                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(LOG_TAG, "Pending Intent unable to execute the request");
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(LOG_TAG, "Location settings are inadequate, and cannot be fixed here. " +
                        "Dialog not created.");
                break;
        }
    }

    /**
     *  Checking if the device's location settings are adequate for the app's needs using the
     *  {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     *  LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     * */
    public void checkLocationSettings() {

        if(mLocationSettingsRequest == null) {
            buildLocationSettingRequest();
        }
        if(mGoogleApiClient == null) {
            buildGoogleApiClient();
        }

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );

        result.setResultCallback(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings: {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
            case R.id.action_current_location: {
                Log.i(LOG_TAG, "Inside MapsFragment");
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
