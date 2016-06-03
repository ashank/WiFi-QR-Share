package com.vasudevrb.wifishare.view;

import android.net.wifi.WifiConfiguration;

import java.util.List;

public interface MainView extends MVPView {

    void showConfigurations(List<WifiConfiguration> configurations);

    void showLoading();

    void showEmpty();

    void showWiFiDisabled();
}
