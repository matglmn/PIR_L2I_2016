package com.example.flightdataacquisition;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class AcquisitionActivity extends AppCompatActivity {

    // Defines variables
    ProgressBar Pbar;
    TextView AcqText;
    double longitude;
    double latitude;

    LocationTool gps = new LocationTool(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquisition);

        Pbar = (ProgressBar) findViewById(R.id.progress);
        Pbar.setVisibility(View.INVISIBLE);

        AcqText = (TextView) findViewById(R.id.acqText);
        AcqText.setVisibility(View.INVISIBLE);

        Button button_start = (Button) findViewById(R.id.startButton);
        button_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (gps.canGetLocation) {
                    Pbar.setVisibility(View.VISIBLE);
                    AcqText.setVisibility(View.VISIBLE);
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    AcqText.append("\n" + latitude + longitude);
                }
                else {
                    gps.showSettingsAlert();
                }
            }
        });

        Button button_stop = (Button) findViewById(R.id.stopButton);
        button_stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Pbar.setVisibility(View.INVISIBLE);
                AcqText.setVisibility(View.INVISIBLE);
                gps.stopUsingGPS();
            }
        });
    }
}
