package com.example.wyattfraley.golftracker.scorecard;

import android.location.Location;

public class HoleLocationData {
    public Location front;
    public Location middle;
    public Location back;

    public HoleLocationData() {
        front = new Location("dummyProvider");
        middle = new Location("dummyProvider");
        back = new Location("dummyProvider");
    }
    public HoleLocationData(Location nMiddle) {
        middle = nMiddle;
    }
    public HoleLocationData(Location nFront, Location nMiddle, Location nBack) {
        front = nFront;
        middle = nMiddle;
        back = nBack;
    }
}
