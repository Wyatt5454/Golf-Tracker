package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

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

public class ScorecardActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    Score currentHole;
    List<TextView> textHoles;
    List<Score> scores;
    Button nextButton;
    Button prevButton;
    CheckBox fairwayCheck;
    CheckBox sandCheck;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = MapsActivity.class.getSimpleName();
    TextView toFront;
    TextView toMiddle;
    TextView toBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));


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
        currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(15 * 100)        // 1.5 seconds, in milliseconds
                .setFastestInterval(15 * 100); // 3 second, in milliseconds
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        /*
         * This function will save the current state of the UI if
         * the OS decides that the local memory needs to be
         * freed up for other applications.
         */

        super.onSaveInstanceState(state);

        ArrayList<Integer> mStrokes = new ArrayList<>();
        ArrayList<Integer> mPutts = new ArrayList<>();
        ArrayList<Integer> mSand = new ArrayList<>();
        ArrayList<Integer> mFairway = new ArrayList<>();
        ArrayList<Integer> mGIR = new ArrayList<>();

        for (int i = 0; i < scores.size(); i++)
        {
            mStrokes.add(scores.get(i).getStrokes());
            mPutts.add(scores.get(i).getPutts());
            mSand.add(scores.get(i).getSand());
            mFairway.add(scores.get(i).getFairway());
            mGIR.add(scores.get(i).getGreenInRegulation());
        }

        ScoreEntry myEntry = new ScoreEntry(mStrokes, mPutts, mSand, mFairway, mGIR);

        state.putSerializable("score", myEntry);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        /*
         * This function restores the state of the UI once the user returns
         * to the app
         */
        super.onRestoreInstanceState(savedInstanceState);
        ScoreEntry myScore = (ScoreEntry)savedInstanceState.getSerializable("score");

        ArrayList<Integer> mStrokes = myScore.getStrokes();
        ArrayList<Integer> mPutts = myScore.getPutts();
        ArrayList<Integer> mSand = myScore.getSand();
        ArrayList<Integer> mFairway = myScore.getFairway();
        ArrayList<Integer> mGIR = myScore.getGreenInRegulation();

        Score score;
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

            afterNine += strokes;
        }
        for (int i = 9; i < 18; i++) {
            score = scores.get(i);
            strokes = mStrokes.get(i);
            score.setStrokes(strokes);
            score.setPutts(mPutts.get(i));
            score.setSand(mSand.get(i));
            score.setFairway(mFairway.get(i));
            score.setGreenInRegulation(mGIR.get(i));

            afterEighteen += strokes;
        }

        TextView ninth = findViewById(R.id.tv20);
        TextView eighteenth = findViewById(R.id.tv40);

        ninth.setText(String.format("%d", afterNine));
        eighteenth.setText(String.format("%d", afterEighteen));
    }

    @Override
    protected void onStart() {
        super.onStart();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ActivityCompat.requestPermissions(ScorecardActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return null;
            }
        }.execute();

    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = null;
        try {
            if (checkLocationPermission()) {
                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }
        catch (Exception e){}


        if (location != null) {
            handleNewLocation(location);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
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
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.UNNECESSARY);

        float distance = location.distanceTo(currentHole.locationData.middle);
        String toDisplay = String.format("%d yds to middle", (int)(distance * 1.09361));
        toMiddle.setText(toDisplay);
        distance = location.distanceTo(currentHole.locationData.back);
        toDisplay = String.format("%d yds to back", (int)(distance * 1.09361));
        toBack.setText(toDisplay);
        distance = location.distanceTo(currentHole.locationData.front);
        toDisplay = String.format("%d yds to front", (int)(distance * 1.09361));
        toFront.setText(toDisplay);
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
            ArrayList<Integer> mStrokes = new ArrayList<>();
            ArrayList<Integer> mPutts = new ArrayList<>();
            ArrayList<Integer> mSand = new ArrayList<>();
            ArrayList<Integer> mFairway = new ArrayList<>();
            ArrayList<Integer> mGIR = new ArrayList<>();
            Integer mFinal;

            for (int i = 0; i < scores.size(); i++)
            {
                mStrokes.add(scores.get(i).getStrokes());
                mPutts.add(scores.get(i).getPutts());
                mSand.add(scores.get(i).getSand());
                mFairway.add(scores.get(i).getFairway());
                mGIR.add(scores.get(i).getGreenInRegulation());
            }
            TextView ninth = findViewById(R.id.tv20);
            TextView eighteenth = findViewById(R.id.tv40);
            mFinal = Integer.parseInt(ninth.getText().toString()) + Integer.parseInt(eighteenth.getText().toString());

            String uId = Calendar.getInstance().getTime().toString();
            ScoreEntry myEntry = new ScoreEntry(uId, mStrokes, mPutts, mSand, mFairway, mGIR, mFinal);
            myIntent.putExtra("Score", myEntry);
            startActivityForResult(myIntent, 99);
        }
        else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 99) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }



    public void InitializeHoles() {
        /*
         * Long and boring method which initializes all the text boxes for the scorecard.
         * Sets the number, par, and location data for each hole.
         */

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
        Score score = scores.get(scoreToGet);
        score.setPar(par);
        score.setNumber(scoreToGet + 1);
        score.locationData.front.setLatitude(frontLat);
        score.locationData.front.setLongitude(frontLong);
        score.locationData.middle.setLatitude(midLat);
        score.locationData.middle.setLongitude(midLong);
        score.locationData.back.setLatitude(backLat);
        score.locationData.back.setLongitude(backLong);
    }
    public List<Score> InitializeScores() {
        /*
         * Long boring method which sets up all the holes for scoring.
         */
        scores = new ArrayList<>();
        final Score score1 = new Score((TextView)findViewById(R.id.tv11));
        score1.setNumber(1);
        score1.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score1;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score1);
        final Score score2 = new Score((TextView)findViewById(R.id.tv12));
        score2.setNumber(2);
        score2.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score2;
                ScoreClick();
                fairwayCheck.setVisibility(View.GONE);
            }
        });
        scores.add(score2);
        final Score score3 = new Score((TextView)findViewById(R.id.tv13));
        score3.setNumber(3);
        score3.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score3;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score3);
        final Score score4 = new Score((TextView)findViewById(R.id.tv14));
        score4.setNumber(4);
        score4.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score4;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score4);
        final Score score5 = new Score((TextView)findViewById(R.id.tv15));
        score5.setNumber(5);
        score5.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score5;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score5);
        final Score score6 = new Score((TextView)findViewById(R.id.tv16));
        score6.setNumber(6);
        score6.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score6;
                ScoreClick();
                fairwayCheck.setVisibility(View.GONE);
            }
        });
        scores.add(score6);
        final Score score7 = new Score((TextView)findViewById(R.id.tv17));
        score7.setNumber(7);
        score7.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score7;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score7);
        final Score score8 = new Score((TextView)findViewById(R.id.tv18));
        score8.setNumber(8);
        score8.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score8;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score8);
        final Score score9 = new Score((TextView)findViewById(R.id.tv19));
        score9.setNumber(9);
        score9.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score9;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score9);
        final Score score10 = new Score((TextView)findViewById(R.id.tv31));
        score10.setNumber(10);
        score10.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score10;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score10);
        final Score score11 = new Score((TextView)findViewById(R.id.tv32));
        score11.setNumber(11);
        score11.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score11;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score11);
        final Score score12 = new Score((TextView)findViewById(R.id.tv33));
        score12.setNumber(12);
        score12.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score12;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score12);
        final Score score13 = new Score((TextView)findViewById(R.id.tv34));
        score13.setNumber(13);
        score13.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score13;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score13);
        final Score score14 = new Score((TextView)findViewById(R.id.tv35));
        score14.setNumber(14);
        score14.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score14;
                ScoreClick();
                fairwayCheck.setVisibility(View.GONE);
            }
        });
        scores.add(score14);
        final Score score15 = new Score((TextView)findViewById(R.id.tv36));
        score15.setNumber(15);
        score15.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score15;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score15);
        final Score score16 = new Score((TextView)findViewById(R.id.tv37));
        score16.setNumber(16);
        score16.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score16;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score16);
        final Score score17 = new Score((TextView)findViewById(R.id.tv38));
        score17.setNumber(17);
        score17.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score17;
                ScoreClick();
                fairwayCheck.setVisibility(View.GONE);
            }
        });
        scores.add(score17);
        final Score score18 = new Score((TextView)findViewById(R.id.tv39));
        score18.setNumber(18);
        score18.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore();
                currentHole = score18;
                ScoreClick();
                fairwayCheck.setVisibility(View.VISIBLE);
            }
        });
        scores.add(score18);

        return scores;
    }
    private void ScoreClick() {
        /*
         * Handles a new location whenever a new hole is clicked.
         */

        currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            handleNewLocation(location);
        }
        catch (SecurityException e) {}
    }


    public void NextHole(View v) {

        // Since the hole numbers are indexed at 1 instead of 0
        // grabbing the score at the hole number is the one after
        // the current hole.
        int holeNumber = currentHole.number;
        if (holeNumber < 18) {
            MarkScore();
            currentHole = scores.get(holeNumber);
            currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            SetBoxes();

            try {
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                handleNewLocation(location);
            }
            catch (SecurityException e) {}

        }
    }
    public void PrevHole(View v) {

        // Since the hole numbers are indexed at 1 instead of 0
        // grabbing the score at the hole number is the one after
        // the current hole.
        int holeNumber = currentHole.number;
        if (holeNumber > 1) {
            MarkScore();
            currentHole = scores.get(holeNumber - 2);
            currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            SetBoxes();

            try {
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                handleNewLocation(location);
            }
            catch (SecurityException e) {}
        }
    }
    public void MarkScore(){
        // This function is responsible for altering the look of the score
        // in the hole textbox. Double circle for eagle or better, single
        // circle for birdie, nothing for par, single square for bogey,
        // and double square for double bogey or worse.

        if (currentHole.strokes == 0 || currentHole.strokes == currentHole.par)
            currentHole.hole.setBackground(getDrawable(R.drawable.holeback));
        else if (currentHole.strokes <= currentHole.par - 2)
            currentHole.hole.setBackground(getDrawable(R.drawable.eagle));
        else if (currentHole.strokes == currentHole.par - 1)
            currentHole.hole.setBackground(getDrawable(R.drawable.birdie));
        else if (currentHole.strokes == currentHole.par + 1)
            currentHole.hole.setBackground(getDrawable(R.drawable.bogey));
        else if (currentHole.strokes >= currentHole.par + 2)
            currentHole.hole.setBackground(getDrawable(R.drawable.doublebogey));

    }
    private void SetBoxes() {
        /*
         * Looks at the current hole to see if the fairway and sand
         * boxes are supposed to be marked, and sets them accordingly
         */
        if (currentHole.par == 3) {
            fairwayCheck.setVisibility(View.GONE);
        }
        else {
            fairwayCheck.setVisibility(View.VISIBLE);
            if (currentHole.fairway == 0) {
                fairwayCheck.setChecked(false);
            }
            else {
                fairwayCheck.setChecked(true);
            }
        }
        if (currentHole.sand == 0) {
            sandCheck.setChecked(false);
        }
        else {
            sandCheck.setChecked(true);
        }
    }

    public void AddScore(View v) {
        int intScore;
        currentHole.strokes++;
        intScore = currentHole.getStrokes();
        currentHole.hole.setText(Integer.toString(intScore));
        currentHole.actions.push(getString(R.string.stroke));

        if ((intScore - currentHole.putts) < currentHole.par - 1) {
            currentHole.setGreenInRegulation(1);
        }
        else {
            currentHole.setGreenInRegulation(0);
        }

        updateTotals(1);
    }
    public void AddPutt(View v) {
        int intScore;
        currentHole.putts++;
        currentHole.strokes++;
        intScore = currentHole.getStrokes();
        currentHole.hole.setText(Integer.toString(intScore));
        currentHole.actions.push(getString(R.string.putt));


        updateTotals(1);

    }
    @SuppressLint("SetTextI18n")
    public void UndoStroke(View v) {
        try {
            String lastAction = currentHole.actions.pop();
            if (lastAction.equals(getString(R.string.putt))) {
                currentHole.putts--;
                currentHole.strokes--;
            } else if (lastAction.equals(getString(R.string.stroke))) {
                currentHole.strokes--;
            }

            if ((currentHole.strokes - currentHole.putts) < currentHole.par - 1) {
                currentHole.setGreenInRegulation(1);
            }
            else {
                currentHole.setGreenInRegulation(0);
            }
            currentHole.hole.setText(Integer.toString(currentHole.getStrokes()));
            updateTotals(-1);
        }
        catch (EmptyStackException e) { }

    }
    public void SandChecked(View v) {
        if (sandCheck.isChecked()) {
            currentHole.setSand(1);
        }
        else {
            currentHole.setSand(0);
        }
    }
    public void FairwayChecked(View v) {
        if (fairwayCheck.isChecked()) {
            currentHole.setFairway(1);
        }
        else {
            currentHole.setFairway(0);
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
            if (currentHole.number < 10) {
                text = (String)ninth.getText();
                current = Integer.parseInt(text);
                current++;
                ninth.setText(Integer.toString(current));
            }
            else {
                text = (String)eighteenth.getText();
                current = Integer.parseInt(text);
                current++;
                eighteenth.setText(Integer.toString(current));
            }
        }
        else {
            if (currentHole.number < 10) {
                text = (String)ninth.getText();
                current = Integer.parseInt(text);
                current--;
                ninth.setText(Integer.toString(current));
            }
            else {
                text = (String)eighteenth.getText();
                current = Integer.parseInt(text);
                current--;
                eighteenth.setText(Integer.toString(current));
            }
        }
    }
}
