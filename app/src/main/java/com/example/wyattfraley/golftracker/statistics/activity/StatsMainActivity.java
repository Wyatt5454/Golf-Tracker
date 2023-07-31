package com.example.wyattfraley.golftracker.statistics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wyattfraley.golftracker.R;
import com.example.wyattfraley.golftracker.database.RealmScoreEntry;
import com.example.wyattfraley.golftracker.statistics.TotalRoundStats;
import com.example.wyattfraley.golftracker.database.activity.ShowAllHoles;
import com.example.wyattfraley.golftracker.database.activity.ShowAllRounds;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.annotation.Nullable;

import io.realm.ObjectChangeSet;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;
import io.realm.mongodb.sync.SyncSession;

/**
 * The main home page for the user to look at statistics.  Provides
 * buttons for showing every round played, and every hole average
 * statistics.
 */
public class StatsMainActivity extends AppCompatActivity {
    private Button showAllRounds;
    private Button showAllHoles;
    private TextView showTotalStats;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_stats_main);

        showAllRounds = findViewById(R.id.ViewAllRounds);
        showTotalStats = findViewById(R.id.StatsHolder);
        showAllHoles = findViewById(R.id.viewAllHoles);

        showAllRounds.setOnClickListener(v -> LoadAllRounds());
        showAllHoles.setOnClickListener(v -> LoadHoleStats());

        LoadTotalStats();
    }

    /**
     * Goes to the All Rounds stats activity
     */
    public void LoadAllRounds() {
        Intent myIntent = new Intent(StatsMainActivity.this, ShowAllRounds.class);
        startActivity(myIntent);
    }

    /**
     * Goes to the individual hole stats activity
     */
    public void LoadHoleStats() {
        Intent myIntent = new Intent(StatsMainActivity.this, ShowAllHoles.class);
        startActivity(myIntent);
    }

    /**
     * Grabs the total stats from a file locally stored
     * on the device.
     */
    public TotalRoundStats LoadTotalStats() {
        System.out.println("LoadTotalStats");
        TotalRoundStats stats = new TotalRoundStats();

        App app = new App(new AppConfiguration.Builder(getString(R.string.AppID)).build());

        String email = "wyatt.fraley@gmail.com";
        String password = "password";
        Credentials creds = Credentials.emailPassword(email, password);

        // Set the default Realm Config as a SyncConfiguration
        SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(app.currentUser())
                .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                    @Override
                    public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                        subscriptions.addOrUpdate(Subscription.create("userSubscription", realm.where(RealmScoreEntry.class)));
                    }
                })
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(false)
                .waitForInitialRemoteData()
                .build();

        // Log in to the app
        app.loginAsync(creds, authResult -> {
            if (authResult.isSuccess()) {
                User user = app.currentUser();
                System.out.println("User logged in successfully: " + user.getMongoClient("mongodb-atlas"));



                Realm.getInstanceAsync(syncConfiguration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        System.out.println("Got our realm successfully");
                        realm.refresh();
                        RealmResults<RealmScoreEntry> results = realm.where(RealmScoreEntry.class).beginsWith("_id", "Tue").findAll();

                        for (RealmScoreEntry score : results) {
                            System.out.println("score entry is valid: " + score.isValid());
                            System.out.println("score entry is loaded: " + score.isLoaded());
                            System.out.println("score entry is frozen: " + score.isFrozen());
                            System.out.println("score entry is managed: " + score.isManaged());

                            System.out.println(score.getFinalScore());
                            int frontStrokes = 0;
                            int backStrokes = 0;

                            RealmList<Integer> strokes = score.getStrokes();
                            System.out.println("strokes loaded: " + strokes.isLoaded());
                            System.out.println("strokes entry is valid: " + strokes.isValid());
                            System.out.println("strokes entry is frozen: " + strokes.isFrozen());
                            System.out.println("strokes entry is managed: " + strokes.isManaged());

                            for (int i = 0; i < 9; i++) {
                                frontStrokes += strokes.get(i);
                            }
                            for (int i = 9; i < 18; i++) {
                                backStrokes += strokes.get(i);
                            }

                            stats.UpdateFrontTotals(frontStrokes, 0, 0,0,0,0);
                            stats.UpdateBackTotals(backStrokes,0,0,0,0,0);


                        }
                        DisplayTotalStats(stats);

//                        results.addChangeListener(realmScoreEntries -> {
//                            RealmScoreEntry entry = realmScoreEntries.get(0);
//
//                            System.out.println(entry.getFinalScore());
//                            for (RealmScoreEntry score : realmScoreEntries) {
//                                System.out.println("score entry is valid: " + score.isValid());
//                                System.out.println("score entry is loaded: " + score.isLoaded());
//                                System.out.println("score entry is frozen: " + score.isFrozen());
//                                System.out.println("score entry is managed: " + score.isManaged());
//
//                                System.out.println(score.getFinalScore());
//                                int frontStrokes = 0;
//                                int backStrokes = 0;
//
//                                RealmList<Integer> strokes = score.getStrokes();
//                                System.out.println("strokes loaded: " + strokes.isLoaded());
//                                System.out.println("strokes entry is valid: " + strokes.isValid());
//                                System.out.println("strokes entry is frozen: " + strokes.isFrozen());
//                                System.out.println("strokes entry is managed: " + strokes.isManaged());
//
//                                for (int i = 0; i < 9; i++) {
//                                    frontStrokes += strokes.get(i);
//                                }
//                                for (int i = 9; i < 18; i++) {
//                                    backStrokes += strokes.get(i);
//                                }
//
//                                stats.UpdateFrontTotals(frontStrokes, 0, 0,0,0,0);
//                                stats.UpdateBackTotals(backStrokes,0,0,0,0,0);
//
//
//                            }
//                            DisplayTotalStats();
//                        });
                    }
                });



                // TODO: Attach some kind of listener to the RealmScoreEntry's or wait for them to be loaded.
                // Getting the proper numbers from Realm but each entry does not have the data in it.

                //realm.close();

            } else {
                System.out.println("Failed to log in: " + authResult.getError().getErrorMessage());
            }
        });

        return stats;
    }

    /**
     * This function shows the average stats for all rounds
     * and puts it in the main text box at the top of the activity.
     * Will be adding more detailed stats later.
     *
     * Gets rid of the stats buttons if there are no rounds
     * to show.
     */
    public void DisplayTotalStats(TotalRoundStats stats) {

        if (stats.totalRoundsFront > 0 && stats.totalRoundsBack > 0) {
            BackAndFront(stats);
        }
        else if (stats.totalRoundsFront > 0) {
            OnlyFront(stats);
        }
        else if (stats.totalRoundsBack > 0) {
            OnlyBack(stats);
        }
        else if (stats.totalRounds > 0) {
            showTotalStats.setText(R.string.stats_no_complete_rounds);
        }
        else {
            showTotalStats.setText(R.string.stats_no_rounds);
            showAllHoles.setVisibility(View.GONE);
            showAllRounds.setVisibility(View.GONE);
        }
    }

    /**
     * Displays the general stats for a round.  That's the total score,
     * the front 9 score, the back 9 score, putts, bunkers, penalties,
     * fairways and greens in regulation
     *
     * @param stats the stats to display
     */
    private void BackAndFront(TotalRoundStats stats) {
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        String toDisplay = " Average Overall Statistics\n\n";

        float frontAverage = (float)stats.totalFrontScore / (float)stats.totalRoundsFront;
        float backAverage = (float)stats.totalBackScore / (float)stats.totalRoundsBack;
        float totalAverage = frontAverage + backAverage;

        float frontPutts = (float)stats.totalFrontPutts / (float)stats.totalRoundsFront;
        float backPutts = (float)stats.totalBackPutts / (float)stats.totalRoundsBack;
        float totalPutts = frontPutts + backPutts;

        float frontSand = (float)stats.totalFrontSand / (float)stats.totalRoundsFront;
        float backSand = (float)stats.totalBackSand / (float)stats.totalRoundsBack;
        float totalSand = frontSand + backSand;

        float frontPenalties = (float)stats.totalFrontPenalties / (float)stats.totalRoundsFront;
        float backPenalties = (float)stats.totalBackPenalties / (float)stats.totalRoundsBack;
        float totalPenalties = frontPenalties + backPenalties;

        float totalFairway = (((float)stats.totalFrontFairway + (float)stats.totalBackFairway)
                / (((float)stats.totalRoundsFront + (float)stats.totalRoundsBack) * 7)) * 100;

        float totalGIR = (((float)stats.totalFrontGIR + (float)stats.totalBackGIR)
                / (((float)stats.totalRoundsFront + (float)stats.totalRoundsBack) * 9)) * 100;

        toDisplay += " Score: " + dF.format(totalAverage) + "\n";
        toDisplay += " Front: " + dF.format(frontAverage) + "\n";
        toDisplay += " Back: " + dF.format(backAverage) + "\n\n";
        toDisplay += " Putts: " + dF.format(totalPutts) + "\n";
        toDisplay += " Sand Traps Hit: " + dF.format(totalSand) + "\n";
        toDisplay += " Penalty Strokes: " + dF.format(totalPenalties) + "\n\n";
        toDisplay += " Fairway in Regulation: " + dF.format(totalFairway) + "%\n";
        toDisplay += " Green in Regulation: " + dF.format(totalGIR) + "%";

        showTotalStats.setText(toDisplay);
    }

    /**
     * Display the stats only for the front 9.  That's score, putts,
     * bunkers, penalties, fairways and greens in regulation.
     *
     * @param stats the stats to display
     */
    private void OnlyFront(TotalRoundStats stats) {
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        String toDisplay = " Average Overall Statistics\n\n";

        float frontAverage = (float)stats.totalFrontScore / (float)stats.totalRoundsFront;
        float frontPutts = (float)stats.totalFrontPutts / (float)stats.totalRoundsFront;
        float frontSand = (float)stats.totalFrontSand / (float)stats.totalRoundsFront;
        float frontPenalties = (float)stats.totalFrontPenalties / (float)stats.totalRoundsFront;
        float frontFairway = ((float)stats.totalFrontFairway / (stats.totalRoundsFront * 7)) * 100;
        float frontGIR = ((float)stats.totalFrontGIR / (stats.totalRoundsFront * 9)) * 100;

        toDisplay += " Front: " + dF.format(frontAverage) + "\n\n";
        toDisplay += " Putts: " + dF.format(frontPutts) + "\n";
        toDisplay += " Sand Traps Hit: " + dF.format(frontSand) + "\n";
        toDisplay += " Penalties: " + dF.format(frontPenalties) + "\n\n";
        toDisplay += " Fairway in Regulation: " + dF.format(frontFairway) + "%\n";
        toDisplay += " Green in Regulation: " + dF.format(frontGIR) + "%";

        showTotalStats.setText(toDisplay);
    }

    /**
     * Display the stats only for the back 9.  That's score, putts,
     * bunkers, penalties, fairways and greens in regulation.
     *
     * @param stats the stats to display
     */
    private void OnlyBack(TotalRoundStats stats) {
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        String toDisplay = " Average Overall Statistics\n\n";

        float backAverage = (float)stats.totalBackScore / (float)stats.totalRoundsBack;
        float backPutts = (float)stats.totalBackPutts / (float)stats.totalRoundsBack;
        float backSand = (float)stats.totalBackSand / (float)stats.totalRoundsBack;
        float backPenalties = (float)stats.totalBackPenalties / (float)stats.totalRoundsFront;
        float backFairway = ((float)stats.totalBackFairway / (stats.totalRoundsBack * 7)) * 100;
        float backGIR = ((float)stats.totalBackGIR / (stats.totalRoundsBack * 9)) * 100;

        toDisplay += " Back: " + dF.format(backAverage) + "\n\n";
        toDisplay += " Putts: " + dF.format(backPutts) + "\n";
        toDisplay += " Sand Traps Hit: " + dF.format(backSand) + "\n";
        toDisplay += " Penalties: " + dF.format(backPenalties) + "\n\n";
        toDisplay += " Fairway in Regulation: " + dF.format(backFairway) + "%\n";
        toDisplay += " Green in Regulation: " + dF.format(backGIR) + "%";

        showTotalStats.setText(toDisplay);
    }
}
