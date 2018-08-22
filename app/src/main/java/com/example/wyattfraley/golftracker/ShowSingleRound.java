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

import java.util.ArrayList;
import java.util.List;

public class ShowSingleRound  extends AppCompatActivity{
    TextView overallStats;
    TextView holeStats;
    List<Score> scores;
    Score currentHole;
    int puttsTotal;
    int sandTotal;
    String finalScore;
    String uid;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_show_single_round);
        Intent myIntent = getIntent();
        String strokes = myIntent.getStringExtra("strokes");
        String putts = myIntent.getStringExtra("putts");
        String sand = myIntent.getStringExtra("sand");
        finalScore = myIntent.getStringExtra("finalScore");
        uid = myIntent.getStringExtra("Id");
        puttsTotal = 0;
        sandTotal = 0;

        overallStats = findViewById(R.id.overallStats);
        overallStats.setBackgroundColor(Color.WHITE);

        holeStats = findViewById(R.id.holeStats);
        holeStats.setBackgroundColor(Color.WHITE);

        scores = InitializeScores();
        LoadScores(strokes, putts, sand);
        currentHole = scores.get(0);

        SetOverallTextBox();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_menu) {
            Intent myIntent = getIntent();
            String strokes = myIntent.getStringExtra("strokes");
            String putts = myIntent.getStringExtra("putts");
            String sand = myIntent.getStringExtra("sand");


            Intent newIntent = new Intent(ShowSingleRound.this, DeleteRound.class);
            newIntent.putExtra("strokes", strokes);
            newIntent.putExtra("putts", putts);
            newIntent.putExtra("sand", sand);
            newIntent.putExtra("Id", uid);
            newIntent.putExtra("finalScore", finalScore);
            startActivityForResult(newIntent, 99);
        }



        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 99) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, null);
                this.finish();
            }
        }
    }

    public List<Score> InitializeScores() {
        scores = new ArrayList<>();
        final Score score1 = new Score((TextView)findViewById(R.id.tv11));
        score1.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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
                MarkScore(v);
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

    public void LoadScores(String strokes, String putts, String sand) {
        // First we have to parse the strings into 18 groups.
        int i = 0;
        int i2 = 0;
        int j = 0;
        int j2 = 0;
        int k = 0;
        int k2 = 0;
        int ninth = 0;
        int eighteenth = 0;

        String mStroke = new String();
        String mPutt = new String();
        String mSand = new String();
        List<Integer> pars = InitializePars();
        Score mScore;


        for (int l = 0; l < 9; l++) {
            i2 = strokes.indexOf("\n", i);
            mStroke = strokes.substring(i, i2);
            i = i2 + 1;

            j2 = putts.indexOf("\n", j);
            mPutt = putts.substring(j, j2);
            j = j2 + 1;

            k2 = sand.indexOf("\n", k);
            mSand = sand.substring(k, k2);
            k = k2 + 1;

            mScore = scores.get(l);
            mScore.setPutts(Integer.parseInt(mPutt));
            mScore.setSand(Integer.parseInt(mSand));
            mScore.setStrokes(Integer.parseInt(mStroke));
            mScore.setPar(pars.get(l));
            mScore.hole.setText(mStroke);
            MarkScoreSpecific(mScore);

            puttsTotal += Integer.parseInt(mPutt);
            sandTotal += Integer.parseInt(mSand);
            ninth += Integer.parseInt(mStroke);
        }

        mScore = scores.get(9);
        mScore.hole.setText(Integer.toString(ninth));

        for (int l = 10; l < 19; l++) {
            i2 = strokes.indexOf("\n", i);
            mStroke = strokes.substring(i, i2);
            i = i2 + 1;

            j2 = putts.indexOf("\n", j);
            mPutt = putts.substring(j, j2);
            j = j2 + 1;

            k2 = sand.indexOf("\n", k);
            mSand = sand.substring(k, k2);
            k = k2 + 1;

            mScore = scores.get(l);
            mScore.setPutts(Integer.parseInt(mPutt));
            mScore.setSand(Integer.parseInt(mSand));
            mScore.setStrokes(Integer.parseInt(mStroke));
            mScore.setPar(pars.get(l - 1));
            mScore.hole.setText(mStroke);
            MarkScoreSpecific(mScore);

            eighteenth += Integer.parseInt(mStroke);
        }

        mScore = scores.get(19);
        mScore.hole.setText(Integer.toString(eighteenth));
    }

    public void MarkScore(View v){
        // This function is responsible for altering the look of the score
        // in the hole textbox. Double circle for eagle or better, single
        // circle for birdie, nothing for par, single square for bogey,
        // and double square for double bogey or worse.

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
        // This function is responsible for altering the look of the score
        // in the hole textbox. Double circle for eagle or better, single
        // circle for birdie, nothing for par, single square for bogey,
        // and double square for double bogey or worse.

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
    public void SetScoreData(Score currentHole, String mPutt, String mSand, String mStroke, List<Integer> pars, Integer l) {
        currentHole.setPutts(Integer.parseInt(mPutt));
        currentHole.setSand(Integer.parseInt(mSand));
        currentHole.setPar(pars.get(l - 1));
        currentHole.hole.setText(mStroke);
        MarkScoreSpecific(currentHole);
    }

    public void SetOverallTextBox() {
        String overallInfo = new String();

        overallInfo += "Final Score: " + finalScore + "\n";
        overallInfo += "Net Score: " + Integer.toString(Integer.parseInt(finalScore) - 72) + "\n\n";
        overallInfo += "Total putts: " + puttsTotal + "\n";
        overallInfo += "putts Per Hole: " + Float.toString((float)puttsTotal / 18) + "\n";

        overallStats.setText(overallInfo);
    }
    public void SetIndividualTextBox() {
        String individualInfo = new String();

        individualInfo += "Par: " + currentHole.par + "\n";
        individualInfo += "Score: " + currentHole.strokes + "\n";
        individualInfo += "Putts: " + currentHole.putts;

        holeStats.setText(individualInfo);
    }

}
