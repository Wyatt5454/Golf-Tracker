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

        /*
         * TODO: Change the way rounds are saved, so they're saved as objects instead of
         * all the string bullshit.
         */


        Intent myIntent = getIntent();
        final ScoreEntry toEnter = (ScoreEntry)myIntent.getSerializableExtra("Score");

        final GolfDatabase Db = Room.databaseBuilder(getApplicationContext(), GolfDatabase.class, "score-db-V3").fallbackToDestructiveMigration().build();


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

        // This loop parses the data into 18 holes and updates the stats.
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
            hole.UpdateStats(Integer.parseInt(mStroke), Integer.parseInt(mPutt), Integer.parseInt(mSand),
                                                    Integer.parseInt(mFairway), Integer.parseInt(mGir));

            totalPutts += Integer.parseInt(mPutt);
            totalSand += Integer.parseInt(mSand);
            totalFairway += Integer.parseInt(mFairway);
            totalGir += Integer.parseInt(mGir);
        }

        int finalS = Integer.parseInt(finalScore);
        stats.UpdateTotals(finalS, totalPutts, totalSand, totalFairway, totalGir);
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
