package com.example.wyattfraley.golftracker;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {ScoreEntry.class}, version = 2)
public abstract class GolfDatabase extends RoomDatabase {
    public abstract  ScoreEntryDao MyScoreEntryDao();
}
