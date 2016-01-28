package com.fisincorporated.democode.threads;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.fisincorporated.utility.ThreadDemoObject;

public class HandlerThreadFragment extends ThreadDemoFragment implements
		HandlerThreadDemo.HandlerThreadDemoListener {
	private static final String TAG = "HandlerThreadFragment";
	private static final String FRAGMENT_TITLE = "HandlerThread";
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
	protected String getFragmentTitle() {
		return FRAGMENT_TITLE;
	}

	@Override
	protected void doStart() {
		mTvStatusArea.append("Adding request " + ++objectCount + " to queue"
				+ lineSeparator);
		handlerThreadDemo.queueRequest(HandlerThreadDemo.RUN_TIMER,
				ThreadDemoObject.newInstance(objectCount + "", 10));
		scrollToBottom();

	}

	@Override
	protected void doCancel() {
		mTvStatusArea.append("Canceling HandlerThread");
		handlerThreadDemo.queueRequest(HandlerThreadDemo.CANCEL, null);

	}

	@Override
	public void onProgressUpdate(int pctComplete, String msg) {
		mProgressBar.setProgress(pctComplete);
		mTvProgressMsg.setText(msg);
		scrollToBottom();

	}

	@Override
	public void cancelComplete() {
		mTvStatusArea.append("Cancel complete" + lineSeparator);
		scrollToBottom();

	}

	@Override
	public void onErrorOccurred(String errorMessage) {
		mTvStatusArea.append(errorMessage + lineSeparator);
		scrollToBottom();

	}

	@Override
	public void sendStatus(String msg) {
		mTvStatusArea.append(msg + lineSeparator);
		scrollToBottom();

	}

}
