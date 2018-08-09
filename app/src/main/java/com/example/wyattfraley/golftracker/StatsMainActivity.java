package com.example.wyattfraley.golftracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StatsMainActivity extends AppCompatActivity {
    Button ShowAllRounds;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_stats_main);

        ShowAllRounds = findViewById(R.id.ViewAllRounds);

        ShowAllRounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadAllRounds();
            }
        });
    }

    public void LoadAllRounds() {
        Intent MyIntent = new Intent(StatsMainActivity.this, ShowAllRounds.class);
        startActivity(MyIntent);
    }
}
