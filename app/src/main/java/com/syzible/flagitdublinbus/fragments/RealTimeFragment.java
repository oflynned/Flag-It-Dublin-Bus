package com.syzible.flagitdublinbus.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.syzible.flagitdublinbus.R;
import com.syzible.flagitdublinbus.activities.MainActivity;
import com.syzible.flagitdublinbus.networking.Endpoints;
import com.syzible.flagitdublinbus.networking.RestClient;
import com.syzible.flagitdublinbus.objects.StopPoint;
import com.syzible.flagitdublinbus.services.LocationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

import static com.syzible.flagitdublinbus.services.LocationService.START_LOCATION;
import static com.syzible.flagitdublinbus.services.LocationService.START_ZOOM;

/**
 * Created by ed on 29/10/2017.
 */

public class RealTimeFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private boolean hasZoomedIn = false;
    private LatLng lastLocation;

    private List<StopPoint> busStops = new ArrayList<>();

    private BroadcastReceiver onLocationChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
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

                    renderPointsToMap();
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

    private void renderPointsToMap() {
        LatLng ne = googleMap.getProjection().getVisibleRegion().latLngBounds.northeast;
        LatLng sw = googleMap.getProjection().getVisibleRegion().latLngBounds.southwest;
        float zoom = googleMap.getCameraPosition().zoom;

        System.out.println(Endpoints.getMapsLocations(ne, sw, zoom));

        RestClient.get(Endpoints.getMapsLocations(ne, sw, zoom), new BaseJsonHttpResponseHandler<JSONObject>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, JSONObject response) {
                try {
                    googleMap.clear();
                    busStops.clear();

                    JSONArray points = response.getJSONArray("points");
                    for (int i = 0; i < points.length(); i++) {
                        StopPoint stop = new StopPoint(points.getJSONObject(i));
                        busStops.add(stop);
                    }

                    for (StopPoint s : busStops) {
                        googleMap.addMarker(
                                new MarkerOptions()
                                        .title(String.valueOf(s.getId()))
                                        .snippet(s.getAddress())
                                        .position(s.getLocation()));
                    }

                    googleMap.setOnMarkerClickListener(marker -> {
                        ResultsFragment resultsFragment = new ResultsFragment();
                        resultsFragment.setStopId(Integer.parseInt(marker.getTitle()));
                        MainActivity.setFragmentBackstack(getFragmentManager(), resultsFragment);
                        return false;
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, JSONObject errorResponse) {

            }

            @Override
            protected JSONObject parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return new JSONObject(rawJsonData);
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(START_LOCATION, START_ZOOM));

        if (isLocationAllowed()) {
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            this.googleMap.setOnCameraChangeListener(cameraPosition -> renderPointsToMap());
        }
    }

    private boolean isLocationAllowed() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}