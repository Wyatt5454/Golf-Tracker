package com.example.wyattfraley.golftracker.database;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wyattfraley.golftracker.R;
import com.example.wyattfraley.golftracker.statistics.TotalHoleStats;
import com.example.wyattfraley.golftracker.statistics.TotalRoundStats;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObjectSchema;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

/**
 * This activity pops up when the user clicks a button on the
 * ScorecardActivity indicating they would like to save the
 * round.  Gets the serializable score and assesses it.
 * Saves the round differently based on the state of
 * the round.
 */
public class SaveCheck extends Activity {
    /** Text on popup for saving a round.  Several options. */
    TextView saveText;
    Button yes;
    Button no;
    RealmScoreEntry toEnter;

    Realm uiThreadRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.save_window);

        DisplayMetrics dM = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dM);

        int width = dM.widthPixels;
        int height = dM.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .3));

        yes = findViewById(R.id.SaveYes);
        no = findViewById(R.id.SaveNo);
        saveText = findViewById(R.id.saveText);
        yes.setOnClickListener(v -> SaveRound());
        no.setOnClickListener(v -> NoPress());

        Intent myIntent = getIntent();
        SerializableScoreEntry serializedEntry = (SerializableScoreEntry)myIntent.getSerializableExtra("Score");

        // Convert our serialized entry to a database entry
        toEnter = serializedEntry.toRealmEntry();

        if (IsFrontComplete() && IsBackComplete()) {
            saveText.setText(R.string.ask_save);
        }
        else if (toEnter.getFinalScore() > 0){
            saveText.setText(R.string.ask_save_incomplete);
        }
        else {
            saveText.setText(R.string.ask_save_none_played);
            yes.setVisibility(View.GONE);
            no.setVisibility(View.GONE);
        }

        //DatabaseTest();
    }

    /**
     *  Here we grab all the data from the ScorecardActivity, open the database,
     *  and make a new entry.  Creates an AsyncTask so that the database
     *  saving does not take place on the main thread to prevent UI locking.
     */
    @SuppressLint("StaticFieldLeak")
    public void SaveRound() {

        App app = new App(new AppConfiguration.Builder(getString(R.string.AppID)).build());

        String email = "wyatt.fraley@gmail.com";
        String password = "password";
        Credentials creds = Credentials.emailPassword(email, password);



        // Log in to the app
        app.loginAsync(creds, authResult -> {
            if (authResult.isSuccess()) {
                User user = app.currentUser();
                System.out.println("User logged in successfully: " + user.getMongoClient("mongodb-atlas"));

                //actualDBPost(user);


                // Set the default Realm Config as a SyncConfiguration
                SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(app.currentUser())
                        .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                            @Override
                            public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                subscriptions.addOrUpdate(Subscription.create("userSubscription", realm.where(RealmScoreEntry.class)));
                            }
                        })
                        .allowQueriesOnUiThread(true)
                        .allowWritesOnUiThread(true)
                        .build();

                Realm realm = Realm.getInstance(syncConfiguration);

                realm.beginTransaction();
                realm.insertOrUpdate(toEnter);
                RealmSchema schema = realm.getSchema();
                Set<RealmObjectSchema> set = schema.getAll();

                for (RealmObjectSchema objSchema : set ) {
                    for (String some : objSchema.getFieldNames() ) {
                        System.out.println(some);
                    }
                }
                realm.commitTransaction();

                RealmResults<RealmScoreEntry> results = realm.where(RealmScoreEntry.class).findAll();

                for (RealmScoreEntry entry : results ) {
                    System.out.println(entry.getFinalScore());
                }

                realm.close();


            } else {
                System.out.println("Failed to log in: " + authResult.getError().getErrorMessage());
            }
        });

        setResult(RESULT_OK, null);
        finish();
    }


    /**
     * Checks to see if the front 9 is complete.  Meaning
     * every hole has a score that is non-zero
     * @return true if it's complete, false if not
     */
    public boolean IsFrontComplete() {
        RealmList<Integer> strokes = toEnter.getStrokes();

        for (int i = 0; i < 9; i++) {
            if (strokes.get(i) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks to see if the back 9 is complete.  Meaning
     * every hole has a score that is non-zero
     * @return true if it's complete, false if not
     */
    public boolean IsBackComplete() {
        RealmList<Integer> strokes = toEnter.getStrokes();

        for (int i = 9; i < 18; i++) {
            if (strokes.get(i) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Used for the user to click "no" when given the option
     * to save the round.  Calls "finish" which ends the activity.
     */
    public void NoPress() {
        finish();
    }

    /**
     * This function saves the newly updated stats object.
     * Saving is a little different depending on if the file already
     * exists or not. Uses serialization to save the object in a file
     * on internal storage.
     */
    private void SaveTotals(TotalRoundStats stats, boolean fileExists) {

        FileOutputStream outputStreamTotal;
        FileOutputStream outputStreamHoles;
        if (!fileExists) {
            try {
                outputStreamTotal = openFileOutput("TotalStats.txt", Context.MODE_PRIVATE);
                outputStreamHoles = openFileOutput("HoleStats.txt", Context.MODE_PRIVATE);
                ObjectOutputStream outTotal = new ObjectOutputStream(outputStreamTotal);
                ObjectOutputStream outHoles = new ObjectOutputStream(outputStreamHoles);

                // Serializes the objects.
                outTotal.writeObject(stats);
                outHoles.writeObject(stats.holes);

                outTotal.close();
                outHoles.close();
                outputStreamTotal.close();
                outputStreamHoles.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            File directory = getFilesDir();
            File fileTotal = new File(directory, "TotalStats.txt");
            File fileHoles = new File(directory, "HoleStats.txt");
            try {
                outputStreamTotal = new FileOutputStream(fileTotal);
                outputStreamHoles = new FileOutputStream(fileHoles);
                ObjectOutputStream outTotal = new ObjectOutputStream(outputStreamTotal);
                ObjectOutputStream outHoles = new ObjectOutputStream(outputStreamHoles);

                outTotal.writeObject(stats);
                outHoles.writeObject(stats.holes);

                outTotal.close();
                outHoles.close();
                outputStreamTotal.close();
                outputStreamHoles.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Hard-coded pars for WG&CC
     * @return A list of length 18 containing the pars.
     */
    public List<Integer> InitializePars() {
        /*
         * Assigns a par to each hole.
         */
        List<Integer> pars = new ArrayList<>();
        pars.add(5);
        pars.add(3);
        pars.add(4);
        pars.add(4);
        pars.add(4);
        pars.add(3);
        pars.add(5);
        pars.add(4);
        pars.add(4);
        pars.add(5);
        pars.add(4);
        pars.add(4);
        pars.add(5);
        pars.add(3);
        pars.add(4);
        pars.add(4);
        pars.add(3);
        pars.add(4);

        return pars;
    }

    /**
     * This function exists only to insert a few rounds into the database to use for testing.
     * Will not be included in the release version.
     */
    private void DatabaseTest() {



        RealmList<Integer> strokes = new RealmList<>();
        RealmList<Integer> putts = new RealmList<>();
        RealmList<Integer> penalties = new RealmList<>();
        RealmList<Integer> sand = new RealmList<>();
        RealmList<Integer> fairway = new RealmList<>();
        RealmList<Integer> gir = new RealmList<>();
        int finalScore = 0;

        Random rand = new Random();

        for (int j = 0; j < 18; j++) {
            int high = rand.nextInt(4) + 2;
            int low = rand.nextInt(3) + 1;
            int zeroOrOne = rand.nextInt(2);

            strokes.add(high);
            putts.add(low);
            penalties.add(zeroOrOne);
            sand.add(zeroOrOne);
            fairway.add(zeroOrOne);
            gir.add(zeroOrOne);
            finalScore += high;
        }
        final RealmScoreEntry toEnter = new RealmScoreEntry(Calendar.getInstance().getTime().toString(), strokes, putts, penalties, sand, fairway, gir, finalScore, 72);


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                App app = new App(new AppConfiguration.Builder(getString(R.string.AppID)).build());

                return null;
            }
        }.execute();
    }
}
