package com.fisincorporated.democode.demoui;


/**
 * Holds the top level list of demo topics
 */
public class MainDemoTopicList extends DemoTopicList {
    private static final long serialVersionUID = 1L;

    public MainDemoTopicList() {
        // !!! Don't add .class postfix. This will give ClassNotFoundException,
        addItem(new DemoTopicInfo("Threads","com.fisincorporated.democode.demoui.DemoListActivity", "com.fisincorporated.democode.demoui.DemoListFragment", "com.fisincorporated.democode.threads.ThreadDemoTopicList"));
        addItem(new DemoTopicInfo("Bluetooth","com.fisincorporated.democode.demoui.DemoListActivity", "com.fisincorporated.democode.demoui.DemoListFragment", "com.fisincorporated.democode.bluetooth.BluetoothDemoTopicList"));

        addItem(new DemoTopicInfo("XML Processing", "com.fisincorporated.democode.demoui.DemoListActivity", "com.fisincorporated.democode.demoui.DemoListFragment", "com.fisincorporated.democode.handlingxml.XMLDemoTopicList"));
        addItem(new DemoTopicInfo("ContentProvider", "com.fisincorporated.democode.demoui.DemoListActivity", "com.fisincorporated.democode.demoui.DemoListFragment", "com.fisincorporated.democode.contentprovider.ContentProviderDemoTopicList"));
        addItem(new DemoTopicInfo("UI", "com.fisincorporated.democode.demoui.DemoListActivity", "com.fisincorporated.democode.demoui.DemoListFragment", "com.fisincorporated.democode.ui.UiDemoTopicList"));

        //addItem(new DemoTopicInfo("ActivityTemplate (Activity only, no Fragment)", "com.fisincorporated.democode.ActivityTemplate", null,null));
        //addItem(new DemoTopicInfo("ActivityWithFragmentTemplate (Activity uses fragment)","com.fisincorporated.democode.ActivityWithFragmentTemplate", "com.fisincorporated.democode.FragmentTemplate", null));
        //addItem(new DemoTopicInfo("Eclipse Master/Detail template", "com.fisincorporated.democode.ItemListActivity", "com.fisincorporated.democode.ItemListFragment", null));
    }

}

 
	
  
	  
 