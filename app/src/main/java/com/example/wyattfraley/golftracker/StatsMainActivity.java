package com.example.wyattfraley.golftracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wyattfraley.golftracker.database.activity.ShowAllHoles;
import com.example.wyattfraley.golftracker.database.activity.ShowAllRounds;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * The main home page for the user to look at statistics.  Provides
 * buttons for showing every round played, and every hole average
 * statistics.
 */
public class StatsMainActivity extends AppCompatActivity {
    private Button showAllRounds;
    private Button showAllHoles;
    private TextView showTotalStats;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_stats_main);

        showAllRounds = findViewById(R.id.ViewAllRounds);
        showTotalStats = findViewById(R.id.StatsHolder);
        showAllHoles = findViewById(R.id.viewAllHoles);

        showAllRounds.setOnClickListener(v -> LoadAllRounds());
        showAllHoles.setOnClickListener(v -> LoadHoleStats());

        DisplayTotalStats();
    }

    /**
     * Goes to the All Rounds stats activity
     */
    public void LoadAllRounds() {
        Intent myIntent = new Intent(StatsMainActivity.this, ShowAllRounds.class);
        startActivity(myIntent);
    }

    /**
     * Goes to the individual hole stats activity
     */
    public void LoadHoleStats() {
        Intent myIntent = new Intent(StatsMainActivity.this, ShowAllHoles.class);
        startActivity(myIntent);
    }

    /**
     * Grabs the total stats from a file locally stored
     * on the device.
     */
    public TotalRoundStats LoadTotalStats() {

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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return stats;
    }

    /**
     * This function shows the average stats for all rounds
     * and puts it in the main text box at the top of the activity.
     * Will be adding more detailed stats later.
     *
     * Gets rid of the stats buttons if there are no rounds
     * to show.
     */
    public void DisplayTotalStats() {

        TotalRoundStats stats = LoadTotalStats();

        if (stats.totalRoundsFront > 0 && stats.totalRoundsBack > 0) {
            BackAndFront(stats);
        }
        else if (stats.totalRoundsFront > 0) {
            OnlyFront(stats);
        }
        else if (stats.totalRoundsBack > 0) {
            OnlyBack(stats);
        }
        else if (stats.totalRounds > 0) {
            showTotalStats.setText(R.string.stats_no_complete_rounds);
        }
        else {
            showTotalStats.setText(R.string.stats_no_rounds);
            showAllHoles.setVisibility(View.GONE);
            showAllRounds.setVisibility(View.GONE);
        }
    }

    /**
     * Displays the general stats for a round.  That's the total score,
     * the front 9 score, the back 9 score, putts, bunkers, penalties,
     * fairways and greens in regulation
     *
     * @param stats the stats to display
     */
    private void BackAndFront(TotalRoundStats stats) {
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        String toDisplay = " Average Overall Statistics\n\n";

        float frontAverage = (float)stats.totalFrontScore / (float)stats.totalRoundsFront;
        float backAverage = (float)stats.totalBackScore / (float)stats.totalRoundsBack;
        float totalAverage = frontAverage + backAverage;

        float frontPutts = (float)stats.totalFrontPutts / (float)stats.totalRoundsFront;
        float backPutts = (float)stats.totalBackPutts / (float)stats.totalRoundsBack;
        float totalPutts = frontPutts + backPutts;

        float frontSand = (float)stats.totalFrontSand / (float)stats.totalRoundsFront;
        float backSand = (float)stats.totalBackSand / (float)stats.totalRoundsBack;
        float totalSand = frontSand + backSand;

        float frontPenalties = (float)stats.totalFrontPenalties / (float)stats.totalRoundsFront;
        float backPenalties = (float)stats.totalBackPenalties / (float)stats.totalRoundsBack;
        float totalPenalties = frontPenalties + backPenalties;

        float totalFairway = (((float)stats.totalFrontFairway + (float)stats.totalBackFairway)
                / (((float)stats.totalRoundsFront + (float)stats.totalRoundsBack) * 7)) * 100;

        float totalGIR = (((float)stats.totalFrontGIR + (float)stats.totalBackGIR)
                / (((float)stats.totalRoundsFront + (float)stats.totalRoundsBack) * 9)) * 100;

        toDisplay += " Score: " + dF.format(totalAverage) + "\n";
        toDisplay += " Front: " + dF.format(frontAverage) + "\n";
        toDisplay += " Back: " + dF.format(backAverage) + "\n\n";
        toDisplay += " Putts: " + dF.format(totalPutts) + "\n";
        toDisplay += " Sand Traps Hit: " + dF.format(totalSand) + "\n";
        toDisplay += " Penalty Strokes: " + dF.format(totalPenalties) + "\n\n";
        toDisplay += " Fairway in Regulation: " + dF.format(totalFairway) + "%\n";
        toDisplay += " Green in Regulation: " + dF.format(totalGIR) + "%";

        showTotalStats.setText(toDisplay);
    }

    /**
     * Display the stats only for the front 9.  That's score, putts,
     * bunkers, penalties, fairways and greens in regulation.
     *
     * @param stats the stats to display
     */
    private void OnlyFront(TotalRoundStats stats) {
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        String toDisplay = " Average Overall Statistics\n\n";

        float frontAverage = (float)stats.totalFrontScore / (float)stats.totalRoundsFront;
        float frontPutts = (float)stats.totalFrontPutts / (float)stats.totalRoundsFront;
        float frontSand = (float)stats.totalFrontSand / (float)stats.totalRoundsFront;
        float frontPenalties = (float)stats.totalFrontPenalties / (float)stats.totalRoundsFront;
        float frontFairway = ((float)stats.totalFrontFairway / (stats.totalRoundsFront * 7)) * 100;
        float frontGIR = ((float)stats.totalFrontGIR / (stats.totalRoundsFront * 9)) * 100;

        toDisplay += " Front: " + dF.format(frontAverage) + "\n\n";
        toDisplay += " Putts: " + dF.format(frontPutts) + "\n";
        toDisplay += " Sand Traps Hit: " + dF.format(frontSand) + "\n";
        toDisplay += " Penalties: " + dF.format(frontPenalties) + "\n\n";
        toDisplay += " Fairway in Regulation: " + dF.format(frontFairway) + "%\n";
        toDisplay += " Green in Regulation: " + dF.format(frontGIR) + "%";

        showTotalStats.setText(toDisplay);
    }

    /**
     * Display the stats only for the back 9.  That's score, putts,
     * bunkers, penalties, fairways and greens in regulation.
     *
     * @param stats the stats to display
     */
    private void OnlyBack(TotalRoundStats stats) {
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        String toDisplay = " Average Overall Statistics\n\n";

        float backAverage = (float)stats.totalBackScore / (float)stats.totalRoundsBack;
        float backPutts = (float)stats.totalBackPutts / (float)stats.totalRoundsBack;
        float backSand = (float)stats.totalBackSand / (float)stats.totalRoundsBack;
        float backPenalties = (float)stats.totalBackPenalties / (float)stats.totalRoundsFront;
        float backFairway = ((float)stats.totalBackFairway / (stats.totalRoundsBack * 7)) * 100;
        float backGIR = ((float)stats.totalBackGIR / (stats.totalRoundsBack * 9)) * 100;

        toDisplay += " Back: " + dF.format(backAverage) + "\n\n";
        toDisplay += " Putts: " + dF.format(backPutts) + "\n";
        toDisplay += " Sand Traps Hit: " + dF.format(backSand) + "\n";
        toDisplay += " Penalties: " + dF.format(backPenalties) + "\n\n";
        toDisplay += " Fairway in Regulation: " + dF.format(backFairway) + "%\n";
        toDisplay += " Green in Regulation: " + dF.format(backGIR) + "%";

        showTotalStats.setText(toDisplay);
    }
}
