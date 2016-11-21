package com.example.flightdataacquisition;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
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


public class AcquisitionActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, SensorEventListener{

    // Define variables
    ProgressBar Pbar;
    TextView AcqText;

    private static final String TAG = AcquisitionActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestLocationUpdates = false;
    private LocationRequest mLocationRequest;

    private TextView lblLocation;
    private TextView sensorTextview;

    private SensorManager mSensorManager;
    private Sensor accelerometer, magnetometer;

    float[] accelerometerVector = new float[3];
    float[] magneticVector = new float[3];
    float[] resultMatrix=  new float[9];
    float[] values = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquisition);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        lblLocation = (TextView) findViewById(R.id.location_textview);
        sensorTextview = (TextView) findViewById(R.id.sensor_textview);

        Pbar = (ProgressBar) findViewById(R.id.progress);
        Pbar.setVisibility(View.INVISIBLE);

        AcqText = (TextView) findViewById(R.id.acqText);
        AcqText.setVisibility(View.INVISIBLE);

        if(checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

        Button button_start = (Button) findViewById(R.id.startButton);
        button_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Pbar.setVisibility(View.VISIBLE);
                AcqText.setVisibility(View.VISIBLE);
                displayLocation();
                mRequestLocationUpdates = true;
                startAcquisition();
                startLocationUpdates();
            }
        });

        Button button_stop = (Button) findViewById(R.id.stopButton);
        button_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Pbar.setVisibility(View.INVISIBLE);
                AcqText.setVisibility(View.INVISIBLE);
                mRequestLocationUpdates = false;
                stopAcquisition();
                stopLocationUpdates();
            }
        });

        Button button_marker = (Button) findViewById(R.id.markerButton);
        button_marker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                writeJSON();
            }
        });
    }

    public void startAcquisition() {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void stopAcquisition() {
        mSensorManager.unregisterListener(this, accelerometer);
        mSensorManager.unregisterListener(this, magnetometer);
    }

    public void onSensorChanged(SensorEvent event){
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerVector = event.values;
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticVector = event.values;
        }
        SensorManager.getRotationMatrix(resultMatrix, null, accelerometerVector, magneticVector);
        SensorManager.getOrientation(resultMatrix, values);
        // yaw
        float yaw = (float) Math.toDegrees(values[0]);
        // pitch
        float pitch = (float) Math.toDegrees(values[1]);
        // roll
        float roll = (float) Math.toDegrees(values[2]);
        sensorTextview.setText("yaw:" + yaw + " " + "roll:" + roll + " " + "pitch:" + pitch);

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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

//    private void displayLocation() {
//        try {
//            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
//        if(mLastLocation != null) {
//            double latitude = mLastLocation.getLatitude();
//            double longitude = mLastLocation.getLongitude();
//
//            lblLocation.setText(latitude + ", " + longitude);
//        } else {
//            lblLocation.setText(R.string.loc_error);
//        }
//    }

    // TEST JSON WRITING ###############################################
    public double[] displayLocation() {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if(mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            double location[] = {latitude, longitude};

            return location;
        }
        else {
            return null;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        int UPDATE_INTERVAL = 1000;
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        int FATEST_INTERVAL = 500;
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        int DISPLACEMENT = 10;
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(resultCode)) {
                googleAPI.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public void writeJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("latitude", displayLocation()[0]);
            object.put("longitude", displayLocation()[1]);
            object.put("yaw", yaw);
            object.put("roll", roll);
            object.put("pitch", pitch);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(object);
    }

    @Override
    public void onConnected(Bundle bundle) {
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
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();

        displayLocation();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: " + connectionResult.getErrorCode());
    }
}
