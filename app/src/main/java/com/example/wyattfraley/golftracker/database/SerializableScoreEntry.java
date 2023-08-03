package com.example.wyattfraley.golftracker.database;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;


/**
 * The intent of this class is to be a serializable version of the ScoreEntry class.
 *  objects are not serializable.  We need this class to be able to pass Score data
 * between Intents, and to save the score data itself when traversing Intents.
 */
public class SerializableScoreEntry implements Serializable {

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
    private List<Integer> fairway = new ArrayList<>();

    /* List of greens hit by hole */
    private List<Integer> greenInRegulation = new ArrayList<>();

    /* Final score of the round */
    private Integer finalScore = 0;

    /* par for the course played */
    private Integer parPlayed = 0;

    /**
     * Constructor which contains every single variable for the class.
     */
    public SerializableScoreEntry(String uId, List<Integer> mStrokes, List<Integer> mPutts, List<Integer> mPenalties, List<Integer> mSand, List<Integer> mFairway, List<Integer> mGreenInRegulation, int mFinal, int mParPlayed) {
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
    public SerializableScoreEntry(List<Integer> mStrokes, List<Integer> mPutts, List<Integer> mPenalties, List<Integer> mSand, List<Integer> mFairway, List<Integer> mGreenInRegulation) {
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
    public List<Integer> getFairway() { return fairway; }
    public void setFairway(@NonNull List<Integer> nFairway) {
        fairway = nFairway;
    }

    @NonNull
    public List<Integer> getGreenInRegulation() {
        return greenInRegulation;
    }
    public void setGreenInRegulation(@NonNull List<Integer> nGreenInRegulation) {
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

    /**
     * Converts our SerializableScoreEntry into a RealmScoreEntry for database usage
     * @return a RealmScoreEntry that is ready for insertion.
     */
    public RealmScoreEntry toRealmScoreEntry() {
        RealmList<Integer> rStrokes = toRealmList(strokes);
        rStrokes.addAll(strokes);
        return new RealmScoreEntry(uId, toRealmList(strokes), toRealmList(putts), toRealmList(penalties),
                toRealmList(sand), toRealmList(fairway), toRealmList(greenInRegulation), finalScore, parPlayed);
    }

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

    /**
     * Converts our SerializableScoreEntry into a RealmScoreEntry for database usage
     * @return a RealmRoundScore that is ready for insertion.
     */
    public RealmRoundScore toRealmRoundScore() {
        RealmRoundScore roundScore = new RealmRoundScore();
        int finalScore = 0;

        // TODO: Add par to this class.  See if we can change fairway and greenInRegulation to be boolean lists
        for (int i = 0; i < 18; i++) {
            RealmHoleScore holeScore = new RealmHoleScore(strokes.get(i), putts.get(i), penalties.get(i), sand.get(i), true ? fairway.get(i) > 0 : false, true ? greenInRegulation.get(i) > 0 : false, 0, 0);
            roundScore.getScores().add(holeScore);
            finalScore += strokes.get(i);
        }

        roundScore.setFinalScore(finalScore);

        return roundScore;
    }
}
