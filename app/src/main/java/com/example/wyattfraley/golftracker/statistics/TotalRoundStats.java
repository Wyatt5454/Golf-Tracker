package com.example.wyattfraley.golftracker.statistics;

import com.example.wyattfraley.golftracker.database.RealmScoreEntry;
import com.example.wyattfraley.golftracker.statistics.TotalHoleStats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class TotalRoundStats implements Serializable{
    public int totalRounds;
    public int totalRoundsFront;
    public int totalRoundsBack;
    public int totalFrontScore;
    public int totalBackScore;
    public int totalFrontPutts;
    public int totalBackPutts;
    public int totalFrontPenalties;
    public int totalBackPenalties;
    public int totalFrontSand;
    public int totalBackSand;
    public int totalFrontFairway;
    public int totalBackFairway;
    public int totalFrontGIR;
    public int totalBackGIR;
    public List<TotalHoleStats> holes;

    public TotalRoundStats() {
        totalFrontPutts = 0;
        totalBackPutts = 0;
        totalFrontPenalties = 0;
        totalBackPenalties = 0;
        totalRounds = 0;
        totalRoundsFront = 0;
        totalRoundsBack = 0;
        totalFrontSand = 0;
        totalBackSand = 0;
        totalFrontScore = 0;
        totalBackScore = 0;
        totalFrontScore = 0;
        totalBackScore = 0;
        totalFrontFairway = 0;
        totalBackFairway = 0;
        totalFrontGIR = 0;
        totalBackGIR = 0;
        holes = new ArrayList<>();
    }

    public void updateFromRealm(RealmScoreEntry score ) {

        RealmList<Integer> strokes = score.getStrokes();

        for (int i = 0; i < 9; i++) {
            totalFrontScore += score.getStrokes().get(i);
            totalFrontPutts += score.getPutts().get(i);
            totalFrontPenalties += score.getPenalties().get(i);
            totalFrontSand += score.getSand().get(i);
            totalFrontFairway += score.getFairway().get(i);
            totalFrontGIR += score.getGreenInRegulation().get(i);
        }
        for (int i = 9; i < 18; i++) {
            totalBackScore += strokes.get(i);
            totalBackScore += score.getStrokes().get(i);
            totalBackPutts += score.getPutts().get(i);
            totalBackPenalties += score.getPenalties().get(i);
            totalBackSand += score.getSand().get(i);
            totalBackFairway += score.getFairway().get(i);
            totalBackGIR += score.getGreenInRegulation().get(i);
        }
        totalRounds++;
        totalRoundsBack++;
        totalRoundsFront++;
    }

}
