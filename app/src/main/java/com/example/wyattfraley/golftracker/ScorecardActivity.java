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
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class ScorecardActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    Score currentHole;
    List<TextView> TextHoles;
    List<Score> Scores;
    Button NextButton;
    Button PrevButton;
    CheckBox SandCheck;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = MapsActivity.class.getSimpleName();
    TextView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);

        NextButton = findViewById(R.id.button3);
        PrevButton = findViewById(R.id.button6);
        SandCheck = findViewById(R.id.CheckSand);
        testView = findViewById(R.id.testView);


        // Grab all the spots for scores and put them in a container.
        Scores = InitializeScores();

        // Now we have to initialize the TextViews for each hole.
        TextHoles = InitializeHoles();

        // Set the current hole.
        currentHole = Scores.get(0);
        currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));



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

        float distance = location.distanceTo(currentHole.locationData.middle);



        testView.setText("Distance to middle: " + distance);
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
            // Have to convert the scores into a saveable format
            Intent MyIntent = new Intent(ScorecardActivity.this, SaveCheck.class);
            String MStrokes = new String();
            String MPutts = new String();
            String MSand = new String();
            String MFinal = new String();

            for (int i = 0; i < Scores.size(); i++)
            {
                MStrokes += Integer.toString(Scores.get(i).Strokes) + "\n";
                MPutts += Integer.toString(Scores.get(i).Putts) + "\n";
                MSand += Integer.toString(Scores.get(i).Sand) + "\n";
            }
            TextView ninth = findViewById(R.id.tv20);
            TextView eighteenth = findViewById(R.id.tv40);
            MFinal = Integer.toString(Integer.parseInt(ninth.getText().toString()) + Integer.parseInt(eighteenth.getText().toString()));

            MyIntent.putExtra("Strokes", MStrokes);
            MyIntent.putExtra("Putts", MPutts);
            MyIntent.putExtra("Sand", MSand);
            MyIntent.putExtra("Final", MFinal);
            startActivityForResult(MyIntent, 99);
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



    public List<TextView> InitializeHoles() {
        /*
         * Long and boring method which initializes all the text boxes for the scorecard.
         * Sets the number, par, and location data for each hole.
         */

        TextHoles = new ArrayList<>();
        Location location = new Location("dummyprovider");

        TextView textView = findViewById(R.id.tv1);
        textView.setText(R.string.hole_one);
        textView.setTextColor(Color.WHITE);
        Score score = Scores.get(0);
        score.setPar(5);
        score.setNumber(1);
        location.setLatitude(R.dimen.one_middle_lat);
        location.setLongitude(R.dimen.one_middle_long);
        score.locationData.middle = location;
        TextHoles.add(textView);

        textView = findViewById(R.id.tv2);
        textView.setText(R.string.hole_two);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(1);
        score.setPar(3);
        score.setNumber(2);
        location.setLatitude(R.dimen.two_middle_lat);
        location.setLongitude(R.dimen.two_middle_long);
        score.locationData.middle = location;
        TextHoles.add(textView);

        textView = findViewById(R.id.tv3);
        textView.setText(R.string.hole_three);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(2);
        score.setPar(4);
        score.setNumber(3);
        location.setLatitude(R.dimen.three_middle_lat);
        location.setLongitude(R.dimen.three_middle_long);
        score.locationData.middle = location;
        TextHoles.add(textView);

        textView = findViewById(R.id.tv4);
        textView.setText(R.string.hole_four);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(3);
        score.setPar(4);
        score.setNumber(4);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv5);
        textView.setText(R.string.hole_five);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(4);
        score.setPar(4);
        score.setNumber(5);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv6);
        textView.setText(R.string.hole_six);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(5);
        score.setPar(4);
        score.setNumber(6);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv7);
        textView.setText(R.string.hole_seven);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(6);
        score.setPar(5);
        score.setNumber(7);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv8);
        textView.setText(R.string.hole_eight);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(7);
        score.setPar(4);
        score.setNumber(8);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv9);
        textView.setText(R.string.hole_nine);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(8);
        score.setPar(4);
        score.setNumber(9);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv10);
        textView.setText(R.string.hole_out);
        textView.setTextColor(Color.WHITE);
        TextHoles.add(textView);


        textView = findViewById(R.id.tv21);
        textView.setText(R.string.hole_ten);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(9);
        score.setPar(5);
        score.setNumber(10);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv22);
        textView.setText(R.string.hole_eleven);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(10);
        score.setPar(4);
        score.setNumber(11);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv23);
        textView.setText(R.string.hole_twelve);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(11);
        score.setPar(4);
        score.setNumber(12);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv24);
        textView.setText(R.string.hole_thirteen);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(12);
        score.setPar(5);
        score.setNumber(13);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv25);
        textView.setText(R.string.hole_fourteen);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(13);
        score.setPar(3);
        score.setNumber(14);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv26);
        textView.setText(R.string.hole_fifteen);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(14);
        score.setPar(4);
        score.setNumber(15);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv27);
        textView.setText(R.string.hole_sixteen);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(15);
        score.setPar(4);
        score.setNumber(16);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv28);
        textView.setText(R.string.hole_seventeen);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(16);
        score.setPar(3);
        score.setNumber(17);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv29);
        textView.setText(R.string.hole_eighteen);
        textView.setTextColor(Color.WHITE);
        score = Scores.get(17);
        score.setPar(4);
        score.setNumber(18);
        TextHoles.add(textView);

        textView = findViewById(R.id.tv30);
        textView.setText(R.string.hole_in);
        textView.setTextColor(Color.WHITE);
        TextHoles.add(textView);

        return TextHoles;
    }
    public List<Score> InitializeScores() {
        Scores = new ArrayList<>();
        final Score score1 = new Score((TextView)findViewById(R.id.tv11));
        score1.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score1;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score1);
        final Score score2 = new Score((TextView)findViewById(R.id.tv12));
        score2.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score2;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score2);
        final Score score3 = new Score((TextView)findViewById(R.id.tv13));
        score3.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score3;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score3);
        final Score score4 = new Score((TextView)findViewById(R.id.tv14));
        score4.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score4;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score4);
        final Score score5 = new Score((TextView)findViewById(R.id.tv15));
        score5.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score5;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score5);
        final Score score6 = new Score((TextView)findViewById(R.id.tv16));
        score6.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score6;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score6);
        final Score score7 = new Score((TextView)findViewById(R.id.tv17));
        score7.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score7;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score7);
        final Score score8 = new Score((TextView)findViewById(R.id.tv18));
        score8.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score8;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score8);
        final Score score9 = new Score((TextView)findViewById(R.id.tv19));
        score9.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score9;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score9);
        final Score score10 = new Score((TextView)findViewById(R.id.tv31));
        score10.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score10;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score10);
        final Score score11 = new Score((TextView)findViewById(R.id.tv32));
        score11.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score11;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score11);
        final Score score12 = new Score((TextView)findViewById(R.id.tv33));
        score12.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score12;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score12);
        final Score score13 = new Score((TextView)findViewById(R.id.tv34));
        score13.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score13;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score13);
        final Score score14 = new Score((TextView)findViewById(R.id.tv35));
        score14.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score14;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score14);
        final Score score15 = new Score((TextView)findViewById(R.id.tv36));
        score15.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score15;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score15);
        final Score score16 = new Score((TextView)findViewById(R.id.tv37));
        score16.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score16;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score16);
        final Score score17 = new Score((TextView)findViewById(R.id.tv38));
        score17.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score17;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score17);
        final Score score18 = new Score((TextView)findViewById(R.id.tv39));
        score18.Hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkScore(v);
                currentHole = score18;
                currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
            }
        });
        Scores.add(score18);

        return Scores;
    }


    public void NextHole(View v) {
        // First we have to check what kind of score it is
        // and show its relation to par.
        MarkScore(v);

        if (SandCheck.isChecked()) {
            currentHole.Sand = 1;
        }
        else {
            currentHole.Sand = 0;
        }

        for (int i = 0; i < Scores.size(); i++) {
            if (currentHole == Scores.get(i)) {
                if (i < Scores.size() - 1) {
                    currentHole = Scores.get(i + 1);
                    currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
                    if (currentHole.Sand == 0) {
                        SandCheck.setChecked(false);
                    }
                    else {
                        SandCheck.setChecked(true);
                    }
                }
                else {
                    currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
                }
                break;
            }
        }
    }
    public void PrevHole(View v) {
        // First we have to check what kind of score it is
        // and show its relation to par.
        MarkScore(v);

        if (SandCheck.isChecked()) {
            currentHole.Sand = 1;
        }
        else {
            currentHole.Sand = 0;
        }

        for (int i = 0; i < Scores.size(); i++) {
            if (currentHole == Scores.get(i)) {
                if (i > 0) {
                    currentHole = Scores.get(i - 1);
                    currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
                    if (currentHole.Sand == 0) {
                        SandCheck.setChecked(false);
                    }
                    else {
                        SandCheck.setChecked(true);
                    }
                }
                else {
                    currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
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

        if (currentHole.Strokes == 0 || currentHole.Strokes == currentHole.Par)
            currentHole.Hole.setBackground(getDrawable(R.drawable.holeback));
        else if (currentHole.Strokes <= currentHole.Par - 2)
            currentHole.Hole.setBackground(getDrawable(R.drawable.eagle));
        else if (currentHole.Strokes == currentHole.Par - 1)
            currentHole.Hole.setBackground(getDrawable(R.drawable.birdie));
        else if (currentHole.Strokes == currentHole.Par + 1)
            currentHole.Hole.setBackground(getDrawable(R.drawable.bogey));
        else if (currentHole.Strokes >= currentHole.Par + 2)
            currentHole.Hole.setBackground(getDrawable(R.drawable.doublebogey));

    }

    public void AddScore(View v) {
        int intScore;
        currentHole.Strokes++;
        intScore = currentHole.Strokes;
        currentHole.Hole.setText(Integer.toString(intScore));
        currentHole.Actions.push(getString(R.string.stroke));

        updateTotals(v);
    }
    public void AddPutt(View v) {
        int intScore;
        currentHole.Putts++;
        currentHole.Strokes++;
        intScore = currentHole.Strokes;
        currentHole.Hole.setText(Integer.toString(intScore));
        currentHole.Actions.push(getString(R.string.putt));

        updateTotals(v);

    }
    public void UndoStroke(View v) {
        try {
            String lastAction = currentHole.Actions.pop();
            if (lastAction.equals("putt")) {
                currentHole.Putts--;
                currentHole.Strokes--;
            } else if (lastAction.equals("stroke")) {
                currentHole.Strokes--;
            }
        }
        catch (EmptyStackException e) {

        }
        currentHole.Hole.setText(Integer.toString(currentHole.Strokes));
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
            current = Scores.get(i);
            score = (String)current.Hole.getText();

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
            current = Scores.get(i);
            score = (String)current.Hole.getText();

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
