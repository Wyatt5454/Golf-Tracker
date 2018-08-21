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

    String ToSaveFormat() {
        return Integer.toString(strokes) + " " + Integer.toString(putts) + " " + Integer.toString(sand) + "\n";
    }

    public int getPutts() {
        return putts;
    }
    public void setPutts(int nPutts) {
        putts = nPutts;
    }
    public int getStrokes() {
        return strokes;
    }

    public void setStrokes(int nStrokes) {
        strokes = nStrokes;
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
}
