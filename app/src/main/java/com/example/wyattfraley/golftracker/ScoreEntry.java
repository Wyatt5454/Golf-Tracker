package com.example.wyattfraley.golftracker;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class ScoreEntry {
    @PrimaryKey
    @NonNull
    private String uId;

    @ColumnInfo(name = "strokes")
    private String strokes;

    @ColumnInfo(name = "putts")
    private String putts;

    @ColumnInfo(name = "sand")
    private String sand;

    @ColumnInfo(name = "finalscore")
    private String finalScore;

    public String getUId() { return uId; }
    public void setUId(String NUid) {
        uId = NUid;
    }

    public String getStrokes() {
        return strokes;
    }
    public void setStrokes(String NStrokes) {
        strokes = NStrokes;
    }

    public String getPutts() {
        return putts;
    }
    public void setPutts(String NPutts) {
        putts = NPutts;
    }

    public String getSand() {
        return sand;
    }
    public void setSand(String NSand) {
        sand = NSand;
    }

    public String getFinalScore() {
        return finalScore;
    }
    public void setFinalScore(String aFinal) { finalScore = aFinal; }
}
