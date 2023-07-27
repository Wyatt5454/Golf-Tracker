package com.example.wyattfraley.golftracker.database.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wyattfraley.golftracker.R;
import com.example.wyattfraley.golftracker.database.RealmScoreEntry;
import com.example.wyattfraley.golftracker.statistics.TotalHoleStats;
import com.example.wyattfraley.golftracker.statistics.TotalRoundStats;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObjectSchema;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

public class ShowAllHoles extends Activity implements AdapterView.OnItemSelectedListener {

    TextView mainText;
    List<Button> buttons;
    List<TextView> textViews;
    TotalRoundStats stats;
    Spinner sortSpinner;
    private final static int VIBRATE_DURATION = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_holes);

        mainText = findViewById(R.id.overallHoleStats);

        stats = LoadTotalStats();

        if (stats.totalRoundsFront > 0 || stats.totalRoundsBack > 0) {
            InitializeButtons();
            InitializeText();
            InitializeSpinner();
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
    private void InitializeSpinner() {
        sortSpinner = findViewById(R.id.sortSpinner);
        sortSpinner.setOnItemSelectedListener(this);
        //sortSpinner.getBackground().setColorFilter(getResources().getColor(gray), PorterDuff.Mode.SRC_ATOP);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_holes, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
    }

    /**
     * Goes through all of the text boxes, and sets the text boxes
     * with the relevant statistics for each particular hole.
     */
    private void SetHoleStats() {

        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        for (int i = 0; i < textViews.size(); i++) {
            TotalHoleStats holeStats = stats.holes.get(i);

            String statsToAdd = "";
            if (holeStats.timesPlayed > 0) {
                statsToAdd += " Average score: " + dF.format((float)holeStats.strokes / (float)holeStats.timesPlayed) + "\n";
                statsToAdd += " Average putts: " + dF.format((float)holeStats.putts / (float)holeStats.timesPlayed) + "\n";

                if (holeStats.penalties > 0) {
                    statsToAdd += " Average Penalties: " + dF.format((float)holeStats.penalties / (float)holeStats.timesPlayed) + "\n\n";
                }
                else {
                    statsToAdd += "\n";
                }

                if (holeStats.par != 3) {
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
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        par3 += stats.holes.get(1).strokes;
        played3 += stats.holes.get(1).timesPlayed;
        par3 += stats.holes.get(5).strokes;
        played3 += stats.holes.get(5).timesPlayed;
        par3 += stats.holes.get(13).strokes;
        played3 += stats.holes.get(13).timesPlayed;
        par3 += stats.holes.get(16).strokes;
        played3 += stats.holes.get(16).timesPlayed;
        if (played3 > 0) {
            par3 /= played3;
        }

        par4 += stats.holes.get(2).strokes;
        played4 += stats.holes.get(2).timesPlayed;
        par4 += stats.holes.get(3).strokes;
        played4 += stats.holes.get(3).timesPlayed;
        par4 += stats.holes.get(4).strokes;
        played4 += stats.holes.get(4).timesPlayed;
        par4 += stats.holes.get(7).strokes;
        played4 += stats.holes.get(7).timesPlayed;
        par4 += stats.holes.get(8).strokes;
        played4 += stats.holes.get(8).timesPlayed;
        par4 += stats.holes.get(10).strokes;
        played4 += stats.holes.get(10).timesPlayed;
        par4 += stats.holes.get(11).strokes;
        played4 += stats.holes.get(11).timesPlayed;
        par4 += stats.holes.get(14).strokes;
        played4 += stats.holes.get(14).timesPlayed;
        par4 += stats.holes.get(15).strokes;
        played4 += stats.holes.get(15).timesPlayed;
        par4 += stats.holes.get(17).strokes;
        played4 += stats.holes.get(17).timesPlayed;
        if (played4 > 0) {
            par4 /= played4;
        }


        par5 += stats.holes.get(0).strokes;
        played5 += stats.holes.get(0).timesPlayed;
        par5 += stats.holes.get(6).strokes;
        played5 += stats.holes.get(6).timesPlayed;
        par5 += stats.holes.get(9).strokes;
        played5 += stats.holes.get(9).timesPlayed;
        par5 += stats.holes.get(12).strokes;
        played5 += stats.holes.get(12).timesPlayed;
        if (played5 > 0) {
            par5 /= played5;
        }

        String toAdd = "";
        toAdd += " Average par 3 Score: " + dF.format(par3) + "\n";
        toAdd += " Average par 4 Score: " + dF.format(par4) + "\n";
        toAdd += " Average par 5 Score: " + dF.format(par5);

        mainText.setText(toAdd);
    }
    private void SetMainTextBoxNoRounds() {
        mainText.setText(R.string.stats_no_rounds);
    }

    /**
     * Compiles the stats on the Realm database into some interesting
     * stats for viewer use.
     */
    private TotalRoundStats LoadTotalStats() {

        TotalRoundStats stats = new TotalRoundStats();




        return stats;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        Object itemAtPosition = parent.getItemAtPosition(pos);

        switch (itemAtPosition.toString()) {
            case "Playing Order":
                SortByPlayingOrder();
                break;
            case "Best to Worst: Score":
                SortByBestToWorstScore();
                break;
            case "Worst to Best: Score":
                SortByWorstToBestScore();
                break;
        }
    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void SortByPlayingOrder() {
        List<TotalHoleStats> holeStats = stats.holes;

        Comparator<TotalHoleStats> comparator = (first, second) -> {
            if (first.number < second.number) {
                return -1;
            }
            else if (first.number > second.number) {
                return 1;
            }
            return 0;
        };

        holeStats.sort(comparator);
        SortButtonsAndBoxes(holeStats);
    }
    private void SortByBestToWorstScore() {
        List<TotalHoleStats> holeStats = stats.holes;

        Comparator<TotalHoleStats> comparator = (first, second) -> {
            float firstAverage, secondAverage;
            if (first.timesPlayed > 0) {
                firstAverage = ((float)first.strokes / (float)first.timesPlayed) - first.par;
            }
            else {
                firstAverage = 999;
            }
            if (second.timesPlayed > 0) {
                secondAverage = ((float)second.strokes / (float)second.timesPlayed) - second.par;
            }
            else {
                secondAverage = 999;
            }

            if (firstAverage < secondAverage) {
                return -1;
            }
            else if (firstAverage > secondAverage) {
                return 1;
            }
            return 0;
        };

        holeStats.sort(comparator);
        SortButtonsAndBoxes(holeStats);
    }
    private void SortByWorstToBestScore() {
        List<TotalHoleStats> holeStats = stats.holes;

        Comparator<TotalHoleStats> comparator = (first, second) -> {
            float firstAverage, secondAverage;
            if (first.timesPlayed > 0) {
                firstAverage = ((float)first.strokes / (float)first.timesPlayed) - first.par;
            }
            else {
                firstAverage = -999;
            }
            if (second.timesPlayed > 0) {
                secondAverage = ((float)second.strokes / (float)second.timesPlayed) - second.par;
            }
            else {
                secondAverage = -999;
            }

            if (firstAverage > secondAverage) {
                return -1;
            }
            else if (firstAverage < secondAverage) {
                return 1;
            }
            return 0;
        };

        holeStats.sort(comparator);
        SortButtonsAndBoxes(holeStats);
    }
    private void SortButtonsAndBoxes(List<TotalHoleStats> totalHoleStats) {
        LinearLayout linearLayout = findViewById(R.id.allHolesLL);
        linearLayout.removeAllViews();
        linearLayout.addView(mainText);
        linearLayout.addView(sortSpinner);

        TotalHoleStats holeStats;
        View button;
        View textBox;

        for (int i = 0; i < totalHoleStats.size(); i++) {
            holeStats = totalHoleStats.get(i);

            button = buttons.get(holeStats.number - 1);
            linearLayout.addView(button);

            textBox = textViews.get(holeStats.number - 1);
            textBox.setVisibility(View.GONE);
            linearLayout.addView(textBox);
        }
    }

    /**
     * Uses the FX utility to animate the boxes opening and closing.
     * Currently the animations are disabled.
     */
    public void toggle_contents(View v){

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
