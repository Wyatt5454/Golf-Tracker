package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HomeScreen extends AppCompatActivity {
    public static final String TAG = "Golf Home Screen";
    private final static int VIBRATE_DURATION = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Log.i(TAG, "Golf application start.");


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ActivityCompat.requestPermissions(HomeScreen.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return null;
            }
        }.execute();
    }

    public void Scorecard(View view)  {
        /*
         * Opens the scorecard activity.
         */

        VibrateOnClick();
        Intent myIntent = new Intent(HomeScreen.this, ScorecardActivity.class);
        startActivity(myIntent);
    }
    public void StatsMain(View view) {
        /*
         * Opens the main stats activity.
         */
        VibrateOnClick();
        Intent myIntent = new Intent(HomeScreen.this, StatsMainActivity.class);
        startActivity(myIntent);
    }
    private void VibrateOnClick() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(VIBRATE_DURATION);
    }
}
