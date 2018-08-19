package com.example.wyattfraley.golftracker;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GolfCourse {
    String Name;
    int NumHoles;
    int par;
    List<Hole> holes;

    public GolfCourse(String newName, List<String> CardInfo) {
        int num = 1;
        int i = 0;
        int j = 0;
        holes = new ArrayList<>();
        par = 0;
        NumHoles = 0;

        // This while loop parses the card information from the file and puts it
        // all in a GolfCourse object.
        while (holes.size() < 18) {
            String newHandicap = "";
            while (i < CardInfo.get(0).length() && CardInfo.get(0).charAt(i) != ' ') {
                newHandicap += CardInfo.get(0).charAt(i);
                i++;
            }
            i++;

            String NewPar = "";
            while (j < CardInfo.get(1).length() && CardInfo.get(1).charAt(j) != ' ') {
                NewPar += CardInfo.get(1).charAt(j);
                par += CardInfo.get(1).charAt(j);
                j++;
            }
            j++;

            Hole NewHole = new Hole(num, Integer.parseInt(newHandicap), Integer.parseInt(NewPar));
            holes.add(NewHole);
            num++;
        }

        Name = newName;
        NumHoles = holes.size();
    }
}
