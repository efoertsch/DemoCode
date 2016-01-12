package com.fisincorporated.democode.bluetooth;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import com.fisincorporated.application.DemoApplication;
import com.fisincorporated.democode.bluetoothevents.A2DPStatusUpdate;
import com.fisincorporated.democode.bluetoothevents.BleAdvertisingStatus;
import com.fisincorporated.democode.bluetoothevents.BleCharacteristicChanged;
import com.fisincorporated.democode.bluetoothevents.BleCharacteristicRead;
import com.fisincorporated.democode.bluetoothevents.BleCharacteristicReadRequest;
import com.fisincorporated.democode.bluetoothevents.BleCharacteristicWriteRequest;
import com.fisincorporated.democode.bluetoothevents.BleClientDeviceStatusChange;
import com.fisincorporated.democode.bluetoothevents.BleConnected;
import com.fisincorporated.democode.bluetoothevents.BleDisconnected;
import com.fisincorporated.democode.bluetoothevents.BleErrorMessage;
import com.fisincorporated.democode.bluetoothevents.BleInvalidServerAddress;
import com.fisincorporated.democode.bluetoothevents.BleNotificationSent;
import com.fisincorporated.democode.bluetoothevents.BleResponseError;
import com.fisincorporated.democode.bluetoothevents.BleScanCallBackFailed;
import com.fisincorporated.democode.bluetoothevents.BleScanEnded;
import com.fisincorporated.democode.bluetoothevents.BleScanResultEvent;
import com.fisincorporated.democode.bluetoothevents.BleServicesDiscovered;
import com.fisincorporated.democode.bluetoothevents.BleStateConnecting;
import com.fisincorporated.democode.bluetoothevents.BleSupportedGattServicies;
import com.fisincorporated.democode.bluetoothevents.BluetoothNotSupported;
import com.fisincorporated.democode.bluetoothevents.BondStatusUpdate;
import com.fisincorporated.democode.bluetoothevents.HFPStatusUpdate;
import com.fisincorporated.utility.OttoBus;
import com.fisincorporated.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import static android.bluetooth.BluetoothDevice.EXTRA_DEVICE;

/**
 * Service to handle all things Bluetooth
 * 1. SPP  (to be implemented)
 * 2. Ble scanning
 * 3. Ble Client
 * 4. Ble Periphal
 * 5. Methods to connect A2DP/HFP (to be implemented)
 * <p/>
 * Cribbed code from Android Ble Demo project and other sources and then did lots of modifications
 * Events and information communicated back to UI via Otto bus events
 * <p/>
 * 'what' value in Handler message      Requires this value in obj
 * CLASSIC_BT_START_SCAN                N/A
 * CLASSIC_BT_STOP_SCAN                 N/A
 * SPP_CONNECT                          String device MAC address
 * SPP_DISCONNECT                       N/A
 * SPP_WRITE_DATA                       byte[]
 * <p/>
 * Ble client related
 * BLE_START_SCAN                       N/A
 * BLE_STOP_SCAN                        N/A
 * BLE_CONNECT_TO_PERIPHERAL            String device MAC address
 * BLE_READ_CHARACTERISTIC               BluetoothCharacteristic
 * BLE_WRITE_CHARACTERISTIC             BluetoothCharacteristic
 * BLE_DISCONNECT_PERIPHERAL            N/A
 * <p/>
 * Ble Peripheral Related
 * BLE_GET_SERVICES                     N/A
 * BLE_SET_SCAN_FILTER                  ScanFilter()[]
 * BLE_PERIPHERAL_SERVICE               ArrayList<BluetoothGattService>
 * BLE_SEND_RESPONSE                    BleResponse
 * BLE_SEND_NOTIFICATION                BleCharacteristicChangedNotification
 */
@TargetApi(21)
public class BluetoothDemoService extends Service {
    private static final String TAG = BluetoothDemoService.class.getSimpleName();
    private static final int SCAN_PERIOD = 10 * 1000;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;


    // List of all requests handed by this service
    public static final int CLASSIC_BT_START_SCAN = 0;
    public static final int CLASSIC_BT_STOP_SCAN = 1;

    public static final int SPP_CONNECT = 20;
    public static final int SPP_DISCONNECT = 21;
    public static final int SPP_WRITE_DATA = 22;

    public static final int BLE_START_SCAN = 100;
    public static final int BLE_SET_SCAN_FILTER = 101;
    public static final int BLE_STOP_SCAN = 102;
    public static final int BLE_CONNECT_TO_PERIPHERAL = 103;
    public static final int BLE_READ_CHARACTERISTIC = 104;
    public static final int BLE_SET_CHARACTERISTIC_NOTIFICATION = 105;
    public static final int BLE_WRITE_CHARACTERISTIC = 106;
    public static final int BLE_WRITE_DESCRIPTOR = 107;
    public static final int BLE_DISCONNECT_PERIPHERAL = 108;

    public static final int BLE_GET_SERVICES = 109;
    /**
     * Start peripheral service and advertising
     */
    public static final int BLE_START_PERIPHERAL_SERVICE = 110;
    /**
     * Stop peripheral service and advertising
     */
    public static final int BLE_STOP_PERIPHERAL_SERVICE = 111;
    public static final int BLE_SEND_RESPONSE = 112;
    public static final int BLE_SEND_NOTIFICATION = 113;


    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothReceiver mBluetoothReceiver;
    private BluetoothGattCallback mGattCallback = null;
    private BluetoothLeScanner mBluetoothLeScanner = null;
    private BluetoothGattServerCallback mGattServerCallback = null;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private Handler mHandler;
    private BtRequestHandler mBtRequestHandler;
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    private ArrayList<BleScanResult> mBleScanResultList = new ArrayList<>();

    private ScanSettings mSettings;
    private List<ScanFilter> mFilters;
    private ScanCallback mScanCallback = null;
    private boolean mScanning = false;

    private OttoBus mOttoBus;


    // Binder given to clients
    private final IBinder mBleBinder = new BleServiceBinder();

    public BluetoothDemoService() {
    }


    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class BleServiceBinder extends Binder {
        public BluetoothDemoService getService() {
            return BluetoothDemoService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBleBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    /**
     * Queue a request for this service
     *
     * @param what    one of BluetoothDemoService requests
     * @param request
     */
    public void queueBluetoothRequest(int what, Object request) {
        mBtRequestHandler.obtainMessage(what, request).sendToTarget();
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        if (!initializeBluetooth()) {
            // must not have BT on this device so quit
            stopSelf();
            return;
        }
        // Put service on background thread
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        Looper serviceLooper = thread.getLooper();
        mBtRequestHandler = new BtRequestHandler(serviceLooper);
        mOttoBus = DemoApplication.getOttoBus();
        registerBluetoothReceiver();
    }

    /**
     * Service is shutting down so do whatever cleanup necessary
     */
    @Override
    public void onDestroy() {
        // unregister the bluetooth receiver
        mBtRequestHandler.stopDiscovery();
        unregisterBlueToothReceiver();

        if (mScanning) mBtRequestHandler.scanLeDevice(false);
        // Ble server related
        if (isAdvertising()) stopAdvertisingAndService();
        if (mGattServer != null) mGattServer.close();

        Toast.makeText(this, "BluetoothDemoService ended", Toast.LENGTH_SHORT).show();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Doing onStartCommand");
        DemoApplication.getOttoBus().register(this);
        // Don't restart if killed
        return Service.START_NOT_STICKY;
    }

    private boolean initializeBluetooth() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (mBluetoothAdapter == null) {
            mOttoBus.post(new BluetoothNotSupported());
            return false;
        }
        return true;
    }

    /**
     * Handle the queued BT requests coming from the UI
     */
    private final class BtRequestHandler extends Handler {

        public BtRequestHandler(Looper looper) {
            super(looper);
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
                mSettings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                mFilters = new ArrayList<ScanFilter>();
                assignScanCallback();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CLASSIC_BT_START_SCAN:
                    startClassicBTScan();
                    break;
                case CLASSIC_BT_STOP_SCAN:
                    stopClassicBTScan();
                    break;
                case SPP_CONNECT:
                    break;
                case SPP_DISCONNECT:
                    break;
                case SPP_WRITE_DATA:
                    break;
                case BLE_START_SCAN:
                    scanLeDevice(true);
                    break;
                case BLE_STOP_SCAN:
                    scanLeDevice(false);
                    break;

                case BLE_CONNECT_TO_PERIPHERAL:
                    // obj must be string containing mac address of
                    // periphal device.
                    connect(msg.obj.toString());
                    break;
                case BLE_DISCONNECT_PERIPHERAL:
                    disconnect();
                    break;
                case BLE_READ_CHARACTERISTIC:
                    readCharacteristic((BluetoothGattCharacteristic) msg.obj);
                    break;
                case BLE_SET_CHARACTERISTIC_NOTIFICATION:
                    setCharacteristicNotification((BleCharacteristicNotification) msg.obj);
                    break;
                case BLE_GET_SERVICES:
                    getSupportedGattServices();
                    break;
                case BLE_WRITE_CHARACTERISTIC:
                    break;
                case BLE_WRITE_DESCRIPTOR:
                    writeDescriptor((BluetoothGattDescriptor) msg.obj);
                    break;
                case BLE_SET_SCAN_FILTER:
                    mFilters = (ArrayList<ScanFilter>) msg.obj;
                    break;
                case BLE_START_PERIPHERAL_SERVICE:
                    // make sure scanning off
                    scanLeDevice(false);
                    saveServices((ArrayList<BluetoothGattService>) msg.obj);
                    startServiceAndAdvertising();
                    // pass in list of advertising services
                    // wait for start
                    break;
                case BLE_STOP_PERIPHERAL_SERVICE:
                    stopAdvertisingAndService();
                    break;
                case BLE_SEND_RESPONSE:
                    sendResponse((BleResponse) msg.obj);
                    break;
                case BLE_SEND_NOTIFICATION:
                    sendBleCharacteristicChanged((BleCharacteristicChangedNotification) msg.obj);
                    break;
                default:
                    break;
            }
        }

        /**
         * Stop bluetooth (either LE or regular Bluetooth) discovery process.
         */
        @SuppressLint("NewApi")
        public void stopDiscovery() {
            if (mBluetoothAdapter == null) {
                return;
            }
            if (getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_BLUETOOTH_LE)) {
                scanLeDevice(false);
            }
            if (mBluetoothAdapter.isDiscovering()) {
                stopClassicBTScan();
            }
        }

        /**
         * Turn on or off scanning
         *
         * @param enable
         */
        private void scanLeDevice(final boolean enable) {
            if (enable) {
                // Stops scanning after a pre-defined scan period.
                this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        if (Build.VERSION.SDK_INT < 21) {
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        } else {
                            mBluetoothLeScanner.stopScan(mScanCallback);
                        }
                        mOttoBus.post(new BleScanEnded());

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
                        BleScanResult bleScanResult = new BleScanResult(device, scanRecordBytes, rssi);
                        Log.i(TAG, "BluetoothAdapter.LeScanCallback()" + bleScanResult.getAddress());
                        postScanResult(bleScanResult);
                    }
                };


        /**
         * This callback is used for versions 21+
         */
        @TargetApi(21)
        private void assignScanCallback() {
            if (mScanCallback == null)
                mScanCallback = new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        postScanResult(new BleScanResult(result));
                    }

                    @Override
                    public void onBatchScanResults(List<ScanResult> results) {
                        for (ScanResult sr : results) {
                            postScanResult(new BleScanResult(sr));
                        }
                    }

                    @Override
                    public void onScanFailed(int errorCode) {
                        mOttoBus.post(new BleScanCallBackFailed(errorCode));
                    }
                };
        }

        public void postScanResult(final BleScanResult bleScanResult) {
            mOttoBus.post(new BleScanResultEvent(bleScanResult));
        }

        private void startClassicBTScan() {
            mBluetoothAdapter.startDiscovery();
        }

        private void stopClassicBTScan() {
            mBluetoothAdapter.cancelDiscovery();
        }


    }


    // ------------ BLE Client related stuff -----------------------------------

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private void assignBluetoothGattCallback() {
        if (mGattCallback == null) {
            mGattCallback = new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    String intentAction;
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        mConnectionState = STATE_CONNECTED;
                        mOttoBus.post(new BleConnected());
                        Log.i(TAG, "Connected to GATT server.");
                        // Attempts to discover services after successful connection.
                        Log.i(TAG, "Attempting to start service discovery:" +
                                mBluetoothGatt.discoverServices());

                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        mConnectionState = STATE_DISCONNECTED;
                        Log.i(TAG, "Disconnected from GATT server.");
                        mOttoBus.post(new BleDisconnected());
                    }
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        mOttoBus.post(new BleServicesDiscovered());
                    } else {
                        Log.w(TAG, "onServicesDiscovered received: " + status);
                    }
                }

                @Override
                public void onCharacteristicRead(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic,
                                                 int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        mOttoBus.post(new BleCharacteristicRead(characteristic));
                    }
                }

                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt,
                                                    BluetoothGattCharacteristic characteristic) {
                    mOttoBus.post(new BleCharacteristicChanged(characteristic));
                }
            };
        }
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            String errMsg = "BluetoothAdapter not initialized or unspecified address.";
            mOttoBus.post(new BleErrorMessage(errMsg));
            Log.w(TAG, errMsg);
            return;
        }
        assignBluetoothGattCallback();
        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            String errMsg = "Trying to use an existing mBluetoothGatt for connection.";
            mOttoBus.post(new BleErrorMessage(errMsg));
            Log.d(TAG, errMsg);
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                mOttoBus.post(new BleStateConnecting());
                return;
            } else {
                return;
            }
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            String errMsg = "Device not found.  Unable to connect.";
            mOttoBus.post(new BleInvalidServerAddress(address, errMsg));
            Log.w(TAG, errMsg);
            return;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        mOttoBus.post(new BleStateConnecting());
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a given characteristic.
     *
     * @param bleCharacteristicNotification How to act on Characteristic
     */
    public void setCharacteristicNotification(BleCharacteristicNotification bleCharacteristicNotification) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(bleCharacteristicNotification.getCharacteristic()
                , bleCharacteristicNotification.isEnabled());


    }

    /**
     * @param descriptor
     */
    private void writeDescriptor(BluetoothGattDescriptor descriptor) {
        if (descriptor != null) {
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }


    /**
     * Posts a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     */
    public void getSupportedGattServices() {
        if (mBluetoothGatt == null) {
            mOttoBus.post(new BleSupportedGattServicies(null));
            return;
        }
        mOttoBus.post(new BleSupportedGattServicies(mBluetoothGatt.getServices()));
    }

    // ------------ BLE Server related stuff -----------------------------------
    // Snippets based on code from https://github.com/geoaxis/BluetoothTest and updated to
    // 5.0 ble code (github code based on pre 5.0 release code so would not work with official 5.0)
    // In any case greatly modified
    private boolean mAdvertising;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser = null;
    private AdvertiseCallback mAdvertiseCallback = null;
    private BluetoothGattServer mGattServer;
    private ArrayList<BluetoothGattService> mPeripheralServices = new ArrayList<>();
    private ArrayList<ParcelUuid> mServiceUuids = new ArrayList<>();


    private void saveServices(ArrayList<BluetoothGattService> services) {
        for (BluetoothGattService service : services) {
            addService(service);
        }
    }

    private void addService(BluetoothGattService service) {
        mPeripheralServices.add(service);
        mServiceUuids.add(new ParcelUuid(service.getUuid()));
    }


    /**
     * Start advertising for the given services.
     */
    private void startServiceAndAdvertising() {
        if (isAdvertising()) return;

        startGattServer();

        // Create Advertise data packet container for Bluetooth LE advertising
        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        dataBuilder.setIncludeDeviceName(true);
        dataBuilder.setIncludeTxPowerLevel(false); //necessity to fit in 31 byte advertisement

        for (ParcelUuid serviceUuid : mServiceUuids) {
            dataBuilder.addServiceUuid(serviceUuid);
        }

        //adjust advertising preferences for each LE advertising instance
        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);

        // And start advertising
        mBluetoothLeAdvertiser.startAdvertising(settingsBuilder.build(), dataBuilder.build(), mAdvertiseCallback);
        mAdvertising = true;
    }

    private void startGattServer() {
        assignAdvertiseCallback();
        assignBluetoothGattServerCallback();

        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        mGattServer = mBluetoothManager.openGattServer(this, mGattServerCallback);

        for (int i = 0; i < mPeripheralServices.size(); i++) {
            mGattServer.addService(mPeripheralServices.get(i));
        }
    }

    //Stop ble advertising and clean up
    private void stopAdvertisingAndService() {
        if (!isAdvertising()) return;
        mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
        mGattServer.clearServices();
        mGattServer.close();
        mPeripheralServices.clear();
        mServiceUuids.clear();
        mAdvertising = false;
    }


    private void sendResponse(BleResponse bleResponse) {
        boolean success = mGattServer.sendResponse(bleResponse.getDevice(), bleResponse.getRequestId(), bleResponse.getStatus(),
                bleResponse.getOffset(), bleResponse.getValue());
        if (!success) {
            mOttoBus.post(new BleResponseError(bleResponse));
        }
    }

    private void sendBleCharacteristicChanged(BleCharacteristicChangedNotification bleCcNotification) {
        mGattServer.notifyCharacteristicChanged(bleCcNotification.getDevice(), bleCcNotification.getCharacteristic(),
                bleCcNotification.isConfirm());
    }



    private void assignAdvertiseCallback() {
        if (mAdvertiseCallback == null) {
            mAdvertiseCallback = new AdvertiseCallback() {
                @Override
                public void onStartSuccess(AdvertiseSettings advertiseSettings) {
                    String successMsg = "Advertisement command attempt successful";
                    Log.d(TAG, successMsg);
                    mOttoBus.post(new BleAdvertisingStatus(0, successMsg, advertiseSettings));
                }

                @Override
                public void onStartFailure(int i) {
                    String failMsg = "Advertisement command attempt failed: " + i;
                    mOttoBus.post(new BleAdvertisingStatus(i, failMsg, null));
                    Log.e(TAG, failMsg);
                }
            };
        }
    }


    private void assignBluetoothGattServerCallback() {
        if (mGattServerCallback == null) {
            mGattServerCallback = new BluetoothGattServerCallback() {
                @Override
                public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
                    super.onConnectionStateChange(device, status, newState);
                    mOttoBus.post(new BleClientDeviceStatusChange(device, status, newState));
                    Log.d(TAG, "onConnectionStateChange status=" + status + "->" + newState);
                }

                @Override
                public void onServiceAdded(int status, BluetoothGattService service) {
                    super.onServiceAdded(status, service);
                }

                /**
                 * A remote client has requested to read a local characteristic
                 * An application must call sendResponse(BluetoothDevice, int, int, int, byte[]) to complete the request.
                 * @param device
                 * @param requestId
                 * @param offset
                 * @param characteristic
                 *
                 *  An application must call sendResponse(BluetoothDevice, int, int, int, byte[]) to complete the request.
                 */
                @Override
                public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
                    // Is call to super needed?
                    //super.onCharacteristicReadRequest(device, requestId, offset, characteristic);

                    mOttoBus.post(new BleCharacteristicReadRequest(device, requestId, offset, characteristic));
                    Log.d(TAG, "onCharacteristicReadRequest requestId=" + requestId + " offset=" + offset);
                }

                /**
                 * A remote client has requested to write to a local characteristic.
                 * @param device
                 * @param requestId
                 * @param characteristic
                 * @param preparedWrite
                 * @param responseNeeded
                 * @param offset
                 * @param value
                 *
                 * An application must call sendResponse(BluetoothDevice, int, int, int, byte[]) to complete the request.
                 */
                @Override
                public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                    // Is call to super needed?
                    //super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
                    mOttoBus.post(new BleCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value));
                    Log.d(TAG, "onCharacteristicWriteRequest requestId=" + requestId + " preparedWrite="
                            + Boolean.toString(preparedWrite) + " responseNeeded="
                            + Boolean.toString(responseNeeded) + " offset=" + offset);
                }

                @Override
                public void onNotificationSent(BluetoothDevice device, int status){
                    mOttoBus.post(new BleNotificationSent(device, status));
                    Log.d(TAG," Notification sent to device:" + device.getName() + " status:" + status );
                }

                // !!! There are other BluetoothGattServerCallback methods not yet implemented.
                // see http://developer.android.com/reference/android/bluetooth/BluetoothGattServerCallback.html

            };
        }
    }


    private boolean isAdvertising() {
        return mAdvertising;
    }


    // ------------ Classic BT related stuff -----------------------------------

    /**
     * Register for all the bluetooth related events that are of interest to
     * this device
     */
    private void registerBluetoothReceiver() {
        mBluetoothReceiver = new BluetoothReceiver();

        // ACTION_FOUND is for discovering classic BT devices
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // ACTION_DISCOVERY_STARTED for classic discovery
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        // ACTION_DISCOVERY_FINISHED end of classic discovery
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // A2DP
        filter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        // Headset (aka HFP) -
        filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        // for bonding
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        // ACTION_UUID is for getting list of UUID's from connected
        // device
        // filter.addAction(BluetoothDevice.ACTION_UUID);
        registerReceiver(mBluetoothReceiver, filter);
    }

    /**
     * Unregister this receiver so don't get any more Bluetooth system
     * broadcasts
     */
    private void unregisterBlueToothReceiver() {
        if (mBluetoothReceiver != null) {
            unregisterReceiver(mBluetoothReceiver);
        } else {
            Log.d(TAG,
                    "In unregisterBlueToothReceiver but mBluetoothReceiver is null");
        }
    }

    /**
     * Handles Bluetooth broadcasts from the Android system
     */
    class BluetoothReceiver extends BroadcastReceiver {
        String message;

        @SuppressLint("NewApi")
        public void onReceive(Context context, Intent intent) {
            int currentState;
            int previousState;
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(EXTRA_DEVICE);

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                currentState = intent.getIntExtra(
                        BluetoothDevice.EXTRA_BOND_STATE,
                        BluetoothDevice.BOND_NONE);
                previousState = intent.getIntExtra(
                        BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE,
                        BluetoothDevice.BOND_NONE);
                message = "Bond state changed. Previous:"
                        + Utility.getBondState(previousState) + " Current:"
                        + Utility.getBondState(currentState);
                mOttoBus.post(new BondStatusUpdate(device, currentState));
            }
            // Following are A2DP events
            else if (action
                    .equals(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)) {
                currentState = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE,
                        BluetoothA2dp.STATE_DISCONNECTED);
                previousState = intent.getIntExtra(
                        BluetoothA2dp.EXTRA_PREVIOUS_STATE,
                        BluetoothA2dp.STATE_DISCONNECTED);
                message = "A2DP Connect state changed. Previous:"
                        + Utility.getConnectionState(previousState)
                        + " Current:"
                        + Utility.getConnectionState(currentState);
                mOttoBus.post(new A2DPStatusUpdate(device, currentState));

            }
            // Following are headset (aka HFP) events
            else if (action
                    .equals(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)) {
                currentState = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE,
                        BluetoothHeadset.STATE_DISCONNECTED);
                previousState = intent.getIntExtra(
                        BluetoothHeadset.EXTRA_PREVIOUS_STATE,
                        BluetoothHeadset.STATE_DISCONNECTED);
                message = "HFP Connection state changed.  Previous:"
                        + Utility.getConnectionState(previousState)
                        + " Current:"
                        + Utility.getConnectionState(currentState);
                mOttoBus.post(new HFPStatusUpdate(device, currentState));

            }
        }
    }

}