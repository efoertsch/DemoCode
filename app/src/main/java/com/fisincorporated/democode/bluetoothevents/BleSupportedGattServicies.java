package com.fisincorporated.democode.bluetoothevents;

import android.bluetooth.BluetoothGattService;

import java.util.List;

/**
 * Contains list of supported Gatt Services
 */
public class BleSupportedGattServicies {
    private List<BluetoothGattService> mBluetoothGattServices;
    private BleSupportedGattServicies(){;}

    public BleSupportedGattServicies(List<BluetoothGattService> bluetoothGattServices){
        mBluetoothGattServices = bluetoothGattServices;
    }

    public List<BluetoothGattService> getBluetoothGattServices(){
        return mBluetoothGattServices;
    }
}
