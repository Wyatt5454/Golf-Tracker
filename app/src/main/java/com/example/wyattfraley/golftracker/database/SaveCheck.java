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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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

        DatabaseTest();
    }

    /**
     *  Here we grab all the data from the ScorecardActivity, open the database,
     *  and make a new entry.  Creates an AsyncTask so that the database
     *  saving does not take place on the main thread to prevent UI locking.
     */
    @SuppressLint("StaticFieldLeak")
    public void SaveRound() {

        App app = new App(new AppConfiguration.Builder(getString(R.string.AppID)).build());

        // Get the default Realm configuration
        SyncConfiguration defaultConfig = new SyncConfiguration.Builder(app.currentUser(), "RoundData")
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();

        Realm.setDefaultConfiguration(defaultConfig);

        // Open the Realm instance
        Realm.getInstanceAsync(defaultConfig, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                // Insert the score entry into the Realm database
                realm.executeTransactionAsync(transactionRealm -> {
                    //transactionRealm.beginTransaction();
                    transactionRealm.insert(toEnter);
                    RealmSchema schema = transactionRealm.getSchema();
                    Set<RealmObjectSchema> set = schema.getAll();

                    for (RealmObjectSchema objSchema : set ) {
                        for (String some : objSchema.getFieldNames() ) {
                            System.out.println(some);
                        }
                    }
                    //transactionRealm.commitTransaction();
                }, () -> {
                    System.out.println("Score entry inserted successfully");
                    realm.close();
                }, error -> {
                    System.out.println("Failed to insert score entry: " + error.getMessage());
                    realm.close();
                });

                RealmResults<RealmScoreEntry> results = realm.where(RealmScoreEntry.class).findAll();

                for (RealmScoreEntry entry : results ) {
                    System.out.println(entry.getFinalScore());
                }
            }

            @Override
            public void onError(Throwable exception) {
                System.out.println("Failed to open the Realm instance: " + exception.getMessage());
            }
        });
        // Now that the round is saved, we must update the totals, but only if
        // the round was complete.
        UpdateTotals(toEnter, IsFrontComplete(), IsBackComplete());

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
     *  This function will update the total scores on a file saved to
     *  internal storage.  Uses serializing to load in and save the
     *  stats object. Test
     */
    public void UpdateTotals(RealmScoreEntry myEntry, boolean frontComplete, boolean backComplete) {

        TotalRoundStats stats = new TotalRoundStats();
        FileInputStream inputStreamTotal = null;
        FileInputStream inputStreamHoles = null;
        boolean fileExists = false;

        // First we have to check if the file already exists.
        String[] filenames = fileList();
        for (String check : filenames) {
            if (check.equals("TotalStats.txt")) {
                fileExists = true;
                try {
                    inputStreamTotal = openFileInput("TotalStats.txt");
                    inputStreamHoles = openFileInput("HoleStats.txt");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        // Check to make sure the file was found.
        // If not, make the file.
        if (inputStreamTotal == null) {
            LoadScores(stats, myEntry, frontComplete, backComplete);
        }
        else {
            try {
                // Reading object in a file
                ObjectInputStream inTotal = new ObjectInputStream(inputStreamTotal);
                ObjectInputStream inHoles = new ObjectInputStream(inputStreamHoles);

                // Method for deserialization of object
                stats = (TotalRoundStats)inTotal.readObject();
                stats.holes = (ArrayList<TotalHoleStats>)inHoles.readObject();
                LoadScores(stats, myEntry, frontComplete, backComplete);

                inTotal.close();
                inHoles.close();
                inputStreamTotal.close();
                if (inputStreamHoles != null) {
                    inputStreamHoles.close();
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        SaveTotals(stats, fileExists);
    }

    /**
     * This function parses the data strings that were sent here from the ScorecardActivity.
     * Then it loads the new stats into the TotalRoundStats object before it is saved.
     */
    public void LoadScores(TotalRoundStats stats, RealmScoreEntry myEntry, boolean frontComplete, boolean backComplete) {

        int puttsFront = 0, puttsBack = 0;
        int penaltiesFront = 0, penaltiesBack = 0;
        int sandFront = 0, sandBack = 0;
        int fairwayFront = 0, fairwayBack = 0;
        int girFront = 0, girBack = 0;

        int mStroke, mPutt, mSand, mFairway, mGir, mPenalty, scoreFront = 0, scoreBack = 0;
        TotalHoleStats hole;

        RealmList<Integer> strokes = myEntry.getStrokes();
        RealmList<Integer> putts = myEntry.getPutts();
        RealmList<Integer> penalties = myEntry.getPenalties();
        RealmList<Integer> sand = myEntry.getSand();
        RealmList<Integer> fairway = myEntry.getFairway();
        RealmList<Integer> gir = myEntry.getGreenInRegulation();

        List<Integer> pars = InitializePars();

        if (stats.holes.size() == 0) {
            for (int z = 0; z < 18; z++) {
                TotalHoleStats nHole = new TotalHoleStats(z + 1, pars.get(z));
                stats.holes.add(nHole);
            }
        }

        // This loop parses the data into 18 holes and updates the stats.
        for (int i = 0; i < 9; i++) {
            mStroke = strokes.get(i);
            mPutt = putts.get(i);
            mPenalty = penalties.get(i);
            mSand = sand.get(i);
            mFairway = fairway.get(i);
            mGir = gir.get(i);

            hole = stats.holes.get(i);
            hole.UpdateStats(mStroke, mPutt, mPenalty, mSand, mFairway, mGir);

            scoreFront += mStroke;
            puttsFront += mPutt;
            penaltiesFront += mPenalty;
            sandFront += mSand;
            fairwayFront += mFairway;
            girFront += mGir;
        }
        for (int i = 9; i < 18; i++) {
            mStroke = strokes.get(i);
            mPutt = putts.get(i);
            mPenalty = penalties.get(i);
            mSand = sand.get(i);
            mFairway = fairway.get(i);
            mGir = gir.get(i);

            hole = stats.holes.get(i);
            hole.UpdateStats(mStroke, mPutt, mPenalty, mSand, mFairway, mGir);

            scoreBack += mStroke;
            puttsBack += mPutt;
            penaltiesBack += mPenalty;
            sandBack += mSand;
            fairwayBack += mFairway;
            girBack += mGir;
        }

        if (frontComplete) {
            stats.UpdateFrontTotals(scoreFront, puttsFront, penaltiesFront, sandFront, fairwayFront, girFront);
        }
        if (backComplete) {
            stats.UpdateBackTotals(scoreBack, puttsBack, penaltiesBack, sandBack, fairwayBack, girBack);
        }
        if (!frontComplete && !backComplete) {
            stats.UpdateTotalsIncomplete();
        }
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
        UpdateTotals(toEnter, true, true);
    }
}
