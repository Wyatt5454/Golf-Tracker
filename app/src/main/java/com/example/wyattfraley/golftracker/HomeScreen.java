package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Vibrator;

import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.wyattfraley.golftracker.scorecard.activity.ScorecardActivity;
import com.example.wyattfraley.golftracker.statistics.activity.StatsMainActivity;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

/**
 * Activity representing the home screen.  This is the
 * first thing users will see when they open the app.
 * Currently presents a button to display the scorecard
 * for a new round, and a button to launch the statistics
 * tracking.
 */
public class HomeScreen extends Activity {
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
        AsyncTask<Void, Void, Void> locationPermissionAsk = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ActivityCompat.requestPermissions(HomeScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return null;
            }
        };
        locationPermissionAsk.execute();

        Realm.init(this);

        App app = new App(new AppConfiguration.Builder("golfuserdataapplication-cqklc").build());
    }

    /**
     * Opens the scorecard activity.
     */
    public void OpenScorecardActivity(View view)  {

        VibrateOnClick();
        Intent myIntent = new Intent(HomeScreen.this, ScorecardActivity.class);
        startActivity(myIntent);
    }

    /**
     * Opens the main stats activity.
     */
    public void openStatsMainActivity(View view) {

        VibrateOnClick();
        Intent myIntent = new Intent(HomeScreen.this, StatsMainActivity.class);
        startActivity(myIntent);
    }

    /**
     * Generally called on a button click.  Makes the phone
     * vibrate to give user feedback that they properly pressed
     * the button.
     */
    private void VibrateOnClick() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(VIBRATE_DURATION);
    }
}
