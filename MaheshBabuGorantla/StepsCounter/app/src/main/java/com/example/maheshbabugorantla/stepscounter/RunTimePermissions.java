package com.example.maheshbabugorantla.stepscounter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.Manifest;

/**
 * Created by MaheshBabuGorantla on 2/9/2017.
 */

public class RunTimePermissions {

    private Context applicationContext; // Stores the Application Context
    private Activity currentActivity; // Stores the Activity that is accessing these run-Time Permissions.

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 538; // Access the Fine Location
    private static final int MY_PERMISSIONS_ACCESS_NETWORK_STATE = 196;
    private static final int MY_PERMISSIONS_CHECK_INTERNET_ACCESS = 390;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 47;

    public RunTimePermissions(Context context, Activity activity) {
        this.applicationContext = context;
        this.currentActivity = activity;
    }

    // This Function displays a dialog box asynchronously to request the user for permission to access the Fine GPS Location of the Device
    public void checkLocationAccess() {
        int permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(currentActivity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION); // The users response can be accessed using MY_PERMISSIONS_ACCESS_FINE_LOCATION.
        }
    }

    // This Function displays a dialog box asynchronously to request the user for permission to access the Network State of the Device
    public void checkNetworkStateAccess() {
        int permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_NETWORK_STATE);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(currentActivity, new String[] {Manifest.permission.ACCESS_NETWORK_STATE}, MY_PERMISSIONS_ACCESS_NETWORK_STATE); // The users response can be accessed using MY_PERMISSIONS_ACCESS_NETWORK_STATE.
        }
    }

    // This Function display a dialog box asynchronously to request the user for permission to access Internet.
    public void checkInternetAccess() {
        int permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.INTERNET);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {

            // The users response can be accessed using MY_PERMISSIONS_CHECK_INTERNET_ACCESS.
            ActivityCompat.requestPermissions(currentActivity, new String[] {Manifest.permission.INTERNET}, MY_PERMISSIONS_CHECK_INTERNET_ACCESS);
        }
    }

    // This Function display a dialog box asynchronously to request the user for permission to allow permission to write to the External Storage.
    public void checkWriteExternalStorage() {
        int permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(currentActivity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE); // The users response can be accessed using MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE.
        }
    }

    // Handling the Application Functionality according the User's Permissions
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {

                if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    // Disable any application functionalities that are dependent on the above permission.
                }

                break;
            }

            case MY_PERMISSIONS_ACCESS_NETWORK_STATE: {

                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    // Disable any application functionalities that are dependent on the above permission.                }
                    break;
                }
            }

            case MY_PERMISSIONS_CHECK_INTERNET_ACCESS: {

                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    // Disable any application functionalities that are dependent on the above permission.
                }

                break;
            }

            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {

                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    // Disable any application functionalities that are dependent on the above permission.
                }
                break;
            }
        }
    }


}
