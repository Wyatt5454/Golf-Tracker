package com.example.wyattfraley.golftracker;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

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

        final GolfDatabase Db = Room.databaseBuilder(getApplicationContext(), GolfDatabase.class, "score-db-V3").fallbackToDestructiveMigration().build();

        final ScoreEntry toDelete = (ScoreEntry)myIntent.getSerializableExtra("Score");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Db.myScoreEntryDao().delete(toDelete);
                return null;
            }
        }.execute();

        UpdateTotals(toDelete);

        setResult(RESULT_OK, null);
        finish();
    }
    @Override
    public void LoadScores(TotalRoundStats stats, ScoreEntry myEntry) {
        /*
         * This function is called by an inherited function from ScoreCheck.
         * Updates the total stats by deleting the stats from the totals.
         *
         * TODO: The string parsing code here is kinda messy.  Figure out a way to store them differently.
         */

        int i = 0, j = 0, k = 0, l = 0, m = 0;
        int i2, j2, k2, l2, m2;
        int totalPutts = 0;
        int totalSand = 0;
        int totalFairway = 0;
        int totalGir = 0;

        String mStroke;
        String mPutt;
        String mSand;
        String mFairway;
        String mGir;
        TotalHoleStats hole;

        String strokes = myEntry.getStrokes();
        String putts = myEntry.getPutts();
        String sand = myEntry.getSand();
        String fairway = myEntry.getFairway();
        String gir = myEntry.getGreenInRegulation();
        String finalScore = myEntry.getFinalScore();

        if (stats.holes.size() == 0) {
            for (int z = 0; z < 18; z++) {
                TotalHoleStats nHole = new TotalHoleStats();
                stats.holes.add(nHole);
            }
        }


        for (int n = 0; n < 18; n++) {
            i2 = strokes.indexOf("\n", i);
            mStroke = strokes.substring(i, i2);
            i = i2 + 1;

            j2 = putts.indexOf("\n", j);
            mPutt = putts.substring(j, j2);
            j = j2 + 1;

            k2 = sand.indexOf("\n", k);
            mSand = sand.substring(k, k2);
            k = k2 + 1;

            l2 = fairway.indexOf("\n", l);
            mFairway = fairway.substring(l, l2);
            l = l2 + 1;

            m2 = gir.indexOf("\n", m);
            mGir = gir.substring(m, m2);
            m = m2 + 1;

            hole = stats.holes.get(n);
            hole.DeleteStats(Integer.parseInt(mStroke), Integer.parseInt(mPutt), Integer.parseInt(mSand),
                                                    Integer.parseInt(mFairway), Integer.parseInt(mGir));

            totalPutts += Integer.parseInt(mPutt);
            totalSand += Integer.parseInt(mSand);
            totalFairway += Integer.parseInt(mFairway);
            totalGir += Integer.parseInt(mGir);
        }

        int finalS = Integer.parseInt(finalScore);
        stats.DeleteTotals(finalS, totalPutts, totalSand, totalFairway, totalGir);
    }
}
