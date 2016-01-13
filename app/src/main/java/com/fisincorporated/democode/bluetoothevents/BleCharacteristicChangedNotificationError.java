package com.fisincorporated.democode.bluetoothevents;

import com.fisincorporated.democode.bluetooth.BleCharacteristicChangedNotification;

/**
 * Hold information from failed characteristic notification
 */
public class BleCharacteristicChangedNotificationError {
    private BleCharacteristicChangedNotification mBleCharacteristicChangedNotification;

    private BleCharacteristicChangedNotificationError(){;}

    public BleCharacteristicChangedNotificationError(BleCharacteristicChangedNotification bleCharacteristicChangedNotification) {
        mBleCharacteristicChangedNotification = bleCharacteristicChangedNotification;
    }

    public BleCharacteristicChangedNotification getBleCharacteristicChangedNotification(){
        return mBleCharacteristicChangedNotification;
    }


}
