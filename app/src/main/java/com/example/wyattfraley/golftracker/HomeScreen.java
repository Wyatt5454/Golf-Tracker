package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.wyattfraley.golftracker.database.RealmScoreEntry;
import com.example.wyattfraley.golftracker.scorecard.activity.ScorecardActivity;
import com.example.wyattfraley.golftracker.statistics.activity.StatsMainActivity;

import java.util.Calendar;
import java.util.Optional;
import java.util.Random;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.auth.EmailPasswordAuth;
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

        EmailPasswordAuth emailPasswordAuth = app.getEmailPassword();
        String email = "wyatt.fraley@gmail.com";
        String password = "password";
        Credentials creds = Credentials.emailPassword(email, password);

        // Register a new user
        emailPasswordAuth.registerUserAsync(email, password, result -> {
            if (result.isSuccess()) {
                System.out.println("User registered successfully");

                // Log in to the app
                app.loginAsync(creds, authResult -> {
                    if (authResult.isSuccess()) {
                        User user = app.currentUser();
                        System.out.println("User logged in successfully");
                        // Additional actions with the logged-in user
                    } else {
                        System.out.println("Failed to log in: " + authResult.getError().getErrorMessage());
                    }
                });

                User user = app.currentUser();
                String partitionValue = "My Project";
                SyncConfiguration config = new SyncConfiguration.Builder(
                        user,
                        partitionValue)
                        .build();
                uiThreadRealm = Realm.getInstance(config);
                addChangeListenerToRealm(uiThreadRealm);
            } else {
                System.out.println("Failed to register user: " + result.getError().getErrorMessage());
            }
        });
    }

    private void addChangeListenerToRealm(Realm realm) {
        // all tasks in the realm
        RealmResults<RealmScoreEntry> tasks = uiThreadRealm.where(RealmScoreEntry.class).findAllAsync();
        tasks.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<RealmScoreEntry>>() {
            @Override
            public void onChange(RealmResults<RealmScoreEntry> collection, OrderedCollectionChangeSet changeSet) {
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

}
