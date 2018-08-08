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
    private String Uid;

    @ColumnInfo(name = "strokes")
    private String Strokes;

    @ColumnInfo(name = "putts")
    private String Putts;

    @ColumnInfo(name = "sand")
    private String Sand;

    public String getUid() {
        return Uid;
    }
    public void setUid(String NUid) {
        Uid = NUid;
    }

    public String getStrokes() {
        return Strokes;
    }
    public void setStrokes(String NStrokes) {
        Strokes = NStrokes;
    }

    public String getPutts() {
        return Putts;
    }
    public void setPutts(String NPutts) {
        Putts = NPutts;
    }

    public String getSand() {
        return Sand;
    }
    public void setSand(String NSand) {
        Sand = NSand;
    }
}
