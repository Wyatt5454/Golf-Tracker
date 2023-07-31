package com.example.wyattfraley.golftracker.database;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.wyattfraley.golftracker.R;

/**
 * Activity for deleting a round.
 *
 * TODO: Give the user a warning that this cannot be undone.
 */
public class DeleteRound extends SaveCheck {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.delete_window);

        DisplayMetrics dM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dM);

        int width = dM.widthPixels;
        int height = dM.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .3));

        yes = findViewById(R.id.DeleteYes);
        no = findViewById(R.id.DeleteNo);
        yes.setOnClickListener(v -> delete());
        no.setOnClickListener(v -> NoPress());
    }

    /**
     * Deletes the round from the database, and then updates the total
     * stats using an inherited function from SaveCheck.
     */
    public void delete() {

        Intent myIntent = getIntent();


        final RealmScoreEntry toDelete = (RealmScoreEntry)myIntent.getSerializableExtra("Score");

        // TODO: Delete round from Realm database
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }
        }.execute();

        setResult(RESULT_OK, null);
        finish();
    }
}