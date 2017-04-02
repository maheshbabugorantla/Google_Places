package com.example.maheshbabugorantla.googlemapclusters.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.maheshbabugorantla.googlemapclusters.CustomViews.CircularProgressBar;
import com.example.maheshbabugorantla.googlemapclusters.HelperClasses.RunTimePermissions;
import com.example.maheshbabugorantla.googlemapclusters.HelperClasses.Utility;
import com.example.maheshbabugorantla.googlemapclusters.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataSourcesResult;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * DESCRIPTION: FitnessFragment class
 * Created by MaheshBabuGorantla
 * First Update On Mar 27, 2017 .
 * Last Update On Mar 27, 2017.
 */

public class FitnessFragment extends Fragment implements
                    GoogleApiClient.ConnectionCallbacks,
                    GoogleApiClient.OnConnectionFailedListener,
                    OnDataPointListener {

    private static final String LOG_TAG = "FitnessFragment";

    /**
     * Provides the google api client to get the google fitness services
     */
    private GoogleApiClient mGoogleApiClient = null;

    /**
     * DataPoint Listener to get the data from the sensor
     */
    private OnDataPointListener mListener;

    /**
     * Constant used in the Google Account Sign-in
     */
    private static final int REQUEST_SIGN_IN = 0x2;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;

    private TextView stepsCount;

    private CircularProgressBar circularProgressBar;

    long totalSteps;

    StringBuilder stringBuilder = new StringBuilder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        RunTimePermissions runTimePermissions = new RunTimePermissions(getActivity().getApplicationContext(), getActivity());

        runTimePermissions.checkLocationAccess();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fitness_data, container, false);

        stepsCount = (TextView) rootView.findViewById(R.id.stepsCount);
        //stepsCount.setAnimation(AnimationUtils.loadAnimation());
        circularProgressBar = (CircularProgressBar) rootView.findViewById(R.id.progressBar);
        circularProgressBar.setMax(10000); // Setting the Default Maximum value

        buildFitnessClient();

        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();

        // Connecting to the Google API Client
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        // Fetching the user preferred Maximum Steps Goal
        circularProgressBar.setMax(new Utility().getMaxStepsCount(getActivity()));
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "Inside onStop");

        Fitness.SensorsApi.remove(mGoogleApiClient, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if(status.isSuccess()) {
                            mGoogleApiClient.disconnect();
                        }
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterFitnessDataListener();
    }

    /**
     * This is allow the user to connect to the Fitness APIs.
     */
    private synchronized void buildFitnessClient() {

        if (mGoogleApiClient == null) // && (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
        {
            Log.d(LOG_TAG, "Inside buildFitnessClient");

            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(Fitness.SENSORS_API)
                    .addApi(Fitness.HISTORY_API)
                    .addApi(Fitness.RECORDING_API)
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    public void subscribe() {

        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        Fitness.RecordingApi.subscribe(mGoogleApiClient, DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            if (status.getStatusCode() == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                Log.i(LOG_TAG, "Existing Subscription for activity detected.");
                            } else {
                                Log.i(LOG_TAG, "Successfully subscribed!");
                            }
                        } else {
                            Log.w(LOG_TAG, "There was a problem subscribing.");
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case REQUEST_SIGN_IN:

                switch (resultCode) {

                    case Activity.RESULT_OK: {
                        Log.i(LOG_TAG, "User Agreed to provide necessary settings change");
                        mGoogleApiClient.connect();
                        if (mGoogleApiClient.isConnected()) {
                            Log.i(LOG_TAG, "Finding Fitness Data");
                            findFitnessDataSources();
                        }

                        break;
                    }

                    case Activity.RESULT_CANCELED: {
                        Log.i(LOG_TAG, "User chose not to Sign-In into the App");
                        break;
                    }
                }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void findFitnessDataSources() {
        // [START find_data_sources]
        // Note: Fitness.SensorsApi.findDataSources() requires the ACCESS_FINE_LOCATION permission.
        Fitness.SensorsApi.findDataSources(mGoogleApiClient, new DataSourcesRequest.Builder()
                // At least one datatype must be specified.
                .setDataTypes(DataType.TYPE_LOCATION_SAMPLE)
                // Can specify whether data type is raw or derived.
                .setDataSourceTypes(DataSource.TYPE_RAW)
                .build())
                .setResultCallback(new ResultCallback<DataSourcesResult>() {
                    @Override
                    public void onResult(DataSourcesResult dataSourcesResult) {
                        Log.i(LOG_TAG, "Result: " + dataSourcesResult.getStatus().toString());
                        for (DataSource dataSource : dataSourcesResult.getDataSources()) {

                            stringBuilder.append("Source: " + dataSource.toString() + " , Type: " + dataSource.getDataType().getName());

                            Log.i(LOG_TAG, "Data source found: " + dataSource.toString());
                            Log.i(LOG_TAG, "Data Source type: " + dataSource.getDataType().getName());

                            //Let's register a listener to receive Activity data!
                            if (dataSource.getDataType().equals(DataType.TYPE_LOCATION_SAMPLE)) {
                                Log.i(LOG_TAG, "Data source for LOCATION_SAMPLE found!  Registering.");
                                registerFitnessDataListener(dataSource,
                                        DataType.TYPE_LOCATION_SAMPLE);
                            }
                        }
                    }
                });
        // [END find_data_sources]
    }

    /**
     * Register a listener with the Sensors API for the provided {@link DataSource} and
     * {@link DataType} combo.
     */
    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {
        // [START register_data_listener]
        SensorRequest sensorRequest = new SensorRequest.Builder()
                .setDataSource(dataSource)
                .setDataType(dataType)
                .setSamplingRate(2, TimeUnit.SECONDS)
                .build();

        Fitness.SensorsApi.add(mGoogleApiClient, sensorRequest, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.e(LOG_TAG, "SensorApi Added Successfully");
                        }
                    }
                });
        // [END register_data_listener]
    }

    /**
     * Unregister the listener with the Sensors API.
     */
    private void unregisterFitnessDataListener() {
        if (mListener == null) {
            // This code only activates one listener at a time.  If there's no listener, there's
            // nothing to unregister.
            return;
        }

        // [START unregister_data_listener]
        // Waiting isn't actually necessary as the unregister call will complete regardless,
        // even if called from within onStop, but a callback can still be added in order to
        // inspect the results.
        Fitness.SensorsApi.remove(
                mGoogleApiClient,
                mListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(LOG_TAG, "Listener was removed!");
                        } else {
                            Log.i(LOG_TAG, "Listener was not removed.");
                        }
                    }
                });
        // [END unregister_data_listener]
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_TAG, "Client Connected");

        subscribe();

        final DataSourcesRequest dataSourcesRequest = new DataSourcesRequest.Builder()
                .setDataTypes(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setDataSourceTypes(DataSource.TYPE_RAW)
                .build();

        ResultCallback<DataSourcesResult> dataSourcesRequestResultCallback = new ResultCallback<DataSourcesResult>() {

            @Override
            public void onResult(@NonNull DataSourcesResult dataSourcesResult) {
                for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                    if (dataSource.getDataType().equals(DataType.TYPE_STEP_COUNT_CUMULATIVE)) {
                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_CUMULATIVE);
                    }
                }
            }
        };

        // Start searching for the valid step data source
        Fitness.SensorsApi.findDataSources(mGoogleApiClient, dataSourcesRequest).setResultCallback(dataSourcesRequestResultCallback);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // If your connection to the sensor gets lost at some point,
        // you'll be able to determine the reason and react to it here.
        if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
            Log.i(LOG_TAG, "Connection lost.  Cause: Network Lost.");
        } else if (i
                == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
            Log.i(LOG_TAG,
                    "Connection lost.  Reason: Service Disconnected");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (!authInProgress) {
            try {
                Log.i(LOG_TAG, "Connection Failed");
                connectionResult.startResolutionForResult(getActivity(), REQUEST_SIGN_IN);
                //Log.i(LOG_TAG, "Google Play services connection failed. Cause: " +
                //      connectionResult.toString());
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "Pending Intent unable to execute the request");
            }
        } else {
            Log.e(LOG_TAG, "Authorization in Progress and waiting for the user's permission");
        }
    }

    /**
     * Whenever a stepCount change is detected this method gets invoked. In this method, you loop
     * through the fields in the DataPoint parameter and display a 'Toast' message with the field name
     * and value.
     */
    @Override
    public void onDataPoint(DataPoint dataPoint) {
        for (final Field field : dataPoint.getDataType().getFields()) {
            final Value value = dataPoint.getValue(field);
            new VerifyDataTask().execute(new String[] {field.getName(), value.toString()});
        }
    }

    private class VerifyDataTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            totalSteps =  0;

            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA);
            // This waits for 20 seconds for the result to be delivered from an API method in the Google Play Services
            DailyTotalResult totalResult = result.await(20, TimeUnit.SECONDS);

            if(totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                totalSteps =  totalSet.isEmpty() ? 0 : totalSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
            }
            else {
                Log.w(LOG_TAG, "There was a problem getting the step count");
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stepsCount.setText(String.format(Locale.US, Long.toString(totalSteps)));
                    circularProgressBar.setProgress((int) totalSteps);
                }
            });
            return null;
        }
    }
}