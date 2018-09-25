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

    @Query("Select uId, finalscore, parPlayed FROM ScoreEntry")
    List<ScoreEntryDisplayRound> getRoundsForDisplay();

    @Query("SELECT * FROM ScoreEntry WHERE uId IN (:userIds)")
    List<ScoreEntry> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM ScoreEntry WHERE uid IS (:userID)")
    ScoreEntry loadSingleRound(String userID);

    @Insert
    void insertAll(ScoreEntry... ScoreEntries);

    @Delete
    void delete(ScoreEntry MyScoreEntry);
}
