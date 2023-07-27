package com.example.wyattfraley.golftracker.statistics;

import com.example.wyattfraley.golftracker.statistics.TotalHoleStats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    public void UpdateFrontTotals(int scoreFront, int puttsFront, int penaltiesFront, int sandFront, int fairwayFront, int girFront) {
        totalFrontScore += scoreFront;
        totalFrontPutts += puttsFront;
        totalFrontPenalties += penaltiesFront;
        totalFrontSand += sandFront;
        totalFrontFairway += fairwayFront;
        totalFrontGIR += girFront;
        totalRoundsFront++;
    }
    public void UpdateBackTotals(int scoreBack, int puttsBack, int penaltiesBack, int sandBack, int fairwayBack, int girBack) {
        totalBackScore += scoreBack;
        totalBackPutts += puttsBack;
        totalBackPenalties += penaltiesBack;
        totalBackSand += sandBack;
        totalBackFairway += fairwayBack;
        totalBackGIR += girBack;
        totalRoundsBack++;
    }

}
