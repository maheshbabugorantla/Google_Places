package com.example.maheshbabugorantla.google_places.Fragments;

import android.content.IntentSender;
import android.graphics.Camera;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshbabugorantla.google_places.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.result.DataReadResult;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CameraScreen extends Fragment implements OnDataPointListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    /**
     * Constant used in the Google Account Sign-in
     */
    private static final int REQUEST_SIGN_IN = 0x2;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;

    private final String LOG_TAG = CameraScreen.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient = null;
    SimpleDateFormat simpleDateFormat;
    String selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.camera_fragment, container, false);

        TextView CameraText = (TextView) rootView.findViewById(R.id.camera);
        CameraText.setText("CAMERA FRAGMENT");

        Calendar calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = simpleDateFormat.format(calendar.getTime());

        buildFitnessClient();

        readHeight();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }

    @Override
    public void onStart() {

        super.onStart();

        // Connecting to the Google API Client
        if (mGoogleApiClient != null) {
            Log.d(LOG_TAG, "Inside onStart");
            mGoogleApiClient.connect();
        }
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
                            if(mGoogleApiClient != null) {
                                mGoogleApiClient.disconnect();
                            }
                        }
                    }
                });
    }

    /**
     * This will allow the user to connect to the Google FIT APIs
     * */
    private synchronized void buildFitnessClient() {

        if(mGoogleApiClient == null) {
            Log.d(LOG_TAG, "Inside the buildFitnessClient");

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Fitness.HISTORY_API)
                    .addApi(Fitness.CONFIG_API)
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                    .useDefaultAccount()
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Get Calories for a Date
        fetchUserGoogleFitData(selectedDate); // Current Date
    }

    @Override
    public void onConnectionSuspended(int i) {

        if(i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
            Log.i(LOG_TAG, "Connection lost. Cause: Network Lost.");
        }
        else if(i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
            Log.i(LOG_TAG, "Connection lost. Cause: Service Disconnected");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "Connection failed. Cause: " + connectionResult.toString());

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

    private DataReadRequest queryDateFitnessData(int year, int month, int day) {

        Calendar startCalendar = Calendar.getInstance(Locale.getDefault());
        startCalendar.set(Calendar.YEAR, year);
        startCalendar.set(Calendar.MONTH, month);
        startCalendar.set(Calendar.DAY_OF_MONTH, day);
        startCalendar.set(Calendar.HOUR_OF_DAY, 23);
        startCalendar.set(Calendar.MINUTE, 59);
        startCalendar.set(Calendar.SECOND, 59);
        startCalendar.set(Calendar.MILLISECOND, 999);
        long endTime = startCalendar.getTimeInMillis();

        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        long startTime = startCalendar.getTimeInMillis();

        return new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .bucketByActivitySegment(1, TimeUnit.MILLISECONDS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
    }



    public void fetchUserGoogleFitData(String data) {

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Date date = null;

            try {
                date = simpleDateFormat.parse(data);
            } catch (Exception e) {

            }
            Calendar calendar = Calendar.getInstance();

            try {
                calendar.setTime(date);
            } catch (Exception e) {
                calendar.setTime(new Date());
            }

            DataReadRequest readRequest = queryDateFitnessData(calendar.get(Calendar.YEAR),
                                                                calendar.get(Calendar.MONTH),
                                                                calendar.get(Calendar.DAY_OF_MONTH));

            new GetCaloriesAsyncTask(readRequest, mGoogleApiClient).execute();
        }
    }

    @Override
    public void onDataPoint(DataPoint dataPoint) {
        Log.d(LOG_TAG, "new DataPoint Detected");
    }

    private class GetCaloriesAsyncTask extends AsyncTask<Void, Void, DataReadResult> {

        DataReadRequest readRequest;
        private final String LOG_TAG = GetCaloriesAsyncTask.class.getSimpleName();
        GoogleApiClient mGoogleApiClient = null;
        private float expendedCalories = 0;

        public GetCaloriesAsyncTask(DataReadRequest dataReadRequest, GoogleApiClient mGoogleApiClient) {
            this.readRequest = dataReadRequest;
            this.mGoogleApiClient = mGoogleApiClient;
        }

        @Override
        protected DataReadResult doInBackground(Void... params) {
            return Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);
        }

        @Override
        protected void onPostExecute(DataReadResult dataReadResult) {
            super.onPostExecute(dataReadResult);
            printData(dataReadResult);
        }

        private void printData(DataReadResult dataReadResult) {

            if(dataReadResult.getBuckets().size() > 0) {
                Log.e(LOG_TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());

                for (Bucket bucket: dataReadResult.getBuckets()) {
                    String bucketActivity = bucket.getActivity();
                    Log.e(LOG_TAG, "Activity: " + bucketActivity);
                    if(bucketActivity.contains(FitnessActivities.WALKING) || bucketActivity.contains(FitnessActivities.RUNNING)) { // || bucketActivity.contains(FitnessActivities.UNKNOWN)) {
                        Log.e(LOG_TAG, "bucket Type -> " + bucketActivity);
                        List<DataSet> dataSets = bucket.getDataSets();
                        for(DataSet dataSet: dataSets) {
                            dumpDataSet(dataSet);
                        }
                    }
                }

                Log.e(LOG_TAG, "BurnedCalories => " + String.valueOf(expendedCalories));
                Toast.makeText(getActivity(), "Calories: " + expendedCalories, Toast.LENGTH_LONG).show();

            } else if (dataReadResult.getDataSets().size() > 0) {
                Log.e(LOG_TAG, "Number of returned DataSets is: " + dataReadResult.getDataSets().size());

                for(DataSet dataSet: dataReadResult.getDataSets()) {
                    dumpDataSet(dataSet);
                }
            }
        }

        private void dumpDataSet(DataSet dataSet) {

            Log.e(LOG_TAG, "Data returned for Data type: " + dataSet.getDataType().getName());

            for (DataPoint dp : dataSet.getDataPoints()) {
                if (dp.getEndTime(TimeUnit.MILLISECONDS) > dp.getStartTime(TimeUnit.MILLISECONDS)) {
                    for (Field field : dp.getDataType().getFields()) {
                        expendedCalories = expendedCalories + dp.getValue(field).asFloat();
                    }
                }
            }
        }
    }

    private void readHeight() {

        long startTime = 1;

        DataReadRequest readHeightRequest = new DataReadRequest.Builder()
                .read(DataType.TYPE_HEIGHT)
                .setTimeRange(startTime, 4, TimeUnit.MILLISECONDS)
                .setLimit(1)
                .build();

//        System.out.println(readHeightRequest.getDataTypes().toString());
    }

}
