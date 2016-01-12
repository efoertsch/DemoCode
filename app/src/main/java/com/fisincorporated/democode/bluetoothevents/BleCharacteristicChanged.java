package com.fisincorporated.democode.bluetoothevents;

import android.bluetooth.BluetoothGattCharacteristic;


/**
 * Created by ericfoertsch on 1/10/16.
 */
public class BleCharacteristicChanged {

    BluetoothGattCharacteristic mCharacteristic;

    private BleCharacteristicChanged() {
        ;
    }

    public BleCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        mCharacteristic = characteristic;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return mCharacteristic;
    }


}

