package com.syzible.flagitdublinbus.objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by ed on 30/10/2017.
 */

public class Bus {
    private long dueTime;
    private int minsUntilBus;
    private String route;
    private String enOrigin, enTerminus, gaOrigin, gaTerminus;

    public Bus(JSONObject o) throws JSONException {
        dueTime = getMillisFromDate(o.getString("arrivaldatetime"));
        minsUntilBus = getMinsUntilBus(o.getString("departureduetime"));
        route = o.getString("route");
        enOrigin = o.getString("origin");
        gaOrigin = o.getString("originlocalized");
        enTerminus = o.getString("destination");
        gaTerminus = o.getString("destinationlocalized");
    }
    private int getMinsUntilBus(String input) {
        if (input.equals("Due")) input = "0";
        return Integer.parseInt(input);
    }

    private long getMillisFromDate(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);

        try {
            return sdf.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public long getDueTime() {
        return dueTime;
    }

    public int getMinsUntilBus() {
        return minsUntilBus;
    }

    public String getRoute() {
        return route;
    }

    public String getEnOrigin() {
        return enOrigin;
    }

    public String getEnTerminus() {
        return enTerminus;
    }

    public String getGaOrigin() {
        return gaOrigin;
    }

    public String getGaTerminus() {
        return gaTerminus;
    }
}
