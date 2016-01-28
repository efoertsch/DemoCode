package com.fisincorporated.democode.demoui;


/**
 * Holds the top level list of demo topics
 */
public class MainDemoTopicList extends DemoTopicList {
    private static final long serialVersionUID = 1L;

    /**
     * Format for setting up topic lists
     *   Activity    Fragment    Topic List     Action
     *     Y            Y           Y           Create Actitivy, pass in list and fragment
     *     Y            N           Y           Create Actitivy, pass in list and fragment (null)
     *     N            Y           Y           Creat new fragment, passing in list
     *     N            N           Y           Replace current list with new list
     *
     *     Y            Y           N           Start activity, pass in fragment name
     *     Y            N           N           Start activity, pass in fragment name (null)
     *     N            Y           N           Display fragment in main or detail fragment container
     */
    public MainDemoTopicList() {
        // !!! Don't add .class postfix. This will give ClassNotFoundException,
        // Order of parms - description, activity, fragment, topic list
        addItem(new DemoTopicInfo("Threads", null, null, "com.fisincorporated.democode.threads.ThreadDemoTopicList"));
        addItem(new DemoTopicInfo("Bluetooth", null, null, "com.fisincorporated.democode.bluetooth.BluetoothDemoTopicList"));

        addItem(new DemoTopicInfo("XML Processing", null, null, "com.fisincorporated.democode.handlingxml.XMLDemoTopicList"));
        addItem(new DemoTopicInfo("ContentProvider", null, null, "com.fisincorporated.democode.contentprovider.ContentProviderDemoTopicList"));
        addItem(new DemoTopicInfo("UI", "com.fisincorporated.democode.ui.CollapsingToolBarActivity", "com.fisincorporated.democode.demoui.DemoListFragment", "com.fisincorporated.democode.ui.UiDemoTopicList"));

        //addItem(new DemoTopicInfo("ActivityTemplate (Activity only, no Fragment)", "com.fisincorporated.democode.oldcodetemplates.ActivityTemplate", null,null));
        //addItem(new DemoTopicInfo("ActivityWithFragmentTemplate (Activity uses fragment)","com.fisincorporated.democode.oldcodetemplates.ActivityWithFragmentTemplate", "com.fisincorporated.democode.oldcodetemplates.FragmentTemplate", null));
        //addItem(new DemoTopicInfo("Eclipse Master/Detail template", "com.fisincorporated.democode.oldcodetemplates.ItemListActivity", "com.fisincorporated.democode.oldcodetemplates.ItemListFragment", null));
    }

}

 
	
  
	  
 