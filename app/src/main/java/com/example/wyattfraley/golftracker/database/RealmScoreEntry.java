package com.example.wyattfraley.golftracker.database;

import androidx.annotation.NonNull;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class RealmScoreEntry extends RealmObject {
    @PrimaryKey
    private String _id = "";

    private String courseUid = "";

    @Required
    private RealmList<Integer> strokes = new RealmList<>();

    @Required
    private RealmList<Integer> putts = new RealmList<>();

    @Required
    private RealmList<Integer> penalties = new RealmList<>();

    @Required
    private RealmList<Integer> sand = new RealmList<>();

    @Required
    private RealmList<Integer> fairway = new RealmList<>();

    @Required
    private RealmList<Integer> greenInRegulation = new RealmList<>();

    @Required
    private Integer finalScore = 0;

    @Required
    private Integer parPlayed = 0;

    public RealmScoreEntry(String _id, RealmList<Integer> strokes, RealmList<Integer> putts, RealmList<Integer> penalties, RealmList<Integer> sand, RealmList<Integer> fairway, RealmList<Integer> greenInRegulation, Integer finalScore, Integer parPlayed) {
        setUId(_id);
        setStrokes(strokes);
        setPutts(putts);
        setPenalties(penalties);
        setSand(sand);
        setFairway(fairway);
        setGreenInRegulation(greenInRegulation);
        setFinalScore(finalScore);
        setParPlayed(parPlayed);
    }
    public RealmScoreEntry(RealmList<Integer> strokes, RealmList<Integer> putts, RealmList<Integer> penalties, RealmList<Integer> sand, RealmList<Integer> fairway, RealmList<Integer> greenInRegulation) {
        setStrokes(strokes);
        setPutts(putts);
        setPenalties(penalties);
        setSand(sand);
        setFairway(fairway);
        setGreenInRegulation(greenInRegulation);
    }
    public RealmScoreEntry() {}

    public String getUId() { return _id; }
    public void setUId(String NUid) {
        _id = NUid;
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
