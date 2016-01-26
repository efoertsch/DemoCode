
package com.fisincorporated.democode;

import com.fisincorporated.democode.bluetooth.ListSearchBluetoothActivity;
import com.fisincorporated.democode.contentprovider.ContactsActivity;
import com.fisincorporated.democode.demoui.DemoListActivity;
import com.fisincorporated.democode.handlingxml.ActivityForParserOutput;


 
public class FunctionListFragment extends MainMenuListFragment {

    @Override
    void prepareMenu() {
   	 addMenuItem("ThreadActivityList",DemoListActivity.class);
   	 addMenuItem("ItemListActivity", ItemListActivity.class);
   	 addMenuItem("Bluetooth Demo2", ListSearchBluetoothActivity.class);
       addMenuItem("ActivityTemplate (Activity only, no Fragment)", ActivityTemplate.class);
       addMenuItem("ActivityWithFragmentTemplate (Activity uses fragment)", ActivityWithFragmentTemplate.class);
       addMenuItem("Activity calling XMLPullParser", ActivityForParserOutput.class);
       addMenuItem("Contacts ContentProvider", ContactsActivity.class);
       addMenuItem("Eclipse Master/Detail template", ItemListActivity.class);
   	  
    }

}