package com.example.wyattfraley.golftracker;

import java.io.Serializable;

public class TotalHoleStats implements Serializable {
    /*
     * For some reason having this class inherit from Score
     * messes up the serializing so it doesn't get saved properly.
     * Not sure why.
     */

    public int par;
    public int number;
    public int timesPlayed;
    public int strokes;
    public int putts;
    public int penalties;
    public int sand;
    public int fairway;
    public int greenInRegulation;

    public TotalHoleStats(int nNumber, int nPar) {
        number = nNumber;
        par = nPar;
        timesPlayed = 0;
        putts = 0;
        strokes = 0;
        penalties = 0;
        sand = 0;
        fairway = 0;
        greenInRegulation = 0;
    }
    public void UpdateStats(int nStrokes, int nPutts, int nPenalties, int nSand, int nFairway, int nGreenInRegulation) {
        if (nStrokes > 0) {
            timesPlayed++;
            putts += nPutts;
            strokes += nStrokes;
            penalties += nPenalties;
            sand += nSand;
            fairway += nFairway;
            greenInRegulation += nGreenInRegulation;
        }
    }
    public void DeleteStats(int nStrokes, int nPutts, int nPenalties, int nSand, int nFairway, int nGreenInRegulation) {
        if (nStrokes > 0) {
            timesPlayed--;
            putts -= nPutts;
            strokes -= nStrokes;
            penalties -= nPenalties;
            sand -= nSand;
            fairway -= nFairway;
            greenInRegulation -= nGreenInRegulation;
        }
    }
}
