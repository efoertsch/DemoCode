package com.fisincorporated.democode.demoui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.fisincorporated.democode.R;

/**
 * A generic activity that will display a fragment based on values placed in the intent.
 * Basically this activity is meant to be referenced as an activity in a DemoTopicList as a 'leaf'
 * activity and most likely displayed on a phone
 */
public class DemoGenericActivity extends DemoMasterActivity {
    private static final String TAG = DemoGenericActivity.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAnimationsPreferences();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            FragmentTransaction ft = fm.beginTransaction();
            setFragmentTransition(ft);
            ft.add(R.id.fragmentContainer, fragment).commit();
        }


    }


    @Override
    protected Fragment createFragment() {
        // create fragment based on values passed in on intent
        Fragment fragment = null;
        try {
            fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
            if (actionBarTitle != null && !actionBarTitle.equals("")) {
                actionBar.setTitle(actionBarTitle);
            } else {
                actionBar.setTitle(TAG);
            }

        } catch (InstantiationException e) {
            displayError("InstantiationException", fragmentClassName + " " + e.toString());
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            displayError("IllegalAccessException", fragmentClassName + " " + e.toString());
        } catch (ClassNotFoundException cnfe) {
            displayError("ClassNotFoundException", fragmentClassName + " could not be instantiated");
        }

        return fragment;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        doExitTransition();
    }

    @Override
    public void onItemSelected(DemoTopicInfo demoTopicInfo) {
        Log.w(TAG, "onItemSelected callback called but no logic defined");
    }


}
