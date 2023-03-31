package com.example.wyattfraley.golftracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreEntryDao {

    @Query("Select uId, finalscore, parPlayed FROM ScoreEntry")
    List<ScoreEntryDisplayRound> getRoundsForDisplay();

    @Query("SELECT * FROM ScoreEntry WHERE uid IS (:userID)")
    ScoreEntry loadSingleRound(String userID);

    @Insert
    void insertAll(ScoreEntry... ScoreEntries);

    @Delete
    void delete(ScoreEntry MyScoreEntry);
}
