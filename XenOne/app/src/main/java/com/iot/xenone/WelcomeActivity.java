package com.iot.xenone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import cz.msebera.android.httpclient.Header;

public class WelcomeActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressBar progressBar;
    private LinearLayout mainLayout;

    private String URL = getString(R.string.api_url);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        listView = findViewById(R.id.list_view);
        progressBar = findViewById(R.id.loading_spinner);
        mainLayout = findViewById(R.id.mainLayout);

        initOnRefresh();
        registerNetwork();
        requestData(URL);
    }

    private void requestData(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        progressBar.setVisibility(View.INVISIBLE);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                Log.d("Akerbr", "JSON: " + response.toString());
                try {
                    HashMap all_data = new Gson().fromJson(response.toString(), HashMap.class);
                    ArrayList<HashMap> item = new ArrayList<HashMap>(all_data.values());
                    InfoAdapter adapter = new InfoAdapter(WelcomeActivity.this, item);
                    listView.setAdapter(adapter);
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
        this.registerReceiver(receiver, filter);
    }

    public void initOnRefresh() {
        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            requestData(URL);
            pullToRefresh.setRefreshing(false);
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
