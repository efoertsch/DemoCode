package com.fisincorporated.democode.bluetoothevents;

import android.bluetooth.BluetoothDevice;

/**
 * Encapsulate info from BluetoothGattServer.onNotificationSent(BluetoothDevice device, int status)
 */
public class BleNotificationSent {
    private BluetoothDevice mDevice;
    private int mStatus;
    private BleNotificationSent(){;}
    public BleNotificationSent(BluetoothDevice device, int status){
        mDevice = device;
        mStatus = status;
    }

    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public int getStatus() {
        return mStatus;
    }
}
