package com.example.wyattfraley.golftracker;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import java.util.Calendar;

public class SaveCheck extends Activity {
    Button Yes;
    Button No;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.save_window);

        DisplayMetrics Dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Dm);

        int width = Dm.widthPixels;
        int height = Dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .3));

        Yes = findViewById(R.id.SaveYes);
        No = findViewById(R.id.SaveNo);
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveRound();
            }
        });
    }

    public void SaveRound() {
        // Here we grab all the data from the ScorecardActivity, open the database,
        // and make a new entry.
        Intent MyIntent = getIntent();
        String Strokes = MyIntent.getStringExtra("Strokes");
        String Putts = MyIntent.getStringExtra("Putts");
        String Sand = MyIntent.getStringExtra("Sand");

        GolfDatabase Db = Room.databaseBuilder(getApplicationContext(), GolfDatabase.class, "score-db").build();

        ScoreEntry ToEnter = new ScoreEntry();
        ToEnter.SetUid(Calendar.getInstance().getTime());
        ToEnter.SetStrokes(Strokes);
        ToEnter.SetPutts(Putts);
        ToEnter.SetSand(Sand);


        Db.MyScoreEntryDao().insertAll(ToEnter);
    }
}
