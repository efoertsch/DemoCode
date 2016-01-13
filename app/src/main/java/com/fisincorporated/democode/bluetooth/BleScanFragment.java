package com.fisincorporated.democode.bluetooth;

import android.annotation.TargetApi;
import android.os.Bundle;
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

import com.fisincorporated.democode.R;
import com.fisincorporated.democode.bluetoothevents.BleScanEnded;
import com.fisincorporated.democode.bluetoothevents.BleScanResultEvent;
import com.fisincorporated.democode.demoui.DemoDrillDownFragment;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Start Ble scanning and display advertising packets
 * Adapted from Android demo code and
 * http://www.truiton.com/2015/04/android-bluetooth-low-energy-ble-example/
 * Handles scanning for both pre and post 5.0 Ble devices.
 */
@TargetApi(18)
public class BleScanFragment extends BleDemoFragment {
    protected static final String TAG = "BleScanFragment";
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private ListView mLvScanList;
    private boolean mScanning;

    public BleScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment
     */
    public static BleScanFragment newInstance() {
        BleScanFragment fragment = new BleScanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Log.d(TAG, "Package :" + this.getClass().getPackage().getName());

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
                Bundle bundle = new Bundle();
                bundle.putString(DemoDrillDownFragment.NEXT_FRAGMENT, BleControlFragment.class.getName());
                bundle.putString(BleControlFragment.EXTRAS_DEVICE_ADDRESS, bleScanResult.getDevice().getAddress());
                bundle.putString(BleControlFragment.EXTRAS_DEVICE_NAME, bleScanResult.getDevice().getName());
                if (mDemoCallbacks != null) {
                    mDemoCallbacks.createAndDisplayFragment(bundle);
                } else {
                    Toast.makeText(getActivity(), R.string.callback_not_available, Toast.LENGTH_SHORT).show();
                }
                scanLeDevice(false);
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
        scanLeDevice(true);


    }

    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }


    /**
     * Turn on or off scanning
     *
     * @param enable
     */
    private void scanLeDevice(final boolean enable) {
        mScanning = enable;
        if (enable) {
            queueBluetoothRequest(BluetoothDemoService.BLE_START_SCAN, null);
        } else {
            queueBluetoothRequest(BluetoothDemoService.BLE_STOP_SCAN, null);
        }
        getActivity().invalidateOptionsMenu();
    }

    /**
     * Fired when service is scanning for Ble devices and finds on
     *
     * @param event BleScanResultEvent
     */
    @Subscribe
    public void onBleScanResultEvent(BleScanResultEvent event) {
        updateScanDisplay(event.getBleScanResult());
    }

    /**
     * Recieved if server stops Ble discovery
     */
    @Subscribe
    public void onBleScanEnded(BleScanEnded event){
        mScanning = false;
        getActivity().invalidateOptionsMenu();
    }


    public void updateScanDisplay(final BleScanResult bleScanResult) {
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
            for (BleScanResult listResult : mLeDevices) {
                if (listResult.getDevice().getAddress().equals(bleScanResult.getDevice().getAddress())) {
                    addScanResult = false;
                }
            }
            if (addScanResult) {
                mLeDevices.add(bleScanResult);
                Log.i(TAG, " Number of devices:" + mLeDevices.size());
            }
            Collections.sort(mLeDevices);
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
