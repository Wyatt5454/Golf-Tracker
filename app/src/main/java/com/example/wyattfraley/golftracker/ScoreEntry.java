package com.example.wyattfraley.golftracker;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Entity
public class ScoreEntry implements Serializable{
    @PrimaryKey
    @NonNull
    private String uId;

    @ColumnInfo(name = "strokes")
    private ArrayList<Integer> strokes;

    @ColumnInfo(name = "putts")
    private ArrayList<Integer> putts;

    @ColumnInfo(name = "sand")
    private ArrayList<Integer> sand;

    @ColumnInfo(name = "fairway")
    private ArrayList<Integer> fairway;

    @ColumnInfo(name = "greenInRegulation")
    private ArrayList<Integer> greenInRegulation;

    @ColumnInfo(name = "finalscore")
    private Integer finalScore;

    @Ignore
    ScoreEntry(String uId, ArrayList<Integer> strokes, ArrayList<Integer> putts, ArrayList<Integer> sand, ArrayList<Integer> fairway, ArrayList<Integer> greenInRegulation, Integer finalScore) {
        setUId(uId);
        setStrokes(strokes);
        setPutts(putts);
        setSand(sand);
        setFairway(fairway);
        setGreenInRegulation(greenInRegulation);
        setFinalScore(finalScore);
    }
    @Ignore
    ScoreEntry(ArrayList<Integer> strokes, ArrayList<Integer> putts, ArrayList<Integer> sand, ArrayList<Integer> fairway, ArrayList<Integer> greenInRegulation) {
        setStrokes(strokes);
        setPutts(putts);
        setSand(sand);
        setFairway(fairway);
        setGreenInRegulation(greenInRegulation);
    }
    ScoreEntry() {}

    public String getUId() { return uId; }
    public void setUId(String NUid) {
        uId = NUid;
    }

    public ArrayList<Integer> getStrokes() {
        return strokes;
    }
    public void setStrokes(ArrayList<Integer> NStrokes) {
        strokes = NStrokes;
    }

    public ArrayList<Integer> getPutts() {
        return putts;
    }
    public void setPutts(ArrayList<Integer> NPutts) {
        putts = NPutts;
    }

    public ArrayList<Integer> getSand() {
        return sand;
    }
    public void setSand(ArrayList<Integer> NSand) {
        sand = NSand;
    }

    public ArrayList<Integer> getFairway() { return fairway; }
    public void setFairway(ArrayList<Integer> nFairway) {
        fairway = nFairway;
    }

    public ArrayList<Integer> getGreenInRegulation() {
        return greenInRegulation;
    }
    public void setGreenInRegulation(ArrayList<Integer> nGreenInRegulation) {
        greenInRegulation = nGreenInRegulation;
    }

    public Integer getFinalScore() {
        return finalScore;
    }
    public void setFinalScore(Integer aFinal) { finalScore = aFinal; }
}
