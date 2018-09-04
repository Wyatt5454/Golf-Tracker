package com.example.wyattfraley.golftracker;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {ScoreEntry.class}, version = 4)
@TypeConverters({Converters.class})
public abstract class GolfDatabase extends RoomDatabase {
    public abstract  ScoreEntryDao myScoreEntryDao();
}
