package com.example.wyattfraley.golftracker.database;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;


public class ScoreEntry extends RealmObject implements Serializable {
    @PrimaryKey
    @NonNull
    private String uId = "";

    private String courseUid = "";

    @NonNull
    private RealmList<Integer> strokes = new RealmList<>();

    @NonNull
    private RealmList<Integer> putts = new RealmList<>();

    @NonNull
    private RealmList<Integer> penalties = new RealmList<>();

    @NonNull
    private RealmList<Integer> sand = new RealmList<>();

    @NonNull
    private RealmList<Integer> fairway = new RealmList<>();

    @NonNull
    private RealmList<Integer> greenInRegulation = new RealmList<>();

    @NonNull
    private Integer finalScore = 0;

    @NonNull
    private Integer parPlayed = 0;

    public ScoreEntry(String uId, RealmList<Integer> strokes, RealmList<Integer> putts, RealmList<Integer> penalties, RealmList<Integer> sand, RealmList<Integer> fairway, RealmList<Integer> greenInRegulation, Integer finalScore, Integer parPlayed) {
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
    public ScoreEntry(RealmList<Integer> strokes, RealmList<Integer> putts, RealmList<Integer> penalties, RealmList<Integer> sand, RealmList<Integer> fairway, RealmList<Integer> greenInRegulation) {
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

    @NonNull
    public RealmList<Integer> getStrokes() {
        return strokes;
    }
    public void setStrokes(@NonNull RealmList<Integer> NStrokes) {
        strokes = NStrokes;
    }

    @NonNull
    public RealmList<Integer> getPutts() {
        return putts;
    }
    public void setPutts(@NonNull RealmList<Integer> NPutts) {
        putts = NPutts;
    }

    @NonNull
    public RealmList<Integer> getPenalties() {
        return penalties;
    }
    public void setPenalties(@NonNull RealmList<Integer> NPenalties) {
        penalties = NPenalties;
    }

    @NonNull
    public RealmList<Integer> getSand() {
        return sand;
    }
    public void setSand(@NonNull RealmList<Integer> NSand) {
        sand = NSand;
    }

    @NonNull
    public RealmList<Integer> getFairway() { return fairway; }
    public void setFairway(@NonNull RealmList<Integer> nFairway) {
        fairway = nFairway;
    }

    @NonNull
    public RealmList<Integer> getGreenInRegulation() {
        return greenInRegulation;
    }
    public void setGreenInRegulation(@NonNull RealmList<Integer> nGreenInRegulation) {
        greenInRegulation = nGreenInRegulation;
    }

    @NonNull
    public Integer getFinalScore() {
        return finalScore;
    }
    public void setFinalScore(@NonNull Integer aFinal) { finalScore = aFinal; }

    @NonNull
    public Integer getParPlayed() {
        return parPlayed;
    }
    public void setParPlayed(@NonNull Integer nParPlayed) { parPlayed = nParPlayed; }

    @NonNull
    public String getCourseUid() {
        return courseUid;
    }

    public void setCourseUid(@NonNull String courseUid) {
        this.courseUid = courseUid;
    }
}
