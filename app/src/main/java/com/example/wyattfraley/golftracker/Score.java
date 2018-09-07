package com.example.wyattfraley.golftracker;

import android.widget.TextView;

import java.util.Stack;

public class Score {
    Stack<String> actions;
    TextView hole;
    public int number;
    public int strokes;
    public int putts;
    public int sand;
    public int fairway;
    public int greenInRegulation;
    int par;
    HoleLocationData locationData;

    Score(TextView newHole) {
        hole = newHole;
        strokes = 0;
        putts = 0;
        sand = 0;
        par = 0;
        actions = new Stack<>();
        locationData = new HoleLocationData();
    }

    Score() {
        strokes = 0;
        putts = 0;
        sand = 0;
        par = 0;
        actions = new Stack<>();
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
}
