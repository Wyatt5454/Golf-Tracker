package com.example.wyattfraley.golftracker.scorecard;

import android.widget.TextView;

import java.io.Serializable;
import java.util.Stack;

/**
 * Serializable class representing the score on a single hole.
 * A data object for passing between Intents
 */
public class HoleScoreData {

    private Stack<String> actions;
    private TextView hole;
    private int number;
    private int strokes;
    private int putts;
    private int penalties;
    private int sand;
    private boolean fairway;
    private boolean greenInRegulation;
    private int par;
    private int yards;

    private HoleLocationData locationData;





    public HoleScoreData(TextView newHole) {
        hole = newHole;
        strokes = 0;
        putts = 0;
        penalties = 0;
        sand = 0;
        par = 0;
        yards = 0;
        actions = new Stack<>();
        locationData = new HoleLocationData();
    }

    public int getPutts() {
        return putts;
    }
    public void setPutts(int nPutts) {
        putts = nPutts;

        for (int i = 0; i < nPutts; i++) {
            actions.push("putt");
        }
    }

    public int getStrokes() {
        return strokes;
    }
    public void setStrokes(int nStrokes) {
        strokes = nStrokes;

        for (int i = 0; i < nStrokes; i++) {
            actions.push("stroke");
        }
    }

    public int getPenalties() {
        return penalties;
    }
    public void setPenalties(int nPenalties) {
        putts = nPenalties;

        for (int i = 0; i < nPenalties; i++) {
            actions.push("penalty");
        }
    }

    public int getSand() {
        return sand;
    }
    public void setSand(int nSand) {
        sand = nSand;
    }

    public void setPar(int nPar) {
        par = nPar;
    }
    public void setNumber(int nNumber) { number = nNumber; }

    public boolean getFairway() {
        return fairway;
    }
    public void setFairway(boolean fairway) {
        this.fairway = fairway;
    }

    public boolean getGreenInRegulation() {
        return greenInRegulation;
    }
    public void setGreenInRegulation(boolean greenInRegulation) {
        this.greenInRegulation = greenInRegulation;
    }

    public Stack<String> getActions() {
        return actions;
    }

    public void setActions(Stack<String> actions) {
        this.actions = actions;
    }

    public HoleLocationData getLocationData() {
        return locationData;
    }

    public void setLocationData(HoleLocationData locationData) {
        this.locationData = locationData;
    }

    public TextView getHole() {
        return hole;
    }

    public void setHole(TextView hole) {
        this.hole = hole;
    }

    public int getNumber() {
        return number;
    }

    public int getPar() {
        return par;
    }

    /**
     * Adds one stroke
     */
    public void addStroke() {
        strokes++;
    }

    /**
     * Adds one putt
     */
    public void addPutt() {
        putts++;
    }

    /**
     * Adds one penalty stroke
     */
    public void addPenalty() {
        penalties++;
    }

    /**
     * Removes one putt
     */
    public void removePutt() {
        putts--;
    }

    /**
     * Removes one stroke
     */
    public void removeStroke() {
        strokes--;
    }

    /**
     * Removes one penalty
     */
    public void removePenalty() {
        penalties--;
    }

    public int getYards() {
        return yards;
    }

    public void setYards(int yards) {
        this.yards = yards;
    }
}
