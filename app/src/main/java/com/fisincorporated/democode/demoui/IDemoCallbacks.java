package com.fisincorporated.democode.demoui;

import android.os.Bundle;

/**
 * Created by ericfoertsch on 1/6/16.
 */
public interface IDemoCallbacks {
        /**
         * Callback for when an menu item has been selected.
         */
        public void onItemSelected(DemoTopicInfo demoTopicInfo);

        public void createAndDisplayFragment(Bundle fragmentBundle);


}
