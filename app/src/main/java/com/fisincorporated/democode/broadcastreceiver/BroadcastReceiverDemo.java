package com.fisincorporated.democode.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

// This receiver is registered in the manifest and is
// called based on  sendBroadcast(intent) being executed in the IntentServiceDemo class
public class BroadcastReceiverDemo extends BroadcastReceiver {
	private static final String TAG = "BroadcastReceiverDemo";
	//BROADCAST_UPDATE is the intent filter to put in the manifest for this receiver to receive the broadcast 
	public static final String BROADCAST_UPDATE =  "com.fisincorporated.democode.broadcastreceiver.BROADCAST_UPDATE";
	public static final String SERVICE_REQUEST = "com.fisincorporated.democode.broadcastreceiver.ServiceRequest";
	public static final String SERVICE_MESSAGE = "com.fisincorporated.democode.broadcastreceiver.ServiceMessage";
	public static final String PERCENT_COMPLETE = "com.fisincorporated.democode.broadcastreceiver.PercentComplete";
	private static final String lineSeparator = System.getProperty("line.separator");

	@Override
	public void onReceive(Context context, Intent intent) {
		String serviceRequest = intent.getStringExtra(SERVICE_REQUEST);
		String serviceMessage = intent.getStringExtra(SERVICE_MESSAGE);
		int percentComplete = intent.getIntExtra(PERCENT_COMPLETE, 0);
		Log.i(TAG, "Request:" + serviceRequest );
		Log.i(TAG, "Message:" + serviceMessage );
		Log.i(TAG, "% Complete:" + percentComplete );

	}

}
