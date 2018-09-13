package com.example.wyattfraley.golftracker;

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

    public void UpdateFrontTotals(int scoreFront, int puttsFront, int sandFront, int fairwayFront, int girFront) {
        totalFrontScore += scoreFront;
        totalFrontPutts += puttsFront;
        totalFrontSand += sandFront;
        totalFrontFairway += fairwayFront;
        totalFrontGIR += girFront;
        totalRoundsFront++;
    }
    public void UpdateBackTotals(int scoreBack, int puttsBack, int sandBack, int fairwayBack, int girBack) {
        totalFrontScore += scoreBack;
        totalFrontPutts += puttsBack;
        totalFrontSand += sandBack;
        totalFrontFairway += fairwayBack;
        totalFrontGIR += girBack;
        totalRoundsBack++;
    }
    public void UpdateTotalsIncomplete() {
        totalRounds++;
    }
    public void DeleteTotals(int scoreFront, int scoreBack, int puttsFront, int puttsBack, int sandFront, int sandBack, int fairwayFront, int fairwayBack, int girFront, int girBack) {
        totalFrontScore -= scoreFront;
        totalBackScore -= scoreBack;
        totalFrontPutts -= puttsFront;
        totalBackPutts -= puttsBack;
        totalFrontSand -= sandFront;
        totalBackSand -= sandBack;
        totalFrontFairway -= fairwayFront;
        totalBackFairway -= fairwayBack;
        totalFrontGIR -= girFront;
        totalBackGIR -= girBack;
        totalRoundsFront--;
        totalRoundsBack--;
    }
    public void DeleteFrontTotals(int scoreFront, int puttsFront, int sandFront, int fairwayFront, int girFront) {
        totalFrontScore -= scoreFront;
        totalFrontPutts -= puttsFront;
        totalFrontSand -= sandFront;
        totalFrontFairway -= fairwayFront;
        totalFrontGIR -= girFront;
        totalRoundsFront--;
    }
    public void DeleteBackTotals(int scoreBack, int puttsBack, int sandBack, int fairwayBack, int girBack) {
        totalFrontScore -= scoreBack;
        totalFrontPutts -= puttsBack;
        totalFrontSand -= sandBack;
        totalFrontFairway -= fairwayBack;
        totalFrontGIR -= girBack;
        totalRoundsBack--;
    }
    public void DeleteTotalsIncomplete() {
        totalRounds--;
    }
}
