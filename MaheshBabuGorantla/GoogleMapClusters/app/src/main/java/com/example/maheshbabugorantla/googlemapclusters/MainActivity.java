package com.example.maheshbabugorantla.googlemapclusters;

import android.content.Intent;
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

import com.example.maheshbabugorantla.googlemapclusters.Fragments.FitnessFragment;
import com.example.maheshbabugorantla.googlemapclusters.Fragments.MapsFragment;
import com.example.maheshbabugorantla.googlemapclusters.HelperClasses.RunTimePermissions;

/**
 * DESCRIPTION: MainActivity class
 * Created by Mahesh Babu Gorantla
 * First Update On Mar 22, 2017 .
 * Last Update On Mar 22, 2017.
 */

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = "MainActivity";

    // RunTime Permissions
    RunTimePermissions runTimePermissions; // Used to ask the user for the runtime permissions

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

        ViewPager viewPager = (ViewPager) findViewById(R.id.swipeTabs);
        FragmentPagerAdapter fragmentPagerAdapter = new myPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        runTimePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {

                case 0:
                    return "Maps";

                case 1:
                    return "Fitness";

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
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
