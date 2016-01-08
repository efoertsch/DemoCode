package com.fisincorporated.democode.demoui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fisincorporated.democode.R;

/**
 * Master demo code activity that can implement fragments either with
 * 1. fragment for small screen (phone) or
 * 2. fragments (parent/child) if on larger screen (tablet)
 */
public abstract class  DemoMasterActivity extends AppCompatActivity implements IDemoCallbacks {
    private static final String TAG = DemoMasterActivity.class.getSimpleName();

    protected ActionBar actionBar;
    //private SearchView searchView = null;
    //private SearchManager searchManager = null;

    protected abstract Fragment createFragment();

    //added for tablet

    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // added for master/detail fragments as on tablet
        //setContentView(R.layout.activity_fragment);
        // if for v11 and above just getFragmentMananger rather than getSupportFragmentManager()
        setContentView(getLayoutResId());
        // do whatever needed for action bar https://developer.android.com/guide/topics/ui/actionbar.html#SplitBar
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }


    @Override
    public void createAndDisplayFragment(Bundle fragmentBundle) {
        Fragment fragment = null;
        String fragmentClassName = fragmentBundle.getString(DemoDrillDownFragment.NEXT_FRAGMENT);
        if (fragmentClassName == null) {
            Log.d(TAG, "createAndDisplayFragment called but no DemoDrillDownFragment.NEXT_FRAGMENT string found in bundle");
            return;
        }

        try {
            Class<?> demoClass = Class.forName(fragmentClassName);
            fragment = (Fragment) demoClass.newInstance();
            fragment.setArguments(fragmentBundle);
        } catch (ClassNotFoundException cnfe) {
            return;
        } catch (java.lang.InstantiationException e) {
            return;
        } catch (IllegalAccessException e) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment oldDetail = fm.findFragmentById(R.id.fragmentContainer);

        if (oldDetail != null) {
            ft.remove(oldDetail);
        }
        if (fragment != null){
            ft.add(R.id.fragmentContainer, fragment);
        }
        ft.commit();
    }



//uncomment/modify to implement search
//Add the menu , in this case just search icon
//@Override
//@TargetApi(11)
//public boolean onCreateOptionsMenu(Menu menu) {
//	super.onCreateOptionsMenu(menu);
//	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
//		getMenuInflater().inflate(R.menu.search_menu, menu);
//		searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//		// Pull out search view
//		MenuItem searchItem = menu.findItem(R.id.menu_item_search);
//		searchView = (SearchView) searchItem.getActionView();
//		// Get data from searchable.xml as Searchable info
//		searchView.setOnQueryTextListener(new OnQueryTextListener(){
//
//			@Override
//			public boolean onQueryTextChange(String arg0) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public boolean onQueryTextSubmit(String query) {
//				Log.i(TAG,"Got click on search submit");
//				doSearch(query);
//				return true;
//			}});
//
//		ComponentName name = getComponentName();
//		SearchableInfo searchInfo = searchManager.getSearchableInfo(name);
//		searchView.setSearchableInfo(searchInfo);
//		return true;
//	}
//	return false;
//
//}

//uncomment/modify to implement search
//public void doSearch(String query){
//	Intent intent = new Intent(this,com.fisincorporated.utility.SearchActivity.class);
//	intent.setAction(Intent.ACTION_SEARCH);
//	intent.putExtra(SearchManager.QUERY, query);
//	startActivity(intent);
//}
//

//uncomment/modify to implement search
//public boolean onOptionsItemSelected(MenuItem item) {
//	switch (item.getItemId()) {
//	case R.id.menu_item_search:
//		onSearchRequested();
//		return true;
//	default:
//		return super.onOptionsItemSelected(item);
//	}

}
