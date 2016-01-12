package com.fisincorporated.democode.bluetooth;


import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fisincorporated.democode.R;
import com.fisincorporated.democode.bluetoothevents.BleCharacteristicReadRequest;
import com.fisincorporated.democode.bluetoothevents.BleClientDeviceStatusChange;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Starts a Ble server. Allows input of heart rate(pulse) for which updates
 */
@TargetApi(21)
public class BleHeartRatePeripheralFragment extends BleDemoFragment {
    public static final String TAG = BleHeartRatePeripheralFragment.class.getSimpleName();

    // Heart rate service is 180d ( the remainder is 'standard' part of uuid
    private static final String HEART_RATE_SERVICE = "0000180d-0000-1000-8000-00805f9b34fb";
    // Heart rate characteristic
    private static final String HEART_RATE_CHARACTERISTIC = "00002a37-0000-1000-8000-00805f9b34fb";
    private static final int HEART_RATE_FORMAT = BluetoothGattCharacteristic.FORMAT_UINT8;
    private static final int HEART_RATE_VALUE_OFFSET = 1;

    ArrayList<BluetoothGattService> bluetoothGattServices = new ArrayList<>();

    private BluetoothGattCharacteristic mHeartRateCharacteristic;
    private BluetoothDevice mClientDevice = null;

    private EditText mEtHeartRate;
    private TextView mTvConnectedClient;


    public BleHeartRatePeripheralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ble_heart_rate_peripheral, container, false);
        mEtHeartRate = (EditText) view.findViewById(R.id.etHeartRate);
        mEtHeartRate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    return true;
                }
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    // send the new heartrate value
                    if (mClientDevice != null) {
                        setHeartRate();
                        queueBluetoothRequest(BluetoothDemoService.BLE_SEND_NOTIFICATION, new BleCharacteristicChangedNotification(
                                mClientDevice, mHeartRateCharacteristic, false));
                    }
                    return true;
                }

                return false;
            }
        });
        mTvConnectedClient = (TextView) view.findViewById(R.id.tvConnectedClient);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start advertising as heart rate periphal
        if (Build.VERSION.SDK_INT >= 21) {
            bluetoothGattServices.add(createHeartRateService());
            queueBluetoothRequest(BluetoothDemoService.BLE_START_PERIPHERAL_SERVICE, bluetoothGattServices);
        } else {
            Toast.makeText(getActivity(), R.string.must_be_at_api_21, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 21) {
            queueBluetoothRequest(BluetoothDemoService.BLE_STOP_PERIPHERAL_SERVICE, null);
            bluetoothGattServices.clear();
            mClientDevice = null;
        }
    }

    private BluetoothGattService createHeartRateService() {
        BluetoothGattService heartRateService = new BluetoothGattService(
                UUID.fromString(HEART_RATE_SERVICE),
                BluetoothGattService.SERVICE_TYPE_PRIMARY);
        // set up heart rate characteristic
        mHeartRateCharacteristic = new BluetoothGattCharacteristic(
                UUID.fromString(HEART_RATE_CHARACTERISTIC),
                BluetoothGattCharacteristic.PROPERTY_READ,
                BluetoothGattCharacteristic.PERMISSION_READ);
        boolean valueSet = mHeartRateCharacteristic.setValue(getHeartRate(), HEART_RATE_FORMAT, HEART_RATE_VALUE_OFFSET);
        Log.d(TAG, "Heartrate value set:" + valueSet);
        mHeartRateCharacteristic.addDescriptor(new BluetoothGattDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG),
                BluetoothGattDescriptor.PERMISSION_READ));

        heartRateService.addCharacteristic(mHeartRateCharacteristic);
        return heartRateService;

    }

    private void setHeartRate() {
        mHeartRateCharacteristic.setValue(getHeartRate(), HEART_RATE_FORMAT, HEART_RATE_VALUE_OFFSET);
    }

    private int getHeartRate() {
        String heartRate = mEtHeartRate.getText().toString();
        if (heartRate.equals("")) {
            return 0;
        }
        return Integer.parseInt(heartRate);

    }

    @Subscribe
    public void onBleClientDeviceStatusChange(BleClientDeviceStatusChange event) {
        String deviceName = event.getDevice().getName();
        if (event.getNewState() == BluetoothProfile.STATE_CONNECTED) {
            mTvConnectedClient.setText(deviceName);
            mClientDevice = event.getDevice();
            Log.d(TAG, " Client connected:" + deviceName);
        } else {
            mTvConnectedClient.setText(R.string.not_connected);
            mClientDevice = null;
            Log.d(TAG, " Client disconnected:" + deviceName);
        }
    }

    @Subscribe
    public void onBleCharacteristicReadRequest(BleCharacteristicReadRequest event) {
        if (event.getCharacteristic().getUuid().toString().equalsIgnoreCase(HEART_RATE_CHARACTERISTIC)) {
            Log.d(TAG, " Got read request for Heart rate characteristic");
            BluetoothGattCharacteristic characteristic = event.getCharacteristic();
            // Do I need to set value on characteristic if my sendResponse requires byte[] value?
            boolean valueSet = characteristic.setValue(getHeartRate(), HEART_RATE_FORMAT, HEART_RATE_VALUE_OFFSET);
            Log.d(TAG, "onBleCharacteristicReadRequest() Heartrate value set:" + valueSet);
            queueBluetoothRequest(BluetoothDemoService.BLE_SEND_RESPONSE,
                    new BleResponse(event.getDevice(), event.getRequestId(),
                            BluetoothGatt.GATT_SUCCESS, 0, characteristic.getValue()));
        }
    }

    @Subscribe
    public void onBleResponseError(BleResponse bleResponse) {
        String errorMsg = "Error sending Ble Response to:" + bleResponse.getDevice().getName();
        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, errorMsg);
    }

}
