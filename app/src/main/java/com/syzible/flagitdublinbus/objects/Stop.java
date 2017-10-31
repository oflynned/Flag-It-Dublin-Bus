package com.syzible.flagitdublinbus.objects;

import org.json.JSONObject;

/**
 * Created by ed on 30/10/2017.
 */

public class Stop {
    private String stopAddress;
    private int stage, stopId;
    private boolean isInbound;
    private String[] availableRoutes;

    public Stop(JSONObject o) {

    }

    public Stop(int stage, String stopAddress, int stopId, boolean isInbound, String[] availableRoutes) {
        this.stage = stage;
        this.stopAddress = stopAddress;
        this.stopId = stopId;
        this.isInbound = isInbound;
        this.availableRoutes = availableRoutes;
    }

    public String getStopAddress() {
        return stopAddress;
    }

    public int getStage() {
        return stage;
    }

    public int getStopId() {
        return stopId;
    }

    public boolean isInbound() {
        return isInbound;
    }

    public String[] getAvailableRoutes() {
        return availableRoutes;
    }

    public boolean isStopAvailableRoute(String busRoute) {
        for (String availableRoute : availableRoutes) {
            if (availableRoute.equals(busRoute)) {
                return true;
            }
        }

        return false;
    }
}
