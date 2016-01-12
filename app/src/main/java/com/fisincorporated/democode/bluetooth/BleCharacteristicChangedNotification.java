package com.fisincorporated.democode.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by ericfoertsch on 1/12/16.
 */
public class BleCharacteristicChangedNotification {
    private BluetoothDevice mDevice;
    private BluetoothGattCharacteristic mCharacteristic;
    private boolean mConfirm;

    private BleCharacteristicChangedNotification() {
        ;
    }

    public BleCharacteristicChangedNotification(BluetoothDevice device, BluetoothGattCharacteristic characteristic, boolean confirm) {
        mDevice = device;
        mCharacteristic = characteristic;
        mConfirm = confirm;
    }


    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }

    public boolean isConfirm() {
        return mConfirm;
    }
}
