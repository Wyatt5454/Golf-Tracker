package com.example.wyattfraley.golftracker;

import android.location.Location;

public class HoleLocationData {
    public Location front;
    public Location middle;
    public Location back;

    public HoleLocationData() {
        front = new Location("dummyprovider");
        middle = new Location("dummyprovider");
        back = new Location("dummyprovider");
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
