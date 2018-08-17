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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ShowAllRounds extends AppCompatActivity{
    List<ScoreEntry> AllRounds;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_show_all_rounds);
        AllRounds = new ArrayList<>();

        LoadRounds();
    }
    public void LoadRounds() {
        final GolfDatabase Db = Room.databaseBuilder(getApplicationContext(), GolfDatabase.class, "score-db-V2").build();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AllRounds = Db.MyScoreEntryDao().getAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                DisplayScoresV2();
            }
        }.execute();
    }

    public void DisplayScores() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ShowAllRounds);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < AllRounds.size(); i++) {
            ScoreEntry MyEntry = AllRounds.get(i);

            Button MyButton = new Button(this);
            String Date = MyEntry.getUid();
            final String Final = MyEntry.getFinal();
            final String strokes = MyEntry.getStrokes();
            final String putts = MyEntry.getPutts();
            final String sand = MyEntry.getSand();
            Date = Date.substring(0, 10);
            MyButton.setText(Date + ":  Score: " + Final);

            MyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(ShowAllRounds.this, ShowSingleRound.class);
                    myIntent.putExtra("Strokes", strokes);
                    myIntent.putExtra("Putts", putts);
                    myIntent.putExtra("Sand", sand);
                    myIntent.putExtra("Final", Final);

                    startActivity(myIntent);
                }
            });

            ll.addView(MyButton, lp);
        }

    }
    public void DisplayScoresV2() {
        ScrollView scrollView = (ScrollView)findViewById(R.id.RoundScroll);

        LinearLayout ll = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < AllRounds.size(); i++) {
            ScoreEntry MyEntry = AllRounds.get(i);

            Button MyButton = new Button(this);
            final String uid = MyEntry.getUid();
            final String Final = MyEntry.getFinal();
            final String strokes = MyEntry.getStrokes();
            final String putts = MyEntry.getPutts();
            final String sand = MyEntry.getSand();
            String toDisplay = uid.substring(0, 10);
            MyButton.setText(toDisplay + ":  Score: " + Final);

            MyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(ShowAllRounds.this, ShowSingleRound.class);
                    myIntent.putExtra("Id", uid);
                    myIntent.putExtra("Strokes", strokes);
                    myIntent.putExtra("Putts", putts);
                    myIntent.putExtra("Sand", sand);
                    myIntent.putExtra("Final", Final);

                    startActivityForResult(myIntent, 100);
                }
            });

            ll.addView(MyButton, lp);
        }
        scrollView.addView(ll);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                LoadRounds();
            }
        }
    }
}
