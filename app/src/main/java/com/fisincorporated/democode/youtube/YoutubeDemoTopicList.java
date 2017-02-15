package com.fisincorporated.democode.youtube;

import com.fisincorporated.democode.demoui.DemoTopicInfo;
import com.fisincorporated.democode.demoui.DemoTopicList;

/**
 * Created by ericfoertsch on 1/28/16.
 */
public class YoutubeDemoTopicList extends DemoTopicList {
    private static final long serialVersionUID = 1L;

    public YoutubeDemoTopicList() {
        // !!! Don't add .class postfix. This will give ClassNotFoundException,
        addItem(new DemoTopicInfo("My Uploads",null , "com.fisincorporated.democode.youtube.MyUploadsFragment", null));


    }
}