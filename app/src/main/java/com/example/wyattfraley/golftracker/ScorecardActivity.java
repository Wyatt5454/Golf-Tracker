package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.example.wyattfraley.golftracker.R.drawable.eagle;

public class ScorecardActivity extends AppCompatActivity {
    Score currentHole;
    List<TextView> TextHoles;
    List<Score> Scores;
    Button NextButton;
    Button PrevButton;
    LocationService mLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);
        NextButton = findViewById(R.id.button3);
        PrevButton = findViewById(R.id.button6);


        List<String> CardInfo = GetCardInfo("WenatcheeGolfAndCountryClub");

        GolfCourse CurrentCourse = new GolfCourse("WenatcheeGolfAndCountryClub", CardInfo);

        // Grab all the spots for scores and put them in a container.
        Scores = InitializeScores();

        // Now we have to initialize the TextViews for each hole.
        TextHoles = InitializeHoles(CurrentCourse);

        // Set the current hole.
        currentHole = Scores.get(0);
        currentHole.Hole.setBackground(getDrawable(R.drawable.holeselected));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_menu) {
            //  Here we create a pop up window asking if they are done with the round and want to save.
            // Have to convert the scores into a saveable format
            Intent MyIntent = new Intent(ScorecardActivity.this, SaveCheck.class);
            String Message = new String();
            for (int i = 0; i < Scores.size(); i++)
            {
                Message += Scores.get(i).ToSaveFormat();
            }

            MyIntent.putExtra("ToSave", Message);
            startActivity(MyIntent);
        }
        return super.onOptionsItemSelected(item);
    }



    public List<TextView> InitializeHoles(GolfCourse CurrentCourse) {
        TextHoles = new ArrayList<>();
        TextHoles.add((TextView) findViewById(R.id.tv1));
        TextHoles.add((TextView) findViewById(R.id.tv2));
        TextHoles.add((TextView) findViewById(R.id.tv3));
        TextHoles.add((TextView) findViewById(R.id.tv4));
        TextHoles.add((TextView) findViewById(R.id.tv5));
        TextHoles.add((TextView) findViewById(R.id.tv6));
        TextHoles.add((TextView) findViewById(R.id.tv7));
        TextHoles.add((TextView) findViewById(R.id.tv8));
        TextHoles.add((TextView) findViewById(R.id.tv9));
        TextHoles.add((TextView) findViewById(R.id.tv10));
        TextHoles.add((TextView) findViewById(R.id.tv21));
        TextHoles.add((TextView) findViewById(R.id.tv22));
        TextHoles.add((TextView) findViewById(R.id.tv23));
        TextHoles.add((TextView) findViewById(R.id.tv24));
        TextHoles.add((TextView) findViewById(R.id.tv25));
        TextHoles.add((TextView) findViewById(R.id.tv26));
        TextHoles.add((TextView) findViewById(R.id.tv27));
        TextHoles.add((TextView) findViewById(R.id.tv28));
        TextHoles.add((TextView) findViewById(R.id.tv29));
        TextHoles.add((TextView) findViewById(R.id.tv30));

        int Par9 = 0;
        for (int i = 0; i < 9; i++)
        {
            String number = Integer.toString(CurrentCourse.Holes.get(i).number);
            int par = CurrentCourse.Holes.get(i).par;
            TextHoles.get(i).setText(number + "\n" + Integer.toString(par));
            TextHoles.get(i).setTextColor(Color.WHITE);
            Scores.get(i).par = par;
        }
        TextHoles.get(9).setText("Out\n" + Integer.toString(Par9));
        TextHoles.get(9).setTextColor(Color.WHITE);

        Par9 = 0;
        for (int i = 9; i < 18; i++)
        {
            String number = Integer.toString(CurrentCourse.Holes.get(i).number);
            int par = CurrentCourse.Holes.get(i).par;
            TextHoles.get(i+1).setText(number + "\n" + Integer.toString(par));
            TextHoles.get(i+1).setTextColor(Color.WHITE);
            Scores.get(i).par = par;
        }
        TextHoles.get(19).setText("In\n" + Integer.toString(Par9));
        TextHoles.get(19).setTextColor(Color.WHITE);

        return TextHoles;
    }
    public List<Score> InitializeScores() {
        Scores = new ArrayList<>();
        Scores.add(new Score((TextView)findViewById(R.id.tv11)));
        Scores.add(new Score((TextView)findViewById(R.id.tv12)));
        Scores.add(new Score((TextView)findViewById(R.id.tv13)));
        Scores.add(new Score((TextView)findViewById(R.id.tv14)));
        Scores.add(new Score((TextView)findViewById(R.id.tv15)));
        Scores.add(new Score((TextView)findViewById(R.id.tv16)));
        Scores.add(new Score((TextView)findViewById(R.id.tv17)));
        Scores.add(new Score((TextView)findViewById(R.id.tv18)));
        Scores.add(new Score((TextView)findViewById(R.id.tv19)));
        Scores.add(new Score((TextView)findViewById(R.id.tv31)));
        Scores.add(new Score((TextView)findViewById(R.id.tv32)));
        Scores.add(new Score((TextView)findViewById(R.id.tv33)));
        Scores.add(new Score((TextView)findViewById(R.id.tv34)));
        Scores.add(new Score((TextView)findViewById(R.id.tv35)));
        Scores.add(new Score((TextView)findViewById(R.id.tv36)));
        Scores.add(new Score((TextView)findViewById(R.id.tv37)));
        Scores.add(new Score((TextView)findViewById(R.id.tv38)));
        Scores.add(new Score((TextView)findViewById(R.id.tv39)));

        return Scores;
    }

    private List<String> GetCardInfo(String CourseName) {
        // This function grabs the necessary information about the current golf course
        // for display on the scorecard activity.
        // Currently it just grabs it from a normal text file, hopefully later it will grab from
        // a database of courses.

        List<String> toReturn = new ArrayList<>();
        File file = new File( Environment.getExternalStorageDirectory() + "/Download/WenatcheeGolfAndCountryClub.txt");
        if (!file.exists()){
            String line = "Could not get info";
            toReturn.add(line);
            return toReturn;
        }
        //Read text from file
        //StringBuilder text = new StringBuilder();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            for (int i = 0; i < 2; i++) {
                String line = br.readLine();
                toReturn.add(line);
            }

        }
        catch (IOException e) {
            System.err.format("Exception occurred trying to read '%s'.", CourseName);
            e.printStackTrace();
            String line = "Exception occurred trying to read " + CourseName;
            toReturn.add(line);
            return toReturn;
        }

        return toReturn;
    }
    public void NextHole(View v) {
        // First we have to check what kind of score it is
        // and show its relation to par.
        MarkScore(v);

        for (int i = 0; i < Scores.size(); i++) {
            if (currentHole == Scores.get(i)) {
                if (i < Scores.size() - 1) {
                    currentHole = Scores.get(i + 1);
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

        for (int i = 0; i < Scores.size(); i++) {
            if (currentHole == Scores.get(i)) {
                if (i > 0) {
                    currentHole = Scores.get(i - 1);
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

        if (currentHole.strokes == 0 || currentHole.strokes == currentHole.par)
            currentHole.Hole.setBackground(getDrawable(R.drawable.holeback));
        else if (currentHole.strokes <= currentHole.par - 2)
            currentHole.Hole.setBackground(getDrawable(R.drawable.eagle));
        else if (currentHole.strokes == currentHole.par - 1)
            currentHole.Hole.setBackground(getDrawable(R.drawable.birdie));
        else if (currentHole.strokes == currentHole.par + 1)
            currentHole.Hole.setBackground(getDrawable(R.drawable.bogey));
        else if (currentHole.strokes >= currentHole.par + 2)
            currentHole.Hole.setBackground(getDrawable(R.drawable.doublebogey));

    }

    public void AddScore(View v) {
        int intScore;
        currentHole.strokes++;
        intScore = currentHole.strokes;
        currentHole.Hole.setText(Integer.toString(intScore));
        currentHole.Actions.push(getString(R.string.stroke));

        updateTotals(v);
    }
    public void AddPutt(View v) {
        int intScore;
        currentHole.putts++;
        currentHole.strokes++;
        intScore = currentHole.strokes;
        currentHole.Hole.setText(Integer.toString(intScore));
        currentHole.Actions.push(getString(R.string.putt));

        updateTotals(v);

    }
    public void UndoStroke(View v) {
        try {
            String lastAction = currentHole.Actions.pop();
            if (lastAction.equals("putt")) {
                currentHole.putts--;
                currentHole.strokes--;
            } else if (lastAction.equals("stroke")) {
                currentHole.strokes--;
            }
        }
        catch (EmptyStackException e) {

        }
        currentHole.Hole.setText(Integer.toString(currentHole.strokes));
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
