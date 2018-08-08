package com.example.wyattfraley.golftracker;

import android.widget.TextView;

import java.util.Stack;

public class Score {
    Stack<String> Actions;
    TextView Hole;
    int Strokes;
    int Putts;
    int Sand;
    int Par;

    Score(TextView newHole) {
        Hole = newHole;
        Strokes = 0;
        Putts = 0;
        Sand = 0;
        Par = 0;
        Actions = new Stack<>();
    }

    String ToSaveFormat() {
        return Integer.toString(Strokes) + " " + Integer.toString(Putts) + " " + Integer.toString(Sand) + "\n";
    }
}
