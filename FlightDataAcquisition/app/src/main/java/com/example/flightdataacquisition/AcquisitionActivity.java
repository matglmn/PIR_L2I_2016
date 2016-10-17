package com.example.flightdataacquisition;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AcquisitionActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;

    ProgressBar Pbar;
    TextView AcqText;

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        // Do something with this sensor value.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquisition);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Pbar = (ProgressBar)findViewById(R.id.progress);
        Pbar.setVisibility(View.INVISIBLE);

        AcqText = (TextView)findViewById(R.id.acqText);
        AcqText.setVisibility(View.INVISIBLE);

        Button button_start = (Button)findViewById(R.id.startButton);
        button_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Pbar.setVisibility(View.VISIBLE);
                AcqText.setVisibility(View.VISIBLE);
            }
        });

        Button button_stop = (Button)findViewById(R.id.stopButton);
        button_stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Pbar.setVisibility(View.INVISIBLE);
                AcqText.setVisibility(View.INVISIBLE);
            }
        });
    }
}
