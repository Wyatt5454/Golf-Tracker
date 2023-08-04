package com.example.wyattfraley.golftracker.database.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.wyattfraley.golftracker.R;
import com.example.wyattfraley.golftracker.database.RealmScoreEntry;
import com.example.wyattfraley.golftracker.database.DeleteRound;
import com.example.wyattfraley.golftracker.scorecard.HoleScoreData;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class ShowSingleRound  extends Activity {
    TextView overallStats;
    TextView holeStats;
    List<HoleScoreData> scores;
    HoleScoreData currentHole;
    RealmScoreEntry myEntry;
    String uid;
    int puttsTotal;
    int sandTotal;
    int fairwayTotal;
    int girTotal;
    int penaltiesTotal;
    int finalScore, netScore;
    int holesPlayed = 0, fairwaysPlayed = 0;
    private final static int VIBRATE_DURATION = 20;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_show_single_round);
        Intent myIntent = getIntent();
        uid = myIntent.getStringExtra("uid");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                initializeUI();
            }
        }.execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * Starts the delete round activity when the menu button
         * is clicked, or quits if the back button is pressed.
         */
        int id = item.getItemId();

        if (id == R.id.delete_menu) {
            Intent newIntent = new Intent(ShowSingleRound.this, DeleteRound.class);
            //newIntent.putExtra("Score", myEntry);
            startActivityForResult(newIntent, 99);
        }
        else if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

    /**
     * Checks to see if the round was deleted, and quits out if it was.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 99) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, null);
                this.finish();
            }
        }
    }

    private void initializeUI() {
        puttsTotal = 0;
        sandTotal = 0;
        fairwayTotal = 0;
        girTotal = 0;

        overallStats = findViewById(R.id.overallStats);
        holeStats = findViewById(R.id.holeStats);

        scores = initializeScores();
        loadScores(myEntry);
        currentHole = scores.get(0);
        finalScore = myEntry.getFinalScore();
        netScore = finalScore - myEntry.getParPlayed();

        setOverallTextBox();
    }

    /**
     * Sets on click listeners for all the text boxes, so the
     * stats boxes will display the proper info for the
     * selected hole.
     */
    public List<HoleScoreData> initializeScores() {

        scores = new ArrayList<>();
        final HoleScoreData score1 = new HoleScoreData(findViewById(R.id.tv11));
        score1.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score1;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score1);
        final HoleScoreData score2 = new HoleScoreData(findViewById(R.id.tv12));
        score2.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score2;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score2);
        final HoleScoreData score3 = new HoleScoreData(findViewById(R.id.tv13));
        score3.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score3;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score3);
        final HoleScoreData score4 = new HoleScoreData(findViewById(R.id.tv14));
        score4.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score4;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score4);
        final HoleScoreData score5 = new HoleScoreData(findViewById(R.id.tv15));
        score5.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score5;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score5);
        final HoleScoreData score6 = new HoleScoreData(findViewById(R.id.tv16));
        score6.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score6;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score6);
        final HoleScoreData score7 = new HoleScoreData(findViewById(R.id.tv17));
        score7.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score7;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score7);
        final HoleScoreData score8 = new HoleScoreData(findViewById(R.id.tv18));
        score8.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score8;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score8);
        final HoleScoreData score9 = new HoleScoreData(findViewById(R.id.tv19));
        score9.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score9;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score9);
        scores.add(new HoleScoreData(findViewById(R.id.tv20)));
        final HoleScoreData score10 = new HoleScoreData(findViewById(R.id.tv31));
        score10.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score10;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score10);
        final HoleScoreData score11 = new HoleScoreData(findViewById(R.id.tv32));
        score11.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score11;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score11);
        final HoleScoreData score12 = new HoleScoreData(findViewById(R.id.tv33));
        score12.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score12;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score12);
        final HoleScoreData score13 = new HoleScoreData(findViewById(R.id.tv34));
        score13.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score13;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score13);
        final HoleScoreData score14 = new HoleScoreData(findViewById(R.id.tv35));
        score14.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score14;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score14);
        final HoleScoreData score15 = new HoleScoreData(findViewById(R.id.tv36));
        score15.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score15;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score15);
        final HoleScoreData score16 = new HoleScoreData(findViewById(R.id.tv37));
        score16.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score16;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score16);
        final HoleScoreData score17 = new HoleScoreData(findViewById(R.id.tv38));
        score17.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score17;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score17);
        final HoleScoreData score18 = new HoleScoreData(findViewById(R.id.tv39));
        score18.getHole().setOnClickListener(v -> {
            markScore();
            currentHole = score18;
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            setIndividualTextBox();
        });
        scores.add(score18);
        scores.add(new HoleScoreData(findViewById(R.id.tv40)));

        return scores;
    }
    public List<Integer> initializePars() {
        /*
         * Assigns a par to each hole.
         */
        List<Integer> pars = new ArrayList<>();
        pars.add(5);
        pars.add(3);
        pars.add(4);
        pars.add(4);
        pars.add(4);
        pars.add(3);
        pars.add(5);
        pars.add(4);
        pars.add(4);
        pars.add(5);
        pars.add(4);
        pars.add(4);
        pars.add(5);
        pars.add(3);
        pars.add(4);
        pars.add(4);
        pars.add(3);
        pars.add(4);

        return pars;
    }

    public void loadScores(RealmScoreEntry myEntry) {
        /*
         * Parses the round data into each individual hole
         * and makes the text box appropriately.  Also adds
         * up all the totals for the round.
         */

        Integer mStroke;
        Integer mPutt;
        Integer mSand;
        Integer mFairway;
        Integer mGir;
        Integer mPenalty;
        int afterNine = 0, par;

        RealmList<Integer> strokes = myEntry.getStrokes();
        RealmList<Integer> putts = myEntry.getPutts();
        RealmList<Integer> sand = myEntry.getSand();
        RealmList<Integer> fairway = myEntry.getFairway();
        RealmList<Integer> gir = myEntry.getGreenInRegulation();
        RealmList<Integer> penalties = myEntry.getPenalties();
        List<Integer> pars = initializePars();
        HoleScoreData mScore;


        for (int i = 0; i < 9; i++) {
            mStroke = strokes.get(i);
            mPutt = putts.get(i);
            mSand = sand.get(i);
            mFairway = fairway.get(i);
            mGir = gir.get(i);
            mPenalty = penalties.get(i);


            mScore = scores.get(i);
            mScore.setPutts(mPutt);
            mScore.setSand(mSand);
            mScore.setStrokes(mStroke);
//            mScore.setFairway(mFairway);
//            mScore.setGreenInRegulation(mGir);
            mScore.setPenalties(mPenalty);
            mScore.setPar(pars.get(i));
            mScore.getHole().setText(Integer.toString(mStroke));
            markScoreSpecific(mScore);

            if (mStroke > 0) {
                par = pars.get(i);
                holesPlayed++;
                if (par > 3) {
                    fairwaysPlayed++;
                }
            }

            puttsTotal += mPutt;
            sandTotal += mSand;
            fairwayTotal += mFairway;
            girTotal += mGir;
            penaltiesTotal += mPenalty;
            afterNine += mStroke;
        }

        mScore = scores.get(9);
        mScore.getHole().setText(Integer.toString(afterNine));
        afterNine = 0;

        for (int i = 9; i < 18; i++) {
            mStroke = strokes.get(i);
            mPutt = putts.get(i);
            mSand = sand.get(i);
            mFairway = fairway.get(i);
            mGir = gir.get(i);
            mPenalty = penalties.get(i);

            mScore = scores.get(i + 1);
            mScore.setPutts(mPutt);
            mScore.setSand(mSand);
            mScore.setStrokes(mStroke);
//            mScore.setFairway(mFairway);
//            mScore.setGreenInRegulation(mGir);
            mScore.setPenalties(mPenalty);
            mScore.setPar(pars.get(i));
            mScore.getHole().setText(Integer.toString(mStroke));
            markScoreSpecific(mScore);

            if (mStroke > 0) {
                par = pars.get(i);
                holesPlayed++;
                if (par > 3) {
                    fairwaysPlayed++;
                }
            }

            puttsTotal += mPutt;
            sandTotal += mSand;
            fairwayTotal += mFairway;
            girTotal += mGir;
            penaltiesTotal += mPenalty;
            afterNine += mStroke;
        }

        mScore = scores.get(19);
        mScore.getHole().setText(Integer.toString(afterNine));
    }

    /**
     * This function is responsible for altering the look of the score
     * in the hole text box. Double circle for eagle or better, single
     * circle for birdie, nothing for par, single square for bogey,
     * and double square for double bogey or worse.
     */
    public void markScore(){

        if (currentHole.getStrokes() == 0 || currentHole.getStrokes() == currentHole.getPar())
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeback));
        else if (currentHole.getStrokes() <= currentHole.getPar() - 2)
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.eagle));
        else if (currentHole.getStrokes() == currentHole.getPar() - 1)
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.birdie));
        else if (currentHole.getStrokes() == currentHole.getPar() + 1)
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.bogey));
        else if (currentHole.getStrokes() >= currentHole.getPar() + 2)
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.doublebogey));
    }

    /**
     * Same as MarkScore, but for a specific hole.
     */
    public void markScoreSpecific(HoleScoreData specificHole) {

        if (specificHole.getStrokes() == 0 || specificHole.getStrokes() == specificHole.getPar())
            specificHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeback));
        else if (specificHole.getStrokes() <= specificHole.getPar() - 2)
            specificHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.eagle));
        else if (specificHole.getStrokes() == specificHole.getPar() - 1)
            specificHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.birdie));
        else if (specificHole.getStrokes() == specificHole.getPar() + 1)
            specificHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.bogey));
        else if (specificHole.getStrokes() >= specificHole.getPar() + 2)
            specificHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.doublebogey));
    }

    /**
     * Sets the main text box with the main
     * statistics related to this particular round.
     */
    public void setOverallTextBox() {

        String overallInfo = "";
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        overallInfo += " Final Score: " + finalScore + "\n";
        overallInfo += " Net Score: ";

        if (netScore > 0) {
            overallInfo += "+";
        }
        overallInfo += netScore + "\n\n";
        overallInfo += " Total putts: " + puttsTotal + "\n";
        overallInfo += " Putts Per Hole: " + dF.format((float)puttsTotal / holesPlayed) + "\n\n";

        float fairwayPercentage = ((float)fairwayTotal / fairwaysPlayed) * 100;
        float girPercentage = ((float)girTotal / holesPlayed) * 100;

        overallInfo += " Fairways hit: " + dF.format(fairwayPercentage) + "%\n";
        overallInfo += " Greens hit: " + dF.format(girPercentage) + "%\n";

        overallInfo += " Penalty Strokes: " + dF.format(penaltiesTotal);

        overallStats.setText(overallInfo);
    }

    /**
     * Sets up the text box details for each individual hole.
     */
    public void setIndividualTextBox() {

        String individualInfo = "";

        individualInfo += " Par: " + currentHole.getPar() + "\n";
        individualInfo += " Score: " + currentHole.getStrokes() + "\n";
        individualInfo += " Putts: " + currentHole.getPutts() + "\n";

        if (currentHole.getPenalties() > 0) {
            individualInfo += " Penalties: " + currentHole.getPenalties() + "\n\n";
        }
        else {
            individualInfo += "\n";
        }

//        if (currentHole.getFairway() == 1) {
//            individualInfo += " " + getString(R.string.hit_fairway);
//        }
//        else if (currentHole.getPar() != 3){
//            individualInfo += " " + getString(R.string.miss_fairway);
//        }
//        if (currentHole.getGreenInRegulation() == 1) {
//            individualInfo += " " + getString(R.string.hit_green);
//        }
//        else {
//            individualInfo += " " + getString(R.string.miss_green);
//        }

        holeStats.setText(individualInfo);
        vibrateOnClick();
    }

    /**
     * Sends a quick vibration to indicate to the user that they
     * clicked something.
     */
    private void vibrateOnClick() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(VIBRATE_DURATION);
    }
}
