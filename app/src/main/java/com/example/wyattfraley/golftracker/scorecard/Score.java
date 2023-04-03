package com.example.wyattfraley.golftracker.scorecard;

import android.widget.TextView;

import java.util.Stack;

public class Score {


    private Stack<String> actions;
    public TextView hole;
    public int number;
    public int strokes;
    public int putts;
    public int penalties;
    public int sand;
    public int fairway;
    public int greenInRegulation;
    public int par;

    private HoleLocationData locationData;





    public Score(TextView newHole) {
        hole = newHole;
        strokes = 0;
        putts = 0;
        sand = 0;
        par = 0;
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

    public int getFairway() {
        return fairway;
    }
    public void setFairway(int fairway) {
        this.fairway = fairway;
    }

    public int getGreenInRegulation() {
        return greenInRegulation;
    }
    public void setGreenInRegulation(int greenInRegulation) {
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
}
