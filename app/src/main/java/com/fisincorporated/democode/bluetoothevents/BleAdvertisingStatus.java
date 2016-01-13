package com.fisincorporated.democode.bluetoothevents;

import android.bluetooth.le.AdvertiseSettings;

/**
 * Created by ericfoertsch on 1/12/16.
 */
public class BleAdvertisingStatus {
    private int mStatusCode;
    private AdvertiseSettings mAdvertiseSettings;
    private String mMsg;

    private BleAdvertisingStatus() {
        ;
    }

    public BleAdvertisingStatus(int statusCode, String msg, AdvertiseSettings advertiseSettings) {
        this.mStatusCode = statusCode;
        this.mMsg = msg;
        this.mAdvertiseSettings = advertiseSettings;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    public AdvertiseSettings getAdvertiseSettings() {
        return mAdvertiseSettings;
    }

    public String getMsg() {
        return mMsg;
    }
}
