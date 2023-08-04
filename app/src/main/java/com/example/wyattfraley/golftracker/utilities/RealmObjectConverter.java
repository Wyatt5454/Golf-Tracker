package com.example.wyattfraley.golftracker.utilities;

import com.example.wyattfraley.golftracker.database.RealmHoleScore;
import com.example.wyattfraley.golftracker.database.RealmRoundScore;
import com.example.wyattfraley.golftracker.database.SerializableRoundScore;
import com.example.wyattfraley.golftracker.scorecard.HoleScoreData;

public class RealmObjectConverter {

//    public static final RealmHoleScore toRealmHoleScore(HoleScoreData scoreData) {
//        return new RealmHoleScore(scoreData.getStrokes(), scoreData.getPutts(), scoreData.getPenalties(), scoreData.getSand(), scoreData.getFairway(), scoreData.getGreenInRegulation(), scoreData.getPar(), scoreData.getYards() );
//    }

    public static final RealmRoundScore toRealmRoundScore(SerializableRoundScore roundData) {
        RealmRoundScore roundScore = new RealmRoundScore();
        int finalScore = 0;

        for (int i = 0; i < 18; i++) {
            RealmHoleScore holeScore = new RealmHoleScore(roundScore.getRound_id(), roundData.getStrokes().get(i), roundData.getPutts().get(i), roundData.getPenalties().get(i), roundData.getSand().get(i), roundData.getFairway().get(i), roundData.getGreenInRegulation().get(i), 0, 0);
            roundScore.getScores().add(holeScore);
            finalScore += roundData.getStrokes().get(i);
        }

        roundScore.setFinalScore(finalScore);

        return roundScore;
    }
}
