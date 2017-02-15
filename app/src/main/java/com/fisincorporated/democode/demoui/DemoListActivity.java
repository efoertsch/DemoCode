package com.fisincorporated.democode.demoui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.fisincorporated.democode.R;
import com.fisincorporated.utility.GlobalConstants;


/**
 * DemoListActivity is a 'generic' activity in that it can
 * 1. Display a selectable list of demo topics
 * 2. Based on the selected demo item drill down to display a sub list OR
 * 3. Display the selected demo topic either as a fragment or activity OR
 * 4. If the demo fragment itself allows 'drill down' allow the demo fragment to be
 * replaced with the drill down fragment.
 * This is a bit of an experiment to see how generic we can make a code demo app.
 */

public class DemoListActivity extends DemoMasterActivity {
    private static final String TAG = DemoListActivity.class.getSimpleName();
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;


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
    private static int sDrillDownLevel = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        lookForArguments(savedInstanceState);
        // do whatever needed for action bar https://developer.android.com/guide/topics/ui/actionbar.html#SplitBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mActionBar = getSupportActionBar();
            //mActionBar.setDisplayHomeAsUpEnabled(true);
            //mActionBar.setTitle(R.string.app_name);
        }

        if (findViewById(R.id.detailFragmentContainer) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        createAndDisplayDemoListFragment(mMainFragmentTitle, mDemoListJson);

        getAnimationsPreferences();

    }


    /**
     * Get saved values either from intent or from savedInstanceState
     * @param savedInstanceState bundle to retrieve saved values
     */
    protected void lookForArguments(Bundle savedInstanceState) {
        if (getIntent() != null) {
            // See if intent had list of topics (to be passed to list fragment)
            // Note mDemoListJson may be null if just starting this as main activity
            mDemoListJson = getIntent().getStringExtra(GlobalConstants.DEMO_LIST_CLASS_NAME);
            mMainFragmentTitle = getIntent().getStringExtra(GlobalConstants.DEMO_LIST_TITLE);

            fragmentClassName = getIntent().getStringExtra(GlobalConstants.FRAGMENT_CLASS_NAME);
            toolBarTitle = getIntent().getStringExtra(GlobalConstants.FRAGMENT_TITLE_BAR_NAME);
        } else {
            toolBarTitle = getResources().getString(R.string.app_name);
        }
        // Look for savedInstance stuff
    }


    @Override
    public void onBackPressed() {
        --sDrillDownLevel;
        if (sDrillDownLevel == 0 ){
            mActionBar.setDisplayHomeAsUpEnabled(false);
        }
        super.onBackPressed();
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
        outState.putString(GlobalConstants.DEMO_LIST_CLASS_NAME, mDemoListJson);
        outState.putString(GlobalConstants.DEMO_LIST_TITLE, mMainFragmentTitle);
        outState.putString(GlobalConstants.DETAIL_FRAGMENT_TITLE, mDetailFragmentTitle);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mDemoListJson = savedInstanceState.getString(GlobalConstants.DEMO_LIST_CLASS_NAME);
        mMainFragmentTitle = savedInstanceState.getString(GlobalConstants.DEMO_LIST_TITLE);
        mDetailFragmentTitle = savedInstanceState.getString(GlobalConstants.DETAIL_FRAGMENT_TITLE);

    }


    /**
     * A demo topic has been selected.
     * 1. If topic is another list, replace current list fragment with new list fragment.
     * 2. If no list
     * If no activity replace current fragment with selected topic fragment
     * If topic activity present, start new activity passing new fragment topic
     * class name (if any)
     */
    public void onItemSelected(DemoTopicInfo demoTopicInfo) {
        ++sDrillDownLevel;
        if (mActionBar != null ) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        getAnimationsPreferences();
        String demoDescription = demoTopicInfo.getDescription();
        String demoActivityClassName = demoTopicInfo.getActivity();
        String demoFragmentClassName = demoTopicInfo.getFragment();
        String demoListClassName = demoTopicInfo.getDemoListClassName();
        if (demoListClassName != null) {
            // No activity or fragment given, replace old list with new
            if (demoActivityClassName == null && demoFragmentClassName == null) {
                createAndDisplayDemoListFragment(demoDescription, demoListClassName);
                return;
            }
            // No activity but fragment given. create new fragment, pass in list and
            // display
            if (demoActivityClassName == null) {
                Fragment fragment = createFragment(demoFragmentClassName);
                Bundle bundle = new Bundle();
                bundle.putString(GlobalConstants.DEMO_LIST_CLASS_NAME, demoListClassName);
                fragment.setArguments(bundle);
                putFragmentInContainer(fragment, R.id.fragmentContainer);
                return;
            }
            else { //demoActivityClassName != null
                //Activity defined, may or may not have fragment and list topic
                Intent intent = createIntentForClass(demoActivityClassName);
                if (intent == null) {
                    // Problem creating intent
                    return;
                }
                intent.putExtra(GlobalConstants.DEMO_LIST_CLASS_NAME, demoListClassName);
                intent.putExtra(GlobalConstants.FRAGMENT_CLASS_NAME, demoFragmentClassName);
                intent.putExtra(GlobalConstants.DEMO_LIST_TITLE, demoDescription);
                startActivityWithTransition(intent);
            }

        } else {
            // List is null so start new activity or display new fragment
            // topic list is null
            if (demoActivityClassName == null && demoFragmentClassName != null) {
                // no activity class, display fragment
                Fragment fragment = createFragment(demoFragmentClassName);
                displayFragment(fragment);
            } else {
                // activity not null. Start activity
                Intent intent = createIntentForClass(demoActivityClassName);
                if (intent == null) {
                    // Problem creating intent
                    return;
                }
                intent.putExtra(GlobalConstants.FRAGMENT_CLASS_NAME, demoFragmentClassName);
                intent.putExtra(GlobalConstants.DEMO_LIST_TITLE, demoDescription);
                startActivityWithTransition(intent);
            }


        }

    }


    /**
     * 1) Start new activity to display the topic
     * 2) Display the demo topic in fragment
     */
//    public void onItemSelected(DemoTopicInfo demoTopicInfo) {
//        // See if demo topic has another demolist (still drilling down in list of demo)
//        // and if so then call the activity passing it the list
//        getAnimationsPreferences();
//        if (demoTopicInfo.getDemoListClassName() != null) {
//            Intent intent = createIntentForClass(demoTopicInfo.getActivity());
//            if (intent == null) {
//                // Problem creating intent
//                return;
//            }
//            intent.putExtra(DemoTopicList.DEMO_LIST_CLASS_NAME, demoTopicInfo.getDemoListClassName());
//            intent.putExtra(DEMO_LIST_TITLE, demoTopicInfo.getDescription());
//            startActivityWithTransition(intent);
//            return;
//        }
//        // Here if no new drill down list
//        // See if we are on tablet
//        if (mTwoPane) {
//            // In two-pane mode, show the detail view in this activity by
//            // adding or replacing the detail fragment using a
//            // fragment transaction.
//            Fragment fragment;
//            String fragmentClassName = demoTopicInfo.getFragment();
//            if (fragmentClassName != null) {
//                // May or may not have a fragment to use for the demo. If no fragment (null) then the
//                // demo will be done via an activity
//                try {
//                    fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
//                    //fragment.setArguments(arguments);
//                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    // MUST set transition BEFORE add/replace
//                    setFragmentTransition(ft);
//                    //.replace(R.id.item_detail_container, fragment).commit();
//                    ft.replace(R.id.detailFragmentContainer, fragment);
//                    mDetailFragmentTitle = demoTopicInfo.getDescription();
//                    mTvFragmentHeaderText.setText(mDetailFragmentTitle);
//                    mTvFragmentHeaderText.setVisibility(View.VISIBLE);
//
//                    ft.commit();
//                } catch (InstantiationException e) {
//                    displayError("InstantiationException", demoTopicInfo.getFragment() + " " + e.toString());
//                } catch (IllegalAccessException e) {
//                    // TODO Auto-generated catch block
//                    displayError("IllegalAccessException", demoTopicInfo.getFragment() + " " + e.toString());
//                } catch (ClassNotFoundException cnfe) {
//                    displayError("ClassNotFoundException", demoTopicInfo.getFragment() + " could not be instantiated");
//                }
//                return;
//            }
//        }
//        // In single-pane mode OR double pane but you don't have a fragment.
//        // Simply start the detail activity for the selected item ID.
//        Intent intent = createIntentForClass(demoTopicInfo.getActivity());
//        // Following 2 values will be null if you don't have fragment
//        intent.putExtra(DemoMasterActivity.FRAGMENT_CLASS_NAME, demoTopicInfo.getFragment());
//        intent.putExtra(DemoMasterActivity.FRAGMENT_TITLE_BAR_NAME, demoTopicInfo.getDescription());
//        if (intent == null) {
//            // Problem creating intent
//            return;
//        }
//        startActivityWithTransition(intent);
//    }
    private void startActivityWithTransition(Intent intent) {
        startActivity(intent);
        doStartTransition();
    }


    /**
     * Create and display new DemoListFragment with corresponding list
     *
     * @param demoListDescription - description of list used for subheader
     * @param demoTopicListClassName - demoTopic class name
     */
    private void createAndDisplayDemoListFragment(String demoListDescription, String demoTopicListClassName) {
        Fragment fragment = new DemoListFragment();
        // pass the list of demo fragments to the list fragment (may be null)
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstants.DEMO_LIST_TITLE, demoListDescription);
        bundle.putString(GlobalConstants.DEMO_LIST_CLASS_NAME, demoTopicListClassName);
        fragment.setArguments(bundle);
        putFragmentInContainer(fragment, R.id.fragmentContainer);
    }

    /**
     * IDemoCallBacks method
     * Create new fragment and display it
     *
     * @param fragmentBundle - contains name of fragment to display and the args to pass to it
     */
    public void createAndDisplayFragment(Bundle fragmentBundle) {
        String fragmentClassName = fragmentBundle.getString(DemoDrillDownFragment.NEXT_FRAGMENT);
        Fragment fragment = createFragment(fragmentClassName);
        if (fragment != null) {
            fragment.setArguments(fragmentBundle);
            displayFragment(fragment);
        }

    }

    protected Fragment createFragment(String fragmentClassName) {
        if (fragmentClassName == null) {
            Log.d(TAG, "Null fragment class name");
            return null;
        } else {  //fragmentClassName != null
            try {
                Fragment fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
                return fragment;
            } catch (InstantiationException e) {
                displayError("InstantiationException", fragmentClassName + " " + e.toString());
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                displayError("IllegalAccessException", fragmentClassName + " " + e.toString());
            } catch (ClassNotFoundException cnfe) {
                displayError("ClassNotFoundException", fragmentClassName + " could not be instantiated");
            }
        }
        return null;
    }

    /**
     * Display 'drill down' fragment in appropriate fragment container
     *
     * @param newFragment fragment to display
     */
    private void displayFragment(Fragment newFragment) {
        int fragmentContainerId;
        // Are we on tablet or not
        if (mTwoPane) {
            fragmentContainerId = R.id.detailFragmentContainer;
        } else {
            fragmentContainerId = R.id.fragmentContainer;
        }
        putFragmentInContainer(newFragment, fragmentContainerId);
    }

    /**
     * Put fragment in selected fragment container
     *
     * @param newFragment         - fragment to place in container
     * @param fragmentContainerId - id of container to hold newFragment
     */
    private void putFragmentInContainer(Fragment newFragment, int fragmentContainerId) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft;
        mPreviousFragment = fm.findFragmentById(fragmentContainerId);
        if (mPreviousFragment == null) {
            ft = getSupportFragmentManager().beginTransaction();
            // fragment transition MUST be set before ft.add
            setFragmentTransition(ft);
            // first time adding a fragment do not put on backstack
            // it will screw up backbutton processing.
            ft.add(fragmentContainerId, newFragment, null);
            ft.commit();

        } else {
            ft = getSupportFragmentManager().beginTransaction();
            // fragment transition MUST be set before ft.replace
            setFragmentTransition(ft);
            ft.replace(fragmentContainerId, newFragment, "option").addToBackStack(null);
            ft.commit();
        }
    }

    /**
     * Create new intent based on class name
     *
     * @param className  name of class to be created and used to create intent
     * @return intent for the given className
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
