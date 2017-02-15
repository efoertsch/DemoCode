package com.fisincorporated.democode.threads;


import com.fisincorporated.democode.demoui.DemoTopicInfo;
import com.fisincorporated.democode.demoui.DemoTopicList;

public class ThreadDemoTopicList<T> extends DemoTopicList {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ThreadDemoTopicList() {
		addItem(new DemoTopicInfo("AsyncTask", null, "com.fisincorporated.democode.threads.AsyncTaskFragment", null));
		addItem(new DemoTopicInfo("HandlerThread", null, "com.fisincorporated.democode.threads.HandlerThreadFragment", null));
		addItem(new DemoTopicInfo("IntentService ", null, "com.fisincorporated.democode.threads.IntentServiceFragment", null));
		addItem(new DemoTopicInfo("SerialExecuter ", null, "com.fisincorporated.democode.threads.SerialExecutorFragment", null));
		 
	}


}
