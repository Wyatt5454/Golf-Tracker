package com.example.wyattfraley.golftracker;

import android.widget.TextView;

import java.util.Stack;

public class Score {
    Stack<String> Actions;
    TextView Hole;
    public int number;
    public int Strokes;
    public int Putts;
    public int Sand;
    int Par;
    HoleLocationData locationData;

    Score(TextView newHole) {
        Hole = newHole;
        Strokes = 0;
        Putts = 0;
        Sand = 0;
        Par = 0;
        Actions = new Stack<>();
        locationData = new HoleLocationData();
    }

    Score() {
        Strokes = 0;
        Putts = 0;
        Sand = 0;
        Par = 0;
        Actions = new Stack<>();
    }

    String ToSaveFormat() {
        return Integer.toString(Strokes) + " " + Integer.toString(Putts) + " " + Integer.toString(Sand) + "\n";
    }

    public int getPutts() {
        return Putts;
    }
    public void setPutts(int nPutts) {
        Putts = nPutts;
    }
    public int getStrokes() {
        return Strokes;
    }

    public void setStrokes(int strokes) {
        Strokes = strokes;
    }

    public int getSand() {
        return Sand;
    }

    public void setSand(int sand) {
        Sand = sand;
    }

    public void setPar(int par) {
        Par = par;
    }
    public void setNumber(int nNumber) { number = nNumber; }
}
