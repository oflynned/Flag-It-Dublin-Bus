package com.syzible.flagitdublinbus.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syzible.flagitdublinbus.R;

/**
 * Created by ed on 29/10/2017.
 */

public class TimetablesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.timetables_fragment, container, false);
    }
}
