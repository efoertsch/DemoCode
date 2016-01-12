package com.fisincorporated.democode.bluetoothevents;

import android.bluetooth.BluetoothDevice;

/**
 * Encapsulate BluettoothServerCallback.onConnectionStateChanged info
 */
public class BleClientDeviceStatusChange {
    private BluetoothDevice mDevice;
    private int mStatus;
    private int mNewState;

    private BleClientDeviceStatusChange() {
        ;
    }

    public BleClientDeviceStatusChange(BluetoothDevice device, int status, int newState) {
        mDevice = device;
        mStatus = status;
        mNewState = newState;
    }

    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public int getStatus() {
        return mStatus;
    }

    public int getNewState() {
        return mNewState;
    }
}
