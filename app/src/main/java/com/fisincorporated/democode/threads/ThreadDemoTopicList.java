package com.fisincorporated.democode.threads;


import com.fisincorporated.democode.demoui.DemoTopicInfo;
import com.fisincorporated.democode.demoui.DemoTopicList;

public class ThreadDemoTopicList<T> extends DemoTopicList {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ThreadDemoTopicList() {
		addItem(new DemoTopicInfo("AsyncTask", "com.fisincorporated.democode.demoui.DemoGenericActivity", "com.fisincorporated.democode.threads.AsyncTaskFragment", null));
		addItem(new DemoTopicInfo("HandlerThread", "com.fisincorporated.democode.demoui.DemoGenericActivity", "com.fisincorporated.democode.threads.HandlerThreadFragment", null));
		addItem(new DemoTopicInfo("IntentService ", "com.fisincorporated.democode.demoui.DemoGenericActivity", "com.fisincorporated.democode.threads.IntentServiceFragment", null));
		 
	}


}
