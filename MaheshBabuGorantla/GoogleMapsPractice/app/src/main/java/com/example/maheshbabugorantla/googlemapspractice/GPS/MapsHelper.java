package com.example.maheshbabugorantla.googlemapspractice.GPS;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by MaheshBabuGorantla on 2/7/2017.
 *
 * This class is used to fetch the GPS Co-Ordinates based on the User.
 */

public class MapsHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String LOG_TAG = "MAPS_HELPER";

    private Double Latitude = 0.0;
    private Double Longitude = 0.0;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public MapsHelper(Context activity_context) {

        mGoogleApiClient = new GoogleApiClient.Builder(activity_context)
                              .addApi(LocationServices.API)
                              .addConnectionCallbacks(this)
                              .addOnConnectionFailedListener(this).build();


    }

    // This is called when a stable Connection to the GoogleAPI Client is
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    // Used to fetch the GPS CoOrdinates of the current user location
    public Double[] getCoordinates() {
        return new Double[] {Latitude, Longitude};
    }
}
