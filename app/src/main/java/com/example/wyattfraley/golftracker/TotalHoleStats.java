package com.example.wyattfraley.golftracker;

import java.io.Serializable;

public class TotalHoleStats implements Serializable {
    /*
     * For some reason having this class inherit from Score
     * messes up the serializing so it doesn't get saved properly.
     * Not sure why.
     */

    public int Strokes;
    public int Putts;
    public int Sand;

    TotalHoleStats() {
        Putts = 0;
        Strokes = 0;
        Sand = 0;
    }
    public void UpdateStats(int strokes, int putts, int sand) {
        Putts += putts;
        Strokes += strokes;
        Sand += sand;
    }
    public void DeleteStats(int strokes, int putts, int sand) {
        Putts -= putts;
        Strokes -= strokes;
        Sand -= sand;
    }
}
