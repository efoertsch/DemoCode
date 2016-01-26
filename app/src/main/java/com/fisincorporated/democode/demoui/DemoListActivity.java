package com.fisincorporated.democode.demoui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fisincorporated.democode.R;
import com.fisincorporated.utility.Utility;

/**
 * DemoListActivity is a 'generic' activity in that it can
 * 1. Display a selectable list of demo topics
 * 2. Based on the selected demo item drill down to display a sub list OR
 * 3. Display the selected demo topic either as a fragment or activity OR
 * 4. If the demo fragment itself allows 'drill down' allow the demo fragment to be
 * replaced with the drill down fragment.
 * This is a bit of an experiment to see how generic we can make a code demo app.
 */

public class DemoListActivity extends DemoMasterActivity  {
    private static final String TAG = DemoListActivity.class.getSimpleName();
    private static final String MASTER_FRAGMENT_TITLE = "com.fisincorporated.democode.demoui.MASTER_FRAGMENT_TITLE";
    private static final String DETAIL_FRAGMENT_TITLE = "com.fisincorporated.democode.demoui.DETAIL_FRAGMENT_TITLE";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private AlertDialog mErrorAlertDialog;
    protected ActionBar mActionBar;
    private TextView mTvActionBarSubHeader;
    private TextView mTvFragmentHeaderText;

    /**
     * A JSON list of the demo topics that can be selected
     */
    private String mDemoListJson = null;
    /**
     * The demo topic header (only not null once you start drilling down
     */
    private String mMainFragmentTitle = null;

    /**
     * Hold detail fragment text (only not null when you get down to last level of list and a
     * demo topic is selected from the master list
     */
    private String mDetailFragmentTitle = null;

    /**
     * Holds the demo fragment when the demo 'drills down' to another fragment
     */
    private Fragment mPreviousFragment = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        mTvActionBarSubHeader = (TextView) findViewById(R.id.tvActionBarSubHeader);
        mTvFragmentHeaderText = (TextView) findViewById(R.id.tvFragmentHeaderText);

        // See if intent had list of fragments (to be passed to list fragment)
        // Note mDemoListJson may be null if just starting this as main activity
        mDemoListJson = getIntent().getStringExtra(DemoTopicList.DEMO_LIST);
        mMainFragmentTitle = getIntent().getStringExtra(MASTER_FRAGMENT_TITLE);
        if (mMainFragmentTitle != null && mTvActionBarSubHeader != null) {
            mTvActionBarSubHeader.setText(mMainFragmentTitle);
            mTvActionBarSubHeader.setVisibility(View.VISIBLE);
        }

        if (findViewById(R.id.detailFragmentContainer) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new DemoListFragment();
            // pass the list of demo fragments to the list fragment
            Bundle bundle = new Bundle();
            bundle.putString(DemoTopicList.DEMO_LIST, mDemoListJson);
            fragment.setArguments(bundle);

            fm.beginTransaction().add(R.id.fragmentContainer, fragment)
                    .commit();
        }

        mExitAnimation = Utility.getAnimationPreference(this, Utility.EXIT_ANIMATION,0);
        mEnterAnimation = Utility.getAnimationPreference(this, Utility.ENTER_ANIMATION,0);
    }

    protected Fragment createFragment(){
        Fragment fragment =  new DemoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DemoTopicList.DEMO_LIST, mDemoListJson);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DemoTopicList.DEMO_LIST, mDemoListJson);
        outState.putString(MASTER_FRAGMENT_TITLE, mMainFragmentTitle);
        outState.putString(DETAIL_FRAGMENT_TITLE, mDetailFragmentTitle);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mDemoListJson = savedInstanceState.getString(DemoTopicList.DEMO_LIST);
        mMainFragmentTitle = savedInstanceState.getString(MASTER_FRAGMENT_TITLE);
        mDetailFragmentTitle = savedInstanceState.getString(DETAIL_FRAGMENT_TITLE);
        if (mDetailFragmentTitle != null) {
            mTvFragmentHeaderText.setText(mDetailFragmentTitle);
            mTvFragmentHeaderText.setVisibility(View.VISIBLE);
        }

    }



    /**
     * A demo topic has been selected. Determine if you need to
     * 1) Start new activity to display the topic
     * 2) Display the demo topic in fragment
     */
    @Override
    public void onItemSelected(DemoTopicInfo demoTopicInfo) {
        // See if demo topic has another demolist (still drilling down in list of demo)
        // and if so then call the activity passing it the list
        getAnimationsPreferences();
        if (demoTopicInfo.getDemoListClassName() != null) {
            Intent intent = createIntentForClass(demoTopicInfo.getActivity());
            if (intent == null) {
                // Problem creating intent
                return;
            }
            intent.putExtra(DemoTopicList.DEMO_LIST, demoTopicInfo.getDemoListClassName());
            intent.putExtra(MASTER_FRAGMENT_TITLE, demoTopicInfo.getDescription());
            startActivityWithTransition(intent);
            return;
        }
        // Here if no new drill down list
        // See if we are on tablet
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Fragment fragment;
            String fragmentClassName = demoTopicInfo.getFragment();
            if (fragmentClassName != null) {
                // May or may not have a fragment to use for the demo. If no fragment (null) then the
                // demo will be done via an activity
                try {
                    fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
                    //fragment.setArguments(arguments);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    // MUST set transition BEFORE add/replace
                    setFragmentTransition(ft);
                    //.replace(R.id.item_detail_container, fragment).commit();
                    ft.replace(R.id.detailFragmentContainer, fragment);
                    mDetailFragmentTitle = demoTopicInfo.getDescription();
                    mTvFragmentHeaderText.setText(mDetailFragmentTitle);
                    mTvFragmentHeaderText.setVisibility(View.VISIBLE);

                    ft.commit();
                } catch (InstantiationException e) {
                    displayError("InstantiationException", demoTopicInfo.getFragment() + " " + e.toString());
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    displayError("IllegalAccessException", demoTopicInfo.getFragment() + " " + e.toString());
                } catch (ClassNotFoundException cnfe) {
                    displayError("ClassNotFoundException", demoTopicInfo.getFragment() + " could not be instantiated");
                }
                return;
            }
        }
        // In single-pane mode OR double pane but you don't have a fragment.
        // Simply start the detail activity for the selected item ID.
        Intent intent = createIntentForClass(demoTopicInfo.getActivity());
        // Following 2 values will be null if you don't have fragment
        intent.putExtra(DemoMasterActivity.FRAGMENT_CLASS_NAME, demoTopicInfo.getFragment());
        intent.putExtra(DemoMasterActivity.FRAGMENT_TITLE_BAR_NAME, demoTopicInfo.getDescription());
        if (intent == null) {
            // Problem creating intent
            return;
        }
        startActivityWithTransition(intent);
    }


    private void startActivityWithTransition(Intent intent) {
        startActivity(intent);
        doStartTransition();
    }


    /**
     * Create new fragment and display it
     *
     * @param fragmentBundle - contains name of fragment to display and the args to pass to it
     */
    public void createAndDisplayFragment(Bundle fragmentBundle) {
        String fragmentClassName = fragmentBundle.getString(DemoDrillDownFragment.NEXT_FRAGMENT);
        if (fragmentClassName != null) {
            try {
                Fragment fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
                fragment.setArguments(fragmentBundle);
                displayFragment(fragment);
            } catch (InstantiationException e) {
                displayError("InstantiationException", fragmentClassName + " " + e.toString());
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                displayError("IllegalAccessException", fragmentClassName + " " + e.toString());
            } catch (ClassNotFoundException cnfe) {
                displayError("ClassNotFoundException", fragmentClassName + " could not be instantiated");
            }
        }

    }

    /**
     * Display 'drill down' fragment in appropriate fragment container
     *
     * @param newFragment
     */
    private void displayFragment(Fragment newFragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft;
        int fragmentId;
        // Are we on tablet or not
        if (mTwoPane) {
            fragmentId = R.id.detailFragmentContainer;
        } else {
            fragmentId = R.id.fragmentContainer;
        }
        mPreviousFragment = fm.findFragmentById(fragmentId);
        if (mPreviousFragment == null) {
            ft = getSupportFragmentManager().beginTransaction();
            // fragment transition MUST be set before ft.add
            setFragmentTransition(ft);
            ft.add(fragmentId, newFragment, "option").addToBackStack(null);
            ft.commit();

        } else {
            ft = getSupportFragmentManager().beginTransaction();
            // fragment transition MUST be set before ft.replace
            setFragmentTransition(ft);
            ft.replace(fragmentId, newFragment, "option").addToBackStack(null);
            ft.commit();
        }
    }

    /**
     * Create new intent based on class name
     *
     * @param className
     * @return
     */
    private Intent createIntentForClass(String className) {
        try {
            return new Intent(this, Class.forName(className));
        } catch (ClassNotFoundException e) {
            displayError("ClassNotFoundException", className + " could not be instantiated");
        }
        return null;
    }




}
