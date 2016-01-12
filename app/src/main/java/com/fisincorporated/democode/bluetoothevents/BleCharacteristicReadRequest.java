package com.fisincorporated.democode.bluetoothevents;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Encapsulate BluettoothServerCallback.onCharacteristicReadRequest info
 */
public class BleCharacteristicReadRequest {

    private BluetoothDevice mDevice;
    private int mRequestId;
    private int mOffset;
    private BluetoothGattCharacteristic mCharacteristic;


    private BleCharacteristicReadRequest(){;}

    public BleCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic){
        mDevice = device;
        mRequestId = requestId;
        mOffset =  offset;
        mCharacteristic = characteristic;
    }
    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public int getRequestId() {
        return mRequestId;
    }

    public int getOffset() {
        return mOffset;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }
}
