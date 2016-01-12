package com.fisincorporated.democode.bluetoothevents;

/**
 * Notifiy UI that the remote(server) address supplied is not valid
 */
public class BleInvalidServerAddress {
    private String mDeviceAddress;
    private String mErrorMessage;

    private BleInvalidServerAddress(){;}

    public BleInvalidServerAddress(String address, String errorMessage) {
        mDeviceAddress = address;
        mErrorMessage = errorMessage;
    }

    public String getDeviceAddress() {
        return mDeviceAddress;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}
