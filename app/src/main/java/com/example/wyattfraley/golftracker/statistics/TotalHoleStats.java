package com.example.wyattfraley.golftracker.statistics;

import java.io.Serializable;

public class TotalHoleStats implements Serializable {
    /*
     * For some reason having this class inherit from Score
     * messes up the serializing so it doesn't get saved properly.
     * Not sure why.
     */

    public int par;
    public int number;
    public int timesPlayed = 0;
    public int strokes = 0;
    public int putts = 0;
    public int penalties = 0;
    public int sand = 0;
    public int fairway = 0;
    public int greenInRegulation = 0;

    public TotalHoleStats(int nNumber, int nPar) {
        number = nNumber;
        par = nPar;
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
