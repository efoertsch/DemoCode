package com.fisincorporated.democode.ui;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fisincorporated.democode.R;
import com.fisincorporated.democode.demoui.DemoTopicInfo;
import com.fisincorporated.democode.demoui.DemoTopicList;
import com.fisincorporated.democode.demoui.MainDemoTopicList;

/**
 * Created by ericfoertsch on 2/8/16.
 */
public class DynamicNavDrawer extends AppCompatActivity {

    private ListView mDrawerList;
    private DemoTopicList mDemoTopicList = new MainDemoTopicList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.LEFT);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Set the adapter for the list view
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<DemoTopicInfo>(this,
                R.layout.drawer_list_item, mDemoTopicList.ITEMS));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    /**
     * Just display selected item as toast
     */
    private void selectItem(int position) {
        DemoTopicInfo demoTopicInfo = mDemoTopicList.ITEMS.get(position);
        Toast.makeText(this, "Demo selected:" + demoTopicInfo.getDescription(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
