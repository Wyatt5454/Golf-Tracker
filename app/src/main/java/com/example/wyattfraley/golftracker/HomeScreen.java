package com.example.wyattfraley.golftracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


    }

    public void selectCourse(View view) {
        // Goes to the select course activity
        Intent intent = new Intent(this, SelectCourseActivity.class);
        startActivity(intent);
    }
    public void Scorecard(View view)  {
        Intent MyIntent = new Intent(HomeScreen.this, ScorecardActivity.class);
        startActivity(MyIntent);
    }
    public void StatsMain(View view) {
        Intent MyIntent = new Intent(HomeScreen.this, StatsMainActivity.class);
        startActivity(MyIntent);
    }
}
