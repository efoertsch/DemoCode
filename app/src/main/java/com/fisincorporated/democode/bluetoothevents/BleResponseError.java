package com.fisincorporated.democode.bluetoothevents;

import com.fisincorporated.democode.bluetooth.BleResponse;

/**
 *  Fired when the BluetoothGattService was unsuccessful in sending a response
 */
public class BleResponseError {
    private BleResponse mBleResponse;
    private BleResponseError(){;}
    public BleResponseError(BleResponse bleResponse){
        mBleResponse = bleResponse;
    }
    public BleResponse getBleResponse(){
        return mBleResponse;
    }
}
