package com.fisincorporated.democode.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fisincorporated.democode.oldcodetemplates.MasterFragment;
import com.fisincorporated.democode.R;

import java.util.ArrayList;


/**
 * For classic Bluetooth
 * Turn on/off bluetoothAdapter and list what bt devices you find
 */
public class ClassicBluetoothDiscoveryFragment extends MasterFragment {

    protected static final String TAG = "ClassicBluetoothDiscoveryFragment";
    protected static final int DISCOVERY_REQUEST = 1;
    private BluetoothAdapter bluetoothAdapter;
    private Button buttonEnable;
    private TextView tvBluetoothStatus;
    private ScrollView svScrollStatus;
    protected static final String lineSeparator = System
            .getProperty("line.separator");
    private ArrayList<BluetoothDevice> deviceList =
            new ArrayList<BluetoothDevice>();
    private static final int ENABLE_BLUETOOTH = 1;
    private boolean isRecieverRegistered = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.bluetooth, container, false);
        getReferencedViews(view);
        return view;
    }

    private void getReferencedViews(View view) {
        buttonEnable = (Button) view.findViewById(R.id.buttonEnable);
        buttonEnable.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (buttonEnable.getText().equals(getResources().getText(R.string.turn_on_bluetooth_discovery))) {
                    initBluetooth();
                    setButtonText(false);
                } else {
                    stopDiscovery();
                    setButtonText(true);
                }

            }
        });
        svScrollStatus = (ScrollView) view.findViewById(R.id.svScrollStatus);
        tvBluetoothStatus = (TextView) view.findViewById(R.id.tvBluetoothStatus);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bluetoothAdapter.isEnabled()) {
            setButtonText(false);
            startDiscovery();

        }
        else {
            setButtonText(true);
        }
    }


    public void onPause() {
        super.onPause();
        stopDiscovery();

    }

    private void setButtonText(boolean turnOn) {
        if (turnOn) {
            buttonEnable.setText(R.string.turn_on_bluetooth_discovery);
        } else {
            buttonEnable.setText(R.string.turn_off_bluetooth_discovery);
        }
    }

    private void updateStatus(String message) {
        tvBluetoothStatus.append(message + lineSeparator);
        svScrollStatus.fullScroll(View.FOCUS_DOWN);
    }


    private void initBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth isn't enabled, prompt the user to turn it on.
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, ENABLE_BLUETOOTH);
        } else {
            // Bluetooth is enabled, initialize the UI.
            updateStatus("Bluetooth enabled.");
            initBluetoothUI();
        }
    }

    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        if (requestCode == ENABLE_BLUETOOTH)
            if (resultCode == Activity.RESULT_OK) {
                updateStatus("Bluetooth enabled.");
                // Bluetooth has been enabled, initialize the UI.
                initBluetoothUI();
            }

        if (requestCode == DISCOVERY_REQUEST) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "Discovery cancelled by user");
            }
        }

    }


    private void initBluetoothUI() {
        tvBluetoothStatus.setText("");
        startDiscovery();
    }

    private void startDiscovery() {
        getActivity().registerReceiver(discoveryResult,
                new IntentFilter(BluetoothDevice.ACTION_FOUND));
        isRecieverRegistered = true;
        if (bluetoothAdapter.isEnabled() && !bluetoothAdapter.isDiscovering()) {
            deviceList.clear();
        }
        bluetoothAdapter.startDiscovery();
        updateStatus("Starting Discovery.");
    }

    private void stopDiscovery() {
        updateStatus("Stopped discovery");
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        if (isRecieverRegistered) {
            getActivity().unregisterReceiver(discoveryResult);
            isRecieverRegistered = false;
            updateStatus("Unregistered receiver");
        }
    }

    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String remoteDeviceName =
                    intent.getStringExtra(BluetoothDevice.EXTRA_NAME);

            BluetoothDevice remoteDevice =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            deviceList.add(remoteDevice);
            updateStatus("Found " + remoteDevice.getName() + "/" + remoteDevice.getAddress() );
            Log.d(TAG, "Discovered " + remoteDeviceName);
        }
    };


}