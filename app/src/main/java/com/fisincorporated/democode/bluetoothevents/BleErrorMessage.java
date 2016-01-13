package com.fisincorporated.democode.bluetoothevents;

/**
 * Created by ericfoertsch on 1/10/16.
 */
public class BleErrorMessage {
    private String mErrorMessage;
    private BleErrorMessage(){;}

    public BleErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }


}
