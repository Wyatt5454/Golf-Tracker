package com.example.wyattfraley.golftracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class StatsMainActivity extends AppCompatActivity {
    Button showAllRounds;
    Button showAllHoles;
    TextView showTotalStats;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_stats_main);

        showAllRounds = findViewById(R.id.ViewAllRounds);
        showTotalStats = findViewById(R.id.StatsHolder);
        showAllHoles = findViewById(R.id.viewAllHoles);

        showAllRounds.setOnClickListener(new View.OnClickListener() {
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
        /*
         * Goes to the All Rounds stats activity
         */

        Intent myIntent = new Intent(StatsMainActivity.this, ShowAllRounds.class);
        startActivity(myIntent);
    }
    public void LoadHoleStats() {
        /*
         * Goes to the individual hole stats activity
         */

        Intent myIntent = new Intent(StatsMainActivity.this, ShowAllHoles.class);
        startActivity(myIntent);
    }
    public TotalRoundStats LoadTotalStats() {
        /*
         * Grabs the total stats from a file locally stored
         * on the device.
         */

        TotalRoundStats stats = new TotalRoundStats();
        File filesDir = getFilesDir();
        File file = new File( filesDir, "TotalStats.txt");

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
        /*
         * This function shows the average stats for all rounds
         * and puts it in the main text box at the top of the activity.
         * Will be adding more detailed stats later.
         *
         * Gets rid of the stats buttons if there are no rounds
         * to show.
         */

        TotalRoundStats stats = LoadTotalStats();
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);
        float fairwayPercentage = 0;
        float girPercentage = 0;


        String toDisplay = new String();
        if (stats.totalRounds > 0) {
            toDisplay += " Average Overall Statistics\n\n";
            toDisplay += " Score: " + dF.format(((float)stats.totalScore / (float)stats.totalRounds)) + "\n";
            toDisplay += " Putts: " + dF.format(((float)stats.totalPutts / (float)stats.totalRounds)) + "\n";
            toDisplay += " Sand Traps Hit: " + dF.format(((float)stats.totalSand / (float)stats.totalRounds)) + "\n\n";

            fairwayPercentage = ((float)stats.totalFairway / ((float)stats.totalRounds * 14)) * 100;
            girPercentage = ((float)stats.totalGIR / ((float)stats.totalRounds * 18)) * 100;
            toDisplay += " Fairway in Regulation: " + dF.format(fairwayPercentage) + "%\n";
            toDisplay += " Green in Regulation: " + dF.format(girPercentage) + "%";
        }
        else {
            toDisplay += "You don't have any rounds saved on this device.  Start a round and save it for detailed stat tracking!";
            showAllHoles.setVisibility(View.GONE);
            showAllRounds.setVisibility(View.GONE);
        }



        showTotalStats.setText(toDisplay);
    }
}
