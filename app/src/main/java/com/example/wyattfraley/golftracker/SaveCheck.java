package com.example.wyattfraley.golftracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class SaveCheck extends Activity {
    Button yes;
    Button no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.save_window);

        DisplayMetrics dM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dM);

        int width = dM.widthPixels;
        int height = dM.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .3));

        yes = findViewById(R.id.SaveYes);
        no = findViewById(R.id.SaveNo);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveRound();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoPress();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void SaveRound() {
        /*
         *Here we grab all the data from the ScorecardActivity, open the database,
         *and make a new entry.  Creates an AsyncTask so that the database
         *saving does not take place on the main thread to prevent UI locking.
         */

        Intent myIntent = getIntent();
        final ScoreEntry toEnter = (ScoreEntry)myIntent.getSerializableExtra("Score");

        final GolfDatabase Db = Room.databaseBuilder(getApplicationContext(), GolfDatabase.class, "score-db-V4").fallbackToDestructiveMigration().build();


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Db.myScoreEntryDao().insertAll(toEnter);
                return null;
            }
        }.execute();

        // Now that the round is saved, we must update the totals.
        UpdateTotals(toEnter);


        setResult(RESULT_OK, null);
        finish();
    }

    public void NoPress() {
        finish();
    }
    public void UpdateTotals(ScoreEntry myEntry) {
        /*
         *  This function will update the total scores on a file saved to
         *  internal storage.  Uses serializing to load in and save the
         *  stats object. Test
         */

        TotalRoundStats stats = new TotalRoundStats();
        FileInputStream inputStreamTotal = null;
        FileInputStream inputStreamHoles = null;
        boolean fileExists = false;

        // First we have to check if the file already exists.
        String[] filenames = fileList();
        for (String check : filenames) {
            if (check.equals("TotalStats.txt")) {
                fileExists = true;
                try {
                    inputStreamTotal = openFileInput("TotalStats.txt");
                    inputStreamHoles = openFileInput("HoleStats.txt");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        // Check to make sure the file was found.
        // If not, make the file.
        if (inputStreamTotal == null) {
            LoadScores(stats, myEntry);
        }
        else {
            try {
                // Reading object in a file
                ObjectInputStream inTotal = new ObjectInputStream(inputStreamTotal);
                ObjectInputStream inHoles = new ObjectInputStream(inputStreamHoles);

                // Method for deserialization of object
                stats = (TotalRoundStats)inTotal.readObject();
                stats.holes = (ArrayList<TotalHoleStats>)inHoles.readObject();
                LoadScores(stats, myEntry);

                inTotal.close();
                inHoles.close();
                inputStreamTotal.close();
                inputStreamHoles.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        SaveTotals(stats, fileExists);
    }
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
        TotalHoleStats hole;

        ArrayList<Integer> strokes = myEntry.getStrokes();
        ArrayList<Integer> putts = myEntry.getPutts();
        ArrayList<Integer> sand = myEntry.getSand();
        ArrayList<Integer> fairway = myEntry.getFairway();
        ArrayList<Integer> gir = myEntry.getGreenInRegulation();
        Integer finalScore = myEntry.getFinalScore();

        if (stats.holes.size() == 0) {
            for (int z = 0; z < 18; z++) {
                TotalHoleStats nHole = new TotalHoleStats();
                stats.holes.add(nHole);
            }
        }

        // This loop parses the data into 18 holes and updates the stats.
        for (int i = 0; i < 18; i++) {
            mStroke = strokes.get(i);
            mPutt = putts.get(i);
            mSand = sand.get(i);
            mFairway = fairway.get(i);
            mGir = gir.get(i);

            hole = stats.holes.get(i);
            hole.UpdateStats(mStroke, mPutt, mSand, mFairway, mGir);

            totalPutts += mPutt;
            totalSand += mSand;
            totalFairway += mFairway;
            totalGir += mGir;
        }

        stats.UpdateTotals(finalScore, totalPutts, totalSand, totalFairway, totalGir);
    }
    private void SaveTotals(TotalRoundStats stats, boolean fileExists) {
        /*
         * This function saves the newly updated stats object.
         * Saving is a little different depending on if the file already
         * exists or not. Uses serialization to save the object in a file
         * on internal storage.
         */

        FileOutputStream outputStreamTotal;
        FileOutputStream outputStreamHoles;
        if (!fileExists) {
            try {
                outputStreamTotal = openFileOutput("TotalStats.txt", Context.MODE_PRIVATE);
                outputStreamHoles = openFileOutput("HoleStats.txt", Context.MODE_PRIVATE);
                ObjectOutputStream outTotal = new ObjectOutputStream(outputStreamTotal);
                ObjectOutputStream outHoles = new ObjectOutputStream(outputStreamHoles);

                // Serializes the objects.
                outTotal.writeObject(stats);
                outHoles.writeObject(stats.holes);

                outTotal.close();
                outHoles.close();
                outputStreamTotal.close();
                outputStreamHoles.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            File directory = getFilesDir();
            File fileTotal = new File(directory, "TotalStats.txt");
            File fileHoles = new File(directory, "HoleStats.txt");
            try {
                outputStreamTotal = new FileOutputStream(fileTotal);
                outputStreamHoles = new FileOutputStream(fileHoles);
                ObjectOutputStream outTotal = new ObjectOutputStream(outputStreamTotal);
                ObjectOutputStream outHoles = new ObjectOutputStream(outputStreamHoles);

                outTotal.writeObject(stats);
                outHoles.writeObject(stats.holes);

                outTotal.close();
                outHoles.close();
                outputStreamTotal.close();
                outputStreamHoles.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
