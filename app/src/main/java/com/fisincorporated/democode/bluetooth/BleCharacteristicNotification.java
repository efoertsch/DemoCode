package com.fisincorporated.democode.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/**
 * Passed to the Ble Service to notify remote(server) to  enable or disable notification
 * on a given characteristic.
 */
public class BleCharacteristicNotification {
    private BluetoothGattCharacteristic mCharacteristic;
    private boolean mEnabled;
    private BluetoothGattDescriptor mDescriptor;

    private BleCharacteristicNotification() {
        ;
    }

    /**
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     * @param descriptor  - if not null, indicate to be notified of changes
     */
    public BleCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                         boolean enabled, BluetoothGattDescriptor descriptor) {
        mCharacteristic = characteristic;
        mEnabled = enabled;
        mDescriptor = descriptor;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public BluetoothGattDescriptor getDescriptor() {
        return mDescriptor;
    }

}
