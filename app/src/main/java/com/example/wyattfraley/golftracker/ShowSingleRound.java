package com.example.wyattfraley.golftracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ShowSingleRound  extends AppCompatActivity{
    TextView OverallStats;
    TextView HoleStats;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_show_single_round);

        OverallStats = findViewById(R.id.OverallStats);
        OverallStats.setBackgroundColor(Color.WHITE);

        HoleStats = findViewById(R.id.HoleStats);
        HoleStats.setBackgroundColor(Color.WHITE);
    }
}
