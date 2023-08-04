package com.example.wyattfraley.golftracker.database;

import android.annotation.SuppressLint;

import java.time.Instant;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * RealmObject class for recording all score data relating
 * to a single hole.  Contained in the RealmScoreEntry object.
 */
public class RealmHoleScore extends RealmObject {

    @Required
    @PrimaryKey
    private String _id = "";

    @Required
    private Integer strokes = 0;

    @Required
    private Integer putts = 0;

    @Required
    private Integer penalties = 0;

    @Required
    private Integer sand = 0;

    @Required
    private Boolean fairway = false;

    @Required
    private Boolean greenInRegulation = false;

    @Required
    private Integer parPlayed = 0;

    private Integer yards = 0;

    /** Need default constructor for Realm */
    public RealmHoleScore() {}

    /**
     * Constructor containing all fields for this object.
     * @param strokes: strokes on the hole
     * @param putts: putts on the hole
     * @param penalties: penalties on the hole
     * @param sand: # of sand shots on the hole
     * @param fairway: Did we hit the fairway in regulation?
     * @param gir: Did we hit the green in regulation?
     * @param par: par for the hole
     * @param yards: yards for the hole.  Only one that isn't required as we may not always have this data.
     */
    @SuppressLint("NewApi")
    public RealmHoleScore(int strokes, int putts, int penalties, int sand, boolean fairway, boolean gir, int par, int yards) {
        set_id(Instant.now().toString());
        setStrokes(strokes);
        setPutts(putts);
        setPenalties(penalties);
        setSand(sand);
        setFairway(fairway);
        setGreenInRegulation(gir);
        setParPlayed(par);
        setYards(yards);
    }

    public Integer getStrokes() {
        return strokes;
    }

    public void setStrokes(Integer strokes) {
        this.strokes = strokes;
    }

    public Integer getPutts() {
        return putts;
    }

    public void setPutts(Integer putts) {
        this.putts = putts;
    }

    public Integer getPenalties() {
        return penalties;
    }

    public void setPenalties(Integer penalties) {
        this.penalties = penalties;
    }

    public Integer getSand() {
        return sand;
    }

    public void setSand(Integer sand) {
        this.sand = sand;
    }

    public Boolean getFairway() {
        return fairway;
    }

    public void setFairway(Boolean fairway) {
        this.fairway = fairway;
    }

    public Boolean getGreenInRegulation() {
        return greenInRegulation;
    }

    public void setGreenInRegulation(Boolean greenInRegulation) {
        this.greenInRegulation = greenInRegulation;
    }

    public Integer getParPlayed() {
        return parPlayed;
    }

    public void setParPlayed(Integer parPlayed) {
        this.parPlayed = parPlayed;
    }

    public Integer getYards() {
        return yards;
    }

    public void setYards(Integer yards) {
        this.yards = yards;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
