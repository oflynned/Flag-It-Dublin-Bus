package com.syzible.flagitdublinbus.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syzible.flagitdublinbus.R;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

/**
 * Created by ed on 29/10/2017.
 */

public class NewsFeedFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.twitter_feed_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        UserTimeline userTimeline = new UserTimeline.Builder().screenName("dublinbusnews").build();
        final TweetTimelineRecyclerViewAdapter adapter =
                new TweetTimelineRecyclerViewAdapter.Builder(getActivity())
                        .setTimeline(userTimeline)
                        .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                        .build();

        recyclerView.setAdapter(adapter);

        return view;
    }
}
