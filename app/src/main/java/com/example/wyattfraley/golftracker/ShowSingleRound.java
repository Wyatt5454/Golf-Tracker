package com.example.wyattfraley.golftracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowSingleRound  extends AppCompatActivity{
    TextView OverallStats;
    TextView HoleStats;
    List<Score> Scores;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_show_single_round);
        Intent myIntent = getIntent();
        String strokes = myIntent.getStringExtra("Strokes");
        String putts = myIntent.getStringExtra("Putts");
        String sand = myIntent.getStringExtra("Sand");

        OverallStats = findViewById(R.id.OverallStats);
        OverallStats.setBackgroundColor(Color.WHITE);

        HoleStats = findViewById(R.id.HoleStats);
        HoleStats.setBackgroundColor(Color.WHITE);

        Scores = InitializeScores();
        LoadScores(strokes, putts, sand);
    }
    public List<Score> InitializeScores() {
        Scores = new ArrayList<>();
        Scores.add(new Score((TextView)findViewById(R.id.tv11)));
        Scores.add(new Score((TextView)findViewById(R.id.tv12)));
        Scores.add(new Score((TextView)findViewById(R.id.tv13)));
        Scores.add(new Score((TextView)findViewById(R.id.tv14)));
        Scores.add(new Score((TextView)findViewById(R.id.tv15)));
        Scores.add(new Score((TextView)findViewById(R.id.tv16)));
        Scores.add(new Score((TextView)findViewById(R.id.tv17)));
        Scores.add(new Score((TextView)findViewById(R.id.tv18)));
        Scores.add(new Score((TextView)findViewById(R.id.tv19)));
        Scores.add(new Score((TextView)findViewById(R.id.tv20)));
        Scores.add(new Score((TextView)findViewById(R.id.tv31)));
        Scores.add(new Score((TextView)findViewById(R.id.tv32)));
        Scores.add(new Score((TextView)findViewById(R.id.tv33)));
        Scores.add(new Score((TextView)findViewById(R.id.tv34)));
        Scores.add(new Score((TextView)findViewById(R.id.tv35)));
        Scores.add(new Score((TextView)findViewById(R.id.tv36)));
        Scores.add(new Score((TextView)findViewById(R.id.tv37)));
        Scores.add(new Score((TextView)findViewById(R.id.tv38)));
        Scores.add(new Score((TextView)findViewById(R.id.tv39)));
        Scores.add(new Score((TextView)findViewById(R.id.tv40)));

        return Scores;
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

            mScore = Scores.get(l);
            mScore.setPutts(Integer.parseInt(mPutt));
            mScore.setSand(Integer.parseInt(mSand));
            mScore.setPar(pars.get(l));
            mScore.Hole.setText(mStroke);
            MarkScore(mScore);

            ninth += Integer.parseInt(mStroke);
        }

        mScore = Scores.get(9);
        mScore.Hole.setText(Integer.toString(ninth));

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

            mScore = Scores.get(l);
            mScore.setPutts(Integer.parseInt(mPutt));
            mScore.setSand(Integer.parseInt(mSand));
            mScore.setPar(pars.get(l - 1));
            mScore.Hole.setText(mStroke);
            MarkScore(mScore);

            eighteenth += Integer.parseInt(mStroke);
        }

        mScore = Scores.get(19);
        mScore.Hole.setText(Integer.toString(eighteenth));
    }

    public void MarkScore(Score currentHole){
        // This function is responsible for altering the look of the score
        // in the hole textbox. Double circle for eagle or better, single
        // circle for birdie, nothing for par, single square for bogey,
        // and double square for double bogey or worse.

        if (currentHole.Strokes == 0 || currentHole.Strokes == currentHole.Par)
            currentHole.Hole.setBackground(getDrawable(R.drawable.holeback));
        else if (currentHole.Strokes <= currentHole.Par - 2)
            currentHole.Hole.setBackground(getDrawable(R.drawable.eagle));
        else if (currentHole.Strokes == currentHole.Par - 1)
            currentHole.Hole.setBackground(getDrawable(R.drawable.birdie));
        else if (currentHole.Strokes == currentHole.Par + 1)
            currentHole.Hole.setBackground(getDrawable(R.drawable.bogey));
        else if (currentHole.Strokes >= currentHole.Par + 2)
            currentHole.Hole.setBackground(getDrawable(R.drawable.doublebogey));

    }
    public void SetScoreData(Score currentHole, String mPutt, String mSand, String mStroke, List<Integer> pars, Integer l) {
        currentHole.setPutts(Integer.parseInt(mPutt));
        currentHole.setSand(Integer.parseInt(mSand));
        currentHole.setPar(pars.get(l - 1));
        currentHole.Hole.setText(mStroke);
        MarkScore(currentHole);
    }

}
