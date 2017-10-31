package com.syzible.flagitdublinbus.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.syzible.flagitdublinbus.R;
import com.syzible.flagitdublinbus.services.LocationService;

import java.util.Objects;

import static com.syzible.flagitdublinbus.services.LocationService.START_LOCATION;
import static com.syzible.flagitdublinbus.services.LocationService.START_ZOOM;

/**
 * Created by ed on 29/10/2017.
 */

public class RealTimeFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private boolean hasZoomedIn = false;
    private LatLng lastLocation;

    private BroadcastReceiver onLocationChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Received broadcast!");
            if (Objects.equals(intent.getAction(), LocationService.LOCATION_CHANGE_FILTER)) {
                if (googleMap != null) {
                    float lat = Float.parseFloat(intent.getStringExtra("lat"));
                    float lng = Float.parseFloat(intent.getStringExtra("lng"));
                    lastLocation = new LatLng(lat, lng);

                    if (!hasZoomedIn) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                lastLocation, LocationService.CLOSE_ZOOM));
                        hasZoomedIn = true;
                    }
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(onLocationChange,
                new IntentFilter(LocationService.LOCATION_CHANGE_FILTER));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(onLocationChange);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.rti_fragment, container, false);

        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = view.findViewById(R.id.focus_gps_fab);
        fab.setOnClickListener((v) -> {
            if (googleMap != null && lastLocation != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, LocationService.CLOSE_ZOOM));
            }
        });

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(START_LOCATION, START_ZOOM));

        if (isLocationAllowed()) {
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            //this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom());
        }
    }

    private boolean isLocationAllowed() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}