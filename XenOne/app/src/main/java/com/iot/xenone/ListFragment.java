package com.iot.xenone;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.iot.xenone.extraclasses.InfoAdapter;
import com.iot.xenone.extraclasses.NetworkChangeReceiver;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class ListFragment extends Fragment {

    private ListView listView;
    private ProgressBar progressBar;
    private LinearLayout mainLayout;

    private String URL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        URL = getString(R.string.api_url);
        listView = view.findViewById(R.id.list_view);
        progressBar = view.findViewById(R.id.loading_spinner);
        mainLayout = view.findViewById(R.id.mainLayout);

        initOnRefresh(view);
        registerNetwork();
        requestData(URL);
    }

    private void requestData(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        progressBar.setVisibility(View.INVISIBLE);
        final ArrayList<HashMap>[] item = new ArrayList[]{new ArrayList<HashMap>()};
        final InfoAdapter[] adapter = {new InfoAdapter(getActivity(), item[0])};
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                Log.d("Akerbr", "JSON: " + response.toString());
                try {
                    HashMap all_data = new Gson().fromJson(response.toString(), HashMap.class);
                    Log.d("Akerbr", "JSON: " + all_data.keySet().toString());
                    adapter[0].addAll(all_data.values());

                    listView.setAdapter(adapter[0]);
                } catch (Exception e) {
                    Log.e("Akerbr", e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e,
                                  JSONObject response) {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(mainLayout, R.string.load_failed, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void registerNetwork() {
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        NetworkChangeReceiver receiver = new NetworkChangeReceiver(mainLayout);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
    }

    public void initOnRefresh(View view) {
        SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            requestData(URL);
            pullToRefresh.setRefreshing(false);
        });
    }
}
