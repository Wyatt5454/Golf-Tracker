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
    Button Yes;
    Button No;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.delete_window);

        DisplayMetrics Dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Dm);

        int width = Dm.widthPixels;
        int height = Dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .3));

        Yes = findViewById(R.id.DeleteYes);
        No = findViewById(R.id.DeleteNo);
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete();
            }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoPress();
            }
        });
    }
    public void Delete() {
        Intent MyIntent = getIntent();
        String uid = MyIntent.getStringExtra("Id");
        String strokes = MyIntent.getStringExtra("Strokes");
        String putts = MyIntent.getStringExtra("Putts");
        String sand = MyIntent.getStringExtra("Sand");
        String Final = MyIntent.getStringExtra("Final");

        final GolfDatabase Db = Room.databaseBuilder(getApplicationContext(), GolfDatabase.class, "score-db-V2").build();

        final ScoreEntry toDelete = new ScoreEntry();
        toDelete.setUid(uid);
        toDelete.setStrokes(strokes);
        toDelete.setPutts(putts);
        toDelete.setSand(sand);
        toDelete.setFinal(Final);


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Db.MyScoreEntryDao().delete(toDelete);
                return null;
            }
        }.execute();

        UpdateTotals(strokes, putts, sand, Final);

        setResult(RESULT_OK, null);
        finish();
    }
    @Override
    public void LoadScores(TotalRoundStats stats, String strokes, String putts, String sand, String finalScore) {
        // First we have to parse the strings into 18 groups.
        int i = 0;
        int i2 = 0;
        int j = 0;
        int j2 = 0;
        int k = 0;
        int k2 = 0;
        int totalPutts = 0;
        int totalSand = 0;

        String mStroke = new String();
        String mPutt = new String();
        String mSand = new String();
        TotalHoleStats hole;

        if (stats.holes.size() == 0) {
            for (int z = 0; z < 18; z++) {
                TotalHoleStats nHole = new TotalHoleStats();
                stats.holes.add(nHole);
            }
        }


        for (int l = 0; l < 18; l++) {
            i2 = strokes.indexOf("\n", i);
            mStroke = strokes.substring(i, i2);
            i = i2 + 1;

            j2 = putts.indexOf("\n", j);
            mPutt = putts.substring(j, j2);
            j = j2 + 1;

            k2 = sand.indexOf("\n", k);
            mSand = sand.substring(k, k2);
            k = k2 + 1;

            hole = stats.holes.get(l);
            hole.DeleteStats(Integer.parseInt(mStroke), Integer.parseInt(mPutt), Integer.parseInt(mSand));

            totalPutts += Integer.parseInt(mPutt);
            totalSand += Integer.parseInt(mSand);
        }

        int finalS = Integer.parseInt(finalScore);
        stats.DeleteTotals(finalS, totalPutts, totalSand);
    }
}
