package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.wyattfraley.golftracker.scorecard.ScorecardActivity;

public class HomeScreen extends AppCompatActivity {
    public static final String TAG = "Golf Home Screen";
    private final static int VIBRATE_DURATION = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Log.i(TAG, "Golf application start.");

        // This checks to see if the app was reopened from the launcher,
        // and goes to the old version if another exists.
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

            finish();
            return;
        }


        // Asks for permission to access location if the app doesn't already have it.
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
