package com.fisincorporated.democode.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Encapsulate all info for sending a value back to a Ble client
 */
public class BleResponse {

    private BluetoothDevice mDevice;
    private int mRequestId;
    private int mStatus;
    private int mOffset;
    private byte[] mValue;

    private BleResponse(){;}

    /**
     * Hold all info needed for BluetoothGattServer.sendResponse()
     * @param device
     * @param requestId
     * @param status
     * @param offset
     * @param value
     */
    public BleResponse(BluetoothDevice  device, int requestId,
                       int status, int offset, byte[] value) {
        mDevice = device;
        mRequestId = requestId;
        mStatus = status;
        mOffset = offset;
        mValue = value;
    }

    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public int getRequestId() {
        return mRequestId;
    }

    public int getStatus() {
        return mStatus;
    }

    public int getOffset() {
        return mOffset;
    }

    public byte[] getValue() {
        return mValue;
    }
}
