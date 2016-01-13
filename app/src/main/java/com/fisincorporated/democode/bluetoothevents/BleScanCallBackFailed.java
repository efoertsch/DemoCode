package com.fisincorporated.democode.bluetoothevents;

/**
 * Created by ericfoertsch on 1/8/16.
 */
public class BleScanCallBackFailed {
    private final int mErrorCode;

    public BleScanCallBackFailed(int error) {
        mErrorCode = error;
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
