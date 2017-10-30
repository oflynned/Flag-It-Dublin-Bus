package com.syzible.flagitdublinbus.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;

/**
 * Created by ed on 04/09/2017.
 */

public class GPSAvailableService {
    public static boolean isGPSAvailable(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        return (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    public static AlertDialog getGPSEnabledDialog(final Activity activity) {
        return new AlertDialog.Builder(activity)
                .setTitle("GPS Service Offline")
                .setMessage("GPS services have been turned off on this device. Re-enable services in order to enable use of location and to find bus stops close to you.")
                .setPositiveButton("Enable", (dialog, which) -> activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("Ignore", null)
                .setCancelable(true)
                .create();
    }
}
