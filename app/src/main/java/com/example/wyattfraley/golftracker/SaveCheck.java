package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

public class SaveCheck extends Activity {
    Button Yes;
    Button No;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.save_window);

        DisplayMetrics Dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Dm);

        int width = Dm.widthPixels;
        int height = Dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .3));

        Yes = findViewById(R.id.SaveYes);
        No = findViewById(R.id.SaveNo);
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveRound();
            }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoPress();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void SaveRound() {
        // Here we grab all the data from the ScorecardActivity, open the database,
        // and make a new entry.  Creates an AsyncTask so that the database
        // saving does not take place on the main thread to prevent UI locking.

        Intent MyIntent = getIntent();
        String Strokes = MyIntent.getStringExtra("Strokes");
        String Putts = MyIntent.getStringExtra("Putts");
        String Sand = MyIntent.getStringExtra("Sand");
        String Final = MyIntent.getStringExtra("Final");

        final GolfDatabase Db = Room.databaseBuilder(getApplicationContext(), GolfDatabase.class, "score-db-V2").build();

        final ScoreEntry ToEnter = new ScoreEntry();
        ToEnter.setUid(Calendar.getInstance().getTime().toString());
        ToEnter.setStrokes(Strokes);
        ToEnter.setPutts(Putts);
        ToEnter.setSand(Sand);
        ToEnter.setFinal(Final);


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Db.MyScoreEntryDao().insertAll(ToEnter);
                return null;
            }
        }.execute();

        // Now that the round is saved, we must update the totals.
        UpdateTotals(Strokes, Putts, Sand);


        finish();
    }

    public void NoPress() {
        finish();
    }
    public void UpdateTotals(String strokes, String putts, String sand) {
        // First load the file in.

        String saveName = "TotalStats.txt";
        TotalRoundStats stats = new TotalRoundStats();

        File file = new File( Environment.getExternalStorageDirectory() + "/Download/TotalStats.txt");

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        try {
            // Reading object in a file
            FileInputStream stream = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(stream);

            // Method for deserialization of object
            stats = (TotalRoundStats)in.readObject();

            in.close();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        stats.Save(this);
    }
}
