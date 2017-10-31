package com.syzible.flagitdublinbus.objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ed on 31/10/2017.
 */

public class StopPoint implements ClusterItem {
    private int id;
    private String address;
    private ArrayList<String> routes = new ArrayList<>();
    private LatLng location;

    public StopPoint(JSONObject o) {
        try {
            this.id = Integer.parseInt(o.getString("stopnumber"));
            this.address = o.getString("address");

            String[] givenRoutes = o.getString("routes").split("</br>");
            this.routes.addAll(Arrays.asList(givenRoutes));

            float lat = (float) o.getDouble("lat");
            float lng = (float) o.getDouble("lng");
            this.location = new LatLng(lat, lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LatLng getPosition() {
        return location;
    }

    @Override
    public String getTitle() {
        return String.valueOf(id);
    }

    @Override
    public String getSnippet() {
        return address;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<String> getRoutes() {
        return routes;
    }

    public LatLng getLocation() {
        return location;
    }
}
