package com.example.flightdataacquisition;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class AcquisitionActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, SensorEventListener{

    // Define interface objects
    private ProgressBar Pbar;
    private TextView AcqText;
    private TextView lblLocation;
//    private TextView sensorTextview;

    // Objects used to get aircraft angles
    float[] accelerometerVector = new float[3];
    float[] magneticVector = new float[3];
    float[] resultMatrix=  new float[9];
    float[] values = new float[3];

    // Data variables
    double latitude, longitude;
    float yaw, roll, pitch;

    private static final String TAG = AcquisitionActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    // Location objects creation
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestLocationUpdates = false;
    private LocationRequest mLocationRequest;

    //Sensor objects creation
    private SensorManager mSensorManager;
    private Sensor accelerometer, magnetometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquisition);

        // Set acceleration and magnetic sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Retrieves interface objects from XML activity file
        lblLocation = (TextView) findViewById(R.id.location_textview);
//        sensorTextview = (TextView) findViewById(R.id.sensor_textview);
        Pbar = (ProgressBar) findViewById(R.id.progress);
        AcqText = (TextView) findViewById(R.id.acqText);

        // Sets invisible objects before acquisition start
        Pbar.setVisibility(View.INVISIBLE);
        AcqText.setVisibility(View.INVISIBLE);

        // Checks Play Services availability and create GoogleApiClient instance
        if(checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

        // Sets acquisition start button
        Button button_start = (Button) findViewById(R.id.startButton);
        button_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Pbar.setVisibility(View.VISIBLE);
                AcqText.setVisibility(View.VISIBLE);
                displayLocation();
                mRequestLocationUpdates = true;
                startLocationUpdates();     //                  Start
                startSensorAcquisition();   //      Location and Sensor acquisition
            }
        });

        // Sets acquisition stop button
        Button button_stop = (Button) findViewById(R.id.stopButton);
        button_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Pbar.setVisibility(View.INVISIBLE);
                AcqText.setVisibility(View.INVISIBLE);
                mRequestLocationUpdates = false;
                stopLocationUpdates();  //                      Stop
                stopSensorAcquisition();    //               Acquisition
            }
        });

        Button button_marker = (Button) findViewById(R.id.markerButton);
        button_marker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                writeJSON();
            }
        });     // Writes data in JSON file
    }

    public void startSensorAcquisition() {
        // Starts listening of sensors
        mSensorManager.registerListener(this, accelerometer, 1000000);
        mSensorManager.registerListener(this, magnetometer, 1000000);
    }

    public void stopSensorAcquisition() {
        // Stops listening of sensors
        mSensorManager.unregisterListener(this, accelerometer);
        mSensorManager.unregisterListener(this, magnetometer);
    }

    public void onSensorChanged(SensorEvent event){
        // Updates sensor values continuously
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerVector = event.values;
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticVector = event.values;
        }

        // Gets orientation values from rotation matrix with acceleration and geomagnetic field
        SensorManager.getRotationMatrix(resultMatrix, null, accelerometerVector, magneticVector);
        SensorManager.getOrientation(resultMatrix, values);

        // Aircraft angles
        yaw = (float) Math.toDegrees(values[0]);
        pitch = (float) Math.toDegrees(values[1]);
        roll = (float) Math.toDegrees(values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
        if(mGoogleApiClient.isConnected() && mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void displayLocation() {
        // Displays location on dedicated textview (may be deleted)
        try {
            // Gets last GPS location
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if(mLastLocation != null) {
            // Gets latitude and longitude
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            lblLocation.setText(latitude + ", " + longitude);
        } else {
            lblLocation.setText(R.string.loc_error);
        }
    }

    public void writeJSON() {
        // Writes acquired data in JSON file
        // Data file name format : MMDDYYYY_HHMMSS_data.txt
        try {
            Calendar rightNow = Calendar.getInstance();
            String curYear = String.valueOf(rightNow.get(Calendar.YEAR));
            String curDay = String.valueOf(rightNow.get(Calendar.DAY_OF_MONTH));
            String curMonth = String.valueOf(rightNow.get(Calendar.MONTH) + 1);
            String time = String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY)) + String.valueOf(rightNow.get(Calendar.MINUTE))
                    + String.valueOf(rightNow.get(Calendar.SECOND));
            System.out.println(time);

            File saveDir = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "FlightDataAcquisition");
            File dataFile = new File((Environment.getExternalStorageDirectory() +
                    File.separator + "FlightDataAcquisition"
                    + File.separator + curMonth + curDay + curYear
                    + "_" + time + "_" + "data.txt"));
            Boolean success=true;

            if (!saveDir.exists()) {
                success = saveDir.mkdir();  // Creates data directory if it doesn't exists
            }
            if (success){
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("latitude", latitude);
                jsonObj.put("longitude", longitude);
                jsonObj.put("yaw", yaw);
                jsonObj.put("roll", roll);
                jsonObj.put("pitch", pitch);

                // Convertx JSONObject to string in data file
                String acquiredData = jsonObj.toString();
                FileOutputStream output = new FileOutputStream(dataFile, true);
                output.write(acquiredData.getBytes());

                System.out.println("Successfully saved acquired data...");
                System.out.println("\nJSON Object: " + jsonObj);
            }
            else {Log.e("FlightDataAcquisition", "ERROR IN DIRECTORY CREATION");}
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        // Creates GoogleApiClient instance
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        // Creates a location request with updating intervals
        mLocationRequest = new LocationRequest();
        int UPDATE_INTERVAL = 1000; // in milliseconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        int FATEST_INTERVAL = 500; // in milliseconds
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        int DISPLACEMENT = 10; // in meters
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
        // Checks Google Play Services availability
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(resultCode)) {
                googleAPI.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported",
                        Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        // Starts location updates when clicking on acquisition start button
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    protected void stopLocationUpdates() {
        // Stops location updates when clicking on acquisition stop button
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Displays location on connection
        displayLocation();

        if(mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Delivers location update message and updates location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();

        displayLocation();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Returns an error message if connection fails
        Log.i(TAG, "Connection failed: " + connectionResult.getErrorCode());
    }
}
