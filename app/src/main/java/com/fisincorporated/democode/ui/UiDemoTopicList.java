package com.fisincorporated.democode.ui;

import com.fisincorporated.democode.demoui.DemoTopicInfo;
import com.fisincorporated.democode.demoui.DemoTopicList;

/**
 * Created by ericfoertsch on 1/22/16.
 */
public class UiDemoTopicList extends DemoTopicList {

    private static final long serialVersionUID = 1L;

    public UiDemoTopicList() {
        addItem(new DemoTopicInfo("Activity/Fragment Transitions", "com.fisincorporated.democode.demoui.DemoGenericActivity", "com.fisincorporated.democode.ui.TransitionOptions", null));
        addItem(new DemoTopicInfo("Static Nav Drawer", "com.fisincorporated.democode.ui.DemoNavDrawer", null,null));
        addItem(new DemoTopicInfo("Dynamic Nav Drawer", "com.fisincorporated.democode.ui.DynamicNavDrawer", null,null));

    }
}