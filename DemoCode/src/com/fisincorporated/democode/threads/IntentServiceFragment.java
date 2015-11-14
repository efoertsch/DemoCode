package com.fisincorporated.democode.threads;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.fisincorporated.democode.broadcastreceiver.BroadcastReceiverDemo;

public class IntentServiceFragment extends ThreadDemoFragment {
	private static final String TAG = "IntentServiceFragment";
	private int objectCount = 0;

	// Create instance of BroadcastReceieverDemo to receive broadcasts
	// Also remember to register/deregister receiver in onResume/onPause
	// This version of the receiver is called based on it being dynamically
	// registered via logic below
	private BroadcastReceiverDemo broadcastReceiverDemo = new BroadcastReceiverDemo() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String serviceRequest = intent.getStringExtra(SERVICE_REQUEST);
			String serviceMessage = intent.getStringExtra(SERVICE_MESSAGE);
			int percentComplete = intent.getIntExtra(PERCENT_COMPLETE, 0);
			if (isVisible()) {
				updateStatus(serviceRequest, serviceMessage, percentComplete);
			} else {
				// actually never called as onPause called when fragment not visible
				// so receiver not executed
				// send notification
				// using getActivity().getClass() as don't count on this fragment
				// being in a specific activity
				PendingIntent pi = PendingIntent.getActivity(getActivity(), 0,
						new Intent(getActivity(), getActivity().getClass()), 0);
				// NotificationCompat is from v4 support library
				Notification notification = new NotificationCompat.Builder(
						getActivity())
						.setTicker("Processing " + serviceRequest)
						.setSmallIcon(android.R.drawable.ic_menu_report_image)
						.setContentTitle("BroadcastReceiverDemo")
						.setContentText(
								"Processing " + serviceRequest + " Percent complete "
										+ percentComplete).setContentIntent(pi)
						.setAutoCancel(true).build();
				NotificationManager notificationManager = (NotificationManager) getActivity()
						.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(0, notification);

			}
		}
	};

	@Override
	protected void doStart() {
		startIntentServiceDemo();

	}

	@Override
	protected void doCancel() {
		cancelService();

	}

	// Register the broadcastreceiver
	@Override
	public void onResume() {
		super.onResume();
		registerBroadcastReceiver();
	}

	// Since broadcast is staying within app, using LocalBroadcastManager
	private void registerBroadcastReceiver() {
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				broadcastReceiverDemo,
				new IntentFilter(BroadcastReceiverDemo.BROADCAST_UPDATE));
	}

	// Unregister when paused
	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				broadcastReceiverDemo);
	}

	private void startIntentServiceDemo() {
		// create intent that will start service
		Intent intent = new Intent(getActivity(),
				com.fisincorporated.democode.threads.IntentServiceDemo.class);
		intent.putExtra(IntentServiceDemo.TIME_PERIOD, 10);
		intent.putExtra(IntentServiceDemo.REQUEST_NAME, ++objectCount + "");
		getActivity().startService(intent);
		tvStatusArea.append("Called IntentServiceDemo for request " + objectCount
				+ lineSeparator);
		scrollToBottom();

	}

	protected void updateStatus(String serviceRequest, String serviceMessage,
			int percentComplete) {
		tvProgressMsg.setText("Processing request" + serviceRequest);
		progressBar.setProgress(percentComplete);
		if (!serviceMessage.equals("")) {
			tvStatusArea.append(serviceMessage + lineSeparator);
			scrollToBottom();
		}

	}

	private void cancelService() {
		tvStatusArea.append("Stopping IntentServiceDemo" + lineSeparator);
		scrollToBottom();
		getActivity().stopService(
				new Intent(getActivity(), IntentServiceDemo.class));

	}

}
