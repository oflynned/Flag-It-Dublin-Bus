package com.syzible.flagitdublinbus.networking;

import android.os.Build;

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
                "&czoom=" + zoom + "&_=" + System.currentTimeMillis();
    }

    public static String getRealtimeInfo(int stopId) {
        return "https://data.dublinked.ie/cgi-bin/rtpi/realtimebusinformation?stopid=" + stopId;
    }

    public static boolean isDebugMode() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

}
