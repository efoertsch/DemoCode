package com.fisincorporated.democode;


import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FunctionListActivity extends MasterActivity {


	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new FunctionListFragment();
	}

	// for callback of fragment to Activity (due to IHandleSelectedAction interface in MasterFragmentActivity)
	@Override
	public void onSelectedAction(Bundle args) {
		// TODO Auto-generated method stub
		
	}

}
