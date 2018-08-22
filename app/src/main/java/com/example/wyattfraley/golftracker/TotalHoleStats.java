package com.example.wyattfraley.golftracker;

import java.io.Serializable;

public class TotalHoleStats implements Serializable {
    /*
     * For some reason having this class inherit from Score
     * messes up the serializing so it doesn't get saved properly.
     * Not sure why.
     */

    public int strokes;
    public int putts;
    public int sand;
    public int fairway;
    public int greenInRegulation;

    TotalHoleStats() {
        putts = 0;
        strokes = 0;
        sand = 0;
        fairway = 0;
        greenInRegulation = 0;
    }
    public void UpdateStats(int nStrokes, int nPutts, int nSand, int nFairway, int nGreenInRegulation) {
        putts += nPutts;
        strokes += nStrokes;
        sand += nSand;
        fairway += nFairway;
        greenInRegulation += nGreenInRegulation;
    }
    public void DeleteStats(int nStrokes, int nPutts, int nSand, int nFairway, int nGreenInRegulation) {
        putts -= nPutts;
        strokes -= nStrokes;
        sand -= nSand;
        fairway -= nFairway;
        greenInRegulation -= nGreenInRegulation;
    }
}
