package com.example.wyattfraley.golftracker.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.wyattfraley.golftracker.Converters;
import com.example.wyattfraley.golftracker.ScoreEntry;
import com.example.wyattfraley.golftracker.ScoreEntryDao;

@Database(entities = {ScoreEntry.class}, version = 6)
@TypeConverters({Converters.class})
public abstract class GolfDatabase extends RoomDatabase {
    public abstract ScoreEntryDao myScoreEntryDao();
}
