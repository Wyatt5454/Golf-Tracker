package com.example.wyattfraley.golftracker;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;

public class SaveCheck extends Activity {
    Button Yes;
    Button No;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.save_window);

        DisplayMetrics Dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(Dm);

        int width = Dm.widthPixels;
        int height = Dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .3));

        Yes = (Button)findViewById(R.id.SaveYes);
        No = (Button)findViewById(R.id.SaveNo);
    }

    public void SaveRound() {

    }
}
