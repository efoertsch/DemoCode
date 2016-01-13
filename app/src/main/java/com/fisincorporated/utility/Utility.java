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

    // http://stackoverflow.com/questions/19450452/how-to-convert-byte-array-to-hex-format-in-java
    private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static final int[] HEX_INT = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};


    public static String toHexString(byte[] bytes, int start, int finish) {
        if (start <  0 || finish < 0 || finish < start ){
            return "";
        }
        if (finish >= bytes.length){
            finish = bytes.length - 1;
        }
        byte[] convertBytes = new byte[1 + finish - start];
        System.arraycopy(bytes, start,convertBytes, 0, 1 + finish - start);
        return toHexString(convertBytes);
    }

    public static String toHexString(byte[] bytes) {
        char[] hexBytes;
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            hexBytes = convertByteToHex(bytes[j]);
            hexChars[j * 2] = hexBytes[0];
            hexChars[j * 2 + 1] = hexBytes[1];
        }
        return new String(hexChars);
    }

    public static char[] convertByteToHex(byte dataByte ){
        char[] hexChars = new char[2];
        int v = dataByte & 0xFF;
        // >>> does shift padding with zeros
        hexChars[0] = HEX_CHARS[v >>> 4];
        hexChars[1] = HEX_CHARS[v & 0x0F];
        return hexChars;
    }



}
