package com.syzible.flagitdublinbus.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.syzible.flagitdublinbus.R;
import com.syzible.flagitdublinbus.networking.Endpoints;
import com.syzible.flagitdublinbus.networking.RestClient;
import com.syzible.flagitdublinbus.objects.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ed on 30/10/2017.
 */

public class ResultsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<Bus> results = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    private String endpoint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_results_fragment, container, false);
        recyclerView = view.findViewById(R.id.bus_feed_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        endpoint = Endpoints.getRealtimeInfo(47);
        loadData();

        swipeRefreshLayout = view.findViewById(R.id.card_container_layout);
        swipeRefreshLayout.setOnRefreshListener(this::loadData);


        return view;
    }

    private void loadData() {
        RestClient.get(endpoint, new BaseJsonHttpResponseHandler<JSONArray>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, JSONArray response) {
                results.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject o = response.getJSONObject(i);
                        Bus bus = new Bus(o);
                        results.add(bus);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapter = new BusAdapter(getActivity(), results);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, JSONArray errorResponse) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            protected JSONArray parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return new JSONObject(rawJsonData).getJSONArray("results");
            }
        });
    }

    public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {
        private List<Bus> dataset;
        private Context context;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView timeTextView, terminusTextView, routeTextView, minsTextView;

            ViewHolder(View v) {
                super(v);
                this.timeTextView = v.findViewById(R.id.due);
                this.terminusTextView = v.findViewById(R.id.terminus);
                this.routeTextView = v.findViewById(R.id.route);
                this.minsTextView = v.findViewById(R.id.due_mins_text);
            }
        }

        private BusAdapter(Context context, ArrayList<Bus> dataset) {
            this.context = context;
            this.dataset = dataset;
        }

        @Override
        public BusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.bus_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Bus bus = dataset.get(position);
            holder.timeTextView.setText(String.valueOf(bus.getMinsUntilBus()));
            holder.terminusTextView.setText(String.valueOf(bus.getEnTerminus()));
            holder.routeTextView.setText(String.valueOf(bus.getRoute()));
            holder.minsTextView.setText(bus.getMinsUntilBus() == 1 ? "min" : "mins");
        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }
    }
}