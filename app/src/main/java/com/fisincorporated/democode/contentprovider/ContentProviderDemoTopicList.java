package com.fisincorporated.democode.contentprovider;

import com.fisincorporated.democode.demoui.DemoTopicInfo;
import com.fisincorporated.democode.demoui.DemoTopicList;

/**
 * Created by ericfoertsch on 1/5/16.
 */
public class ContentProviderDemoTopicList extends DemoTopicList {
    private static final long serialVersionUID = 1L;
    public ContentProviderDemoTopicList() {
        addItem(new DemoTopicInfo("Contacts ContentProvider", "com.fisincorporated.democode.contentprovider.ContactsActivity", null, null));
    }
}









