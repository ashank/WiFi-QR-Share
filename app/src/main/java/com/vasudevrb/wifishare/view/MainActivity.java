package com.vasudevrb.wifishare.view;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vasudevrb.wifishare.R;
import com.vasudevrb.wifishare.WiFiListAdapter;
import com.vasudevrb.wifishare.presenter.MainPresenter;
import com.vasudevrb.wifishare.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView, WiFiListAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_wifi_list)
    RecyclerView recyclerView;

    @BindView(R.id.progress_wifi_list)
    ProgressBar progressBar;

    @BindView(R.id.view_no_config)
    View viewNoConfig;

    @BindView(R.id.view_wifi_disabled)
    View viewWiFiDisabled;

    WiFiListAdapter wifiListAdapter;

    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        presenter = new MainPresenter();
        presenter.attachView(this);

        wifiListAdapter = new WiFiListAdapter(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(wifiListAdapter);

        //Load configurations here? or in onResume?
        //presenter.loadConfigurations();

        //Setup the add a network and enable wifi button that appear when no networks are configured
        //and when the wifi is disabled
        Button btnWiFiSettings = (Button) viewNoConfig.findViewById(R.id.btn_add_network);
        btnWiFiSettings.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));

        Button btnEnableWiFi = (Button) viewWiFiDisabled.findViewById(R.id.btn_enable_wifi);
        btnEnableWiFi.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadConfigurations();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showConfigurations(List<WifiConfiguration> configurations) {
        wifiListAdapter.update(configurations);

        recyclerView.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.GONE);
        viewNoConfig.setVisibility(View.GONE);
        viewWiFiDisabled.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setVisibility(View.GONE);
        viewNoConfig.setVisibility(View.GONE);
        viewWiFiDisabled.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        viewNoConfig.setVisibility(View.VISIBLE);

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        viewWiFiDisabled.setVisibility(View.GONE);
    }

    @Override
    public void showWiFiDisabled() {
        viewWiFiDisabled.setVisibility(View.VISIBLE);

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        viewNoConfig.setVisibility(View.GONE);

    }

    @Override
    public Context getContext() {
        return MainActivity.this;
    }

    @Override
    public void onItemClick(int position) {
        WifiConfiguration wifiConfiguration = wifiListAdapter.getItemAt(position);

        if (Utils.getSecurity(wifiConfiguration).equals("OPEN")) {
            Toast.makeText(getContext(), R.string.message_connect_open, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, WifiInfoActivity.class);
        intent.putExtra("config", wifiConfiguration);
        startActivity(intent);
    }
}
