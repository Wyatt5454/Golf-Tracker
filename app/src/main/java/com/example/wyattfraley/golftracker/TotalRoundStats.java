package com.example.wyattfraley.golftracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TotalRoundStats implements Serializable{
    public int totalRounds;
    public int totalScore;
    public int totalScoreFront;
    public int totalScoreBack;
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
        totalScoreFront = 0;
        totalScoreBack = 0;
        holes = new ArrayList<>();
    }

    public void UpdateTotals(int scoreFront, int scoreBack, int putts, int sand, int fairway, int gir) {
        totalScore += scoreFront + scoreBack;
        totalScoreFront += scoreFront;
        totalScoreBack += scoreBack;
        totalPutts += putts;
        totalSand += sand;
        totalFairway += fairway;
        totalGIR += gir;
        totalRounds++;
    }
    public void DeleteTotals(int scoreFront, int scoreBack, int putts, int sand, int fairway, int gir) {
        totalScore -= scoreFront + scoreBack;
        totalScoreFront -= scoreFront;
        totalScoreBack -= scoreBack;
        totalPutts -= putts;
        totalSand -= sand;
        totalFairway -= fairway;
        totalGIR -= gir;
        totalRounds--;
    }
}
