package com.example.wyattfraley.golftracker.database;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wyattfraley.golftracker.R;
import com.example.wyattfraley.golftracker.utilities.RealmObjectConverter;

import java.util.Calendar;
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
    RealmRoundScore toEnter;

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
        SerializableRoundScore serializedEntry = (SerializableRoundScore)myIntent.getSerializableExtra("Score");

        // Convert our serialized entry to a database entry
        toEnter = RealmObjectConverter.toRealmRoundScore(serializedEntry);

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
                                subscriptions.addOrUpdate(Subscription.create("userSubscription", realm.where(RealmRoundScore.class)));
                                subscriptions.addOrUpdate(Subscription.create("RealmHoleScore", realm.where(RealmHoleScore.class)));
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
        RealmList<RealmHoleScore> holes = toEnter.getScores();

        for (int i = 0; i < 9; i++) {
            if (holes.get(i).getStrokes() == 0) {
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
        RealmList<RealmHoleScore> holes = toEnter.getScores();

        for (int i = 9; i < 18; i++) {
            if (holes.get(i).getStrokes() == 0) {
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
