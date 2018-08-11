package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class StatsMainActivity extends AppCompatActivity {
    Button ShowAllRounds;
    TextView showTotalStats;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_stats_main);

        ShowAllRounds = findViewById(R.id.ViewAllRounds);
        showTotalStats = findViewById(R.id.StatsHolder);

        ShowAllRounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadAllRounds();
            }
        });

        DisplayTotalStats();
    }

    public void LoadAllRounds() {
        Intent MyIntent = new Intent(StatsMainActivity.this, ShowAllRounds.class);
        startActivity(MyIntent);
    }
    public TotalRoundStats LoadTotalStats() {
        TotalRoundStats stats = new TotalRoundStats();

        File file = new File( Environment.getExternalStorageDirectory() + "/Download/TotalStats.txt");

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        try {
            // Reading object in a file
            FileInputStream stream = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(stream);

            // Method for deserialization of object
            stats = (TotalRoundStats)in.readObject();

            in.close();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return stats;
    }
    public void DisplayTotalStats() {
        TotalRoundStats stats = LoadTotalStats();

        String toDisplay = new String();
        toDisplay += "Average Score: " + (stats.totalScore / stats.totalRounds) + "\n";
        toDisplay += "Average Putts: " + (stats.totalPutts / stats.totalRounds) + "\n";
        toDisplay += "Average Sand Traps Hit: " + (stats.totalSand / stats.totalRounds);

        showTotalStats.setText(toDisplay);
    }
}
