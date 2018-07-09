package com.example.wyattfraley.golftracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class SelectCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spinner;
    ArrayList<Integer> pars;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);



        //  Get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner);
        //  Create a list of items for the spinner.
        String[] items = new String[]{};
        //
        //  Create an adapter to describe how the items are displayed.
        //
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //  Set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

    }

    public void scorecard(View view) {
        // Goes to the select course activity
        Intent intent = new Intent(this, ScorecardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
