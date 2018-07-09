package com.example.wyattfraley.golftracker;

import java.util.ArrayList;
import java.util.List;

public class GolfCourse {
    String name;
    int numHoles;
    int par;
    List<Hole> holes;

    public GolfCourse(String newName, List<Hole> newHoles) {
        name = newName;
        numHoles = newHoles.size();
        holes = newHoles;
        par = 0;

        for (int i = 0; i < newHoles.size(); i++) {
            par += newHoles.get(i).par;
        }
    }
}
