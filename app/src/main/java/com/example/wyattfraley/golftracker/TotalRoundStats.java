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
    public List<TotalHoleStats> holes;

    public TotalRoundStats() {
        totalPutts = 0;
        totalRounds = 0;
        totalSand = 0;
        totalScore = 0;
        holes = new ArrayList<>();
    }

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
    public void UpdateTotals(int score, int putts, int sand) {
        totalScore += score;
        totalPutts += putts;
        totalSand += sand;
        totalRounds++;
    }
}
