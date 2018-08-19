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
        /*
         *Here we grab all the data from the ScorecardActivity, open the database,
         *and make a new entry.  Creates an AsyncTask so that the database
         *saving does not take place on the main thread to prevent UI locking.
         */

        /*
         * TODO: Change the way rounds are saved, so they're saved as objects instead of
         * all the string bullshit.
         */


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
        UpdateTotals(Strokes, Putts, Sand, Final);


        setResult(RESULT_OK, null);
        finish();
    }

    public void NoPress() {
        finish();
    }
    public void UpdateTotals(String strokes, String putts, String sand, String finalScore) {
        /*
         *  This function will update the total scores on a file saved to
         *  internal storage.  Uses serializing to load in and save the
         *  stats object.
         */

        TotalRoundStats stats = new TotalRoundStats();
        FileInputStream inputStream = null;
        boolean fileExists = false;

        // First we have to check if the file already exists.
        String[] filenames = fileList();
        for (String check : filenames) {
            if (check.equals("TotalStats.txt")) {
                fileExists = true;
                try {
                    inputStream = openFileInput("TotalStats.txt");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        // Check to make sure the file was found.
        // If not, make the file.
        if (inputStream == null) {
            LoadScores(stats, strokes, putts, sand, finalScore);
        }
        else {
            try {
                // Reading object in a file
                ObjectInputStream in = new ObjectInputStream(inputStream);

                // Method for deserialization of object
                stats = (TotalRoundStats)in.readObject();
                LoadScores(stats, strokes, putts, sand, finalScore);

                in.close();
                inputStream.close();
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
    public void LoadScores(TotalRoundStats stats, String strokes, String putts, String sand, String finalScore) {
        /*
         * This function parses the data strings that were sent here from the ScorecardActivity.
         * Then it loads the new stats into the TotalRoundStats object before it is saved.
         */

        // First we have to parse the strings into 18 groups.
        int i = 0, j = 0, k = 0;
        int i2, j2, k2;
        int totalPutts = 0;
        int totalSand = 0;

        String mStroke;
        String mPutt;
        String mSand;
        TotalHoleStats hole;

        if (stats.holes.size() == 0) {
            for (int z = 0; z < 18; z++) {
                TotalHoleStats nHole = new TotalHoleStats();
                stats.holes.add(nHole);
            }
        }

        // This loop parses the data into 18 holes and updates the stats.
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
            hole.UpdateStats(Integer.parseInt(mStroke), Integer.parseInt(mPutt), Integer.parseInt(mSand));

            totalPutts += Integer.parseInt(mPutt);
            totalSand += Integer.parseInt(mSand);
        }

        int finalS = Integer.parseInt(finalScore);
        stats.UpdateTotals(finalS, totalPutts, totalSand);
    }
    private void SaveTotals(TotalRoundStats stats, boolean fileExists) {
        /*
         * This function saves the newly updated stats object.
         * Saving is a little different depending on if the file already
         * exists or not. Uses serialization to save the object in a file
         * on internal storage.
         */

        // TODO: Save the hole stats into a separate file. It isn't serializing properly
        // TODO: so it needs a different save method.

        FileOutputStream outputStream;
        if (!fileExists) {
            try {
                outputStream = openFileOutput("TotalStats.txt", Context.MODE_PRIVATE);
                ObjectOutputStream out = new ObjectOutputStream(outputStream);

                // Serializes the object.
                out.writeObject(stats);

                out.close();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            File directory = getFilesDir();
            File file = new File(directory, "TotalStats.txt");
            try {
                outputStream = new FileOutputStream(file);
                ObjectOutputStream out = new ObjectOutputStream(outputStream);

                out.writeObject(stats);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
