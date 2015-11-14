package com.fisincorporated.democode.demoui;



public class MainDemoList extends DemoList {
	private static final long serialVersionUID = 1L;
	public MainDemoList() {
		// !!! Don't add .class postfix. This will give ClassNotFoundException, 
		addItem(new FragmentItem("Thread Demos", "com.fisincorporated.democode.demoui.DemoListActivity",   "com.fisincorporated.democode.demoui.DemoListFragment", "com.fisincorporated.democode.demoui.ThreadDemoList"));
		addItem(new FragmentItem("Bluetooth Demo", "com.fisincorporated.democode.bluetooth.BluetoothActivity",   "com.fisincorporated.democode.bluetooth.BluetoothFragment", null));
		addItem(new FragmentItem("Bluetooth Demo2", "com.fisincorporated.democode.bluetooth.BluetoothActivity2",   "com.fisincorporated.democode.bluetooth.BluetoothFragment",null));
		
		addItem(new FragmentItem("ActivityTemplate (Activity only, no Fragment)", "com.fisincorporated.democode.ActivityTemplate", null,null));
		addItem(new FragmentItem("ActivityWithFragmentTemplate (Activity uses fragment)","com.fisincorporated.democode.ActivityWithFragmentTemplate", "com.fisincorporated.democode.FragmentTemplate", null));
		addItem(new FragmentItem("Activity calling XMLPullParser", "com.fisincorporated.democode.ActivityForParserOutput", null, null));
		addItem(new FragmentItem("Contacts ContentProvider", "com.fisincorporated.democode.ContactsActivity", null, null));
		addItem(new FragmentItem("Eclipse Master/Detail template", "com.fisincorporated.democode.ItemListActivity", "com.fisincorporated.democode.ItemListFragment", null));
	}

}

 
	
  
	  
 