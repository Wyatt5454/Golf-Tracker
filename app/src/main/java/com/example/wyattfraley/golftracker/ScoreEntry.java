package com.example.wyattfraley.golftracker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class ScoreEntry implements Serializable{
    @PrimaryKey
    @NonNull
    private String uId = "";

    @ColumnInfo(name = "strokes")
    private ArrayList<Integer> strokes;

    @ColumnInfo(name = "putts")
    private ArrayList<Integer> putts;

    @ColumnInfo(name = "penalties")
    private ArrayList<Integer> penalties;

    @ColumnInfo(name = "sand")
    private ArrayList<Integer> sand;

    @ColumnInfo(name = "fairway")
    private ArrayList<Integer> fairway;

    @ColumnInfo(name = "greenInRegulation")
    private ArrayList<Integer> greenInRegulation;

    @ColumnInfo(name = "finalScore")
    private Integer finalScore;

    @ColumnInfo(name = "parPlayed")
    private Integer parPlayed;

    @Ignore
    public ScoreEntry(String uId, ArrayList<Integer> strokes, ArrayList<Integer> putts, ArrayList<Integer> penalties, ArrayList<Integer> sand, ArrayList<Integer> fairway, ArrayList<Integer> greenInRegulation, Integer finalScore, Integer parPlayed) {
        setUId(uId);
        setStrokes(strokes);
        setPutts(putts);
        setPenalties(penalties);
        setSand(sand);
        setFairway(fairway);
        setGreenInRegulation(greenInRegulation);
        setFinalScore(finalScore);
        setParPlayed(parPlayed);
    }
    @Ignore
    public ScoreEntry(ArrayList<Integer> strokes, ArrayList<Integer> putts, ArrayList<Integer> penalties, ArrayList<Integer> sand, ArrayList<Integer> fairway, ArrayList<Integer> greenInRegulation) {
        setStrokes(strokes);
        setPutts(putts);
        setPenalties(penalties);
        setSand(sand);
        setFairway(fairway);
        setGreenInRegulation(greenInRegulation);
    }
    public ScoreEntry() {}

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

    public ArrayList<Integer> getPenalties() {
        return penalties;
    }
    public void setPenalties(ArrayList<Integer> NPenalties) {
        penalties = NPenalties;
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

    public Integer getParPlayed() {
        return parPlayed;
    }
    public void setParPlayed(Integer nParPlayed) { parPlayed = nParPlayed; }
}
