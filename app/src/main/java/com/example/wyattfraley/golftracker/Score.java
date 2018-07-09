package com.example.wyattfraley.golftracker;

import android.widget.TextView;

import java.util.Stack;

public class Score {
    Stack<String> Actions;
    TextView Hole;
    int strokes;
    int putts;

    Score(TextView newHole) {
        Hole = newHole;
        strokes = 0;
        putts = 0;
        Actions = new Stack<>();
    }
}
