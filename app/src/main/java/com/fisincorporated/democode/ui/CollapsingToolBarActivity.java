package com.fisincorporated.democode.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;

import com.fisincorporated.democode.R;
import com.fisincorporated.democode.demoui.DemoListActivity;

/**
 * Demo with collapsing mActionBar.
 */
public class CollapsingToolBarActivity extends DemoListActivity {
    private static final String TAG = CollapsingToolBarActivity.class.getSimpleName();
    protected CollapsingToolbarLayout mCollapsingToolbar;



    @Override
    protected int getLayoutResId() {
        return R.layout.collapsing_toolbar_activity_fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //mCollapsingToolbar =
        //        (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //mCollapsingToolbar.setTitle(toolBarTitle);
        //collapsingToolbar.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        //loadBackdrop();

//        mActionBar = getSupportActionBar();
//        if (mActionBar != null) {
//            // hamburger icon
//            //mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//        }
    }




}
