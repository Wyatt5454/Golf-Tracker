package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Calendar;

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
        Intent myIntent = getIntent();
        String uid = myIntent.getStringExtra("Id");
        String strokes = myIntent.getStringExtra("strokes");
        String putts = myIntent.getStringExtra("putts");
        String sand = myIntent.getStringExtra("sand");
        String fairway = myIntent.getStringExtra("fairway");
        String gir = myIntent.getStringExtra("gir");
        String finalScore = myIntent.getStringExtra("finalScore");

        final GolfDatabase Db = Room.databaseBuilder(getApplicationContext(), GolfDatabase.class, "score-db-V3").build();

        final ScoreEntry toDelete = new ScoreEntry();
        toDelete.setUId(uid);
        toDelete.setStrokes(strokes);
        toDelete.setPutts(putts);
        toDelete.setSand(sand);
        toDelete.setFairway(fairway);
        toDelete.setGreenInRegulation(gir);
        toDelete.setFinalScore(finalScore);


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Db.myScoreEntryDao().delete(toDelete);
                return null;
            }
        }.execute();

        UpdateTotals(strokes, putts, sand, fairway, gir, finalScore);

        setResult(RESULT_OK, null);
        finish();
    }
    @Override
    public void LoadScores(TotalRoundStats stats, String strokes, String putts, String sand, String fairway, String gir, String finalScore) {
        // First we have to parse the strings into 18 groups.
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
