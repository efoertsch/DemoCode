package com.fisincorporated.democode.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.fisincorporated.application.DemoApplication;
import com.fisincorporated.democode.R;
import com.fisincorporated.democode.demoui.DemoDrillDownFragment;

import java.util.LinkedList;

/**
 * Use this fragment as parent class for Ble related UI
 * If will bind to BluetoothDemoService and pass requests to the service
 * It also registers for Otto events (probably from BluetoothDemoService)
 */
public class BleDemoFragment extends DemoDrillDownFragment {
    private static final String TAG = DemoDrillDownFragment.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 1;

    protected BluetoothDemoService mBluetoothDemoService;
    private LinkedList<BluetoothServiceRequest> mBluetoothServiceRequestList = new LinkedList<>();
    protected boolean mBound = false;
    private BluetoothAdapter mBluetoothAdapter;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Use this check to determine whether BLE is supported on the device.
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        // Initializes a Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT < 18) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        } else {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        bindToBluetoothDemoService();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        // Register for the Otto bus so it can receive events
        DemoApplication.getOttoBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop scanning (just in case it is)
        queueBluetoothRequest(BluetoothDemoService.BLE_STOP_SCAN, null);
        // Don't get anymore bus events - in this case from BleDemoService(at least for now)
        DemoApplication.getOttoBus().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        unBindFromBluetoothDemoService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBluetoothDemoService = null;
    }


    /**
     * If BluetoothDemoService not yet bound, queue the request until it is
     * request(s) first
     *
     * @param what    BluetoothDemoService request  o
     * @param request - what needs to be passed to BluetoothDemoService based on the 'what'
     */
    protected void queueBluetoothRequest(int what, Object request) {
        synchronized (mBluetoothServiceRequestList) {
            if (mBound) {
                if (mBluetoothServiceRequestList.size() > 0) {
                    processQueuedRequests();
                }
                mBluetoothDemoService.queueBluetoothRequest(what, request);

            } else {
                mBluetoothServiceRequestList.addLast(new BluetoothServiceRequest(what, request));
                bindToBluetoothDemoService();
                ;
            }
        }
    }

    /**
     * Queue request to BluetoothDemoService if not yet bound (not complete yet).
     */
    private void processQueuedRequests() {
        BluetoothServiceRequest bluetoothServiceRequest;
        synchronized (mBluetoothServiceRequestList) {
            for (int i = 0; i < mBluetoothServiceRequestList.size(); ) {
                bluetoothServiceRequest = mBluetoothServiceRequestList.getFirst();
                mBluetoothDemoService.queueBluetoothRequest(bluetoothServiceRequest.getWhat(),
                        bluetoothServiceRequest.getObject());
                mBluetoothServiceRequestList.removeFirst();
            }
        }
    }

    /**
     * Bind to BluetoothDemoService
     */
    private void bindToBluetoothDemoService() {
        Intent intent = new Intent(getActivity(),
                BluetoothDemoService.class);
        boolean bindComplete = getActivity().bindService(
                intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Define callbacks for BluetoothDemoService connection
     * When connection occurs see if there were any queued requests and send them.
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            BluetoothDemoService.BleServiceBinder binder = (BluetoothDemoService.BleServiceBinder) service;
            mBluetoothDemoService = binder.getService();
            mBound = true;
            processQueuedRequests();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    /**
     * Unbind from MonetService. Call when you want to exit app and stop
     * MonetService
     */
    public void unBindFromBluetoothDemoService() {
        if (mBound) {
            getActivity().unbindService(mServiceConnection);
            mBound = false;
        }
    }


}
