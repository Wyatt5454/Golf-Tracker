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
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class StatsMainActivity extends AppCompatActivity {
    Button ShowAllRounds;
    Button showAllHoles;
    TextView showTotalStats;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_stats_main);

        ShowAllRounds = findViewById(R.id.ViewAllRounds);
        showTotalStats = findViewById(R.id.StatsHolder);
        showAllHoles = findViewById(R.id.viewAllHoles);

        ShowAllRounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadAllRounds();
            }
        });
        showAllHoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadHoleStats();
            }
        });


        DisplayTotalStats();
    }

    public void LoadAllRounds() {
        // Goes to the All Rounds stats activity

        Intent MyIntent = new Intent(StatsMainActivity.this, ShowAllRounds.class);
        startActivity(MyIntent);
    }
    public TotalRoundStats LoadTotalStats() {
        // This function grabs the total stats from a file locally
        // stored on the device.


        TotalRoundStats stats = new TotalRoundStats();

        File filesDir = getFilesDir();
        File file = new File( filesDir, "TotalStats.txt");

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
        // This function shows the average stats for all rounds
        // and puts it in the main text box at the top of the activity.

        TotalRoundStats stats = LoadTotalStats();
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);



        String toDisplay = new String();
        toDisplay += "Average Score: " + dF.format(((float)stats.totalScore / (float)stats.totalRounds)) + "\n";
        toDisplay += "Average Putts: " + dF.format(((float)stats.totalPutts / (float)stats.totalRounds)) + "\n";
        toDisplay += "Average Sand Traps Hit: " + dF.format(((float)stats.totalSand / (float)stats.totalRounds));

        showTotalStats.setText(toDisplay);
    }
    public void LoadHoleStats() {
        // Goes to the individual hole stats activity.

        Intent myIntent = new Intent(StatsMainActivity.this, ShowAllHoles.class);
        startActivity(myIntent);
    }
}
