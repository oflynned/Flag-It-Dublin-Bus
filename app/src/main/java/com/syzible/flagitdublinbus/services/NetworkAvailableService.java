package com.syzible.flagitdublinbus.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ed on 04/09/2017.
 */

public class NetworkAvailableService {
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }
}
