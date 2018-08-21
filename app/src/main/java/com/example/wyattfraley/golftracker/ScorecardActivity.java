package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Environment;
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
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

        nextButton = findViewById(R.id.button3);
        prevButton = findViewById(R.id.button6);
        sandCheck = findViewById(R.id.CheckSand);
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
                .setInterval(1 * 1000)        // 1 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
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
            checkPermission("ACCESS_FINE_LOCATION", 1, 1);

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
            String MStrokes = new String();
            String MPutts = new String();
            String MSand = new String();
            String MFinal;

            for (int i = 0; i < scores.size(); i++)
            {
                MStrokes += Integer.toString(scores.get(i).strokes) + "\n";
                MPutts += Integer.toString(scores.get(i).putts) + "\n";
                MSand += Integer.toString(scores.get(i).sand) + "\n";
            }
            TextView ninth = findViewById(R.id.tv20);
            TextView eighteenth = findViewById(R.id.tv40);
            MFinal = Integer.toString(Integer.parseInt(ninth.getText().toString()) + Integer.parseInt(eighteenth.getText().toString()));

            myIntent.putExtra("strokes", MStrokes);
            myIntent.putExtra("putts", MPutts);
            myIntent.putExtra("sand", MSand);
            myIntent.putExtra("finalScore", MFinal);
            startActivityForResult(myIntent, 99);
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


        InitializeSingleHole(R.id.tv1, R.string.hole_one, 0, 5, R.dimen.one_front_lat,
                R.dimen.one_front_long, R.dimen.one_middle_lat, R.dimen.one_middle_long,
                R.dimen.one_back_lat, R.dimen.one_back_long);
        InitializeSingleHole(R.id.tv2, R.string.hole_two, 1, 3, R.dimen.two_front_lat,
                R.dimen.two_front_long, R.dimen.two_middle_lat, R.dimen.two_middle_long,
                R.dimen.two_back_lat, R.dimen.two_back_long);
        InitializeSingleHole(R.id.tv3, R.string.hole_three, 2, 4, R.dimen.three_front_lat,
                R.dimen.three_front_long, R.dimen.three_middle_lat, R.dimen.three_middle_long,
                R.dimen.three_back_lat, R.dimen.three_back_long);
        InitializeSingleHole(R.id.tv4, R.string.hole_four, 3, 4, R.dimen.four_front_lat,
                R.dimen.four_front_long, R.dimen.four_middle_lat, R.dimen.four_middle_long,
                R.dimen.four_back_lat, R.dimen.four_back_long);
        InitializeSingleHole(R.id.tv5, R.string.hole_five, 4, 4, R.dimen.five_front_lat,
                R.dimen.five_front_long, R.dimen.five_middle_lat, R.dimen.five_middle_long,
                R.dimen.five_back_lat, R.dimen.five_back_long);
        InitializeSingleHole(R.id.tv6, R.string.hole_six, 5, 3, R.dimen.six_front_lat,
                R.dimen.six_front_long, R.dimen.six_middle_lat, R.dimen.six_middle_long,
                R.dimen.six_back_lat, R.dimen.six_back_long);
        InitializeSingleHole(R.id.tv7, R.string.hole_seven, 6, 5, R.dimen.seven_front_lat,
                R.dimen.seven_front_long, R.dimen.seven_middle_lat, R.dimen.seven_middle_long,
                R.dimen.seven_back_lat, R.dimen.seven_back_long);
        InitializeSingleHole(R.id.tv8, R.string.hole_eight, 7, 4, R.dimen.eight_front_lat,
                R.dimen.eight_front_long, R.dimen.eight_middle_lat, R.dimen.eight_middle_long,
                R.dimen.eight_back_lat, R.dimen.eight_back_long);
        InitializeSingleHole(R.id.tv9, R.string.hole_nine, 8, 4, R.dimen.nine_front_lat,
                R.dimen.nine_front_long, R.dimen.nine_middle_lat, R.dimen.nine_middle_long,
                R.dimen.nine_back_lat, R.dimen.nine_back_long);

        textView = findViewById(R.id.tv10);
        textView.setText(R.string.hole_out);
        textView.setTextColor(Color.WHITE);
        textHoles.add(textView);

        InitializeSingleHole(R.id.tv21, R.string.hole_ten, 9, 5, R.dimen.ten_front_lat,
                R.dimen.ten_front_long, R.dimen.ten_middle_lat, R.dimen.ten_middle_long,
                R.dimen.ten_back_lat, R.dimen.ten_back_long);
        InitializeSingleHole(R.id.tv22, R.string.hole_eleven, 10, 4, R.dimen.eleven_front_lat,
                R.dimen.eleven_front_long, R.dimen.eleven_middle_lat, R.dimen.eleven_middle_long,
                R.dimen.eleven_back_lat, R.dimen.eleven_back_long);
        InitializeSingleHole(R.id.tv23, R.string.hole_twelve, 11, 4, R.dimen.twelve_front_lat,
                R.dimen.twelve_front_long, R.dimen.twelve_middle_lat, R.dimen.twelve_middle_long,
                R.dimen.twelve_back_lat, R.dimen.twelve_back_long);
        InitializeSingleHole(R.id.tv24, R.string.hole_thirteen, 12, 5, R.dimen.thirteen_front_lat,
                R.dimen.thirteen_front_long, R.dimen.thirteen_middle_lat, R.dimen.thirteen_middle_long,
                R.dimen.thirteen_back_lat, R.dimen.thirteen_back_long);
        InitializeSingleHole(R.id.tv25, R.string.hole_fourteen, 13, 3, R.dimen.fourteen_front_lat,
                R.dimen.fourteen_front_long, R.dimen.fourteen_middle_lat, R.dimen.fourteen_middle_long,
                R.dimen.fourteen_back_lat, R.dimen.fourteen_back_long);
        InitializeSingleHole(R.id.tv26, R.string.hole_fifteen, 14, 4, R.dimen.fifteen_front_lat,
                R.dimen.fifteen_front_long, R.dimen.fifteen_middle_lat, R.dimen.fifteen_middle_long,
                R.dimen.fifteen_back_lat, R.dimen.fifteen_back_long);
        InitializeSingleHole(R.id.tv27, R.string.hole_sixteen, 15, 4, R.dimen.sixteen_front_lat,
                R.dimen.sixteen_front_long, R.dimen.sixteen_middle_lat, R.dimen.sixteen_middle_long,
                R.dimen.sixteen_back_lat, R.dimen.sixteen_back_long);
        InitializeSingleHole(R.id.tv28, R.string.hole_seventeen, 16, 3, R.dimen.seventeen_front_lat,
                R.dimen.seventeen_front_long, R.dimen.seventeen_middle_lat, R.dimen.seventeen_middle_long,
                R.dimen.seventeen_back_lat, R.dimen.seventeen_back_long);
        InitializeSingleHole(R.id.tv29, R.string.hole_eighteen, 17, 4, R.dimen.eighteen_front_lat,
                R.dimen.eighteen_front_long, R.dimen.eighteen_middle_lat, R.dimen.eighteen_middle_long,
                R.dimen.eighteen_back_lat, R.dimen.eighteen_back_long);


        textView = findViewById(R.id.tv30);
        textView.setText(R.string.hole_in);
        textView.setTextColor(Color.WHITE);
        textHoles.add(textView);
    }
    private void InitializeSingleHole(Integer viewID, Integer holeText, Integer scoreToGet, Integer par,
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
        textView.setText(holeText);
        textView.setTextColor(Color.WHITE);
        Score score = scores.get(scoreToGet);
        score.setPar(par);
        score.setNumber(scoreToGet + 1);
        score.locationData.front.setLatitude(frontLat);
        score.locationData.front.setLongitude(frontLong);
        score.locationData.middle.setLatitude(midLat);
        score.locationData.middle.setLongitude(midLong);
        score.locationData.back.setLatitude(backLat);
        score.locationData.back.setLongitude(backLong);
        textHoles.add(textView);
    }
    public List<Score> InitializeScores() {
        scores = new ArrayList<>();
        final Score score1 = new Score((TextView)findViewById(R.id.tv11));
        score1.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score1;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score1);
        final Score score2 = new Score((TextView)findViewById(R.id.tv12));
        score2.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score2;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score2);
        final Score score3 = new Score((TextView)findViewById(R.id.tv13));
        score3.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score3;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score3);
        final Score score4 = new Score((TextView)findViewById(R.id.tv14));
        score4.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score4;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score4);
        final Score score5 = new Score((TextView)findViewById(R.id.tv15));
        score5.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score5;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score5);
        final Score score6 = new Score((TextView)findViewById(R.id.tv16));
        score6.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score6;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score6);
        final Score score7 = new Score((TextView)findViewById(R.id.tv17));
        score7.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score7;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score7);
        final Score score8 = new Score((TextView)findViewById(R.id.tv18));
        score8.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score8;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score8);
        final Score score9 = new Score((TextView)findViewById(R.id.tv19));
        score9.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score9;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score9);
        final Score score10 = new Score((TextView)findViewById(R.id.tv31));
        score10.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score10;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score10);
        final Score score11 = new Score((TextView)findViewById(R.id.tv32));
        score11.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score11;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score11);
        final Score score12 = new Score((TextView)findViewById(R.id.tv33));
        score12.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score12;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score12);
        final Score score13 = new Score((TextView)findViewById(R.id.tv34));
        score13.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score13;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score13);
        final Score score14 = new Score((TextView)findViewById(R.id.tv35));
        score14.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score14;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score14);
        final Score score15 = new Score((TextView)findViewById(R.id.tv36));
        score15.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score15;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score15);
        final Score score16 = new Score((TextView)findViewById(R.id.tv37));
        score16.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score16;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score16);
        final Score score17 = new Score((TextView)findViewById(R.id.tv38));
        score17.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score17;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score17);
        final Score score18 = new Score((TextView)findViewById(R.id.tv39));
        score18.hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score18;
                currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        scores.add(score18);

        return scores;
    }


    public void NextHole(View v) {
        // First we have to check what kind of score it is
        // and show its relation to par.
        MarkScore(v);

        if (sandCheck.isChecked()) {
            currentHole.sand = 1;
        }
        else {
            currentHole.sand = 0;
        }

        for (int i = 0; i < scores.size(); i++) {
            if (currentHole == scores.get(i)) {
                if (i < scores.size() - 1) {
                    currentHole = scores.get(i + 1);
                    currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                    if (currentHole.sand == 0) {
                        sandCheck.setChecked(false);
                    }
                    else {
                        sandCheck.setChecked(true);
                    }
                }
                else {
                    currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                }
                break;
            }
        }
    }
    public void PrevHole(View v) {
        // First we have to check what kind of score it is
        // and show its relation to par.
        MarkScore(v);

        if (sandCheck.isChecked()) {
            currentHole.sand = 1;
        }
        else {
            currentHole.sand = 0;
        }
        for (int i = 0; i < scores.size(); i++) {
            if (currentHole == scores.get(i)) {
                if (i > 0) {
                    currentHole = scores.get(i - 1);
                    currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                    if (currentHole.sand == 0) {
                        sandCheck.setChecked(false);
                    }
                    else {
                        sandCheck.setChecked(true);
                    }
                }
                else {
                    currentHole.hole.setBackground(getDrawable(R.drawable.holeselected));
                }
                break;
            }
        }
    }
    public void MarkScore(View v){
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

    public void AddScore(View v) {
        int intScore;
        currentHole.strokes++;
        intScore = currentHole.strokes;
        currentHole.hole.setText(Integer.toString(intScore));
        currentHole.actions.push(getString(R.string.stroke));

        updateTotals(v);
    }
    public void AddPutt(View v) {
        int intScore;
        currentHole.putts++;
        currentHole.strokes++;
        intScore = currentHole.strokes;
        currentHole.hole.setText(Integer.toString(intScore));
        currentHole.actions.push(getString(R.string.putt));

        updateTotals(v);

    }
    public void UndoStroke(View v) {
        try {
            String lastAction = currentHole.actions.pop();
            if (lastAction.equals("putt")) {
                currentHole.putts--;
                currentHole.strokes--;
            } else if (lastAction.equals("stroke")) {
                currentHole.strokes--;
            }
        }
        catch (EmptyStackException e) {

        }
        currentHole.hole.setText(Integer.toString(currentHole.strokes));
        updateTotals(v);
    }

    public void updateTotals(View v) {
        TextView ninth = findViewById(R.id.tv20);
        TextView eighteenth = findViewById(R.id.tv40);
        Score current;
        int ninthS = 0;
        int eighteenthS = 0;
        String score;
        int intScore;

        for (int i = 0; i < 9; i++) {
            current = scores.get(i);
            score = (String)current.hole.getText();

            if (score.isEmpty()){
                intScore = 0;
            }
            else {
                intScore = Integer.parseInt(score);
            }
            ninthS += intScore;
        }
        ninth.setText(Integer.toString(ninthS));

        for (int i = 9; i < 18; i++) {
            current = scores.get(i);
            score = (String)current.hole.getText();

            if (score.isEmpty()){
                intScore = 0;
            }
            else {
                intScore = Integer.parseInt(score);
            }
            eighteenthS += intScore;
        }
        eighteenth.setText(Integer.toString(eighteenthS));
    }
}
