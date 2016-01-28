package com.fisincorporated.democode.demoui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.fisincorporated.democode.R;
import com.fisincorporated.utility.GlobalConstants;

/**
 * A generic activity that will display a fragment based on values placed in the intent.
 * Basically this activity is meant to be referenced as an activity in a DemoTopicList as a 'leaf'
 * activity and most likely displayed on a phone
 */
public class DemoGenericActivity extends DemoMasterActivity {
    private static final String TAG = DemoGenericActivity.class.getSimpleName();

    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        getAnimationsPreferences();
        lookForArguments(savedInstanceState);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mActionBar = getSupportActionBar();
            //mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(R.string.app_name);
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            FragmentTransaction ft = fm.beginTransaction();
            setFragmentTransition(ft);
            ft.add(R.id.fragmentContainer, fragment).commit();
        }
        mActionBar = getSupportActionBar();
        if (mActionBar != null && toolBarTitle != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);

            if (toolBarTitle != null && !toolBarTitle.equals("")) {
                mActionBar.setTitle(toolBarTitle);
            } else {
                mActionBar.setTitle(TAG);
            }
        }
    }

    /**
     * Get saved values either from intent or from savedInstanceState
     *
     * @param savedInstanceState bundle to retrieve saved values
     */
    protected void lookForArguments(Bundle savedInstanceState) {
        if (getIntent() != null) {
            fragmentClassName = getIntent().getStringExtra(GlobalConstants.FRAGMENT_CLASS_NAME);
            toolBarTitle = getIntent().getStringExtra(GlobalConstants.FRAGMENT_TITLE_BAR_NAME);
        } else {
            toolBarTitle = getResources().getString(R.string.app_name);
        }
    }


    protected Fragment createFragment() {
        // create fragment based on values passed in on intent
        Fragment fragment = null;
        try {
            fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
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

    /**
     * IDemoCallBacks method
     * Create new fragment and display it
     *
     * @param fragmentBundle - contains name of fragment to display and the args to pass to it
     */
    public void createAndDisplayFragment(Bundle fragmentBundle) {
        fragmentClassName = fragmentBundle.getString(DemoDrillDownFragment.NEXT_FRAGMENT);
        createFragment();
        Log.w(TAG, "onItemSelected callback called but no logic defined");

    }


}
