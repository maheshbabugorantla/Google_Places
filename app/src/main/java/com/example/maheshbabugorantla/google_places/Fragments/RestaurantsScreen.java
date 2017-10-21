package com.example.maheshbabugorantla.google_places.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.maheshbabugorantla.google_places.Activities.RestaurantDetailsActivity;
import com.example.maheshbabugorantla.google_places.Adapters.RestaurantAdapter;
import com.example.maheshbabugorantla.google_places.CustomViews.RestaurantItem;
import com.example.maheshbabugorantla.google_places.R;
import com.example.maheshbabugorantla.google_places.RunTimePermissions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class RestaurantsScreen extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /**
     *  Constant used in the location settings dialog
     * */
    private static final int REQUEST_CHECK_SETTINGS = 0x02;

    /**
     *  The desired interval for location updates. Inexact. Updates may be more or less frequent
     * */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     *  The fastest rate for active location updates. Exact. Updates will never be more frequent
     *  than this value.
     * */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     *  Stores parameters for requests to the FusedLocationProviderApi
     * */
    private LocationRequest mLocationRequest;

    /**
     * GoogleApiClient to request LocationServices API
     * */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Create a Location Services Client
     * */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     * */
    private Location mCurrentLocation;

    private ListView placesList;

    private RunTimePermissions runTimePermissions;

    private ProgressBar progressBar;

    View rootView; // This is used to set the parent layout for the Fragment

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        runTimePermissions = new RunTimePermissions(getActivity().getApplicationContext(), getActivity());

        // Asking for the necessary permissions
        runTimePermissions.checkInternetAccess();
        runTimePermissions.checkLocationAccess();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        buildGoogleApiClient();

        while (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("RestaurantsFragment", "Requesting the Location Services Access");
            runTimePermissions.checkLocationAccess();
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    mCurrentLocation = location;
                }
            }
        });

        mFusedLocationClient.getLastLocation().addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.restaurant_fragment, container, false);

        placesList = (ListView) rootView.findViewById(R.id.placesList);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        return rootView;
    }

    private void populateListView() {
        RestaurantItem[] restaurantItems = null;

        retrieveJSONData retrieveJSONData = new retrieveJSONData();

        String[] placeIDs;

        try {
            if (mCurrentLocation != null) {
                String[] restaurants = retrieveJSONData.execute("3000", Double.toString(mCurrentLocation.getLatitude()), Double.toString(mCurrentLocation.getLongitude())).get();

                restaurantItems = new RestaurantItem[restaurants.length];
                placeIDs = new String[restaurants.length];

                for (int index = 0; index < restaurants.length; index++) {
                    // System.out.println(Integer.toString(index) + ") " + restaurants[index]);
                    String[] restaurant = restaurants[index].split(";");
                    //                String openNow = "CLOSED";
                    //                if (restaurant[2].equals("true")) {
                    //                    openNow = "OPEN";
                    //                }
                    //restaurantItems.add(new RestaurantItem(Double.parseDouble(restaurant[5]), restaurant[1], Boolean.parseBoolean(openNow)));
                    restaurantItems[index] = new RestaurantItem(Double.parseDouble(restaurant[5]), restaurant[1], Boolean.parseBoolean(restaurant[2]), restaurant[3]);
                    placeIDs[index] = restaurant[3];
                }
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (restaurantItems != null) {
            Arrays.sort(restaurantItems, new Comparator<RestaurantItem>() {
                @Override
                public int compare(RestaurantItem o1, RestaurantItem o2) {
                    return o1.compareTo(o2);
                }
            });

            final ArrayList<RestaurantItem> Restaurants = new ArrayList<>(Arrays.asList(restaurantItems));

            RestaurantAdapter restaurantAdapter = new RestaurantAdapter(getActivity(), Restaurants);
            if(placesList.getAdapter() == null) {
                placesList.setAdapter(restaurantAdapter);
            }
            placesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent ToRestaurantDetails = new Intent(view.getContext(), RestaurantDetailsActivity.class);
                    ToRestaurantDetails.putExtra(Intent.EXTRA_TEXT, Restaurants.get(position).getPlaceId());
                    startActivity(ToRestaurantDetails);
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkGPSSettings();

        // Step-2: Connect the GoogleApiClient to the Relevant APIs (In here we will connect to LocationServices API)
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * This is used to swap layouts of the Restaurant Fragment when the data is loading
     * */
    private void swapLayoutWith(int id) {

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = layoutInflater.inflate(id, null);
//        ViewGroup viewGroup = (ViewGroup) getView();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(10);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void checkGPSSettings() {

        createLocationRequest();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All Location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(getActivity(),
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        runTimePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        while (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("RestaurantsFragment", "Requesting the Location Services Access");
            runTimePermissions.checkLocationAccess();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        populateListView();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("RestaurantsFragment", "Google Location Services Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("RestaurantsFragment", "Google Location Services Failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("RestaurantsFragment", "Location Changed: " + location.toString());
        mCurrentLocation = location;
    }


    /**
     * AsyncTask Class used to retrieve the Restaurants data using Google Places API
     */
    private class retrieveJSONData extends AsyncTask<String, Integer, String[]>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setMax(100);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected String[] doInBackground(String... params) {
            return parseJSONData(
                    Integer.parseInt(params[0]),
                    Double.parseDouble(params[1]),
                    Double.parseDouble(params[2])
            );
        }

        /**
         * @param distance: Proximity to various food places from User's Current Location in metres
         * @param latitude: Latitude from the User's Current Position
         * @param longitude Longitude from the User's Current Position
         * @return Return the JSONData
         */
        // TODO: Need to change the parameter data types for latitude and longitude
        private String[] parseJSONData(int distance, double latitude, double longitude) {
            String jsonData = getJSONData(distance, latitude, longitude);
            publishProgress(70);
            String [] restaurants = null;

            try {
                // Parsing the JSON Data in the form of a String
                JSONObject jsonReader = new JSONObject(jsonData);
                JSONArray resultsArray = jsonReader.getJSONArray("results");

                int jsonArrayLength = resultsArray.length();

                // TODO: Sort the results based on the User Rating on the Business
                restaurants = new String[jsonArrayLength];

                for(int index = 0; index < jsonArrayLength; index++) {
                    JSONObject jsonObject = resultsArray.getJSONObject(index);

                    StringBuilder stringBuilder = new StringBuilder();

                    // Get the GPS CoOrdinates of the Business Location
                    JSONObject latLng = jsonObject.getJSONObject("geometry").getJSONObject("location");

                    String lat = Double.toString(latLng.getDouble("lat"));
                    String lng = Double.toString(latLng.getDouble("lng"));
                    stringBuilder.append(lat).append(" ").append(lng).append(";");

                    String name = jsonObject.getString("name");
                    stringBuilder.append(name).append(";");

                    JSONObject opening_hours = jsonObject.getJSONObject("opening_hours");
                    String openNow = Boolean.toString(opening_hours.getBoolean("open_now"));
                    stringBuilder.append(openNow).append(";");

                    // Place Id Useful for PlaceDetails
                    String Place_ID = jsonObject.getString("place_id");
                    stringBuilder.append(Place_ID).append(";");

                    // Pricing Level
                    String pricing_level = Integer.toString(jsonObject.getInt("price_level"));
                    stringBuilder.append(pricing_level).append(";");

                    // Rating
                    String rating = Double.toString(jsonObject.getDouble("rating"));
                    stringBuilder.append(rating).append(";");

                    // TODO: Might use this in future
                    // Show if Delivery Available
                    // JSONArray types = jsonObject.getJSONArray("types");

                    // Vicinity
                    // String vicinity = jsonObject.getString("vicinity");
                    restaurants[index] = stringBuilder.toString();
                }

                publishProgress(90);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }

            return restaurants;
        }

        private String getJSONData(int distance, double latitude, double longitude) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            final String GOOGLE_PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
            final String API_KEY = "AIzaSyDFGrHTpQvTgOuGu065RYYpgemLmokgetA";
            final String ID = "key";
            final String RADIUS = "radius";
            final String CO_ORDINATES = "location";
            final String TYPE = "type";
            final String NEXTPAGE = "hasNextPage"; //true
            final String GETNXTPAGE = "nextPage()"; //true
            // Building the URI to fetch the data from
            Uri BuiltUri = Uri.parse(GOOGLE_PLACES_BASE_URL).buildUpon()
                    .appendQueryParameter(ID, API_KEY)
                    .appendQueryParameter(RADIUS, Integer.toString(distance))
                    .appendQueryParameter(CO_ORDINATES, Double.toString(latitude) + ", " + Double.toString(longitude))
                    .appendQueryParameter(TYPE, "restaurant").build();
            //.appendQueryParameter(NEXTPAGE, "true")
            //.appendQueryParameter(GETNXTPAGE, "true").build();

            Log.d("PLACES URL:", BuiltUri.toString());

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
                publishProgress(30);
            } catch (MalformedURLException e) {
                Log.d("getJSONData", "MalformedURLException occurred");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("getJSONData", "IOException occurred");
                e.printStackTrace();
            }

            // Closing all the open Network Connections and the BufferedReaders
            finally {

                publishProgress(40);

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

            return rawJSONData;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE); // Hide the Progress Bar
        }
    }

}
