package com.fisincorporated.utility;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;

/**
 * Created by ericfoertsch on 1/8/16.
 */
public class Utility {
    /**
     * Convert the connection state int value to a string
     * Used for debugging/logging purposes
     * @param intExtra
     * @return
     */
    public static String getConnectionState(int intExtra) {
        switch (intExtra) {
            case BluetoothProfile.STATE_CONNECTING:
                return "STATE CONNECTING";
            case BluetoothProfile.STATE_CONNECTED:
                return "STATE CONNECTED";
            case BluetoothProfile.STATE_DISCONNECTING:
                return "STATE DISCONNECTING";
            case BluetoothProfile.STATE_DISCONNECTED:
                return "STATE DISCONNECTED";
            default:
                return "Undefined bluetooth profile state" + intExtra;
        }
    }

    /**
     * Convert the connection state int value to a string
     * Used for debugging/logging purposes
     * @param intExtra
     * @return
     */
    public static String getBondState(int intExtra) {
        switch (intExtra) {
            case BluetoothDevice.BOND_NONE:
                return "BOND_NONE";
            case BluetoothDevice.BOND_BONDING:
                return "BOND_BONDING";
            case BluetoothDevice.BOND_BONDED:
                return "BOND_BONDED";
            default:
                return "Undefined bluetooth bond state" + intExtra;
        }
    }

}
