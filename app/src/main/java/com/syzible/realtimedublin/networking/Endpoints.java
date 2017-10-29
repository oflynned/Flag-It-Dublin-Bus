package com.syzible.realtimedublin.networking;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ed on 29/10/2017.
 */

public class Endpoints {
    public static final String REALTIME_SOURCE = "";
    public static final String TWITTER_FEED_SOURCE = "";

    public static String getMapsLocations(LatLng ne, LatLng sw, float zoom) {
        return "https://www.dublinbus.ie/Templates/Public/RoutePlannerService/RTPIMapHandler.ashx" +
                "?ne=" + ne.latitude + "," + ne.longitude +
                "&sw=" + sw.latitude + "," + sw.longitude + "&zoom=" + zoom +
                "&czoom=16&_=" + System.currentTimeMillis();
    }
}
