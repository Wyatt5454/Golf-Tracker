package com.example.wyattfraley.golftracker;

import android.arch.persistence.room.RoomDatabase;

public abstract class GolfDatabase extends RoomDatabase {
    public abstract  ScoreEntryDao MyScoreEntryDao();
}
