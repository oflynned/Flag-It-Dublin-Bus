package com.syzible.flagitdublinbus.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.syzible.flagitdublinbus.R;
import com.syzible.flagitdublinbus.fragments.NewsFeedFragment;
import com.syzible.flagitdublinbus.fragments.PlannerFragment;
import com.syzible.flagitdublinbus.fragments.RealTimeFragment;
import com.syzible.flagitdublinbus.fragments.ResultsFragment;
import com.syzible.flagitdublinbus.fragments.TimetablesFragment;
import com.syzible.flagitdublinbus.services.LocationService;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_rti:
                setFragment(getFragmentManager(), new RealTimeFragment());
                return true;
            case R.id.navigation_planner:
                setFragment(getFragmentManager(), new PlannerFragment());
                return true;
            case R.id.navigation_timetables:
                setFragment(getFragmentManager(), new TimetablesFragment());
                return true;
            case R.id.navigation_news:
                setFragment(getFragmentManager(), new NewsFeedFragment());
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(
                        getResources().getString(R.string.CONSUMER_KEY),
                        getResources().getString(R.string.CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);

        setFragment(getFragmentManager(), new ResultsFragment());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startService(new Intent(this, LocationService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stopService(new Intent(this, LocationService.class));
    }

    public static void setFragment(FragmentManager fragmentManager, Fragment fragment) {
        if (fragmentManager != null)
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_holder, fragment)
                    .commit();
    }

    public static void setFragmentBackstack(FragmentManager fragmentManager, Fragment fragment) {
        if (fragmentManager != null)
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_holder, fragment)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
    }

    public static void removeFragment(FragmentManager fragmentManager) {
        if (fragmentManager != null)
            fragmentManager.popBackStack();
    }

    public static void clearBackstack(FragmentManager fragmentManager) {
        if (fragmentManager != null)
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
