package com.vasudevrb.wifishare.util;

import android.net.wifi.WifiConfiguration;
import android.text.TextUtils;

public class Utils {

    public static String getSSID(WifiConfiguration wifiConfiguration) {
        return wifiConfiguration.SSID.replaceAll("\"", "");
    }

    public static String getSecurity(WifiConfiguration wifiConfig) {
        if (!TextUtils.isEmpty(wifiConfig.preSharedKey)) {
            return "WPA";
        } else if (!TextUtils.isEmpty(wifiConfig.wepKeys[0])) {
            return "WEP";
        }
        return "OPEN";
    }
}
