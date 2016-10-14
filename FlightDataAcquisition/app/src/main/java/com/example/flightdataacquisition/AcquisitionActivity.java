package com.example.flightdataacquisition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class AcquisitionActivity extends AppCompatActivity {

    ProgressBar Pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquisition);

        Pbar = (ProgressBar)findViewById(R.id.progress);
        Pbar.setVisibility(View.INVISIBLE);
        Button button_start = (Button)findViewById(R.id.startButton);
        button_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Pbar.setVisibility(View.VISIBLE);
            }
        });
    }
}
