package com.example.wyattfraley.golftracker.database;

import android.annotation.SuppressLint;

import java.time.Instant;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Realm Object representing one complete round of golf.
 */
public class RealmRoundScore extends RealmObject {

    /**
     * This should be the player's UUID
     * TODO: Grab this from a database of users.  Currently this is set as a date/time
     */
    @PrimaryKey
    private String _id;

    @Required
    private String round_id = UUID.randomUUID().toString();

    /**
     * The played course's UUID
     */
    private String courseUid = "";

    /**
     * List of scores.  Should be 18
     */
    private RealmList<RealmHoleScore> scores = new RealmList<>();

    /**
     * Total score for the round
     */
    @Required
    private Integer finalScore = 0;

    /** Realm needs a default constructor to work */
    @SuppressLint("NewApi")
    public RealmRoundScore() {
        set_id(Instant.now().toString());
    }


    public RealmList<RealmHoleScore> getScores() {
        return scores;
    }

    public void setScores(RealmList<RealmHoleScore> scores) {
        this.scores = scores;
    }

    public Integer getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }

    public String getRound_id() {
        return round_id;
    }

    public void setRound_id(String round_id) {
        this.round_id = round_id;
    }

    public String getCourseUid() {
        return courseUid;
    }

    public void setCourseUid(String courseUid) {
        this.courseUid = courseUid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
