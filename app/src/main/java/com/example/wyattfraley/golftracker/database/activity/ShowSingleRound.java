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
import com.example.wyattfraley.golftracker.scorecard.Score;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class ShowSingleRound  extends Activity {
    TextView overallStats;
    TextView holeStats;
    List<Score> scores;
    Score currentHole;
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
                InitializeUI();
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

    private void InitializeUI() {
        puttsTotal = 0;
        sandTotal = 0;
        fairwayTotal = 0;
        girTotal = 0;

        overallStats = findViewById(R.id.overallStats);
        holeStats = findViewById(R.id.holeStats);

        scores = InitializeScores();
        LoadScores(myEntry);
        currentHole = scores.get(0);
        finalScore = myEntry.getFinalScore();
        netScore = finalScore - myEntry.getParPlayed();

        SetOverallTextBox();
    }

    /**
     * Sets on click listeners for all the text boxes, so the
     * stats boxes will display the proper info for the
     * selected hole.
     */
    public List<Score> InitializeScores() {

        scores = new ArrayList<>();
        final Score score1 = new Score(findViewById(R.id.tv11));
        score1.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score1;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score1);
        final Score score2 = new Score(findViewById(R.id.tv12));
        score2.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score2;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score2);
        final Score score3 = new Score(findViewById(R.id.tv13));
        score3.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score3;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score3);
        final Score score4 = new Score(findViewById(R.id.tv14));
        score4.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score4;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score4);
        final Score score5 = new Score(findViewById(R.id.tv15));
        score5.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score5;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score5);
        final Score score6 = new Score(findViewById(R.id.tv16));
        score6.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score6;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score6);
        final Score score7 = new Score(findViewById(R.id.tv17));
        score7.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score7;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score7);
        final Score score8 = new Score(findViewById(R.id.tv18));
        score8.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score8;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score8);
        final Score score9 = new Score(findViewById(R.id.tv19));
        score9.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score9;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score9);
        scores.add(new Score(findViewById(R.id.tv20)));
        final Score score10 = new Score(findViewById(R.id.tv31));
        score10.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score10;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score10);
        final Score score11 = new Score(findViewById(R.id.tv32));
        score11.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score11;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score11);
        final Score score12 = new Score(findViewById(R.id.tv33));
        score12.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score12;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score12);
        final Score score13 = new Score(findViewById(R.id.tv34));
        score13.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score13;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score13);
        final Score score14 = new Score(findViewById(R.id.tv35));
        score14.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score14;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score14);
        final Score score15 = new Score(findViewById(R.id.tv36));
        score15.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score15;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score15);
        final Score score16 = new Score(findViewById(R.id.tv37));
        score16.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score16;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score16);
        final Score score17 = new Score(findViewById(R.id.tv38));
        score17.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score17;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score17);
        final Score score18 = new Score(findViewById(R.id.tv39));
        score18.hole.setOnClickListener(v -> {
            MarkScore();
            currentHole = score18;
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetIndividualTextBox();
        });
        scores.add(score18);
        scores.add(new Score(findViewById(R.id.tv40)));

        return scores;
    }
    public List<Integer> InitializePars() {
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

    public void LoadScores(RealmScoreEntry myEntry) {
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
        List<Integer> pars = InitializePars();
        Score mScore;


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
            mScore.setFairway(mFairway);
            mScore.setGreenInRegulation(mGir);
            mScore.setPenalties(mPenalty);
            mScore.setPar(pars.get(i));
            mScore.hole.setText(Integer.toString(mStroke));
            MarkScoreSpecific(mScore);

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
        mScore.hole.setText(Integer.toString(afterNine));
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
            mScore.setFairway(mFairway);
            mScore.setGreenInRegulation(mGir);
            mScore.setPenalties(mPenalty);
            mScore.setPar(pars.get(i));
            mScore.hole.setText(Integer.toString(mStroke));
            MarkScoreSpecific(mScore);

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
        mScore.hole.setText(Integer.toString(afterNine));
    }

    /**
     * This function is responsible for altering the look of the score
     * in the hole text box. Double circle for eagle or better, single
     * circle for birdie, nothing for par, single square for bogey,
     * and double square for double bogey or worse.
     */
    public void MarkScore(){

        if (currentHole.strokes == 0 || currentHole.strokes == currentHole.par)
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeback));
        else if (currentHole.strokes <= currentHole.par - 2)
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.eagle));
        else if (currentHole.strokes == currentHole.par - 1)
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.birdie));
        else if (currentHole.strokes == currentHole.par + 1)
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.bogey));
        else if (currentHole.strokes >= currentHole.par + 2)
            currentHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.doublebogey));
    }

    /**
     * Same as MarkScore, but for a specific hole.
     */
    public void MarkScoreSpecific(Score specificHole) {

        if (specificHole.strokes == 0 || specificHole.strokes == specificHole.par)
            specificHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.holeback));
        else if (specificHole.strokes <= specificHole.par - 2)
            specificHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.eagle));
        else if (specificHole.strokes == specificHole.par - 1)
            specificHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.birdie));
        else if (specificHole.strokes == specificHole.par + 1)
            specificHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.bogey));
        else if (specificHole.strokes >= specificHole.par + 2)
            specificHole.hole.setBackground(AppCompatResources.getDrawable(this, R.drawable.doublebogey));
    }

    /**
     * Sets the main text box with the main
     * statistics related to this particular round.
     */
    public void SetOverallTextBox() {

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
    public void SetIndividualTextBox() {

        String individualInfo = "";

        individualInfo += " Par: " + currentHole.par + "\n";
        individualInfo += " Score: " + currentHole.strokes + "\n";
        individualInfo += " Putts: " + currentHole.putts + "\n";

        if (currentHole.penalties > 0) {
            individualInfo += " Penalties: " + currentHole.penalties + "\n\n";
        }
        else {
            individualInfo += "\n";
        }

        if (currentHole.getFairway() == 1) {
            individualInfo += " " + getString(R.string.hit_fairway);
        }
        else if (currentHole.par != 3){
            individualInfo += " " + getString(R.string.miss_fairway);
        }
        if (currentHole.getGreenInRegulation() == 1) {
            individualInfo += " " + getString(R.string.hit_green);
        }
        else {
            individualInfo += " " + getString(R.string.miss_green);
        }

        holeStats.setText(individualInfo);
        VibrateOnClick();
    }

    /**
     * Sends a quick vibration to indicate to the user that they
     * clicked something.
     */
    private void VibrateOnClick() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(VIBRATE_DURATION);
    }
}
