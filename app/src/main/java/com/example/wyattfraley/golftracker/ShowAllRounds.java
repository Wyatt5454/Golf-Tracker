package com.example.wyattfraley.golftracker;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

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

        final GolfDatabase Db = Room.databaseBuilder(getApplicationContext(), GolfDatabase.class, "score-db-V2").build();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AllRounds = Db.MyScoreEntryDao().getAll();
                return null;
            }
        }.execute();
    }
}
