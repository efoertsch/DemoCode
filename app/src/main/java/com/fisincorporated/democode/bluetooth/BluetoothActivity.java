package com.fisincorporated.democode.bluetooth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.fisincorporated.democode.MasterActivity;


public class BluetoothActivity extends MasterActivity {
	private static final String TAG = "BluetoothActivity";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar.setTitle(TAG);
	}

	@Override
	public void onSelectedAction(Bundle args) {
		// TODO Auto-generated method stub
		Log.w(TAG,"onSelectedAction callback called but no logic defined");
	}

	@Override
	protected Fragment createFragment() {
		// no arguments need to be set
		return new BluetoothFragment();
		
	}

}
