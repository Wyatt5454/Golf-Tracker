package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HomeScreen extends AppCompatActivity {
    public static final String TAG = "Golf Home Screen";

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

        if (savedInstanceState != null) {
            Log.i(TAG, "Restoring instance state from onCreate");

            Intent myIntent = new Intent(HomeScreen.this, ScorecardActivity.class);
            ScoreEntry entry = (ScoreEntry)savedInstanceState.getSerializable("score");
            int current = savedInstanceState.getInt("current");
            myIntent.putExtra("score", entry);
            myIntent.putExtra("current", current);
            startActivity(myIntent);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "Restoring instance state");

        Intent myIntent = new Intent(HomeScreen.this, ScorecardActivity.class);
        ScoreEntry entry = (ScoreEntry)savedInstanceState.getSerializable("score");
        int current = savedInstanceState.getInt("current");
        myIntent.putExtra("score", entry);
        myIntent.putExtra("current", current);
        startActivity(myIntent);
    }

    public void selectCourse(View view) {
        // Goes to the select course activity
        Intent myIntent = new Intent(this, SelectCourseActivity.class);
        startActivity(myIntent);
    }
    public void Scorecard(View view)  {
        /*
         * Opens the scorecard activity.
         */
        Intent myIntent = new Intent(HomeScreen.this, ScorecardActivity.class);
        startActivity(myIntent);
    }
    public void StatsMain(View view) {
        /*
         * Opens the main stats activity.
         */
        Intent myIntent = new Intent(HomeScreen.this, StatsMainActivity.class);
        startActivity(myIntent);
    }
}
