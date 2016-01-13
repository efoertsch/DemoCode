package com.fisincorporated.democode.bluetoothevents;

import android.bluetooth.BluetoothDevice;

public class BondStatusUpdate {
    private int status;
    private BluetoothDevice device;

    public BondStatusUpdate(BluetoothDevice device, int status) {
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
