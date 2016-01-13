package com.fisincorporated.democode.bluetoothevents;

import android.bluetooth.BluetoothDevice;

/**
 * Hold device HFP status info
 */
public class HFPStatusUpdate {
    private int status;
    private BluetoothDevice device;

    public HFPStatusUpdate(BluetoothDevice device, int status) {
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
