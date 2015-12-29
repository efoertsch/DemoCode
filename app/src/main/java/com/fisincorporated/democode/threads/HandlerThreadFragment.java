package com.fisincorporated.democode.threads;

import com.fisincorporated.democode.MasterFragment;
import com.fisincorporated.democode.R;
import com.fisincorporated.democode.R.id;
import com.fisincorporated.democode.R.layout;
import com.fisincorporated.utility.ThreadDemoObject;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HandlerThreadFragment extends ThreadDemoFragment implements
		HandlerThreadDemo.HandlerThreadDemoListener {
	private static final String TAG = "HandlerThreadFragment";
	private static final String lineSeparator = System
			.getProperty("line.separator");
	private HandlerThreadDemo handlerThreadDemo = null;
	private int objectCount = 0;

	public HandlerThreadFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handlerThreadDemo = new HandlerThreadDemo(new Handler());
		handlerThreadDemo.setListener(this);
		// getLooper goes after start to ensure thread is ready
		handlerThreadDemo.start();
		handlerThreadDemo.getLooper();
		Log.i(TAG, "HandlerThreadDemo thread started");
	}

	@Override
	protected void doStart() {
		tvStatusArea.append("Adding request " + ++objectCount + " to queue"
				+ lineSeparator);
		handlerThreadDemo.queueRequest(HandlerThreadDemo.RUN_TIMER,
				ThreadDemoObject.newInstance(objectCount + "", 10));
		scrollToBottom();

	}

	@Override
	protected void doCancel() {
		tvStatusArea.append("Canceling HandlerThread");
		handlerThreadDemo.queueRequest(HandlerThreadDemo.CANCEL, null);

	}

	@Override
	public void onProgressUpdate(int pctComplete, String msg) {
		progressBar.setProgress(pctComplete);
		tvProgressMsg.setText(msg);
		scrollToBottom();

	}

	@Override
	public void cancelComplete() {
		tvStatusArea.append("Cancel complete" + lineSeparator);
		scrollToBottom();

	}

	@Override
	public void onErrorOccurred(String errorMessage) {
		tvStatusArea.append(errorMessage + lineSeparator);
		scrollToBottom();

	}

	@Override
	public void sendStatus(String msg) {
		tvStatusArea.append(msg + lineSeparator);
		scrollToBottom();

	}

}
