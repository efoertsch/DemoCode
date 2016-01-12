package com.fisincorporated.democode.bluetoothevents;

import android.bluetooth.BluetoothDevice;

/**
 * Hold info on this device A2DP status
 */
public class A2DPStatusUpdate {
    private int status;
    private BluetoothDevice device;

    public A2DPStatusUpdate(BluetoothDevice device, int status) {
        this.device = device;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public BluetoothDevice getDevice() {
        return device;
    }


}
