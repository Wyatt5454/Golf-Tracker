package com.example.wyattfraley.golftracker.database;

import androidx.annotation.NonNull;

import io.realm.RealmList;
import io.realm.annotations.PrimaryKey;


public class ScoreEntryDisplayRound {
    @PrimaryKey
    @NonNull
    public String uid;
    private Integer finalScore;
    private Integer parPlayed;

    public ScoreEntryDisplayRound() {
        uid = "";
        finalScore = 0;
        parPlayed = 0;
    }

    public Integer getParPlayed() {
        return parPlayed;
    }
    public Integer getFinalScore() {
        return finalScore;
    }
    @NonNull
    public String getUid() {
        return uid;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }
    public void setParPlayed(Integer parPlayed) {
        this.parPlayed = parPlayed;
    }
    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }


}
