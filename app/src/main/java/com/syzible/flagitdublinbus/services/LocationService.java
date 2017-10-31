package com.syzible.flagitdublinbus.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.maps.model.LatLng;
import com.syzible.flagitdublinbus.helpers.Constants;
import com.syzible.flagitdublinbus.networking.Endpoints;

/**
 * Created by ed on 30/05/2017.
 */

public class LocationService extends Service {

    public static final LatLng START_LOCATION = new LatLng(53.3471471, -6.2605128);
    public static final float START_ZOOM = 10.0f;
    public static final float CLOSE_ZOOM = 15.0f;

    private static final int LOCATION_FOREGROUND_INTERVAL = Endpoints.isDebugMode() ? Constants.ONE_SECOND : Constants.FIVE_MINUTES;
    private static final float LOCATION_DISTANCE = 200f;

    public static final String LOCATION_CHANGE_FILTER = "com.syzible.flagitdublinbus.location_change";

    private LocationManager locationManager = null;

    private class LocationListener implements android.location.LocationListener {

        private Location lastLocation;

        LocationListener(String provider) {
            this.lastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            lastLocation.set(location);

            Intent intent = new Intent(LOCATION_CHANGE_FILTER);
            intent.putExtra("lat", String.valueOf(location.getLatitude()));
            intent.putExtra("lng", String.valueOf(location.getLongitude()));
            getApplicationContext().sendBroadcast(intent);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("onStatusChanged: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("onProviderEnabled: " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled: " + provider);
        }

        public Location getLastLocation() {
            return lastLocation;
        }
    }

    LocationListener[] locationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startPollingLocation(LOCATION_FOREGROUND_INTERVAL);
        return START_STICKY;
    }

    private void startPollingLocation(int frequency) {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            try {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, frequency, LOCATION_DISTANCE,
                        locationListeners[1]);
            } catch (java.lang.SecurityException | IllegalArgumentException e) {
                e.printStackTrace();
            }

            try {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, frequency, LOCATION_DISTANCE,
                        locationListeners[0]);
            } catch (java.lang.SecurityException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopPollingLocation() {
        if (locationManager != null) {
            for (LocationListener locationListener : locationListeners) {
                try {
                    locationManager.removeUpdates(locationListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPollingLocation();
    }
}