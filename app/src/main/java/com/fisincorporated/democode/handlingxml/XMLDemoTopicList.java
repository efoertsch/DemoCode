package com.fisincorporated.democode.handlingxml;

import com.fisincorporated.democode.demoui.DemoTopicInfo;
import com.fisincorporated.democode.demoui.DemoTopicList;

/**
 * Created by ericfoertsch on 1/5/16.
 */
public class XMLDemoTopicList extends DemoTopicList {
    private static final long serialVersionUID = 1L;

    public XMLDemoTopicList() {
        addItem(new DemoTopicInfo("XMLPullParser", "com.fisincorporated.democode.handlingxml.ActivityForParserOutput", null, null));
    }

}
