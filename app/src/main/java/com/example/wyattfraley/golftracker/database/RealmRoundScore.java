package com.example.wyattfraley.golftracker.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RealmRoundScore extends RealmObject {

    @Required
    @PrimaryKey
    private String _id;

    private RealmList<RealmHoleScore> scores = new RealmList<RealmHoleScore>();
}
