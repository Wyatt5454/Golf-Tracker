package com.example.wyattfraley.golftracker;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class DeleteRound extends SaveCheck {
    Button yes;
    Button no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.delete_window);

        DisplayMetrics dM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dM);

        int width = dM.widthPixels;
        int height = dM.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .3));

        yes = findViewById(R.id.DeleteYes);
        no = findViewById(R.id.DeleteNo);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoPress();
            }
        });
    }
    public void Delete() {
        /*
         * Deletes the round from the database, and then updates the total
         * stats using an inherited function from SaveCheck.
         */
        Intent myIntent = getIntent();

        final GolfDatabase Db = Room.databaseBuilder(getApplicationContext(), GolfDatabase.class, "score-db-V4").fallbackToDestructiveMigration().build();

        final ScoreEntry toDelete = (ScoreEntry)myIntent.getSerializableExtra("Score");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Db.myScoreEntryDao().delete(toDelete);
                return null;
            }
        }.execute();

        // Updates the total stats, but only if it was a complete round.
        if (IsComplete()) {
            UpdateTotals(toDelete);
        }


        setResult(RESULT_OK, null);
        finish();
    }
    @Override
    public void LoadScores(TotalRoundStats stats, ScoreEntry myEntry) {
        /*
         * This function parses the data strings that were sent here from the ScorecardActivity.
         * Then it loads the new stats into the TotalRoundStats object before it is saved.
         */

        int totalPutts = 0;
        int totalSand = 0;
        int totalFairway = 0;
        int totalGir = 0;

        Integer mStroke;
        Integer mPutt;
        Integer mSand;
        Integer mFairway;
        Integer mGir;
        Integer scoreFront = 0;
        Integer scoreBack = 0;
        TotalHoleStats hole;

        ArrayList<Integer> strokes = myEntry.getStrokes();
        ArrayList<Integer> putts = myEntry.getPutts();
        ArrayList<Integer> sand = myEntry.getSand();
        ArrayList<Integer> fairway = myEntry.getFairway();
        ArrayList<Integer> gir = myEntry.getGreenInRegulation();

        if (stats.holes.size() == 0) {
            for (int z = 0; z < 18; z++) {
                TotalHoleStats nHole = new TotalHoleStats();
                stats.holes.add(nHole);
            }
        }

        // This loop parses the data into 18 holes and updates the stats.
        for (int i = 0; i < 9; i++) {
            mStroke = strokes.get(i);
            mPutt = putts.get(i);
            mSand = sand.get(i);
            mFairway = fairway.get(i);
            mGir = gir.get(i);

            hole = stats.holes.get(i);
            hole.DeleteStats(mStroke, mPutt, mSand, mFairway, mGir);

            scoreFront += mStroke;
            totalPutts += mPutt;
            totalSand += mSand;
            totalFairway += mFairway;
            totalGir += mGir;
        }
        for (int i = 9; i < 18; i++) {
            mStroke = strokes.get(i);
            mPutt = putts.get(i);
            mSand = sand.get(i);
            mFairway = fairway.get(i);
            mGir = gir.get(i);

            hole = stats.holes.get(i);
            hole.DeleteStats(mStroke, mPutt, mSand, mFairway, mGir);

            scoreBack += mStroke;
            totalPutts += mPutt;
            totalSand += mSand;
            totalFairway += mFairway;
            totalGir += mGir;
        }

        stats.DeleteTotals(scoreFront, scoreBack, totalPutts, totalSand, totalFairway, totalGir);
    }
}
