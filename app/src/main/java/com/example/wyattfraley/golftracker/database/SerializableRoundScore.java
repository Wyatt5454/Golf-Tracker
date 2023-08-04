package com.example.wyattfraley.golftracker.database;

import androidx.annotation.NonNull;

import com.example.wyattfraley.golftracker.scorecard.HoleScoreData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;


/**
 * The intent of this class is to be a serializable version of the ScoreEntry class.
 *  objects are not serializable.  We need this class to be able to pass Score data
 * between Intents, and to save the score data itself when traversing Intents.
 */
public class SerializableRoundScore implements Serializable {

    /* Unique id for the round */
    private String uId = "";

    /* Unique id for the course */
    private String courseUid = "";

    /* List of strokes taken per hole */
    private List<Integer> strokes = new ArrayList<>();

    /* List of putts taken per hole */
    private List<Integer> putts = new ArrayList<>();

    /* List of penalties taken per hole */
    private List<Integer> penalties = new ArrayList<>();

    /* List of bunkers hit per hole */
    private List<Integer> sand = new ArrayList<>();

    /* List of fairways by hole */
    private List<Boolean> fairway = new ArrayList<>();

    /* List of greens hit by hole */
    private List<Boolean> greenInRegulation = new ArrayList<>();

    /* Final score of the round */
    private Integer finalScore = 0;

    /* par for the course played */
    private Integer parPlayed = 0;

    /**
     * Constructor which contains every single variable for the class.
     */
    public SerializableRoundScore(String uId, List<Integer> mStrokes, List<Integer> mPutts, List<Integer> mPenalties, List<Integer> mSand, List<Boolean> mFairway, List<Boolean> mGreenInRegulation, int mFinal, int mParPlayed) {
        setUId(uId);
        setStrokes(mStrokes);
        setPutts(mPutts);
        setPenalties(mPenalties);
        setSand(mSand);
        setFairway(mFairway);
        setGreenInRegulation(mGreenInRegulation);
        setFinalScore(mFinal);
        setParPlayed(mParPlayed);
    }

    /**
     * Constructor missing the uid, final score, and par played.  Used for moving the data between Intents.
     */
    public SerializableRoundScore(List<Integer> mStrokes, List<Integer> mPutts, List<Integer> mPenalties, List<Integer> mSand, List<Boolean> mFairway, List<Boolean> mGreenInRegulation) {
        setStrokes(mStrokes);
        setPutts(mPutts);
        setPenalties(mPenalties);
        setSand(mSand);
        setFairway(mFairway);
        setGreenInRegulation(mGreenInRegulation);
    }

    public String getUId() { return uId; }
    public void setUId(String NUid) {
        uId = NUid;
    }

    @NonNull
    public List<Integer> getStrokes() {
        return strokes;
    }
    public void setStrokes(@NonNull List<Integer> NStrokes) {
        strokes = NStrokes;
    }

    @NonNull
    public List<Integer> getPutts() {
        return putts;
    }
    public void setPutts(@NonNull List<Integer> NPutts) {
        putts = NPutts;
    }

    @NonNull
    public List<Integer> getPenalties() {
        return penalties;
    }
    public void setPenalties(@NonNull List<Integer> NPenalties) {
        penalties = NPenalties;
    }

    @NonNull
    public List<Integer> getSand() {
        return sand;
    }
    public void setSand(@NonNull List<Integer> NSand) {
        sand = NSand;
    }

    @NonNull
    public List<Boolean> getFairway() { return fairway; }
    public void setFairway(@NonNull List<Boolean> nFairway) {
        fairway = nFairway;
    }

    @NonNull
    public List<Boolean> getGreenInRegulation() {
        return greenInRegulation;
    }
    public void setGreenInRegulation(@NonNull List<Boolean> nGreenInRegulation) {
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

//    /**
//     * Converts our SerializableScoreEntry into a RealmScoreEntry for database usage
//     * @return a RealmScoreEntry that is ready for insertion.
//     */
//    public RealmScoreEntry toRealmScoreEntry() {
//        RealmList<Integer> rStrokes = toRealmList(strokes);
//        rStrokes.addAll(strokes);
//        return new RealmScoreEntry(uId, toRealmList(strokes), toRealmList(putts), toRealmList(penalties),
//                toRealmList(sand), toRealmList(fairway), toRealmList(greenInRegulation), finalScore, parPlayed);
//    }

    /**
     * Quick conversion from a List to a RealmList.
     * @param list: to convert
     * @return a RealmList with the same contents.
     */
    private RealmList<Integer> toRealmList(List<Integer> list) {
        RealmList<Integer> realmList = new RealmList<>();
        realmList.addAll(list);
        return realmList;
    }

}
