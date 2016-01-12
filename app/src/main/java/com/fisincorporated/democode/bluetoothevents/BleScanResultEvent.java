package com.fisincorporated.democode.bluetoothevents;

import com.fisincorporated.democode.bluetooth.BleScanResult;

/**
 * Notify BleScanResult event
 * Created by ericfoertsch on 1/8/16.
 */
public class BleScanResultEvent {
    private BleScanResult mBleScanResult;
    public BleScanResultEvent(BleScanResult bleScanResult){
        mBleScanResult = bleScanResult;
    }

    public BleScanResult getBleScanResult() {
        return mBleScanResult;
    }
}
