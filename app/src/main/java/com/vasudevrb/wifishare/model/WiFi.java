package com.vasudevrb.wifishare.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.vasudevrb.wifishare.util.Utils;

import net.glxn.qrgen.android.QRCode;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//For the lack of a better name
public class WiFi {

    private static final String QRCODE = "WIFI:S:%s;T:%s;P:%s;;";

    private WifiManager wifiManager;

    public WiFi(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public boolean isWiFiEnabled() {
        return wifiManager.isWifiEnabled();
    }

    public Observable<List<WifiConfiguration>> getConfigurations() {
        return Observable.just(1)
                .map(i -> wifiManager.getConfiguredNetworks())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Bitmap> getQR(Context context, WifiConfiguration wifiConfiguration, String password) {
        String ssid = Utils.getSSID(wifiConfiguration);
        String security = Utils.getSecurity(wifiConfiguration);

        int height = context.getResources().getDisplayMetrics().heightPixels;
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int size = Math.min(width, height);

        String qr = String.format(QRCODE, ssid, security, password);

        return Observable.just(1)
                .map(i -> QRCode.from(qr).withSize(size, size).bitmap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
