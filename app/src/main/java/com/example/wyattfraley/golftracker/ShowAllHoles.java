package com.example.wyattfraley.golftracker;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShowAllHoles extends AppCompatActivity {
    FXUtility fxUtility;
    TextView mainText;
    List<Button> buttons;
    List<TextView> textViews;
    TotalRoundStats stats;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_holes);

        fxUtility = new FXUtility();
        mainText = findViewById(R.id.overallHoleStats);

        stats = LoadTotalStats();

        if (stats.totalRounds > 0) {
            InitializeButtons();
            InitializeText();
            SetMainTextBox();
            SetHoleStats();
        }
        else {
            LinearLayout linearLayout = findViewById(R.id.allHolesLL);
            linearLayout.removeAllViews();
            linearLayout.addView(mainText);
            SetMainTextBoxNoRounds();
        }

    }
    public void InitializeButtons() {
        buttons = new ArrayList<>();

        Button toAdd = findViewById(R.id.holeButton1);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton2);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton3);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton4);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton5);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton6);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton7);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton8);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton9);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton10);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton11);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton12);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton13);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton14);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton15);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton16);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton17);
        buttons.add(toAdd);
        toAdd = findViewById(R.id.holeButton18);
        buttons.add(toAdd);
    }
    public void InitializeText() {
        textViews = new ArrayList<>();

        TextView toAdd = findViewById(R.id.holeStats1);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats2);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats3);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats4);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats5);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats6);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats7);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats8);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats9);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats10);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats11);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats12);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats13);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats14);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats15);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats16);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats17);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
        toAdd = findViewById(R.id.holeStats18);
        toAdd.setVisibility(View.GONE);
        textViews.add(toAdd);
    }
    public void SetHoleStats() {

        for (int i = 0; i < textViews.size(); i++) {
            TotalHoleStats holeStats = stats.holes.get(i);


            String statsToAdd = new String();
            statsToAdd += "Average score: " + Float.toString(holeStats.strokes / stats.totalRounds) + "\n";
            statsToAdd += "Average putts: " + Float.toString(holeStats.putts / stats.totalRounds) + "\n";

            textViews.get(i).setText(statsToAdd);
        }

    }
    public void SetMainTextBox() {
        float par3 = 0;
        float par4 = 0;
        float par5 = 0;
        DecimalFormat dF = new DecimalFormat("##.##");
        dF.setRoundingMode(RoundingMode.DOWN);

        par3 += stats.holes.get(1).strokes;
        par3 += stats.holes.get(5).strokes;
        par3 += stats.holes.get(13).strokes;
        par3 += stats.holes.get(16).strokes;
        par3 /= stats.totalRounds;
        par3 /= 4;

        par4 += stats.holes.get(2).strokes;
        par4 += stats.holes.get(3).strokes;
        par4 += stats.holes.get(4).strokes;
        par4 += stats.holes.get(7).strokes;
        par4 += stats.holes.get(8).strokes;
        par4 += stats.holes.get(10).strokes;
        par4 += stats.holes.get(11).strokes;
        par4 += stats.holes.get(14).strokes;
        par4 += stats.holes.get(15).strokes;
        par4 += stats.holes.get(17).strokes;
        par4 /= stats.totalRounds;
        par4 /= 10;

        par5 += stats.holes.get(0).strokes;
        par5 += stats.holes.get(6).strokes;
        par5 += stats.holes.get(9).strokes;
        par5 += stats.holes.get(12).strokes;
        par5 /= stats.totalRounds;
        par5 /= 4;

        String toAdd = new String();
        toAdd += "Average par 3 Score: " + dF.format(par3) + "\n";
        toAdd += "Average par 4 Score: " + dF.format(par4) + "\n";
        toAdd += "Average par 5 Score: " + dF.format(par5) + "\n";

        mainText.setText(toAdd);
    }
    public void SetMainTextBoxNoRounds() {
        String toAdd = "You don't have any rounds saved on this device.  Start a round and save it for detailed stat tracking!";
        mainText.setText(toAdd);
    }

    public TotalRoundStats LoadTotalStats() {
        /*
         * Grabs the total stats from a file locally stored
         * on the device.
         */

        TotalRoundStats stats = new TotalRoundStats();
        File filesDir = getFilesDir();
        File fileTotal = new File( filesDir, "TotalStats.txt");
        File fileHoles = new File(filesDir, "HoleStats.txt");

        try {
            // Reading object in a file
            FileInputStream inputStreamTotal = new FileInputStream(fileTotal);
            FileInputStream inputStreamHoles = new FileInputStream(fileHoles);

            // Reading object in a file
            ObjectInputStream inTotal = new ObjectInputStream(inputStreamTotal);
            ObjectInputStream inHoles = new ObjectInputStream(inputStreamHoles);

            // Method for deserialization of object
            stats = (TotalRoundStats)inTotal.readObject();
            stats.holes = (ArrayList<TotalHoleStats>)inHoles.readObject();

            inTotal.close();
            inHoles.close();
            inputStreamTotal.close();
            inputStreamHoles.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public void toggle_contents(View v){
        // First have to find the appropriate text box.
        int found = 0;
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isPressed()) {
                found = i;
                break;
            }
        }

        TextView toAnimate = textViews.get(found);


        if(toAnimate.isShown()){
            fxUtility.slide_up(this, toAnimate);
            toAnimate.setVisibility(View.GONE);
        }
        else{
            toAnimate.setVisibility(View.VISIBLE);
            fxUtility.slide_down(this, toAnimate);
        }
    }
}
