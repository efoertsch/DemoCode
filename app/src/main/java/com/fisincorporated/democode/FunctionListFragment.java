
package com.fisincorporated.democode;

import com.fisincorporated.democode.bluetooth.BluetoothActivity;
import com.fisincorporated.democode.bluetooth.BluetoothActivity2;
import com.fisincorporated.democode.demoui.DemoListActivity;
import com.fisincorporated.democode.threads.AsyncTaskActivity;
import com.fisincorporated.democode.threads.HandlerThreadActivity;
import com.fisincorporated.democode.threads.IntentServiceActivity;


 
public class FunctionListFragment extends MainMenuListFragment {

    @Override
    void prepareMenu() {
   	 addMenuItem("ThreadActivityList",DemoListActivity.class);
   	 addMenuItem("ItemListActivity", ItemListActivity.class);
   	 addMenuItem("Bluetooth Demo", BluetoothActivity.class);
   	 addMenuItem("Bluetooth Demo2", BluetoothActivity2.class);
   	 addMenuItem("IntentServiceDemo", IntentServiceActivity.class);
   	 addMenuItem("HandlerThreadDemo", HandlerThreadActivity.class);
   	 addMenuItem("AsyncTaskActivity", AsyncTaskActivity.class);
       addMenuItem("ActivityTemplate (Activity only, no Fragment)", ActivityTemplate.class);
       addMenuItem("ActivityWithFragmentTemplate (Activity uses fragment)", ActivityWithFragmentTemplate.class);
       addMenuItem("Activity calling XMLPullParser", ActivityForParserOutput.class);
       addMenuItem("Contacts ContentProvider", ContactsActivity.class);
       addMenuItem("Eclipse Master/Detail template", ItemListActivity.class);
   	  
    }

}