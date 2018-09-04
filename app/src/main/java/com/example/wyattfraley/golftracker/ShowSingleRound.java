package com.example.wyattfraley.golftracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShowSingleRound  extends AppCompatActivity{
    TextView overallStats;
    TextView holeStats;
    List<Score> scores;
    Score currentHole;
    ScoreEntry myEntry;
    int puttsTotal;
    int sandTotal;
    int fairwayTotal;
    int girTotal;
    int finalScore;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_show_single_round);
        Intent myIntent = getIntent();
        myEntry = (ScoreEntry)myIntent.getSerializableExtra("Score");

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

        SetOverallTextBox();
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
            newIntent.putExtra("Score", myEntry);
            startActivityForResult(newIntent, 99);
        }
        else if (id == android.R.id.home) {
            finish();
        }

        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         * Checks to see if the round was deleted, and quits out if it was.
         */
        if (requestCode == 99) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, null);
                this.finish();
            }
        }
    }

    public List<Score> InitializeScores() {
        /*
         * Sets on click listeners for all the text boxes, so the
         * stats boxes will display the proper info for the
         * selected hole.
         */
        scores = new ArrayList<>();
        final Score score1 = new Score((TextView)findViewById(R.id.tv11));
        score1.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score1;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score1);
        final Score score2 = new Score((TextView)findViewById(R.id.tv12));
        score2.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score2;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score2);
        final Score score3 = new Score((TextView)findViewById(R.id.tv13));
        score3.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score3;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score3);
        final Score score4 = new Score((TextView)findViewById(R.id.tv14));
        score4.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score4;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score4);
        final Score score5 = new Score((TextView)findViewById(R.id.tv15));
        score5.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score5;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score5);
        final Score score6 = new Score((TextView)findViewById(R.id.tv16));
        score6.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score6;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score6);
        final Score score7 = new Score((TextView)findViewById(R.id.tv17));
        score7.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score7;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score7);
        final Score score8 = new Score((TextView)findViewById(R.id.tv18));
        score8.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score8;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score8);
        final Score score9 = new Score((TextView)findViewById(R.id.tv19));
        score9.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score9;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score9);
        scores.add(new Score((TextView)findViewById(R.id.tv20)));
        final Score score10 = new Score((TextView)findViewById(R.id.tv31));
        score10.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score10;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score10);
        final Score score11 = new Score((TextView)findViewById(R.id.tv32));
        score11.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score11;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score11);
        final Score score12 = new Score((TextView)findViewById(R.id.tv33));
        score12.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score12;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score12);
        final Score score13 = new Score((TextView)findViewById(R.id.tv34));
        score13.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score13;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score13);
        final Score score14 = new Score((TextView)findViewById(R.id.tv35));
        score14.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score14;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score14);
        final Score score15 = new Score((TextView)findViewById(R.id.tv36));
        score15.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score15;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score15);
        final Score score16 = new Score((TextView)findViewById(R.id.tv37));
        score16.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score16;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score16);
        final Score score17 = new Score((TextView)findViewById(R.id.tv38));
        score17.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score17;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score17);
        final Score score18 = new Score((TextView)findViewById(R.id.tv39));
        score18.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score18;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                SetIndividualTextBox();
            }
        });
        scores.add(score18);
        scores.add(new Score((TextView)findViewById(R.id.tv40)));

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

    public void LoadScores(ScoreEntry myEntry) {
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
        int afterNine = 0;

        ArrayList<Integer> strokes = myEntry.getStrokes();
        ArrayList<Integer> putts = myEntry.getPutts();
        ArrayList<Integer> sand = myEntry.getSand();
        ArrayList<Integer> fairway = myEntry.getFairway();
        ArrayList<Integer> gir = myEntry.getGreenInRegulation();
        Integer finalScore = myEntry.getFinalScore();
        List<Integer> pars = InitializePars();
        Score mScore;


        for (int i = 0; i < 9; i++) {
            mStroke = strokes.get(i);
            mPutt = putts.get(i);
            mSand = sand.get(i);
            mFairway = fairway.get(i);
            mGir = gir.get(i);


            mScore = scores.get(i);
            mScore.setPutts(mPutt);
            mScore.setSand(mSand);
            mScore.setStrokes(mStroke);
            mScore.setFairway(mFairway);
            mScore.setGreenInRegulation(mGir);
            mScore.setPar(pars.get(i));
            mScore.hole.setText(mStroke);
            MarkScoreSpecific(mScore);

            puttsTotal += mPutt;
            sandTotal += mSand;
            fairwayTotal += mFairway;
            girTotal += mGir;
            afterNine += mStroke;
        }

        mScore = scores.get(9);
        mScore.hole.setText(Integer.toString(afterNine));

        for (int i = 9; i < 18; i++) {
            mStroke = strokes.get(i);
            mPutt = putts.get(i);
            mSand = sand.get(i);
            mFairway = fairway.get(i);
            mGir = gir.get(i);

            mScore = scores.get(i + 1);
            mScore.setPutts(mPutt);
            mScore.setSand(mSand);
            mScore.setStrokes(mStroke);
            mScore.setFairway(mFairway);
            mScore.setGreenInRegulation(mGir);
            mScore.setPar(pars.get(i));
            mScore.hole.setText(mStroke);
            MarkScoreSpecific(mScore);

            puttsTotal += mPutt;
            sandTotal += mSand;
            fairwayTotal += mFairway;
            girTotal += mGir;
        }

        mScore = scores.get(19);
        mScore.hole.setText(Integer.toString(finalScore));
    }

    public void MarkScore(){
        /*
         * This function is responsible for altering the look of the score
         * in the hole text box. Double circle for eagle or better, single
         * circle for birdie, nothing for par, single square for bogey,
         * and double square for double bogey or worse.
         */

        if (currentHole.strokes == 0 || currentHole.strokes == currentHole.par)
            currentHole.hole.setBackground(getDrawable(R.drawable.holeback));
        else if (currentHole.strokes <= currentHole.par - 2)
            currentHole.hole.setBackground(getDrawable(R.drawable.eagle));
        else if (currentHole.strokes == currentHole.par - 1)
            currentHole.hole.setBackground(getDrawable(R.drawable.birdie));
        else if (currentHole.strokes == currentHole.par + 1)
            currentHole.hole.setBackground(getDrawable(R.drawable.bogey));
        else if (currentHole.strokes >= currentHole.par + 2)
            currentHole.hole.setBackground(getDrawable(R.drawable.doublebogey));

    }
    public void MarkScoreSpecific(Score specificHole) {
        /*
         * Same as MarkScore, but for a specific hole.
         */

        if (specificHole.strokes == 0 || specificHole.strokes == specificHole.par)
            specificHole.hole.setBackground(getDrawable(R.drawable.holeback));
        else if (specificHole.strokes <= specificHole.par - 2)
            specificHole.hole.setBackground(getDrawable(R.drawable.eagle));
        else if (specificHole.strokes == specificHole.par - 1)
            specificHole.hole.setBackground(getDrawable(R.drawable.birdie));
        else if (specificHole.strokes == specificHole.par + 1)
            specificHole.hole.setBackground(getDrawable(R.drawable.bogey));
        else if (specificHole.strokes >= specificHole.par + 2)
            specificHole.hole.setBackground(getDrawable(R.drawable.doublebogey));
    }

    public void SetOverallTextBox() {
        /*
         * Sets the main text box with the main
         * statistics related to this particular round.
         */
        String overallInfo = new String();
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        overallInfo += " Final Score: " + finalScore + "\n";
        overallInfo += " Net Score: ";
        int score = finalScore - 72;
        if (score > 0) {
            overallInfo += "+";
        }
        overallInfo += Integer.toString(score) + "\n\n";
        overallInfo += " Total putts: " + puttsTotal + "\n";
        overallInfo += " Putts Per Hole: " + dF.format((float)puttsTotal / 18) + "\n\n";

        float fairwayPercentage = ((float)fairwayTotal / 14) * 100;
        float girPercentage = ((float)girTotal / 18) * 100;

        overallInfo += " Fairways hit: " + dF.format(fairwayPercentage) + "%\n";
        overallInfo += " Greens hit: " + dF.format(girPercentage) + "%";

        overallStats.setText(overallInfo);
    }
    public void SetIndividualTextBox() {
        /*
         * Sets up the text box details for each individual hole.
         */
        String individualInfo = new String();

        individualInfo += " Par: " + currentHole.par + "\n";
        individualInfo += " Score: " + currentHole.strokes + "\n";
        individualInfo += " Putts: " + currentHole.putts + "\n\n";

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
    }
}
