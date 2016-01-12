package com.fisincorporated.democode.bluetooth;

import com.fisincorporated.democode.demoui.DemoTopicInfo;
import com.fisincorporated.democode.demoui.DemoTopicList;

/**
 * Hold the list of Bluetooth demos
 * Created by ericfoertsch on 12/30/15.
 */
public class BluetoothDemoTopicList extends DemoTopicList {
    private static final long serialVersionUID = 1L;

    public BluetoothDemoTopicList() {
        // !!! Don't add .class postfix. This will give ClassNotFoundException,
        addItem(new DemoTopicInfo("Turn on/off Classic BT Discovery", "com.fisincorporated.democode.bluetooth.ClassicBluetoothDiscoveryActivity", "com.fisincorporated.democode.bluetooth.ClassicBluetoothDiscoveryFragment", null));
        addItem(new DemoTopicInfo("List/Search for BT devices", "com.fisincorporated.democode.bluetooth.ListSearchBluetoothActivity", null, null));
        addItem(new DemoTopicInfo("Ble Scan", "com.fisincorporated.democode.demoui.DemoGenericActivity","com.fisincorporated.democode.bluetooth.BleScanFragment", null));
        addItem(new DemoTopicInfo("HeartRate Peripheral(Server)", "com.fisincorporated.democode.demoui.DemoGenericActivity","com.fisincorporated.democode.bluetooth.BleHeartRatePeripheralFragment", null));

    }
}