package com.fisincorporated.democode.threads;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View.OnClickListener;

// Loosely based on various code in Android Programming - The Big Nerd Ranch Guide
// PhotoGalleryFragment
public class AsyncTaskFragment extends ThreadDemoFragment implements OnClickListener {
	
	private static final String TAG = "AsyncTaskFragment";
    private static final String FRAGMENT_TITLE = "AsyncTask";
	AsyncTaskDemo asyncTaskDemo = null;

	// The following are called by the onClick() method in ThreadDemoFragment

    @Override
    protected String getFragmentTitle() {
        return FRAGMENT_TITLE;
    }

    protected void doStart() {
		asyncTaskDemo = new AsyncTaskDemo();
		mStartButton.setEnabled(false);
		mCancelButton.setEnabled(true);
		asyncTaskDemo.execute("Parm1", "Parm2", "Parm3", "Parm4");
	}
 
	protected void doCancel() {
		 if (asyncTaskDemo != null){
			 asyncTaskDemo.cancel(true);
		 }
	}
	
	// The 3 possible parms in defining an AsyncTask are in order
	// 1. Type of input parms, eg. it could be something like AsyncTask<String,
	// Void, Void>()
	// The input parms are passed in to the execute method , eg.
	// task.excute("Param 1", Param 2", "Etc.") which are then passed to
	// doInBackground
	// 2. The 2nd position allows you to specify the type for sending progress
	// updates
	// eg. AsyncTask<String, Integer, Void>()
	// You need to use publishProgess(int) in doInBackground which in turns makes
	// call to
	// onProgressUpdate(Integer... params) with can do update on UI thread
	// 3. Type of result produced by AsyncTask, eg. an ArrayList
	// For more complicated uses you will want to assign the AsyncTask to an
	// instance variable so you
	// can do things like call AysncTask.cancel(boolean)
	// If your AsyncTask is in it's own class, implement a callback interface for
	// the
	// onPreExecute, onProgressUpdate, and onPostExecute calls.
	// Taking it all together we do

	class AsyncTaskDemo extends
			AsyncTask<String, String, ArrayList<String>> {
		@Override
		// Runs on the UI thread before doInBackground(Params...).
		protected void onPreExecute() {
		}

		// This runs on background thread
		// String params passed in execute() ..
		// the return ArrayList<Object> is based on AsyncTaskDemo definition above
		// Contrived example of just adding Strings to ArrayList
		protected ArrayList<String> doInBackground(String... params) {
			ArrayList<String> stringArrayList = new ArrayList<String>();
			ArrayList<String> returnedList = new ArrayList<String>();
			int p = 0;
			for (String parameter : params) {
				final String finalParm = parameter;
				// do something with parameter
				Log.i(TAG, "Received paramter:" + parameter);
				stringArrayList.add(parameter);
				p += 25;
				// this results in calling onProgressUpdate (which is done on UI
				// thread)
				publishProgress(finalParm, p + "");
				// sneaking little thing to post update on UI thread just to show concept 
				// Not really encouraged through as maintenace of this sort of thing can
				// get out of hand if widely used. 
				// http://developer.android.com/guide/components/processes-and-threads.html
				mTvStatusArea.post(new Runnable() {
                public void run() {
               	 mTvStatusArea.append("Processing on parm: " + finalParm + sLineSeparator);
                }
            });
				// see if need to quit. Did something call AsyncTask.cancel(false)
				// to be polite
				// AsyncTask.cancel(true) will interrupt doInBackground Thread but
				// try to avoid this option
				// if cancelled onPostExecute won't be called.
				if (isCancelled()) {
					mTvStatusArea.post(new Runnable() {
	                public void run() {
	               	 mTvStatusArea.append("AsyncTask cancelled " + sLineSeparator);
	                }
	            });
					break;
				}
				try {
					Thread.sleep(2000);
					returnedList.add("Processed on param: " + parameter);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// return the arraylist as defined as the 3rd param in the class definition
			return returnedList;
		}

		// Runs on UI thread
		public void onProgressUpdate(String ... params) {
			String process = params[0];
			String progress = params[1];
			mTvProgressMsg.setText("Processing " + process);
			scrollToBottom();
			 try{
				mProgressBar.setProgress(Integer.parseInt(progress));
			 }
			 catch(NumberFormatException nfe){
				 Log.e(TAG,"NumberFormatException caught trying to convert " + progress + " to integer");
			 }
		}

		// This runs on UI thread, so UI can be updated of course!
		// It receives the returned ArrayList<Object> from doInBackground()
		@Override
		protected void onPostExecute(ArrayList<String> items) {
			mTvStatusArea.append(sLineSeparator);
			for (int i = 0 ; i < items.size();++i){
				mTvStatusArea.append(items.get(i)  + sLineSeparator);
				scrollToBottom();
			}
			mStartButton.setEnabled(true);
			mCancelButton.setEnabled(false);
		}

	
	}

	
}
