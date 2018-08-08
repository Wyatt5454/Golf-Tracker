package com.example.wyattfraley.golftracker;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Bundle;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class ScoreEntry {
    @PrimaryKey
    private Date Uid;

    @ColumnInfo(name = "strokes")
    private String Strokes;

    @ColumnInfo(name = "putts")
    private String Putts;

    @ColumnInfo(name = "sand")
    private String Sand;

    public Date GetUid() {
        return Uid;
    }
    public void SetUid(Date NUid) {
        Uid = NUid;
    }

    public String GetStrokes() {
        return Strokes;
    }
    public void SetStrokes(String NStrokes) {
        Strokes = NStrokes;
    }

    public String GetPutts() {
        return Putts;
    }
    public void SetPutts(String NPutts) {
        Putts = NPutts;
    }

    public String GetSand() {
        return Sand;
    }
    public void SetSand(String NSand) {
        Sand = NSand;
    }
}
