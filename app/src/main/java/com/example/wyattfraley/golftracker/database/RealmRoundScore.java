package com.example.wyattfraley.golftracker.database;

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
     */
    @Required
    @PrimaryKey
    private String _id;

    /**
     * The played course's UUID
     */
    private String courseUid = "";

    /**
     * List of scores.  Should be 18
     */
    @Required
    private RealmList<RealmHoleScore> scores = new RealmList<RealmHoleScore>();

    /**
     * Total score for the round
     */
    @Required
    private Integer finalScore = 0;

    /** Realm needs a default constructor to work */
    public RealmRoundScore() {}


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
}
