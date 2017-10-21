package com.example.maheshbabugorantla.google_places.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.Manifest;

import com.example.maheshbabugorantla.google_places.R;
import com.example.maheshbabugorantla.google_places.RunTimePermissions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * DESCRIPTION: RestaurantDetailsActivity class
 * Created by MaheshBabuGorantla
 * First Update On Sep 02, 2017 .
 * Last Update On Sep 02, 2017.
 */
public class RestaurantDetailsActivity extends AppCompatActivity {

    private final String LOG_TAG = RestaurantDetailsActivity.class.getSimpleName();
    private String[] photoRefIDs = null;
    private String placeId;

    /**
     *  The Object to access the appropriate permissions from the User
     */

    private RunTimePermissions runTimePermissions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_details);

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            placeId = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.d(LOG_TAG, "Restaurant ID: " + placeId);
        }

        runTimePermissions = new RunTimePermissions(getApplicationContext(), this);
        runTimePermissions.checkPhoneCallAccess(); // Checks for the Permission to access the Phone Call

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(mCustomPagerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start populating the View now
        populateDetailsScreen(placeId);
    }

    private void populateDetailsScreen(String placeId) {
        getRestaurantDetails restaurantDetails = new getRestaurantDetails();

        try
        {
            final String[] placeDetails = restaurantDetails.execute(placeId).get();

            // For some business Locations there are no photos Available
            if(!placeDetails[7].equals("NULL")) {
                photoRefIDs = placeDetails[7].split(";");
            }
            else {
                photoRefIDs = new String[]{}; // No Photos
            }
            // Populating the Restaurant Name
            // Log.d(LOG_TAG, "Name: " + placeDetails[1]);
            TextView restaurantName = (TextView) findViewById(R.id.restaurantName);

            if(restaurantName == null) {
                Log.d(LOG_TAG, "Restaurant Name TextView is NULL");
            }

            restaurantName.setText(placeDetails[1]);

            // Populating the Restaurant Address
            TextView restaurantAddress = (TextView) findViewById(R.id.restaurant_address);
            restaurantAddress.setText(placeDetails[2]);

            // Setting the Click Events for the Map and Phone Icons

            // PhoneIcon
            ImageView phoneIcon = (ImageView) findViewById(R.id.phone_call);
            phoneIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    runTimePermissions.checkPhoneCallAccess(); // Once Again check for Phone Call Permission Access

                    // Only make a phone call when the user has permitted to do so
                    if(ActivityCompat.checkSelfPermission(RestaurantDetailsActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent phoneCallIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + placeDetails[3]));
                        startActivity(phoneCallIntent);
                    }
                }
            });

            // Directions Icon
            ImageView directionsIcon = (ImageView) findViewById(R.id.getDirections);
            directionsIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(placeDetails[4]));
                    startActivity(mapIntent);
                }
            });
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private class getRestaurantDetails extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            return parseJSONData(params[0]);
        }

        /**
         * @param place_id: This is used to fetch the data for particular place
         * @return Return the JSONData
         */
        // TODO: Need to change the parameter data types for latitude and longitude
        private String[] parseJSONData(String place_id) {

            String JSONData = getJSONData(place_id);

            String [] placeDetails = new String[8];

            try {
                // Parsing the JSON Data in the form of a String
                JSONObject jsonReader = new JSONObject(JSONData);
                JSONObject resultsObject = jsonReader.getJSONObject("result");

                StringBuilder stringBuilder = new StringBuilder();

                // Get the GPS CoOrdinates of the Business Location
                JSONObject latLng = resultsObject.getJSONObject("geometry").getJSONObject("location");

                String lat = Double.toString(latLng.getDouble("lat"));
                String lng = Double.toString(latLng.getDouble("lng"));

                Log.d(LOG_TAG, "GPS: " + lat + ", " + lng);

                placeDetails[0] = stringBuilder.append(lat).append(" ").append(lng).toString();

                stringBuilder.setLength(0); // Clearing the StringBuilder

                placeDetails[1] = resultsObject.getString("name"); // Name of the Business Location

                placeDetails[2] = resultsObject.getString("formatted_address"); // Address of the Business Location

                placeDetails[3] = resultsObject.getString("international_phone_number"); // Phone Number of the Business

                placeDetails[4] = resultsObject.getString("url"); // URL to fetch the Location of the Business Location on the Map

                if(! resultsObject.isNull("website")) {
                    placeDetails[5] = resultsObject.getString("website"); // Website of the Business Location
                }
                else {
                    placeDetails[5] = "NULL"; // No Website
                }

                placeDetails[6] = resultsObject.getString("rating");

                // Get the References for the Photos of the Business Locations
                if(!resultsObject.isNull("photos")) {
                    JSONArray photoReferences = resultsObject.getJSONArray("photos");

                    StringBuilder photoLinks = new StringBuilder();

                    for (int index = 0; index < photoReferences.length(); index++) {
                        JSONObject photoReference = photoReferences.getJSONObject(index);

                        photoLinks.append(photoReference.getString("photo_reference")).append(";");
                    }

                    placeDetails[7] = photoLinks.toString();
                }
                else {
                    placeDetails[7] = "NULL";
                }
                /*// Place Id Useful for PlaceDetails
                String Place_ID = jsonObject.getString("place_id");
                stringBuilder.append(Place_ID).append(";");

                // Pricing Level
                String pricing_level = Integer.toString(jsonObject.getInt("price_level"));
                stringBuilder.append(pricing_level).append(";");

                // Rating
                String rating = Double.toString(jsonObject.getDouble("rating"));
                stringBuilder.append(rating).append(";"); */

                // TODO: Might use this in future
                // Show if Delivery Available
                // JSONArray types = jsonObject.getJSONArray("types");

                // Vicinity
                // String vicinity = jsonObject.getString("vicinity");
                // restaurants[index] = stringBuilder.toString();
            }

            catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }

            return placeDetails;
        }

        private String getJSONData(String placeID) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            final String GOOGLE_PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json";
            final String API_KEY = "AIzaSyDFGrHTpQvTgOuGu065RYYpgemLmokgetA";
            final String ID = "key";
            final String PLACE_ID = "placeid";

            // Building the URI to fetch the data from
            Uri BuiltUri = Uri.parse(GOOGLE_PLACES_BASE_URL).buildUpon()
                    .appendQueryParameter(ID, API_KEY)
                    .appendQueryParameter(PLACE_ID, placeID).build();

            Log.d("PLACE URL:", BuiltUri.toString());

            String rawJSONData = null;

            try {
                URL Places_URL = new URL(BuiltUri.toString());

                // Creates a request to the Google Places API and opens the Connection
                httpURLConnection = (HttpURLConnection) Places_URL.openConnection();
                httpURLConnection.setRequestMethod("GET"); // Letting the Connection know that the data will be fetched
                httpURLConnection.connect();

                // Read the Input Stream into the String
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuilder stringBuilder = new StringBuilder();

                // This happens where there is no data being read from the Opened Connection to Places_URL
                if(inputStream == null) {
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                // Now reading the raw JSON Data into BufferReader
                while((line=bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                // This can happen when the read from URL is unsuccessful or may be the connection might drop in between
                if(stringBuilder.length() == 0) {
                    return null;
                }
                rawJSONData = stringBuilder.toString();
            } catch (MalformedURLException e) {
                Log.d("getJSONData", "MalformedURLException occurred");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("getJSONData", "IOException occurred");
                e.printStackTrace();
            }

            // Closing all the open Network Connections and the BufferedReaders
            finally {

                // Checking to see if the connection to Web URL is still Open. If Yes, then close it
                if(httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                if(bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("GetJSONData", "Google_Places.retrieveJSONData IOException occurred");
                    }
                }
            }

            Log.d(LOG_TAG, rawJSONData);

            return rawJSONData;
        }
    }

    private class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        private CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Log.d(LOG_TAG, "Inside instantiateItem");

            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            String BASE_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo";
            final String API_KEY = "AIzaSyDFGrHTpQvTgOuGu065RYYpgemLmokgetA";
            final String ID = "key";
            final String PHOTO_REF_KEY = "photoreference";
            final String MAX_WIDTH = "maxwidth";
            final String SENSOR = "sensor";
            final String Sensor_Value = "false";

            if(photoRefIDs.length > 0) {
                ImageView imageView = (ImageView) itemView.findViewById(R.id.newImage);
                Uri photoURL = Uri.parse(BASE_PHOTO_URL).buildUpon()
                        .appendQueryParameter(MAX_WIDTH, "400")
                        .appendQueryParameter(PHOTO_REF_KEY, photoRefIDs[position])
                        .appendQueryParameter(ID, API_KEY)
                        .appendQueryParameter(SENSOR, Sensor_Value)
                        .build();

                Log.d(LOG_TAG, "Photo URL: " + photoURL.toString());
                Picasso.with(getApplicationContext()).load(photoURL.toString()).fit().centerCrop().into(imageView);
            }

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
