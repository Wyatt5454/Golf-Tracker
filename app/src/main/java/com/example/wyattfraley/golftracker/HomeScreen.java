package com.example.wyattfraley.golftracker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        checkLocationPermission();
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
    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
