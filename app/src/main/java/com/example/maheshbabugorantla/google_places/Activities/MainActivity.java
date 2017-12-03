package com.example.maheshbabugorantla.google_places.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.example.maheshbabugorantla.google_places.Fragments.FitnessRecord;
import com.example.maheshbabugorantla.google_places.Fragments.RecipeActivity;
import com.example.maheshbabugorantla.google_places.Fragments.RestaurantsScreen;

import com.example.maheshbabugorantla.google_places.R;

/**
 * DESCRIPTION: MainActivity class
 * Created by Mahesh Babu Gorantla
 * First Update On Aug 31, 2017.
 * Last Update On Aug 31, 2017.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Used to implement the Tabs Layout
        ViewPager viewPager;
        FragmentPagerAdapter fragmentPagerAdapter;

        viewPager = (ViewPager) findViewById(R.id.swipeTabs);
        fragmentPagerAdapter = new myPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_header);
        tabLayout.setupWithViewPager(viewPager);

        setupIconsforTabs(tabLayout);
    }

    private void setupIconsforTabs(TabLayout tabLayout) {

        // int tabCount = tabLayout.getTabCount()

        tabLayout.getTabAt(0).setIcon(R.drawable.food);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_fitness_center_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_camera_black_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class myPagerAdapter extends FragmentPagerAdapter {

        private myPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new RestaurantsScreen();

                case 1:
                    return new FitnessRecord();

                case 2:
                    return new RecipeActivity();

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
                    return "FOOD";

                case 1:
                    return "FITNESS";

                case 2:
                    return "CAMERA";

                default:
                    return "TAB";
            }
        }
    }
}
