package com.fisincorporated.democode.bluetooth;

/**
 * Hold info relating to a request to be send to BluetoothDemoservice
 */
public class BluetoothServiceRequest {
    private int what;
    private Object object;

    public BluetoothServiceRequest(int what, Object object){
        this.what = what;
        this.object = object;
    }

    public int getWhat() {
        return what;
    }

    public Object getObject() {
        return object;
    }


}
