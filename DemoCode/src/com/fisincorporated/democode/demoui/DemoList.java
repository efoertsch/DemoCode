package com.fisincorporated.democode.demoui;

 import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;

public class DemoList<T>  {
	private static final long serialVersionUID = 1L;
	public  final static String DEMO_LIST = "com.fisincorporated.democode.threads.DEMO_LIST";

	public   List<FragmentItem> ITEMS = new ArrayList<FragmentItem>();
	public   Map<String, FragmentItem> ITEM_MAP = new HashMap<String, FragmentItem>();
	
	protected   void addItem(FragmentItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.toString(), item);
	}

	public DemoList() {
		super();
	}

	
	
}