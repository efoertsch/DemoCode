package com.fisincorporated.democode.bluetooth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.fisincorporated.democode.MasterActivity;
import com.fisincorporated.democode.R;

public class BleScanActivity extends MasterActivity {
    private static final String TAG = "BleScanActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setTitle(R.string.ble_scanner);
    }

    @Override
    public void onSelectedAction(Bundle args) {
        Log.w(TAG, "onSelectedAction callback called but no logic defined");
    }

    @Override
    protected Fragment createFragment() {
        // no arguments need to be set
        return new BleScanFragment();

    }

}
