package com.example.maheshbabugorantla.googlemapclusters;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.maheshbabugorantla.googlemapclusters.Fragments.FitnessFragment;
import com.example.maheshbabugorantla.googlemapclusters.Fragments.FoodFragment;
import com.example.maheshbabugorantla.googlemapclusters.Fragments.MapsFragment;
import com.example.maheshbabugorantla.googlemapclusters.HelperClasses.RunTimePermissions;
import com.example.maheshbabugorantla.googlemapclusters.HelperClasses.Utility;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * DESCRIPTION: MainActivity class
 * Created by Mahesh Babu Gorantla
 * First Update On Mar 22, 2017 .
 * Last Update On Mar 22, 2017.
 */

public class MainActivity extends AppCompatActivity implements ResultCallback<LocationSettingsResult>{

    protected final String LOG_TAG = "MainActivity";

    // RunTime Permissions
    RunTimePermissions runTimePermissions; // Used to ask the user for the runtime permissions

    LocationSettingsRequest mLocationSettingsRequest;

    GoogleApiClient mGoogleApiClient;

    boolean isLocationEnabled = false;

    /**
     *  Constant used in the location settings dialog
     * */
    protected static final int REQUEST_CHECK_SETTINGS = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runTimePermissions = new RunTimePermissions(getApplicationContext(), this);

        // Used to implement the Swiping Tabs that contains both the Maps and Fitness Fragments
        ViewPager viewPager;
        FragmentPagerAdapter fragmentPagerAdapter;

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

        viewPager = (ViewPager) findViewById(R.id.swipeTabs);
        fragmentPagerAdapter = new myPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);

        buildGoogleApiClient();
        buildLocationSettingRequest();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        runTimePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();

        Log.i(LOG_TAG, "Inside OnResult");

        switch (status.getStatusCode()) {

            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(LOG_TAG, "All Location Settings are satisfied");
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: // This case is used to show the user the dialog
                Log.i(LOG_TAG, "Location Settings not satisfied. Showing the Location Services " +
                        "settings dialog to the user");

                // Asking the user for the settings
                try {
                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
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

    @Override
    protected void onStart() {

        if(new Utility().isLocationEnabled(getApplicationContext())) {
            Log.d(LOG_TAG, "Location Settings Enabled");
            //fragmentPagerAdapter.notifyDataSetChanged();
        }
        else {
            Log.d(LOG_TAG, "Google Location Services not available");
            Toast.makeText(getApplicationContext(), "Please Switch on the GPS for better experience", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CHECK_SETTINGS);
        }

        super.onStart();
    }

    private static class myPagerAdapter extends FragmentPagerAdapter {

        private myPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case 0:
                    return new MapsFragment();
                case 1:
                    return new FitnessFragment();
                case 2:
                    return new FoodFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {

                case 0:
                    return "Maps";

                case 1:
                    return "Fitness";

                case 2:
                    return "Food";

                default:
                    return "TAB";
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings: {
                startActivity(new Intent(this, SettingsActivity.class));
            }
            case R.id.action_current_location: {
                Log.i(LOG_TAG, "Inside the MainActivity");

                if(! new Utility().isLocationEnabled(getApplicationContext())) {
                     startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CHECK_SETTINGS);
                }
                else {
                    isLocationEnabled = true;
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *  Building the GoogleApiClient to request the Google Location Updates
     * */
    private synchronized void buildGoogleApiClient() {

        Log.i(LOG_TAG, "Building the GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .build();
    }

    /**
     *  Building Location Services Settings Checker that ensures that the device has tbe needed
     *  location settings
     * */
    protected void buildLocationSettingRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest());
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK: // Need to clarify this
                        Log.i(LOG_TAG, "User agreed to make required location settings changes");
                        }
                        break;

                    case Activity.RESULT_CANCELED:
                        Log.i(LOG_TAG, "User chose not to make required location settings changes");
                        break;
                }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
