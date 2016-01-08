package com.fisincorporated.democode.bluetooth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.fisincorporated.democode.R;
import com.fisincorporated.democode.demoui.DemoMasterActivity;
import com.fisincorporated.democode.demoui.DemoTopicInfo;


public class BleScanActivity extends DemoMasterActivity {
    private static final String TAG = "BleScanActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setTitle(R.string.ble_scanner);
    }


    @Override
    // TODO make a generic activity that can instantiate a fragment based on info in intent
    // or savedInstanceState
    protected Fragment createFragment() {
        // no arguments need to be set
        return new BleScanFragment();

    }

    @Override
    public void onItemSelected(DemoTopicInfo demoTopicInfo) {
        Log.w(TAG, "onItemSelected callback called but no logic defined");
    }


}
