package com.example.wyattfraley.golftracker;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

public class ScoreEntryDisplayRound {
    @PrimaryKey
    @NonNull
    @ColumnInfo( name = "uId" )
    public String uid;
    @ColumnInfo( name = "finalscore" )
    public Integer finalScore;
    @ColumnInfo( name = "parPlayed" )
    public Integer parPlayed;

    ScoreEntryDisplayRound() {
        uid = new String();
        finalScore = 0;
        parPlayed = 0;
    }
    ScoreEntryDisplayRound(String nUid, Integer nFinalScore, Integer nParPlayed) {
        uid = nUid;
        finalScore = nFinalScore;
        parPlayed = nParPlayed;
    }

    public Integer getParPlayed() {
        return parPlayed;
    }
    public Integer getFinalScore() {
        return finalScore;
    }
    public String getUid() {
        return uid;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }
    public void setParPlayed(Integer parPlayed) {
        this.parPlayed = parPlayed;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
}
