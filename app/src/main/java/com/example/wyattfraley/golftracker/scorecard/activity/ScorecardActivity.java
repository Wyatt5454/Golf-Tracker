package com.example.wyattfraley.golftracker.scorecard.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.wyattfraley.golftracker.R;
import com.example.wyattfraley.golftracker.database.SaveCheck;
import com.example.wyattfraley.golftracker.database.SerializableRoundScore;
import com.example.wyattfraley.golftracker.scorecard.HoleScoreData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EmptyStackException;
import java.util.List;

/**
 * Activity for the scorecard.  This will be doing the bulk of the work
 * during a round.  Contains 18 holes, scores, strokes, penalties, and
 * location services for greens.
 */
public class ScorecardActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public static final String TAG = "Golf Scorecard Activity";

    /** Return code **/
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /** vibrate duration in milliseconds */
    private final static int VIBRATE_DURATION = 20;

    /** Holds the currently selected hole */
    HoleScoreData currentHole;

    /** List of TextViews for each hole.  Should be 18 */
    List<TextView> textHoles;

    /** List of Scores.  Should be 18 **/
    List<HoleScoreData> scores;

    /** Button to traverse to the next hole **/
    Button nextButton;

    /** Button to traverse to the previous hole **/
    Button prevButton;

    /** Checkbox for user input if they hit the fairway */
    CheckBox fairwayCheck;

    /** Checkbox for user input if they hit a sand trap */
    CheckBox sandCheck;

    /** Holds the distance to the front of the green */
    TextView toFront;

    /** Holds the distance to the middle of the green */
    TextView toMiddle;

    /** Holds the distance to the back of the green */
    TextView toBack;

    /** Google API for retrieving the location */
    private GoogleApiClient mGoogleApiClient;

    /** Location request passed to the google API */
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);
        Log.i(TAG, "Golf scorecard start.");

        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        sandCheck = findViewById(R.id.checkSand);
        fairwayCheck = findViewById(R.id.checkFairway);
        toFront = findViewById(R.id.toFront);
        toMiddle = findViewById(R.id.toMiddle);
        toBack = findViewById(R.id.toBack);


        // Grab all the spots for scores and put them in a container.
        scores = InitializeScores();

        // Now we have to initialize the TextViews for each hole.
        InitializeHoles();

        // Set the current hole.
        currentHole = scores.get(0);
        currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 100)        // .5 seconds, in milliseconds
                .setFastestInterval(5 * 100); // .5 second, in milliseconds
    }

    /**
     * This function will save the current state of the UI if
     * the OS decides that the local memory needs to be
     * freed up for other applications.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle state) {

        Log.i(TAG, "Saving instance state");

        super.onSaveInstanceState(state);

        List<Integer> mStrokes = new ArrayList<>();
        List<Integer> mPutts = new ArrayList<>();
        List<Integer> mPenalties = new ArrayList<>();
        List<Integer> mSand = new ArrayList<>();
        List<Boolean> mFairway = new ArrayList<>();
        List<Boolean> mGIR = new ArrayList<>();

        for (int i = 0; i < scores.size(); i++) {
            HoleScoreData score = scores.get(i);

            mStrokes.add(score.getStrokes());
            mPutts.add(score.getPutts());
            mPenalties.add(score.getPenalties());
            mSand.add(score.getSand());
            mFairway.add(true);
            mGIR.add(true);
        }

        SerializableRoundScore myEntry = new SerializableRoundScore(mStrokes, mPutts, mPenalties, mSand, mFairway, mGIR);

        state.putSerializable("score", myEntry);
        state.putInt("current", currentHole.getNumber());
    }

    /**
     * This function restores the state of the UI once the user returns
     * to the app
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        SerializableRoundScore myScore = (SerializableRoundScore) savedInstanceState.getSerializable("score");
        Log.i(TAG, "RestoreFromHomeScreen grabbed the serial object.");
        Log.i(TAG, String.format("Serial object has strokes of size: %d", myScore.getStrokes().size()));

        List<Integer> mStrokes = myScore.getStrokes();
        List<Integer> mPutts = myScore.getPutts();
        List<Integer> mSand = myScore.getSand();
        List<Boolean> mFairway = myScore.getFairway();
        List<Boolean> mGIR = myScore.getGreenInRegulation();

        HoleScoreData score;
        int strokes;
        int afterNine = 0;
        int afterEighteen = 0;
        for (int i = 0; i < 9; i++) {
            score = scores.get(i);
            strokes = mStrokes.get(i);
            score.setStrokes(strokes);
            score.setPutts(mPutts.get(i));
            score.setSand(mSand.get(i));
            score.setFairway(mFairway.get(i));
            score.setGreenInRegulation(mGIR.get(i));

            MarkScoreSpecific(score);
            Log.i(TAG, String.format("Score %d marked.", i + 1));

            afterNine += strokes;
        }
        Log.i(TAG, "Restored through nine");
        for (int i = 9; i < 18; i++) {
            score = scores.get(i);
            strokes = mStrokes.get(i);
            score.setStrokes(strokes);
            score.setPutts(mPutts.get(i));
            score.setSand(mSand.get(i));
            score.setFairway(true);
            score.setGreenInRegulation(true);

            MarkScoreSpecific(score);
            Log.i(TAG, String.format("Score %d marked.", i + 1));

            afterEighteen += strokes;
        }
        Log.i(TAG, "Restored through eighteen");

        TextView ninth = findViewById(R.id.tv20);
        TextView eighteenth = findViewById(R.id.tv40);

        ninth.setText(String.format("%d", afterNine));
        eighteenth.setText(String.format("%d", afterEighteen));

        int current = savedInstanceState.getInt("current");
        currentHole = scores.get(current - 1);
        currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            handleNewLocation(location);
        } catch (SecurityException ignored) {
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = null;
        try {
            if (checkLocationPermission()) {
                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        } catch (Exception ignored) {
        }

        if (location != null) {
            handleNewLocation(location);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            handleNewLocation(location);
        } catch (SecurityException ignored) {
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        handleNewLocation(location);
    }

    private void handleNewLocation(Location location) {
        if (location != null) {
            Log.d(TAG, location.toString());
            DecimalFormat dF = new DecimalFormat("##.##");
            dF.setRoundingMode(RoundingMode.UNNECESSARY);

            float distance = location.distanceTo(currentHole.getLocationData().middle);
            String toDisplay = String.format("%d yds to middle", (int) (distance * 1.09361));
            toMiddle.setText(toDisplay);
            distance = location.distanceTo(currentHole.getLocationData().back);
            toDisplay = String.format("%d yds to back", (int) (distance * 1.09361));
            toBack.setText(toDisplay);
            distance = location.distanceTo(currentHole.getLocationData().front);
            toDisplay = String.format("%d yds to front", (int) (distance * 1.09361));
            toFront.setText(toDisplay);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_menu) {
            //  Here we create a pop up window asking if they are done with the round and want to save.
            // Have to convert the scores into a savable format
            Intent myIntent = new Intent(ScorecardActivity.this, SaveCheck.class);
            List<Integer> mStrokes = new ArrayList<>();
            List<Integer> mPutts = new ArrayList<>();
            List<Integer> mPenalties = new ArrayList<>();
            List<Integer> mSand = new ArrayList<>();
            List<Boolean> mFairway = new ArrayList<>();
            List<Boolean> mGIR = new ArrayList<>();
            int mFinal, mParPlayed = 0;

            for (int i = 0; i < scores.size(); i++) {
                HoleScoreData score = scores.get(i);

                mStrokes.add(score.getStrokes());
                mPutts.add(score.getPutts());
                mPenalties.add(score.getPenalties());
                mSand.add(score.getSand());
                mFairway.add(score.getFairway());
                mGIR.add(score.getGreenInRegulation());

                if (score.getStrokes() > 0) {
                    mParPlayed += score.getPar();
                }
            }
            TextView ninth = findViewById(R.id.tv20);
            TextView eighteenth = findViewById(R.id.tv40);
            mFinal = Integer.parseInt(ninth.getText().toString()) + Integer.parseInt(eighteenth.getText().toString());
            String uId = Calendar.getInstance().getTime().toString();
            SerializableRoundScore myEntry = new SerializableRoundScore(uId, mStrokes, mPutts, mPenalties, mSand, mFairway, mGIR, mFinal, mParPlayed);
            myIntent.putExtra("Score", myEntry);
            startActivityForResult(myIntent, 99);
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    /**
     * See if we have permission for location access on
     * this device
     * @return true if we have permission, false if we don't.
     */
    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Long and boring method which initializes all the text boxes for the scorecard.
     * Sets the number, par, and location data for each hole.
     */
    public void InitializeHoles() {


        textHoles = new ArrayList<>();
        TextView textView;


        InitializeSingleHole(R.id.tv1, 0, 5, R.dimen.one_front_lat,
                R.dimen.one_front_long, R.dimen.one_middle_lat, R.dimen.one_middle_long,
                R.dimen.one_back_lat, R.dimen.one_back_long);
        InitializeSingleHole(R.id.tv2, 1, 3, R.dimen.two_front_lat,
                R.dimen.two_front_long, R.dimen.two_middle_lat, R.dimen.two_middle_long,
                R.dimen.two_back_lat, R.dimen.two_back_long);
        InitializeSingleHole(R.id.tv3, 2, 4, R.dimen.three_front_lat,
                R.dimen.three_front_long, R.dimen.three_middle_lat, R.dimen.three_middle_long,
                R.dimen.three_back_lat, R.dimen.three_back_long);
        InitializeSingleHole(R.id.tv4, 3, 4, R.dimen.four_front_lat,
                R.dimen.four_front_long, R.dimen.four_middle_lat, R.dimen.four_middle_long,
                R.dimen.four_back_lat, R.dimen.four_back_long);
        InitializeSingleHole(R.id.tv5, 4, 4, R.dimen.five_front_lat,
                R.dimen.five_front_long, R.dimen.five_middle_lat, R.dimen.five_middle_long,
                R.dimen.five_back_lat, R.dimen.five_back_long);
        InitializeSingleHole(R.id.tv6, 5, 3, R.dimen.six_front_lat,
                R.dimen.six_front_long, R.dimen.six_middle_lat, R.dimen.six_middle_long,
                R.dimen.six_back_lat, R.dimen.six_back_long);
        InitializeSingleHole(R.id.tv7, 6, 5, R.dimen.seven_front_lat,
                R.dimen.seven_front_long, R.dimen.seven_middle_lat, R.dimen.seven_middle_long,
                R.dimen.seven_back_lat, R.dimen.seven_back_long);
        InitializeSingleHole(R.id.tv8, 7, 4, R.dimen.eight_front_lat,
                R.dimen.eight_front_long, R.dimen.eight_middle_lat, R.dimen.eight_middle_long,
                R.dimen.eight_back_lat, R.dimen.eight_back_long);
        InitializeSingleHole(R.id.tv9, 8, 4, R.dimen.nine_front_lat,
                R.dimen.nine_front_long, R.dimen.nine_middle_lat, R.dimen.nine_middle_long,
                R.dimen.nine_back_lat, R.dimen.nine_back_long);

        textView = findViewById(R.id.tv10);
        textHoles.add(textView);

        InitializeSingleHole(R.id.tv21, 9, 5, R.dimen.ten_front_lat,
                R.dimen.ten_front_long, R.dimen.ten_middle_lat, R.dimen.ten_middle_long,
                R.dimen.ten_back_lat, R.dimen.ten_back_long);
        InitializeSingleHole(R.id.tv22, 10, 4, R.dimen.eleven_front_lat,
                R.dimen.eleven_front_long, R.dimen.eleven_middle_lat, R.dimen.eleven_middle_long,
                R.dimen.eleven_back_lat, R.dimen.eleven_back_long);
        InitializeSingleHole(R.id.tv23, 11, 4, R.dimen.twelve_front_lat,
                R.dimen.twelve_front_long, R.dimen.twelve_middle_lat, R.dimen.twelve_middle_long,
                R.dimen.twelve_back_lat, R.dimen.twelve_back_long);
        InitializeSingleHole(R.id.tv24, 12, 5, R.dimen.thirteen_front_lat,
                R.dimen.thirteen_front_long, R.dimen.thirteen_middle_lat, R.dimen.thirteen_middle_long,
                R.dimen.thirteen_back_lat, R.dimen.thirteen_back_long);
        InitializeSingleHole(R.id.tv25, 13, 3, R.dimen.fourteen_front_lat,
                R.dimen.fourteen_front_long, R.dimen.fourteen_middle_lat, R.dimen.fourteen_middle_long,
                R.dimen.fourteen_back_lat, R.dimen.fourteen_back_long);
        InitializeSingleHole(R.id.tv26, 14, 4, R.dimen.fifteen_front_lat,
                R.dimen.fifteen_front_long, R.dimen.fifteen_middle_lat, R.dimen.fifteen_middle_long,
                R.dimen.fifteen_back_lat, R.dimen.fifteen_back_long);
        InitializeSingleHole(R.id.tv27, 15, 4, R.dimen.sixteen_front_lat,
                R.dimen.sixteen_front_long, R.dimen.sixteen_middle_lat, R.dimen.sixteen_middle_long,
                R.dimen.sixteen_back_lat, R.dimen.sixteen_back_long);
        InitializeSingleHole(R.id.tv28, 16, 3, R.dimen.seventeen_front_lat,
                R.dimen.seventeen_front_long, R.dimen.seventeen_middle_lat, R.dimen.seventeen_middle_long,
                R.dimen.seventeen_back_lat, R.dimen.seventeen_back_long);
        InitializeSingleHole(R.id.tv29, 17, 4, R.dimen.eighteen_front_lat,
                R.dimen.eighteen_front_long, R.dimen.eighteen_middle_lat, R.dimen.eighteen_middle_long,
                R.dimen.eighteen_back_lat, R.dimen.eighteen_back_long);


        textView = findViewById(R.id.tv30);
        textHoles.add(textView);
    }

    private void InitializeSingleHole(Integer viewID, Integer scoreToGet, Integer par,
                                      Integer frontLatID, Integer frontLongID, Integer midLatID, Integer midLongID,
                                      Integer backLatID, Integer backLongID) {
        TypedValue outValue = new TypedValue();
        getResources().getValue(frontLatID, outValue, true);
        float frontLat = outValue.getFloat();
        getResources().getValue(frontLongID, outValue, true);
        float frontLong = outValue.getFloat();
        getResources().getValue(midLatID, outValue, true);
        float midLat = outValue.getFloat();
        getResources().getValue(midLongID, outValue, true);
        float midLong = outValue.getFloat();
        getResources().getValue(backLatID, outValue, true);
        float backLat = outValue.getFloat();
        getResources().getValue(backLongID, outValue, true);
        float backLong = outValue.getFloat();


        TextView textView = findViewById(viewID);
        textHoles.add(textView);
        HoleScoreData score = scores.get(scoreToGet);
        score.setPar(par);
        score.setNumber(scoreToGet + 1);
        score.getLocationData().front.setLatitude(frontLat);
        score.getLocationData().front.setLongitude(frontLong);
        score.getLocationData().middle.setLatitude(midLat);
        score.getLocationData().middle.setLongitude(midLong);
        score.getLocationData().back.setLatitude(backLat);
        score.getLocationData().back.setLongitude(backLong);
    }

    public List<HoleScoreData> InitializeScores() {
        /*
         * Long boring method which sets up all the holes for scoring.
         */
        scores = new ArrayList<>();
        final HoleScoreData score1 = new HoleScoreData(findViewById(R.id.tv11));
        score1.setNumber(1);
        score1.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score1;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score1);
        final HoleScoreData score2 = new HoleScoreData(findViewById(R.id.tv12));
        score2.setNumber(2);
        score2.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score2;
            ScoreClick();
            fairwayCheck.setVisibility(View.GONE);
        });
        scores.add(score2);
        final HoleScoreData score3 = new HoleScoreData(findViewById(R.id.tv13));
        score3.setNumber(3);
        score3.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score3;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score3);
        final HoleScoreData score4 = new HoleScoreData(findViewById(R.id.tv14));
        score4.setNumber(4);
        score4.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score4;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score4);
        final HoleScoreData score5 = new HoleScoreData(findViewById(R.id.tv15));
        score5.setNumber(5);
        score5.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score5;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score5);
        final HoleScoreData score6 = new HoleScoreData(findViewById(R.id.tv16));
        score6.setNumber(6);
        score6.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score6;
            ScoreClick();
            fairwayCheck.setVisibility(View.GONE);
        });
        scores.add(score6);
        final HoleScoreData score7 = new HoleScoreData(findViewById(R.id.tv17));
        score7.setNumber(7);
        score7.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score7;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score7);
        final HoleScoreData score8 = new HoleScoreData(findViewById(R.id.tv18));
        score8.setNumber(8);
        score8.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score8;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score8);
        final HoleScoreData score9 = new HoleScoreData(findViewById(R.id.tv19));
        score9.setNumber(9);
        score9.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score9;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score9);
        final HoleScoreData score10 = new HoleScoreData(findViewById(R.id.tv31));
        score10.setNumber(10);
        score10.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score10;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score10);
        final HoleScoreData score11 = new HoleScoreData(findViewById(R.id.tv32));
        score11.setNumber(11);
        score11.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score11;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score11);
        final HoleScoreData score12 = new HoleScoreData(findViewById(R.id.tv33));
        score12.setNumber(12);
        score12.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score12;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score12);
        final HoleScoreData score13 = new HoleScoreData(findViewById(R.id.tv34));
        score13.setNumber(13);
        score13.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score13;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score13);
        final HoleScoreData score14 = new HoleScoreData(findViewById(R.id.tv35));
        score14.setNumber(14);
        score14.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score14;
            ScoreClick();
            fairwayCheck.setVisibility(View.GONE);
        });
        scores.add(score14);
        final HoleScoreData score15 = new HoleScoreData(findViewById(R.id.tv36));
        score15.setNumber(15);
        score15.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score15;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score15);
        final HoleScoreData score16 = new HoleScoreData(findViewById(R.id.tv37));
        score16.setNumber(16);
        score16.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score16;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score16);
        final HoleScoreData score17 = new HoleScoreData(findViewById(R.id.tv38));
        score17.setNumber(17);
        score17.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score17;
            ScoreClick();
            fairwayCheck.setVisibility(View.GONE);
        });
        scores.add(score17);
        final HoleScoreData score18 = new HoleScoreData(findViewById(R.id.tv39));
        score18.setNumber(18);
        score18.getHole().setOnClickListener(v -> {
            MarkScore();
            currentHole = score18;
            ScoreClick();
            fairwayCheck.setVisibility(View.VISIBLE);
        });
        scores.add(score18);

        return scores;
    }

    /**
     * Handles a new location whenever a new hole is clicked.
     */
    private void ScoreClick() {


        currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            handleNewLocation(location);
        } catch (SecurityException ignored) {
        }
        VibrateOnClick();
    }

    /**
     * Increments the selected hole
     * @param v
     */
    public void NextHole(View v) {

        // Since the hole numbers are indexed at 1 instead of 0
        // grabbing the score at the hole number is the one after
        // the current hole.
        int holeNumber = currentHole.getNumber();
        if (holeNumber < 18) {
            MarkScore();
            currentHole = scores.get(holeNumber);
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetBoxes();

            try {
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                handleNewLocation(location);
            } catch (SecurityException ignored) {
            }

        }
        VibrateOnClick();
    }

    /**
     * Decrements the selected hole
     * @param v
     */
    public void PrevHole(View v) {

        // Since the hole numbers are indexed at 1 instead of 0
        // grabbing the score at the hole number is the one after
        // the current hole.
        int holeNumber = currentHole.getNumber();
        if (holeNumber > 1) {
            MarkScore();
            currentHole = scores.get(holeNumber - 2);
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeselected));
            SetBoxes();

            try {
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                handleNewLocation(location);
            } catch (SecurityException ignored) {
            }
        }
        VibrateOnClick();
    }

    /**
     * This function is responsible for altering the look of the score
     * in the hole text-box. Double circle for eagle or better, single
     * circle for birdie, nothing for par, single square for bogey,
     * and double square for double bogey or worse.
     */
    private void MarkScore() {

        if (currentHole.getStrokes() == 0 || currentHole.getStrokes() == currentHole.getPar())
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeback));
        else if (currentHole.getStrokes() <= currentHole.getPar() - 2)
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.eagle));
        else if (currentHole.getStrokes() == currentHole.getPar() - 1)
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.birdie));
        else if (currentHole.getStrokes() == currentHole.getPar() + 1)
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.bogey));
        else if (currentHole.getStrokes() >= currentHole.getPar() + 2)
            currentHole.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.doublebogey));

    }

    private void MarkScoreSpecific(HoleScoreData score) {

        int strokes = score.getStrokes();
        if (strokes != 0) {
            score.getHole().setText(Integer.toString(strokes));
        }

        if (score.getStrokes() == 0 || score.getStrokes() == score.getPar())
            score.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.holeback));
        else if (score.getStrokes() <= score.getPar() - 2)
            score.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.eagle));
        else if (score.getStrokes() == score.getPar() - 1)
            score.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.birdie));
        else if (score.getStrokes() == score.getPar() + 1)
            score.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.bogey));
        else if (score.getStrokes() >= score.getPar() + 2)
            score.getHole().setBackground(AppCompatResources.getDrawable(this, R.drawable.doublebogey));
    }

    /**
     * Looks at the current hole to see if the fairway and sand
     * boxes are supposed to be marked, and sets them accordingly
     */
    private void SetBoxes() {

        if (currentHole.getPar() == 3) {
            fairwayCheck.setVisibility(View.GONE);
        } else {
            fairwayCheck.setVisibility(View.VISIBLE);
            fairwayCheck.setChecked(currentHole.getFairway());
        }
        sandCheck.setChecked(currentHole.getSand() != 0);
    }

    private void VibrateOnClick() {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(VIBRATE_DURATION);
    }

    public void AddScore(View v) {
        int intScore;
        currentHole.addStroke();
        intScore = currentHole.getStrokes();
        currentHole.getHole().setText(Integer.toString(intScore));
        currentHole.getActions().push(getString(R.string.stroke));

        if ((intScore - currentHole.getPutts()) < currentHole.getPar() - 1) {
            currentHole.setGreenInRegulation(true);
        } else {
            currentHole.setGreenInRegulation(false);
        }
        VibrateOnClick();
        updateTotals(1);
    }

    /**
     * Adds a putt to the currently selected hole. Increments
     * putts and strokes and putts a putt on the action stack
     * for the hole
     */
    public void AddPutt(View v) {
        int intScore;
        currentHole.addPutt();
        currentHole.addStroke();
        intScore = currentHole.getStrokes();
        currentHole.getHole().setText(Integer.toString(intScore));
        currentHole.getActions().push(getString(R.string.putt));

        updateTotals(1);
        VibrateOnClick();
    }

    /**
     * Adds a penalty stroke to the currently selected hole.
     * Increments strokes and penalties and putts a "penalty"
     * on the action stack
     */
    public void AddPenalty(View v) {
        currentHole.addStroke();
        currentHole.addPenalty();
        int intScore = currentHole.getStrokes();
        currentHole.getHole().setText(Integer.toString(intScore));
        currentHole.getActions().push(getString(R.string.penalty));

        updateTotals(1);
        VibrateOnClick();
    }

    @SuppressLint("SetTextI18n")
    public void UndoStroke(View v) {
        try {
            String lastAction = currentHole.getActions().pop();
            if (lastAction.equals(getString(R.string.putt))) {
                currentHole.removePutt();
                currentHole.removeStroke();
            } else if (lastAction.equals(getString(R.string.stroke))) {
                currentHole.removeStroke();
            } else if (lastAction.equals(getString(R.string.penalty))) {
                currentHole.removeStroke();
                currentHole.removePenalty();
            }

            if ((currentHole.getStrokes() - currentHole.getPutts()) < currentHole.getPar() - 1) {
                if (currentHole.getStrokes() > 0) {
                    currentHole.setGreenInRegulation(true);
                } else {
                    currentHole.setGreenInRegulation(false);
                }
            } else {
                currentHole.setGreenInRegulation(false);
            }
            if (currentHole.getStrokes() > 0) {
                currentHole.getHole().setText(Integer.toString(currentHole.getStrokes()));
            } else {
                currentHole.getHole().setText("");
            }

            updateTotals(-1);
            VibrateOnClick();
        } catch (EmptyStackException ignored) {
        }

    }

    public void SandChecked(View v) {
        if (sandCheck.isChecked()) {
            currentHole.setSand(1);
        } else {
            currentHole.setSand(0);
        }
    }

    public void FairwayChecked(View v) {
        if (fairwayCheck.isChecked()) {
            currentHole.setFairway(true);
        } else {
            currentHole.setFairway(false);
        }
    }

    private void updateTotals(int change) {
        /*
         * Updates the score boxes for each nine holes.
         * Used whenever a score box is changed.
         */
        TextView ninth = findViewById(R.id.tv20);
        TextView eighteenth = findViewById(R.id.tv40);
        String text;
        int current;

        if (change == 1) {
            if (currentHole.getNumber() < 10) {
                text = (String) ninth.getText();
                current = Integer.parseInt(text);
                current++;
                ninth.setText(Integer.toString(current));
            } else {
                text = (String) eighteenth.getText();
                current = Integer.parseInt(text);
                current++;
                eighteenth.setText(Integer.toString(current));
            }
        } else {
            if (currentHole.getNumber() < 10) {
                text = (String) ninth.getText();
                current = Integer.parseInt(text);
                current--;
                ninth.setText(Integer.toString(current));
            } else {
                text = (String) eighteenth.getText();
                current = Integer.parseInt(text);
                current--;
                eighteenth.setText(Integer.toString(current));
            }
        }
    }
}
