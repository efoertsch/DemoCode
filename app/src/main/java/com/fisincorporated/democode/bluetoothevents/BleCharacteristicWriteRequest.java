package com.fisincorporated.democode.bluetoothevents;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Encapsulate BluettoothServerCallback.onCharacteristicWriteRequest info
 */

public class BleCharacteristicWriteRequest {
    private BluetoothDevice mDevice;
    private int mRequestId;
    private BluetoothGattCharacteristic mCharacteristic;
    private boolean mPreparedWrite;
    private boolean mResponseNeeded;
    private int mOffset;
    private byte[] mValue;

    private BleCharacteristicWriteRequest() {
        ;
    }

    public BleCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
    this.mDevice = device;
        this.mRequestId = requestId;
        this.mCharacteristic = characteristic;
        this.mPreparedWrite = preparedWrite;
        this.mResponseNeeded = responseNeeded;
        this.mOffset = offset;
        this.mValue = value;
    }

    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public int getRequestId() {
        return mRequestId;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }

    public boolean isPreparedWrite() {
        return mPreparedWrite;
    }

    public boolean isResponseNeeded() {
        return mResponseNeeded;
    }

    public int getOffset() {
        return mOffset;
    }

    public byte[] getValue() {
        return mValue;
    }
}
