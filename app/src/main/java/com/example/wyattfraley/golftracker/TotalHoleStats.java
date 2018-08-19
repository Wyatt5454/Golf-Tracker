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

    TotalHoleStats() {
        putts = 0;
        strokes = 0;
        sand = 0;
    }
    public void UpdateStats(int strokes, int putts, int sand) {
        putts += putts;
        strokes += strokes;
        sand += sand;
    }
    public void DeleteStats(int strokes, int putts, int sand) {
        putts -= putts;
        strokes -= strokes;
        sand -= sand;
    }
}
