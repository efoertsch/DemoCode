package com.fisincorporated.democode.demoui;

 import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hold the list of demo topics being presented by the UI
 */
public class DemoTopicList {
	private static final long serialVersionUID = 1L;
	public  final static String DEMO_LIST = "com.fisincorporated.democode.threads.DEMO_LIST";

	public   List<DemoTopicInfo> ITEMS = new ArrayList<DemoTopicInfo>();
	public   Map<String, DemoTopicInfo> ITEM_MAP = new HashMap<String, DemoTopicInfo>();
	
	protected   void addItem(DemoTopicInfo item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.toString(), item);
	}

	public DemoTopicList() {
		super();
	}

	
	
}