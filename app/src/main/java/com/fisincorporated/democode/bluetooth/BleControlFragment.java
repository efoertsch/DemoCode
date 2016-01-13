package com.fisincorporated.democode.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.fisincorporated.democode.R;
import com.fisincorporated.democode.bluetoothevents.BleCharacteristicChanged;
import com.fisincorporated.democode.bluetoothevents.BleCharacteristicRead;
import com.fisincorporated.democode.bluetoothevents.BleConnected;
import com.fisincorporated.democode.bluetoothevents.BleDisconnected;
import com.fisincorporated.democode.bluetoothevents.BleServicesDiscovered;
import com.fisincorporated.democode.bluetoothevents.BleSupportedGattServicies;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Basically the DeviceControlActivity from the Android Ble demo code converted to fragment
 * Also uses Otto to get updates from BluetoothDemoService
 */
@TargetApi(18)
public class BleControlFragment extends BleDemoFragment {
    private final static String TAG = BleControlFragment.class.getSimpleName();
    private static final String LIST_NAME = "NAME";
    private static final String LIST_UUID = "UUID";

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);


    private TextView mConnectionState;
    private TextView mDataField;
    private LinearLayout mLlProgressInd;
    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;


    public BleControlFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gatt_services_characteristics, container, false);
        lookForArguments(savedInstanceState);
        setHasOptionsMenu(true);
        // Sets up UI references.
        mLlProgressInd = (LinearLayout) view.findViewById(R.id.llProgressInd);
        ((TextView) view.findViewById(R.id.device_address)).setText(mDeviceAddress);
        ((TextView) view.findViewById(R.id.device_name)).setText(mDeviceName);
        mGattServicesList = (ExpandableListView) view.findViewById(R.id.gatt_services_list);
        mGattServicesList.setOnChildClickListener(servicesListClickListner);
        mConnectionState = (TextView) view.findViewById(R.id.connection_state);
        mDataField = (TextView) view.findViewById(R.id.data_value);

        return view;
    }

    private void lookForArguments(Bundle savedInstanceState) {
        Bundle bundle = null;
        if (getArguments() != null) {
            bundle = getArguments();
        }
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        }
        if (bundle != null) {
            mDeviceName = bundle.getString(EXTRAS_DEVICE_NAME);
            mDeviceAddress = bundle.getString(EXTRAS_DEVICE_ADDRESS);

        }
    }

    public void onResume(){
        super.onResume();
        resetUI();
    }

    private void resetUI() {
        mConnected = false;
        updateConnectionState(R.string.disconnected);
        getActivity().invalidateOptionsMenu();
        clearUI();
    }

    private void clearUI() {
        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        mDataField.setText(R.string.no_data);
    }

    // If a given GATT characteristic is selected, check for supported features.  This sample
    // demonstrates 'Read' and 'Notify' features.  See
    // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
    // list of supported characteristic features.
    private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    if (mGattCharacteristics != null) {
                        final BluetoothGattCharacteristic characteristic =
                                mGattCharacteristics.get(groupPosition).get(childPosition);
                        final int charaProp = characteristic.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            // If there is an active notification on a characteristic, clear
                            // it first so it doesn't update the data field on the user interface.
                            if (mNotifyCharacteristic != null) {
                                queueBluetoothRequest(BluetoothDemoService.BLE_SET_CHARACTERISTIC_NOTIFICATION,
                                        new BleCharacteristicNotification(mNotifyCharacteristic, false, null));
                                setDescriptorIfHeartRateMeasurement(mNotifyCharacteristic);
                                mNotifyCharacteristic = null;
                            }
                            queueBluetoothRequest(BluetoothDemoService.BLE_READ_CHARACTERISTIC, characteristic);
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = characteristic;
                            queueBluetoothRequest(BluetoothDemoService.BLE_SET_CHARACTERISTIC_NOTIFICATION,
                                    new BleCharacteristicNotification(mNotifyCharacteristic, true, null));
                        }
                        setDescriptorIfHeartRateMeasurement(mNotifyCharacteristic);
                        return true;
                    }
                    return false;
                }
            };

    /**
     * A bit of a hack to follow Ble demo code.
     * @param characteristic
     */
    private void setDescriptorIfHeartRateMeasurement(BluetoothGattCharacteristic characteristic) {
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                queueBluetoothRequest(BluetoothDemoService.BLE_WRITE_DESCRIPTOR, descriptor);
            }
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                queueBluetoothRequest(BluetoothDemoService.BLE_CONNECT_TO_PERIPHERAL,
                        mDeviceAddress);
                mLlProgressInd.setVisibility(View.VISIBLE);
                return true;
            case R.id.menu_disconnect:
                queueBluetoothRequest(BluetoothDemoService.BLE_DISCONNECT_PERIPHERAL, null);
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Otto event from BluetoothDemoService fired when this devices connects to Ble peripheral
     * @param event
     */
    @Subscribe
    public void onBleConnected(BleConnected event) {
        mConnected = true;
        updateConnectionState(R.string.connected);
        getActivity().invalidateOptionsMenu();
        mLlProgressInd.setVisibility(View.GONE);
    }


    /**
     * Otto event from BluetoothDemoService fired when this devices disconnects from Ble peripheral
     * @param event
     */
    @Subscribe
    public void onBleDisconnected(BleDisconnected event) {
        resetUI();
    }


    /**
     * Otto event from BluetoothDemoService fired when Ble peripheral services discovered
     * @param event
     */
    @Subscribe
    public void onBleServicesDiscovered(BleServicesDiscovered event) {
        queueBluetoothRequest(BluetoothDemoService.BLE_GET_SERVICES, null);
    }


    /**
     * Otto event from BluetoothDemoService fired when Ble peripheral services received
     * @param event
     */
    @Subscribe
    public void onBleSupportedGattServices(BleSupportedGattServicies event) {
        displayGattServices(event.getBluetoothGattServices());
    }

    /**
     * Otto event from BluetoothDemoService fired when Ble peripheral service has responded to
     * this apps read request
     * @param event
     */
    @Subscribe
    public void onBleCharacteristicRead(BleCharacteristicRead event) {
        displayData(event.getCharacteristic());
    }

    /**
     * Otto event from BluetoothDemoService fired when Ble peripheral service has responded to
     * this apps request to be notified when a characteristic value has changed
     * @param event
     */
    @Subscribe
    public void onBleCharacteristicChanged(BleCharacteristicChanged event){
        displayData(event.getCharacteristic());
    }


    private void updateConnectionState(final int resourceId) {
                mConnectionState.setText(resourceId);
    }

    private void displayData(BluetoothGattCharacteristic characteristic) {
        String data = "";
        if (characteristic != null) {
            data = interpretCharacteristicData(characteristic);
            mDataField.setText(data);
        }
    }

    private String interpretCharacteristicData(BluetoothGattCharacteristic characteristic) {
        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
            return String.valueOf(heartRate);
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                return new String(data) + "\n" + stringBuilder.toString();
            }
        }
        return "";
    }


    /**
     * Demonstrates how to iterate through the supported GATT Services/Characteristics.
     * In this sample, we populate the data structure that is bound to the ExpandableListView
     * on the UI.
     * @param gattServices List of available services on the peripheral(server)
     */
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<>();
        mGattCharacteristics = new ArrayList<>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                getActivity(),
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2},
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        mGattServicesList.setAdapter(gattServiceAdapter);
    }


}
