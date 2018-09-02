package com.example.wyattfraley.golftracker;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ShowAllRounds extends AppCompatActivity{
    List<ScoreEntry> allRounds;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_show_all_rounds);
        allRounds = new ArrayList<>();

        LoadRounds();
    }
    @SuppressLint("StaticFieldLeak")
    public void LoadRounds() {
        final GolfDatabase Db = Room.databaseBuilder(getApplicationContext(), GolfDatabase.class, "score-db-V3").fallbackToDestructiveMigration().build();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                allRounds = Db.myScoreEntryDao().getAll();
                Collections.reverse(allRounds);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                DisplayScoresV2();
            }
        }.execute();
    }

    public void DisplayScores() {
        LinearLayout ll = findViewById(R.id.ShowAllRounds);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < allRounds.size(); i++) {
            ScoreEntry myEntry = allRounds.get(i);

            Button myButton = new Button(this);
            String Date = myEntry.getUId();
            final String finalScore = myEntry.getFinalScore();
            final String strokes = myEntry.getStrokes();
            final String putts = myEntry.getPutts();
            final String sand = myEntry.getSand();
            Date = Date.substring(0, 10);
            myButton.setText(Date + ":  Score: " + finalScore);

            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(ShowAllRounds.this, ShowSingleRound.class);
                    myIntent.putExtra("strokes", strokes);
                    myIntent.putExtra("putts", putts);
                    myIntent.putExtra("sand", sand);
                    myIntent.putExtra("finalScore", finalScore);

                    startActivity(myIntent);
                }
            });

            ll.addView(myButton, lp);
        }

    }
    public void DisplayScoresV2() {
        /*
         * Initializes each round as its own button,
         * and sets up the on click listener to open up a new activity,
         * which allows the user to look at more detailed stats for an individual round.
         */
        ScrollView scrollView = findViewById(R.id.RoundScroll);

        LinearLayout ll = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < allRounds.size(); i++) {
            final ScoreEntry myEntry = allRounds.get(i);

            Button myButton = new Button(this);
            final String uid = myEntry.getUId();
            final String finalScore = myEntry.getFinalScore();
            String toDisplay = uid.substring(0, 10);
            myButton.setText(String.format("%s:  Score: %s", toDisplay, finalScore));
            myButton.setBackgroundResource(R.drawable.round_button);


            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(ShowAllRounds.this, ShowSingleRound.class);
                    myIntent.putExtra("Score", myEntry);

                    startActivityForResult(myIntent, 100);
                }
            });

            ll.addView(myButton, lp);
        }
        scrollView.removeAllViews();
        scrollView.addView(ll);
    }
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         * This function reloads the rounds if a round was deleted in
         * the ShowSingleRound activity.
         */
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        LoadRounds();
                        return null;
                    }
                }.execute();
            }
        }
    }
}
