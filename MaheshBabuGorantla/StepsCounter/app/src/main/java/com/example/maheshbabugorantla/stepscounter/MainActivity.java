package com.example.maheshbabugorantla.stepscounter;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Variables for the SensorManager
    SensorManager mSensorManager;
    Sensor mSensor = null;

    boolean mRunning = false;

    RunTimePermissions runTimePermissions; // USed to ask the User for the RunTime Permissions

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
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    @Override
    protected void onStart() {

        super.onStart();

        mRunning = true;

        Toast.makeText(this, "Inside onStart", Toast.LENGTH_SHORT).show();

        // Registering the StepCounter Listener to listen for the changes
        if(mSensor != null) {
            // Here the sampling frequency is set to the rate that is suitable for the user interface which has a value of 2
            Toast.makeText(this, "Registering StepsCount Listener", Toast.LENGTH_SHORT);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else {
            Toast.makeText(this, "Step Counter not available!", Toast.LENGTH_LONG).show();

            // Else May be implement our own custom Algorithm to count the steps using Accelerometer and Gyroscopes
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "Inside onStop()", Toast.LENGTH_SHORT).show();
        mRunning = false;
        mSensorManager.unregisterListener(this, mSensor); // UnRegistering the Sensor when the Application is not in Focus
    }

    // W
    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "Inside onRestart", Toast.LENGTH_SHORT).show();
    }

    // This function is called whenever there is a new sensor Event
    @Override
    public void onSensorChanged(SensorEvent event) {

        if(mRunning) {
            ((TextView) findViewById(R.id.stepsCount)).setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        runTimePermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}