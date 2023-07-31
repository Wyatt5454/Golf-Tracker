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

import java.math.RoundingMode;
import java.text.DecimalFormat;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

/**
 * The main home page for the user to look at statistics.  Provides
 * buttons for showing every round played, and every hole average
 * statistics.
 */
public class StatsMainActivity extends AppCompatActivity {

    /** Button for the ShowAllRounds Activity */
    private Button showAllRounds;

    /** Button for the ShowAllHoles Activity */
    private Button showAllHoles;

    /** The text view which will hold the compiled statistics */
    private TextView showTotalStats;

    /** Realm this class will use to get statistics */
    private Realm realm;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_stats_main);

        showAllRounds = findViewById(R.id.ViewAllRounds);
        showTotalStats = findViewById(R.id.StatsHolder);
        showAllHoles = findViewById(R.id.viewAllHoles);

        showAllRounds.setOnClickListener(v -> loadAllRounds());
        showAllHoles.setOnClickListener(v -> loadHoleStats());

        showTotalStats.setText(R.string.wait_for_stats);

        loadTotalStats();
    }

    /**
     * Goes to the All Rounds stats activity
     */
    public void loadAllRounds() {
        Intent myIntent = new Intent(StatsMainActivity.this, ShowAllRounds.class);
        startActivity(myIntent);
    }

    /**
     * Goes to the individual hole stats activity
     */
    public void loadHoleStats() {
        Intent myIntent = new Intent(StatsMainActivity.this, ShowAllHoles.class);
        startActivity(myIntent);
    }

    /**
     * Queries the Realm database for all the rounds associated with this user.
     * Compiles the stats and displays them on the main page.
     */
    public TotalRoundStats loadTotalStats() {
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


                /*
                 * TODO:  I think it'd be nice to have the compiled total statistics in a separate database.
                 *  We could have a java instance that continuously runs on the database and waits for changes,
                 *  then compiles the statistics on its own.  This way the device this app is installed on won't
                 *  have to compile statistics, which could be quite costly when there are many rounds.
                 */
                Realm.getInstanceAsync(syncConfiguration, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        RealmResults<RealmScoreEntry> results = realm.where(RealmScoreEntry.class).beginsWith("_id", "Tue").findAll();

                        for (RealmScoreEntry score : results) {
                            stats.updateFromRealm(score);
                        }
                        displayTotalStats(stats);
                    }
                });

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
    public void displayTotalStats(TotalRoundStats stats) {

        if (stats.totalRoundsFront > 0 && stats.totalRoundsBack > 0) {
            backAndFront(stats);
        }
        else if (stats.totalRoundsFront > 0) {
            onlyFront(stats);
        }
        else if (stats.totalRoundsBack > 0) {
            onlyBack(stats);
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
    private void backAndFront(TotalRoundStats stats) {
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
    private void onlyFront(TotalRoundStats stats) {
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
    private void onlyBack(TotalRoundStats stats) {
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
