package com.fisincorporated.democode.threads;

 

import com.fisincorporated.utility.ThreadDemoObject;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

// http://developer.android.com/guide/components/processes-and-threads.html
// http://developer.android.com/reference/android/os/HandlerThread.html
// See more detailed example in LanguageLesson/LanguagePhraseLoader

public class HandlerThreadDemo extends HandlerThread {
	
	public static final int RUN_TIMER = 0;
	public static final int CANCEL = 1;
	private static final String TAG = "HandlerThreadDemo";
	private Handler mResponseHandler;
	private HandlerThreadDemoListener mListener = null;;
	private Handler mHandler;
	private boolean doCancel;

	
	// various callbacks - roll you own!
	public interface HandlerThreadDemoListener {
		void onProgressUpdate(int pctComplete, String message);
		void cancelComplete();
		void onErrorOccurred(String errorMessage);
		void sendStatus(String msg);
	}

	
		
	public void setListener(HandlerThreadDemoListener listener) {
		mListener = listener;
		
	}
	public HandlerThreadDemo(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	// Default constructor as created in Eclipse
	public HandlerThreadDemo(String name, int priority) {
		super(name, priority);
		// TODO Auto-generated constructor stub
	}
	
	// Custom constructor (if not worried about thread priority and naming)
	public HandlerThreadDemo(Handler responseHandler) {
		super(TAG);
		mResponseHandler = responseHandler;
		// add any other initialization stuff here
	}
	
	
	// This method is running on UI thread from call but just puts message on queue.
	// So fast operation is no problemo
	public void queueRequest(int what,	ThreadDemoObject threadDemoObject) {
		// Note this call to mListener is on the UI thread
		if (what != CANCEL){
			mListener.sendStatus("Queueing " + threadDemoObject.getId());
		// Make sure to append .sendToTarget()!
			mHandler.obtainMessage(what, threadDemoObject).sendToTarget();
		}
		else {
			mListener.sendStatus("Received cancel");
			mHandler.removeMessages(RUN_TIMER);
			mListener.sendStatus("Cleared threadhandler queue of RUN_TIMER (the only kind here) msgs");
			cancel();
	}
		

		
	}
	
	@SuppressLint("HandlerLeak")
	@Override
	// overridden so we can create Handler and implement handleMessage that
	// handles the messages on the queue
	// (that are sent here by the looper)
	protected void onLooperPrepared() {
		// the handler created here is associated with HandlerThreadDemo - i.e. HandlerThread which is not the UI thread
		mHandler = new Handler() {
			@Override
			// Called by the looper as it processes the messages on queue one by one
			public void handleMessage(Message msg) {
				if (msg.what == RUN_TIMER) {
					ThreadDemoObject threadDemoObject = (ThreadDemoObject) msg.obj ;
					int timePeriod= threadDemoObject.getTimePeriod();
					String id =  threadDemoObject.getId();
					sendStatusMessage("Processing run request for " + id);
					Log.i(TAG, "Got request to run timer for :"
							+ timePeriod);
					handleRequest(msg.what, timePeriod, id );
				}
				else	 {
					sendStatusMessage("Undefined msg.what type:" + ". Request ignored.");
				}
			}
			
			
		};
	}
	
	protected void sendStatusMessage(final String msg){
		mResponseHandler.post(new Runnable() {
			public void run() {
				// check to see if on same teacher/class/lesson
				// load language phrases to display arrays
				mListener.sendStatus(msg);
			}
		});
	}
	
	
	
	protected void handleRequest(int what,  int timePeriod, final String id) {
		doCancel = false;
		for (int  i = 0 ; i < timePeriod && !doCancel; ++i){
			try {
				Log.i(TAG, "For object id " + id +  " - Sleeping " + i );
				Thread.sleep(1000);
				final int percentComplete = ((i + 1) * 100)/timePeriod;
				final String msg = "Object id " + id + " - " + percentComplete + "% complete " ;
				mResponseHandler.post(new Runnable() {
					public void run() {
						mListener.onProgressUpdate(percentComplete, msg);
					}
				});
			} catch (InterruptedException e) {
				final String error = e.toString();
				Log.e(TAG, e.toString());
				mResponseHandler.post(new Runnable() {
					public void run() {
						mListener.onErrorOccurred("Object + " + id + " error - "  + error);
					}
				});
			}
		}
			sendStatusMessage("HandlerThread completed on " + id);
	}
	

	// Note this is running under UI thread
	protected void cancel() {
		Log.i(TAG, "Canceling timer");
		doCancel = true;
		mListener.cancelComplete();

	}

}
