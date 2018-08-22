package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.app.Activity;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TotalRoundStats implements Serializable{
    public int totalRounds;
    public int totalScore;
    public int totalPutts;
    public int totalSand;
    public int totalFairway;
    public int totalGIR;
    public List<TotalHoleStats> holes;

    public TotalRoundStats() {
        totalPutts = 0;
        totalRounds = 0;
        totalSand = 0;
        totalScore = 0;
        holes = new ArrayList<>();
    }

    // Outdated save function
    public void Save(Activity saveActivity) {
        File file = new File( Environment.getExternalStorageDirectory() + "/Download/TotalStats.txt");
        file.setReadable(true);
        file.setWritable(true);
        file.setExecutable(true);


        ActivityCompat.requestPermissions(saveActivity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        try {
            FileOutputStream stream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(stream);

            // Serializes the object.
            out.writeObject(this);

            out.close();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Simply adds to the totals.
    public void UpdateTotals(int score, int putts, int sand, int fairway, int gir) {
        totalScore += score;
        totalPutts += putts;
        totalSand += sand;
        totalFairway += fairway;
        totalGIR += gir;
        totalRounds++;
    }
    // Deletes from the totals.
    public void DeleteTotals(int score, int putts, int sand, int fairway, int gir) {
        totalScore -= score;
        totalPutts -= putts;
        totalSand -= sand;
        totalFairway += fairway;
        totalGIR += gir;
        totalRounds--;
    }
}
