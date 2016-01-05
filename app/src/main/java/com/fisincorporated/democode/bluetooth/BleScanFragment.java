package com.fisincorporated.democode.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fisincorporated.democode.MasterFragment;
import com.fisincorporated.democode.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Start Ble scanning and display advertising packets
 * Adapted from Android demo code and
 * http://www.truiton.com/2015/04/android-bluetooth-low-energy-ble-example/
 */
@TargetApi(18)
public class BleScanFragment extends MasterFragment {
    protected static final String TAG = "BleScanFragment";
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    protected static final String lineSeparator = System
            .getProperty("line.separator");
    private ArrayList<BluetoothDevice> mDeviceList =
            new ArrayList<BluetoothDevice>();
    private ArrayList<BleScanResult> mBleScanResultList = new ArrayList<>();
    private BluetoothLeScanner mBluetoothLeScanner = null;
    private ScanSettings mSettings;
    private List<ScanFilter> mFilters;
    private ScanCallback mScanCallback = null;


    private ListView mLvScanList;
    private boolean mScanning;
    private Handler mHandler;

    // Stops scanning after 30 seconds.
    private static final long SCAN_PERIOD = 30000;


    public BleScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment
     */
    // TODO: Rename and change types and number of parameters
    public static BleScanFragment newInstance() {
        BleScanFragment fragment = new BleScanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), R.string.bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ble_scan, container, false);
        mLvScanList = (ListView) view.findViewById(R.id.lvScanList);
        mLvScanList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BleScanResult bleScanResult = mLeDeviceListAdapter.getDevice(position);
                if (bleScanResult == null) return;
                final Intent intent = new Intent(getActivity(), DeviceControlActivity.class);
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, bleScanResult.getDevice().getName());
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, bleScanResult.getDevice().getAddress());
                if (mScanning) {
                    scanLeDevice(false);
                }
                startActivity(intent);
            }
        });

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        mLvScanList.setAdapter(mLeDeviceListAdapter);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.bluetooth_menu, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            // menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
//            menu.findItem(R.id.menu_refresh).setActionView(
//                    R.layout.actionbar_indeterminate_progress);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {

            if (android.os.Build.VERSION.SDK_INT >= 21) {
                mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
                mSettings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                mFilters = new ArrayList<ScanFilter>();
                getScanCallback();
            }
            scanLeDevice(true);
        }



    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false);
        }
        mLeDeviceListAdapter.clear();
    }


    /**
     * Turn on or off scanning
     * @param enable
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mBluetoothLeScanner.stopScan(mScanCallback);

                    }

                }
            }, SCAN_PERIOD);
            mScanning = true;
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mBluetoothLeScanner.startScan(mFilters, mSettings, mScanCallback);
            }

        } else {
            mScanning = false;
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mBluetoothLeScanner.stopScan(mScanCallback);
            }
        }
    }

    /**
     * This callback is used for versions 18->20
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecordBytes) {
                    BleScanResult bleScanResult = new BleScanResult(device,scanRecordBytes,   rssi);
                    Log.i(TAG,"BluetoothAdapter.LeScanCallback()" + bleScanResult.getAddress());
                    updateScanDisplay(bleScanResult);
                }
            };


    /**
     * This callback is used for versions 21+
     */
    @TargetApi(21)
    private ScanCallback getScanCallback() {
        if (mScanCallback == null)
            mScanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    updateScanDisplay(new BleScanResult(result));
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    for (ScanResult sr : results) {
                        Log.i(TAG, "onBatchScanResults:" + sr.toString());
                    }
                }

                @Override
                public void onScanFailed(int errorCode) {
                    Log.e(TAG, "Scan Failed Error Code: " + errorCode);
                }
            };
        return mScanCallback;
    }

    public void updateScanDisplay(final BleScanResult bleScanResult){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLeDeviceListAdapter.addDevice(bleScanResult);
                mLeDeviceListAdapter.notifyDataSetChanged();
            }
        });
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BleScanResult> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BleScanResult>();
            mInflator = getActivity().getLayoutInflater();
        }

        public void addDevice(BleScanResult bleScanResult) {
            boolean addScanResult = true;
            for(BleScanResult listResult : mLeDevices){
                if (listResult.getDevice().getAddress().equals(bleScanResult.getDevice().getAddress())) {
                    addScanResult = false;
                }
            }
            if (addScanResult) {
                mLeDevices.add(bleScanResult);
                Log.i(TAG, " Number of devices:" + mLeDevices.size());
            }
        }

        public BleScanResult getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.ble_scan_record, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                viewHolder.scanString = (TextView) view.findViewById(R.id.device_scan_string);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            Log.i(TAG, " getView() i:" + i);
            BleScanResult bleScanResult = mLeDevices.get(i);
            final String deviceName = bleScanResult.getDevice().getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(bleScanResult.getDevice().getAddress());
            viewHolder.scanString.setText(bleScanResult.toString());

            return view;
        }
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView scanString;
    }


}
