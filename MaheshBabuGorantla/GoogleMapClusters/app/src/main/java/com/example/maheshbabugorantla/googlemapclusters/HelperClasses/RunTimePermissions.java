package com.example.maheshbabugorantla.googlemapclusters.HelperClasses;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * DESCRIPTION: RunTimePermissions class
 *              This class is used handle all the run-time permissions that are accessed by the
 *              application to access the resources on the device by the app.
 * Created by MaheshBabuGorantla
 * First Update On Mar 22, 2017 .
 * Last Update On Mar 22, 2017.
 */

public class RunTimePermissions {

    private Context applicationContext;
    private Activity currentActivity;

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 538;
    private static final int MY_PERMISSIONS_ACCESS_NETWORK_STATE = 196;
    private static final int MY_PERMISSIONS_CHECK_INTERNET_ACCESS = 390;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 47;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 55;

    protected static final String TAG = "RunTimePermissions";

    public RunTimePermissions(Context context, Activity activity) {
        this.applicationContext = context;
        this.currentActivity = activity;
    }

    // This Function displays a dialog box asynchronously to request the user for permission to access the Fine GPS Location of the Device
    public void checkLocationAccess() {

        int fineLocation_permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocation_permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION);

        Log.i(TAG, "Location Access Permission");

        if(fineLocation_permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Fetching Fine Location Access Permission");
            ActivityCompat.requestPermissions(currentActivity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION); // The users response can be accessed using MY_PERMISSIONS_ACCESS_FINE_LOCATION.
        }

        if(coarseLocation_permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Fetching Coarse Location Access Permission");
            ActivityCompat.requestPermissions(currentActivity, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
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

        Log.i(TAG, "Inside onRequestPermissionsResult");

        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {

                if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.i(TAG, "Fine Location access permission granted");
                } else {
                    // Disable any application functionalities that are dependent on the above permission.
                }

                break;
            }

            case MY_PERMISSION_ACCESS_COARSE_LOCATION: {

                if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.i(TAG, "Coarse Location access permission granted");
                } else {
                    // Disable any application functionalities that are dependent on the above permission.
                }
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
