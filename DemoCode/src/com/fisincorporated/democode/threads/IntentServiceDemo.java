package com.fisincorporated.democode.threads;

import com.fisincorporated.democode.broadcastreceiver.BroadcastReceiverDemo;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

// registered in manifest via
//   <service android:name="com.fisincorporated.democode.broadcastreceiver.IntentServiceDemo"
//   android:exported="false" />
public class IntentServiceDemo extends IntentService {
	private static final String TAG = "IntentServiceDemo";
	public static final String TIME_PERIOD = "com.fisincorporated.democode.TimePeriod";
	public static final String REQUEST_NAME = "com.fisincorporated.democode.RequestName";
	private static boolean cancel = false;
	private int timePeriod = 0;
	private String requestName ="";

	/**
	 * A constructor is required, and must call the super IntentService(String)
	 * constructor with a name for the worker thread.
	 */
	public IntentServiceDemo() {
		super(TAG);
		Log.i(TAG,TAG + " constructor called");
	}

	public IntentServiceDemo(String name) {
		super(name);
		Log.i(TAG,TAG + " constructor called with name:" + name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Get any needed info from intent
		timePeriod = intent.getIntExtra(TIME_PERIOD, 0);
		requestName = intent.getStringExtra(REQUEST_NAME);
		broadcastStatus(requestName, 0, "Starting request:" + requestName);
		handleRequest(timePeriod, requestName);
		
	}

	protected void handleRequest(int timePeriod, String requestName) {
		int percentComplete = 0;
		String msg;
		for (int i = 0; i < timePeriod && !cancel; ++i) {
			Log.i(TAG, "For object id " + requestName + " - Sleeping " + i);
			try {
				Thread.sleep(1000);
				percentComplete = ((i + 1) * 100) / timePeriod;
				broadcastStatus(requestName, percentComplete, "");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		broadcastStatus(requestName, 100, "Process Complete");
	}

	// Create the broadcast intent. By using BroadcastReceiverDemo.BROADCAST_UPDATE
	// the system will call (via the manifest entry) any activities/fragments that
	// have a BroadcastReceiverDemo created
	private void broadcastStatus(String serviceRequest, int percentComplete,
			String statusMessage) {
		Intent intent = new Intent(BroadcastReceiverDemo.BROADCAST_UPDATE);
		intent.putExtra(BroadcastReceiverDemo.SERVICE_REQUEST, serviceRequest);
		intent.putExtra(BroadcastReceiverDemo.PERCENT_COMPLETE, percentComplete);
		intent.putExtra(BroadcastReceiverDemo.SERVICE_MESSAGE, statusMessage);
		// This sends broadcast dependent on receiver being registered in manifest and calls
		// BroadcastReceiverDemo (the one in its own class)
		 sendBroadcast(intent);
		// This sends broadcast dependent on receiver being dynamically registered
		// (so it called the BroadcastReceiverDemo class withing IntentServiceFragment
		LocalBroadcastManager.getInstance(this.getApplicationContext())
				.sendBroadcast(intent);
	}
	
	public boolean stopService(Intent name) {
		// need to determine if current request completes or stopService halts everything in its tracks
		cancel = true;
		broadcastStatus(requestName,0,"IntentServiceDemo received stopService");
		return super.stopService(name);
	}

}
