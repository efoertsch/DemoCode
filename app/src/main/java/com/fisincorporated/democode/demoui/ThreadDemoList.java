package com.fisincorporated.democode.demoui;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;


public class ThreadDemoList<T> extends DemoList  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ThreadDemoList() {
		addItem(new FragmentItem("AsyncTask", "com.fisincorporated.democode.threads.AsyncTaskActivity", "com.fisincorporated.democode.threads.AsyncTaskFragment", null));
		addItem(new FragmentItem("HandlerThread", "com.fisincorporated.democode.threads.HandlerThreadActivity", "com.fisincorporated.democode.threads.HandlerThreadFragment", null));
		addItem(new FragmentItem("IntentService ", "com.fisincorporated.democode.threads.IntentServiceActivity", "com.fisincorporated.democode.threads.IntentServiceFragment", null));
		 
	}


}
