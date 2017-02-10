package com.example.maheshbabugorantla.stepscounter;

import android.*;
import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshbabugorantla.stepscounter.CustomViews.CircularProgressBar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Variables for the SensorManager
    SensorManager mSensorManager;
    Sensor mStepsCounter = null;

    boolean mRunning = false;

    RunTimePermissions runTimePermissions; // USed to ask the User for the RunTime Permissions

     CircularProgressBar circularProgressBar;

     // Steps Count
     int oldSteps = 0;
     int newSteps = 0;

     static final String OLD_STEPS = "old steps";

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

        // Checking for the presence of the StepsCounter Sensor on the User's Device.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepsCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        circularProgressBar = (CircularProgressBar) findViewById(R.id.progressBar);
        circularProgressBar.setMax(10000);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    /**
     *  This function is called immediately after onCreate
     *  Author: Mahesh Babu Gorantla , Date: Feb 09, 2017
     */
    @Override
    protected void onStart() {

        super.onStart();

        mRunning = true;

        Toast.makeText(this, "Inside onStart", Toast.LENGTH_SHORT).show();

        oldSteps = newSteps;

        // Registering the StepCounter Listener to listen for the changes
        if(mStepsCounter != null) {
            // Here the sampling frequency is set to the rate that is suitable for the user interface which has a value of 2
            Toast.makeText(this, "Registering StepsCount Listener", Toast.LENGTH_SHORT);
            mSensorManager.registerListener(this, mStepsCounter, SensorManager.SENSOR_DELAY_UI);
        }
        else {
            Toast.makeText(this, "Step Counter not available!", Toast.LENGTH_LONG).show();

            // Else May be implement our own custom Algorithm to count the steps using Accelerometer and Gyroscopes
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        System.out.println("New Steps: " + newSteps);
        outState.putString(OLD_STEPS, String.valueOf(newSteps));
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * This function is called when the System shuts down the app to free up the resources
     * Author: Mahesh Babu Gorantla , Date: Feb 09, 2017
     * **/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRunning = false;
        mSensorManager.unregisterListener(this, mStepsCounter); // UnRegistering the Sensor when the Application is not in Focus
    }

    /**
     * This function is called whenever there is a new sensor Event
     * Author: Mahesh Babu Gorantla , Date: Feb 09, 2017
     * **/
    @Override
    public void onSensorChanged(SensorEvent event) {

        if(mRunning) {
            newSteps = Math.round(event.values[0]);
            ((TextView) findViewById(R.id.stepsCount)).setText(String.valueOf(newSteps - oldSteps));
            circularProgressBar.setProgress(Math.round(newSteps - oldSteps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * This is used to determine if a run-time permission has been granted by the user/not
     * Author: Mahesh Babu Gorantla , Date: Feb 10, 2017
     **/
     @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        runTimePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}