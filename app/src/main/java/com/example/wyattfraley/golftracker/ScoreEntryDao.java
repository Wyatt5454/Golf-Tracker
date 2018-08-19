package com.example.wyattfraley.golftracker;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ScoreEntryDao {
    @Query("SELECT * FROM ScoreEntry")
    List<ScoreEntry> getAll();

    @Query("SELECT * FROM ScoreEntry WHERE uId IN (:userIds)")
    List<ScoreEntry> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(ScoreEntry... ScoreEntries);

    @Delete
    void delete(ScoreEntry MyScoreEntry);
}
