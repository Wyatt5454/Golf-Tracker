package com.example.wyattfraley.golftracker;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShowAllHoles extends AppCompatActivity {
    FXUtility fxUtility;
    TextView mainText;
    List<Button> buttons;
    List<TextView> textViews;
    TotalRoundStats stats;
    private final static int VIBRATE_DURATION = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_holes);

        fxUtility = new FXUtility();
        mainText = findViewById(R.id.overallHoleStats);

        stats = LoadTotalStats();

        if (stats.totalRoundsFront > 0 || stats.totalRoundsBack > 0) {
            InitializeButtons();
            InitializeText();
            SetMainTextBox();
            SetHoleStats();
        }
        else {
            LinearLayout linearLayout = findViewById(R.id.allHolesLL);
            linearLayout.removeAllViews();
            linearLayout.addView(mainText);
            SetMainTextBoxNoRounds();
        }

    }
    private void InitializeButtons() {
        /*
         * Simple boring function that initializes all the hole buttons.
         */
        buttons = new ArrayList<>();

        Button toAdd = findViewById(R.id.holeButton1);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton2);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton3);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton4);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton5);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton6);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton7);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton8);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton9);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton10);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton11);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton12);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton13);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton14);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton15);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton16);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton17);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton18);
        buttons.add(toAdd);
    }
    private void InitializeText() {
        /*
         * Simple boring function that sets up all the text boxes.
         */
        textViews = new ArrayList<>();

        TextView toAdd = findViewById(R.id.holeStats1);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats2);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats3);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats4);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats5);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats6);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats7);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats8);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats9);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats10);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats11);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats12);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats13);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats14);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats15);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats16);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats17);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats18);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
    }
    private void SetHoleStats() {
        /*
         * Goes through all of the text boxes, and sets the text boxes
         * with the relevant statistics for each particular hole.
         */

        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        for (int i = 0; i < textViews.size(); i++) {
            TotalHoleStats holeStats = stats.holes.get(i);

            String statsToAdd = new String();
            if (holeStats.timesPlayed > 0) {
                statsToAdd += " Average score: " + dF.format(holeStats.strokes / holeStats.timesPlayed) + "\n";
                statsToAdd += " Average putts: " + dF.format(holeStats.putts / holeStats.timesPlayed) + "\n\n";

                if (i != 1 && i != 5 && i != 13 && i != 16) {
                    float fairwayPercentage = ((float)holeStats.fairway / (float)holeStats.timesPlayed) * 100;
                    statsToAdd += " Fairway Percentage: " + dF.format(fairwayPercentage) + "%\n";
                }

                float girPercentage = ((float)holeStats.greenInRegulation / (float)holeStats.timesPlayed) * 100;
                statsToAdd += " GIR Percentage: " + dF.format(girPercentage) + "%";
            }
            else {
                statsToAdd += " No data available yet for this hole.";
            }

            textViews.get(i).setText(statsToAdd);
        }

    }
    private void SetMainTextBox() {
        /*
         * Grabs the average stats for each type of hole.
         * TODO: Come up with more interesting stats to include in this box.
         */
        float par3 = 0, played3 = 0;
        float par4 = 0, played4 = 0;
        float par5 = 0, played5 = 0;
        int threesPlayed = 0, foursPlayed = 0, fivesPlayed = 0;
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        if (stats.holes.get(1).timesPlayed > 0) {
            par3 += stats.holes.get(1).strokes;
            played3 += stats.holes.get(1).timesPlayed;
            threesPlayed++;
        }
        if (stats.holes.get(5).timesPlayed > 0) {
            par3 += stats.holes.get(5).strokes;
            played3 += stats.holes.get(5).timesPlayed;
            threesPlayed++;
        }
        if (stats.holes.get(13).timesPlayed > 0) {
            par3 += stats.holes.get(13).strokes;
            played3 += stats.holes.get(13).timesPlayed;
            threesPlayed++;
        }
        if (stats.holes.get(16).timesPlayed > 0) {
            par3 += stats.holes.get(16).strokes;
            played3 += stats.holes.get(16).timesPlayed;
            threesPlayed++;
        }
        if (played3 > 0) {
            par3 /= played3;
            par3 /= threesPlayed;
        }


        if (stats.holes.get(2).timesPlayed > 0) {
            par4 += stats.holes.get(2).strokes;
            played4 += stats.holes.get(2).timesPlayed;
            foursPlayed++;
        }
        if (stats.holes.get(3).timesPlayed > 0) {
            par4 += stats.holes.get(3).strokes;
            played4 += stats.holes.get(3).timesPlayed;
            foursPlayed++;
        }
        if (stats.holes.get(4).timesPlayed > 0) {
            par4 += stats.holes.get(4).strokes;
            played4 += stats.holes.get(4).timesPlayed;
            foursPlayed++;
        }
        if (stats.holes.get(7).timesPlayed > 0) {
            par4 += stats.holes.get(7).strokes;
            played4 += stats.holes.get(7).timesPlayed;
            foursPlayed++;
        }
        if (stats.holes.get(8).timesPlayed > 0) {
            par4 += stats.holes.get(8).strokes;
            played4 += stats.holes.get(8).timesPlayed;
            foursPlayed++;
        }
        if (stats.holes.get(10).timesPlayed > 0) {
            par4 += stats.holes.get(10).strokes;
            played4 += stats.holes.get(10).timesPlayed;
            foursPlayed++;
        }
        if (stats.holes.get(11).timesPlayed > 0) {
            par4 += stats.holes.get(11).strokes;
            played4 += stats.holes.get(11).timesPlayed;
            foursPlayed++;
        }
        if (stats.holes.get(14).timesPlayed > 0) {
            par4 += stats.holes.get(14).strokes;
            played4 += stats.holes.get(14).timesPlayed;
            foursPlayed++;
        }
        if (stats.holes.get(15).timesPlayed > 0) {
            par4 += stats.holes.get(15).strokes;
            played4 += stats.holes.get(15).timesPlayed;
            foursPlayed++;
        }
        if (stats.holes.get(17).timesPlayed > 0) {
            par4 += stats.holes.get(17).strokes;
            played4 += stats.holes.get(17).timesPlayed;
            foursPlayed++;
        }
        if (played4 > 0) {
            par4 /= played4;
            par4 /= foursPlayed;
        }


        if (stats.holes.get(0).timesPlayed > 0) {
            par5 += stats.holes.get(0).strokes;
            played5 += stats.holes.get(0).timesPlayed;
            fivesPlayed++;
        }
        if (stats.holes.get(6).timesPlayed > 0) {
            par5 += stats.holes.get(6).strokes;
            played5 += stats.holes.get(6).timesPlayed;
            fivesPlayed++;
        }
        if (stats.holes.get(9).timesPlayed > 0) {
            par5 += stats.holes.get(9).strokes;
            played5 += stats.holes.get(9).timesPlayed;
            fivesPlayed++;
        }
        if (stats.holes.get(12).timesPlayed > 0) {
            par5 += stats.holes.get(12).strokes;
            played5 += stats.holes.get(12).timesPlayed;
            fivesPlayed++;
        }
        if (played5 > 0) {
            par5 /= played5;
            par5 /= fivesPlayed;
        }

        String toAdd = new String();
        toAdd += " Average par 3 Score: " + dF.format(par3) + "\n";
        toAdd += " Average par 4 Score: " + dF.format(par4) + "\n";
        toAdd += " Average par 5 Score: " + dF.format(par5);

        mainText.setText(toAdd);
    }
    private void SetMainTextBoxNoRounds() {
        mainText.setText(R.string.stats_no_rounds);
    }
    private void SetMainTextBoxNoCompleteRounds() {
        mainText.setText(R.string.stats_no_complete_rounds);
    }

    private TotalRoundStats LoadTotalStats() {
        /*
         * Grabs the total stats from a file locally stored
         * on the device.
         */

        TotalRoundStats stats = new TotalRoundStats();
        File filesDir = getFilesDir();
        File fileTotal = new File( filesDir, "TotalStats.txt");
        File fileHoles = new File(filesDir, "HoleStats.txt");

        try {
            // Reading object in a file
            FileInputStream inputStreamTotal = new FileInputStream(fileTotal);
            FileInputStream inputStreamHoles = new FileInputStream(fileHoles);

            // Reading object in a file
            ObjectInputStream inTotal = new ObjectInputStream(inputStreamTotal);
            ObjectInputStream inHoles = new ObjectInputStream(inputStreamHoles);

            // Method for deserialization of object
            stats = (TotalRoundStats)inTotal.readObject();
            stats.holes = (ArrayList<TotalHoleStats>)inHoles.readObject();

            inTotal.close();
            inHoles.close();
            inputStreamTotal.close();
            inputStreamHoles.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public void toggle_contents(View v){
        /*
         * Uses the FX utility to animate the boxes opening and closing.
         * Currently the animations are disabled.
         */
        int found = 0;
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isPressed()) {
                found = i;
                break;
            }
        }

        TextView toAnimate = textViews.get(found);


        if(toAnimate.isShown()){
            //fxUtility.slide_up(this, toAnimate);
            toAnimate.setVisibility(View.GONE);
        }
        else{
            toAnimate.setVisibility(View.VISIBLE);
            //fxUtility.slide_down(this, toAnimate);
        }
        VibrateOnClick();
    }
    private void VibrateOnClick() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(VIBRATE_DURATION);
    }
}
