package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.wyattfraley.golftracker.database.ScoreEntry;
import com.example.wyattfraley.golftracker.scorecard.activity.ScorecardActivity;
import com.example.wyattfraley.golftracker.statistics.activity.StatsMainActivity;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

/**
 * Activity representing the home screen.  This is the
 * first thing users will see when they open the app.
 * Currently presents a button to display the scorecard
 * for a new round, and a button to launch the statistics
 * tracking.
 */
public class HomeScreen extends AppCompatActivity {
    public static final String TAG = "Golf Home Screen";
    private final static int VIBRATE_DURATION = 20;

    private Realm uiThreadRealm;

    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Log.i(TAG, "Golf application start.");

        // This checks to see if the app was reopened from the launcher,
        // and goes to the old version if another exists.
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {

            finish();
            return;
        }

        // Asks for permission to access location if the app doesn't already have it.
        AsyncTask<Void, Void, Void> locationPermissionAsk = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ActivityCompat.requestPermissions(HomeScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return null;
            }
        };
        locationPermissionAsk.execute();

        Realm.init(this);

        app = new App(new AppConfiguration.Builder(getString(R.string.AppID)).build());
        app.login(Credentials.anonymous());

        app.loginAsync(Credentials.anonymous(), result -> {
            if (result.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated anonymously.");
                User user = app.currentUser();
                String partitionValue = "My Project";
                SyncConfiguration config = new SyncConfiguration.Builder(
                        user,
                        partitionValue)
                        .build();
                uiThreadRealm = Realm.getInstance(config);
                addChangeListenerToRealm(uiThreadRealm);
                FutureTask<String> task = new FutureTask(new BackgroundQuickStart(app.currentUser()), "test");
                ExecutorService executorService = Executors.newFixedThreadPool(2);
                executorService.execute(task);
            } else {
                Log.e("QUICKSTART", "Failed to log in. Error: " + result.getError());
            }
        });
    }

    private void addChangeListenerToRealm(Realm realm) {
        // all tasks in the realm
        RealmResults<ScoreEntry> tasks = uiThreadRealm.where(ScoreEntry.class).findAllAsync();
        tasks.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<ScoreEntry>>() {
            @Override
            public void onChange(RealmResults<ScoreEntry> collection, OrderedCollectionChangeSet changeSet) {
                // process deletions in reverse order if maintaining parallel data structures so indices don't change as you iterate
                OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                for (OrderedCollectionChangeSet.Range range : deletions) {
                    Log.v("QUICKSTART", "Deleted range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));
                }
                OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                for (OrderedCollectionChangeSet.Range range : insertions) {
                    Log.v("QUICKSTART", "Inserted range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));                            }
                OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                for (OrderedCollectionChangeSet.Range range : modifications) {
                    Log.v("QUICKSTART", "Updated range: " + range.startIndex + " to " + (range.startIndex + range.length - 1));                            }
            }
        });
    }

    /**
     * Opens the scorecard activity.
     */
    public void OpenScorecardActivity(View view)  {

        VibrateOnClick();
        Intent myIntent = new Intent(HomeScreen.this, ScorecardActivity.class);
        startActivity(myIntent);
    }

    /**
     * Opens the main stats activity.
     */
    public void openStatsMainActivity(View view) {

        VibrateOnClick();
        Intent myIntent = new Intent(HomeScreen.this, StatsMainActivity.class);
        startActivity(myIntent);
    }

    /**
     * Generally called on a button click.  Makes the phone
     * vibrate to give user feedback that they properly pressed
     * the button.
     */
    private void VibrateOnClick() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(VIBRATE_DURATION);
    }

    public class BackgroundQuickStart implements Runnable {
        User user;
        public BackgroundQuickStart(User user) {
            this.user = user;
        }
        @Override
        public void run() {
            String partitionValue = "My Project";
            SyncConfiguration config = new SyncConfiguration.Builder(
                    user,
                    partitionValue)
                    .build();
            Realm backgroundThreadRealm = Realm.getInstance(config);
            ScoreEntry ScoreEntry = makeRandomScoreEntry();
            backgroundThreadRealm.executeTransaction (transactionRealm -> {
                transactionRealm.insert(ScoreEntry);
            });
            // all ScoreEntryies in the realm
            RealmResults<ScoreEntry> scoreEntries = backgroundThreadRealm.where(ScoreEntry.class).findAll();
            ScoreEntry otherScoreEntry = scoreEntries.get(0);

            Optional.ofNullable(otherScoreEntry).ifPresent( entry -> {
                System.out.println(entry.getUId());
            });

            // because this background thread uses synchronous realm transactions, at this point all
            // transactions have completed and we can safely close the realm
            backgroundThreadRealm.close();
        }

        private ScoreEntry makeRandomScoreEntry() {

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
            final ScoreEntry toEnter = new ScoreEntry(Calendar.getInstance().getTime().toString(), strokes, putts, penalties, sand, fairway, gir, finalScore, 72);

            return toEnter;
        }
    }
}
