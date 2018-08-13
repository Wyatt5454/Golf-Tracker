package com.example.wyattfraley.golftracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ShowAllHoles extends AppCompatActivity {
    FXUtility fxUtility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_holes);

        fxUtility = new FXUtility();

        // TODO: Make the stats object into an extra accessible by this activity.
        Intent MyIntent = getIntent();
        //TotalRoundStats stats = MyIntent.getSerializableExtra("Stats");
    }
}
