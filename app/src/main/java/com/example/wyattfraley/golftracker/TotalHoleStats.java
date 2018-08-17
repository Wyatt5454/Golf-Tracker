package com.example.wyattfraley.golftracker;

import java.io.Serializable;

public class TotalHoleStats extends Score implements Serializable {
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
