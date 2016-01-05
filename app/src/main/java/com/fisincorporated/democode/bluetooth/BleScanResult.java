package com.fisincorporated.democode.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;

/**
 * Hold scan info running on V4.3+
 * Created by ericfoertsch on 1/4/16.
 * cribbed some code from http://stackoverflow.com/questions/22016224/ble-obtain-uuid-encoded-in-advertising-packet
 */
public class BleScanResult {
    private static final String TAG = BleScanResult.class.getSimpleName();
    private byte[] mScanRecord;
    private BluetoothDevice mBluetoothDevice;
    private int mRssi;
    private BleScanRecord mBleScanRecord = null;
    private ScanResult mScanResult = null;
    private boolean preLollipop = true;

    /**
     * Constructor for versions 18->20
     *
     * @param device
     * @param scanRecord
     * @param rssi
     */
    public BleScanResult(BluetoothDevice device, byte[] scanRecord, int rssi) {
        this.mScanRecord = scanRecord;
        this.mBluetoothDevice = device;
        this.mRssi = rssi;
        mBleScanRecord = BleScanRecord.parseFromBytes(scanRecord);
    }

    /**
     * Constructor for V21+
     *
     * @param scanResult
     */
    public BleScanResult(ScanResult scanResult) {
        mScanResult = scanResult;
        preLollipop = false;
    }

    @TargetApi(21)
    public byte[] getScanRecord() {
        if (preLollipop) {
            return mScanRecord;
        } else {
            return mScanResult.getScanRecord().getBytes();
        }
    }

    @TargetApi(21)
    public BluetoothDevice getDevice() {
        if (preLollipop) {
            return mBluetoothDevice;
        } else {
            return mScanResult.getDevice();
        }
    }

    @TargetApi(21)
    public String getDeviceName() {
        if (preLollipop) {
            return mBluetoothDevice.getName();
        } else {
            return mScanResult.getDevice().getName();
        }
    }

    @TargetApi(21)
    public String getAddress() {
        if (preLollipop) {
            return mBluetoothDevice.getAddress();
        } else {
            return mScanResult.getDevice().getAddress();
        }
    }

    @TargetApi(21)
    public int getRssi() {
        if (preLollipop) {
            return mRssi;
        } else {
            return mScanResult.getRssi();
        }
    }

    @TargetApi(21)
    public String toString() {
        if (preLollipop) {
            return "ScanResult{" + "mDevice=" + mBluetoothDevice + ", mScanRecord="
                    +mBleScanRecord.toString() + ", mRssi=" + mRssi + ", mTimestampNanos="
                    + 0 + '}';
        } else {
            return mScanResult.toString();
        }
    }


}



