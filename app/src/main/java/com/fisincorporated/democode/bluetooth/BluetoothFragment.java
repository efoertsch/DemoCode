package com.fisincorporated.democode.bluetooth;

import com.fisincorporated.democode.MasterFragment;
import com.fisincorporated.democode.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
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

public class BluetoothFragment extends MasterFragment {
  
    protected static final String TAG = "BLUETOOTH";
    protected static final int DISCOVERY_REQUEST = 1;
    BluetoothAdapter bluetooth;
    private Button buttonEnable;
    private TextView tvBluetoothStatus;
    private ScrollView svScrollStatus;
    protected static final String lineSeparator = System
 			.getProperty("line.separator");

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();   
      this.bluetooth = bluetooth;
    }
    @Override
 	public View onCreateView(LayoutInflater inflater, ViewGroup container,
 			Bundle savedInstanceState) {
 		Log.i(TAG, "onCreateView");
 		View view = inflater.inflate(R.layout.bluetooth, container, false);
 		getReferencedViews(view);
 		//getSavedInstanceState(savedInstanceState);
 		return view;
    }
    
    private void getReferencedViews(View view) {
   	 buttonEnable = (Button) view.findViewById(R.id.buttonEnable);
   	 buttonEnable.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				initBluetooth() ;
				
			}});
   	 svScrollStatus = (ScrollView) view.findViewById(R.id.svScrollStatus);
   	 tvBluetoothStatus = (TextView)view.findViewById(R.id.tvBluetoothStatus);
	}
    
   private void updateStatus(String message){
   	tvBluetoothStatus.append(message + lineSeparator);
   	svScrollStatus.fullScroll(View.FOCUS_DOWN);
   }

   public void onPause(){
   	super.onPause();
   	getActivity().unregisterReceiver(discoveryResult);
   }

	/**
     * Listing 16-2: Enabling Bluetooth
     */
    private static final int ENABLE_BLUETOOTH = 1;

    private void initBluetooth() {
      if (!bluetooth.isEnabled()) { 
        // Bluetooth isn't enabled, prompt the user to turn it on.
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, ENABLE_BLUETOOTH);
      } else {
        // Bluetooth is enabled, initialize the UI.
        initBluetoothUI();
      }
    }

    public void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
      if (requestCode == ENABLE_BLUETOOTH)
        if (resultCode == Activity.RESULT_OK) {
          // Bluetooth has been enabled, initialize the UI.
          initBluetoothUI();
        }
      
      /**
       * Listing 16-4: Monitoring discoverability request approval
       */
      if (requestCode == DISCOVERY_REQUEST) {
        if (resultCode == Activity.RESULT_CANCELED) {
          Log.d(TAG, "Discovery cancelled by user");
        }
      }

    }
    
    private void makeDiscoverable() {
      /**
       * Listing 16-3: Enabling discoverability
       */
      startActivityForResult(
        new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE),
                   DISCOVERY_REQUEST);
    }
    
    /**
     * Listing 16-5: Discovering remote Bluetooth Devices
     */
    private ArrayList<BluetoothDevice> deviceList = 
      new ArrayList<BluetoothDevice>();

    private void startDiscovery() {
      getActivity().registerReceiver(discoveryResult,
                       new IntentFilter(BluetoothDevice.ACTION_FOUND));

      if (bluetooth.isEnabled() && !bluetooth.isDiscovering())
        deviceList.clear();
        bluetooth.startDiscovery();
    }

    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        String remoteDeviceName = 
          intent.getStringExtra(BluetoothDevice.EXTRA_NAME);

        BluetoothDevice remoteDevice =  
          intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        deviceList.add(remoteDevice);
        updateStatus("Added " + remoteDevice.getName());

        Log.d(TAG, "Discovered " + remoteDeviceName);
      }
    };
    
    /**
     * Listing 16-6: Listening for Bluetooth Socket connection requests
     */
    private BluetoothSocket transferSocket;

    private UUID startServerSocket(BluetoothAdapter bluetooth) {
      UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");
      String name = "bluetoothserver";

      try {
        final BluetoothServerSocket btserver = 
          bluetooth.listenUsingRfcommWithServiceRecord(name, uuid);

        Thread acceptThread = new Thread(new Runnable() {
          public void run() {
            try {
              // Block until client connection established.
              BluetoothSocket serverSocket = btserver.accept();
              // Start listening for messages.
              StringBuilder incoming = new StringBuilder();
              listenForMessages(serverSocket, incoming);
              // Add a reference to the socket used to send messages.
              transferSocket = serverSocket;
            } catch (IOException e) {
              Log.e("BLUETOOTH", "Server connection IO Exception", e);
            }
          }
        });
        acceptThread.start();
      } catch (IOException e) {
        Log.e("BLUETOOTH", "Socket listener IO Exception", e);
      }
      return uuid;
    }

    /**
     * Listing 16-7: Creating a Bluetooth client socket
     */
    private void connectToServerSocket(BluetoothDevice device, UUID uuid) {
      try{
        BluetoothSocket clientSocket 
          = device.createRfcommSocketToServiceRecord(uuid);

        // Block until server connection accepted.
        clientSocket.connect();

        // Start listening for messages.
        StringBuilder incoming = new StringBuilder();
        listenForMessages(clientSocket, incoming);

        // Add a reference to the socket used to send messages.
        transferSocket = clientSocket;

      } catch (IOException e) {
        Log.e("BLUETOOTH", "Blueooth client I/O Exception", e);
      }
    }

    /**
     * Listing 16-8: Sending and receiving strings using Bluetooth Sockets
     */
    private void sendMessage(BluetoothSocket socket, String message) {
      OutputStream outStream;
      try {
        outStream = socket.getOutputStream();

        // Add a stop character.
        byte[] byteArray = (message + " ").getBytes();
        byteArray[byteArray.length - 1] = 0;

        outStream.write(byteArray);
      } catch (IOException e) { 
        Log.e(TAG, "Message send failed.", e);
      }
    }

    private boolean listening = false;
     
    private void listenForMessages(BluetoothSocket socket, 
                                   StringBuilder incoming) {
      listening = true;


      int bufferSize = 1024;
      byte[] buffer = new byte[bufferSize];

      try {
        InputStream instream = socket.getInputStream();
        int bytesRead = -1;

        while (listening) {
          bytesRead = instream.read(buffer);
          if (bytesRead != -1) {
            String result = "";
            while ((bytesRead == bufferSize) &&
                   (buffer[bufferSize-1] != 0)){
              result = result + new String(buffer, 0, bytesRead - 1);
              bytesRead = instream.read(buffer);
            }
            result = result + new String(buffer, 0, bytesRead - 1);
            incoming.append(result);
          }
          socket.close();
        }
      } catch (IOException e) {
        Log.e(TAG, "Message received failed.", e);
      }
      finally {
      }
    }
    
    private void initBluetoothUI() {
   	 updateStatus("Bluetooth enabled");
   	 startDiscovery();
       
    }
}