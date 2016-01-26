package com.fisincorporated.interfaces;

import android.os.Bundle;

import com.fisincorporated.democode.demoui.DemoTopicInfo;

/**
 * Created by ericfoertsch on 1/6/16.
 */
public interface IDemoCallbacks {
    /**
     * Callback for when an menu item has been selected.
     */
    public void onItemSelected(DemoTopicInfo demoTopicInfo);

    public void createAndDisplayFragment(Bundle fragmentBundle);

    /**
     * Notify that exit and enter view transitions have been updated.
     */
    public void exitEnterTransitionsUpdated();

}
