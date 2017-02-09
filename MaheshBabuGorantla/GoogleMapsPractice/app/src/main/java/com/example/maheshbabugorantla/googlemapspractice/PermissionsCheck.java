package com.example.maheshbabugorantla.googlemapspractice;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.Manifest;

/**
 * Created by Mahesh Babu Gorantla on 1/20/2017.
 *
 * This Class contains all the helper methods to enable Run-Time Permissions for the Application
 *
 * Remember: These Run-Time Permissions are a new Feature added in Android Marshmallow.
 *           For Android versions such as Lollipop and below do not require these run-time permissions,
 *           Just mentioning those permissions in the AndroidManifest.xml file will suffice.
 *
 * Documentation Available: https://developer.android.com/training/permissions/requesting.html
 */

public class PermissionsCheck {

    private Context applicationContext; // Stores the Application Context
    private Activity currentActivity; // Stores the Activity that is accessing these run-Time Permissions.

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 538; // Access the Fine Location
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 210;
    private static final int MY_PERMISSIONS_ACCESS_NETWORK_STATE = 196;
    private static final int MY_PERMISSIONS_ACCESS_WIFI_STATE = 200;
    private static final int MY_PERMISSIONS_CHECK_INTERNET_ACCESS = 390;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 47;

    public PermissionsCheck(Context context, Activity activity) {
        this.applicationContext = context;
        this.currentActivity = activity;
    }

    // This Function displays a dialog box asynchronously to request the user for permission to access the Fine GPS Location of the Device
    public void checkFineLocationAccess() {
        int permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(currentActivity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION); // The users response can be accessed using MY_PERMISSIONS_ACCESS_FINE_LOCATION.
        }
    }

    // This Function displays a dialog box asynchronously to request the user for permission to access the Fine GPS Location of the Device
    public void checkCoarseLocationAccess() {
        int permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(currentActivity, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION); // The users response can be accessed using MY_PERMISSIONS_ACCESS_COARSE_LOCATION.
        }
    }

    public void checkWifiStateAccess() {
        int permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_WIFI_STATE);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(currentActivity, new String[] {Manifest.permission.ACCESS_WIFI_STATE}, MY_PERMISSIONS_ACCESS_WIFI_STATE); // The users response can be accessed using MY_PERMISSIONS_ACCESS_WIFI_STATE.
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

            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION: {
                if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    // Disable any application functionalities that are dependent on the above permission.
                }
            }

            case MY_PERMISSIONS_ACCESS_NETWORK_STATE: {

                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    // Disable any application functionalities that are dependent on the above permission.                }
                }

                break;
            }

            case MY_PERMISSIONS_ACCESS_WIFI_STATE: {
                if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    // Disable any application functionalities that are dependent on the above permission.                }
                }

                break;
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