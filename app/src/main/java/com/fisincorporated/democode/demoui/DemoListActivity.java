package com.fisincorporated.democode.demoui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fisincorporated.democode.R;

/**
 * DemoListActivity is a 'generic' activity in that all should do is
 * display a fragment that contains a list of demo topics. A callback is provided such that
 * that selecting a demo topic on the fragment causes the activity to either
 * 1) Call the associated activity for that topic Or
 * 2) Display the demo fragment (that demos a particular aspect of Android code)
 * Whether or not to do one or the other is dependent on the info contained in the
 * DemoTopicInfo class passed in the callback
 */
public class DemoListActivity extends AppCompatActivity implements
        DemoListFragment.Callbacks {
    private static final String TAG = DemoListActivity.class.getSimpleName();
    private static final String MAIN_FRAGMENT_TITLE = "com.fisincorporated.democode.demoui.MAIN_FRAGMENT_TITLE";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private AlertDialog errorAlertDialog;
    protected ActionBar actionBar;
    private TextView tvActionBarSubHeader ;
    private TextView tvFragmentHeaderText;

    //added for tablet
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DemoTopicList demoTopicList = null;
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        tvActionBarSubHeader = (TextView) findViewById(R.id.tvActionBarSubHeader);
        tvFragmentHeaderText = (TextView) findViewById(R.id.tvFragmentHeaderText);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // See if intent had list of fragments (to be passed to list fragment)
        // Note demoListJson may be null if just starting this as main activity
        String demoListJson = getIntent().getStringExtra(DemoTopicList.DEMO_LIST);
        String mainFragmentTitle = getIntent().getStringExtra(MAIN_FRAGMENT_TITLE);
        if (mainFragmentTitle != null) {
            tvActionBarSubHeader.setText(mainFragmentTitle);
            tvActionBarSubHeader.setVisibility(View.VISIBLE);
        }


        // if (findViewById(R.id.item_detail_container) != null) {
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
            bundle.putString(DemoTopicList.DEMO_LIST, demoListJson);
            fragment.setArguments(bundle);

            fm.beginTransaction().add(R.id.fragmentContainer, fragment)
                    .commit();
        }
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


    /**
     * A demo topic has been selected. Determine if you need to
     * 1) Start new activity to display the topic
     * 2) Display the demo topic in
     *
     * @throws ClassNotFoundException
     */
    @Override
    public void onItemSelected(DemoTopicInfo demoTopicInfo) throws ClassNotFoundException {
        // See if demo topic has another demolist (still drilling down in list of demo)
        // and if so then call the activity passing it the list
        if (demoTopicInfo.getDemoListClassName() != null) {
            Intent listIntent = new Intent(this, Class.forName(demoTopicInfo.getActivity()));
            listIntent.putExtra(DemoTopicList.DEMO_LIST, demoTopicInfo.getDemoListClassName());
            listIntent.putExtra(MAIN_FRAGMENT_TITLE, demoTopicInfo.getDescription());
            startActivity(listIntent);
            return;
        }
        // Here if no list
        // See if we are on tablet
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            // Eventually set up to pass arguments (like a drill down list)
            //Bundle arguments = new Bundle();
            //arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
            //ItemDetailFragment fragment = new ItemDetailFragment();
            Fragment fragment;
            String fragmentClassName = demoTopicInfo.getFragment();
            if (fragmentClassName != null) {
                // May or may not have a fragment to use for the demo. If no fragment (null) then the
                // demo will be done via an activity
                try {
                    fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
                    //fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            //.replace(R.id.item_detail_container, fragment).commit();
                            .replace(R.id.detailFragmentContainer, fragment).commit();
                    tvFragmentHeaderText.setText(demoTopicInfo.getDescription());
                    tvFragmentHeaderText.setVisibility(View.VISIBLE);
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
        Intent detailIntent = new Intent(this, Class.forName(demoTopicInfo.getActivity()));
        // Eventually add arguments
        //detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
        startActivity(detailIntent);

    }

    private void displayError(String title, String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(description)
                .setTitle(title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        errorAlertDialog.dismiss();
                    }
                });
        errorAlertDialog = builder.create();
        errorAlertDialog.show();
    }
}
