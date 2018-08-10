package com.example.wyattfraley.golftracker;

import java.io.Serializable;

public class TotalHoleStats extends Score implements Serializable {
    TotalHoleStats() {
        Putts = 0;
        Strokes = 0;
        Sand = 0;
    }
}
