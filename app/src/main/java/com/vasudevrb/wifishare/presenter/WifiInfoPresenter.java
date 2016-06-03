package com.vasudevrb.wifishare.presenter;

import android.graphics.Bitmap;
import android.net.wifi.WifiConfiguration;

import com.vasudevrb.wifishare.model.WiFi;
import com.vasudevrb.wifishare.view.WifiInfoView;

import rx.Subscriber;
import rx.Subscription;

public class WifiInfoPresenter implements Presenter<WifiInfoView> {

    WifiInfoView wifiInfoView;
    WiFi wiFi;
    Subscription subscription;

    Bitmap qr;

    @Override
    public void attachView(WifiInfoView view) {
        wifiInfoView = view;
        wiFi = new WiFi(wifiInfoView.getContext());
    }

    @Override
    public void detachView() {
        wifiInfoView = null;
        wiFi = null;
        if (subscription != null) subscription.unsubscribe();
    }

    public void getQR(WifiConfiguration wifiConfiguration, String password) {
        qr = null;
        subscription = wiFi.getQR(wifiInfoView.getContext(), wifiConfiguration, password)
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {
                        if (qr != null) {
                            wifiInfoView.showQR(qr);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        qr = bitmap;
                    }
                });
    }
}
