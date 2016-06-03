package com.vasudevrb.wifishare.presenter;

import android.net.wifi.WifiConfiguration;

import com.vasudevrb.wifishare.model.WiFi;
import com.vasudevrb.wifishare.view.MainView;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

public class MainPresenter implements Presenter<MainView> {

    MainView wifiListView;

    WiFi wiFi;

    Subscription subscription;

    List<WifiConfiguration> configurations;

    @Override
    public void attachView(MainView view) {
        wifiListView = view;
        wiFi = new WiFi(wifiListView.getContext());
    }

    @Override
    public void detachView() {
        wifiListView = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadConfigurations() {
        wifiListView.showLoading();

        if (!wiFi.isWiFiEnabled()) {
            wifiListView.showWiFiDisabled();
            return;
        }

        subscription = wiFi.getConfigurations()
                .subscribe(new Subscriber<List<WifiConfiguration>>() {
                    @Override
                    public void onCompleted() {
                        if (configurations != null) {
                            if (configurations.isEmpty()) {
                                wifiListView.showEmpty();
                            } else {
                                wifiListView.showConfigurations(configurations);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<WifiConfiguration> c) {
                        configurations = c;
                    }
                });
    }
}
