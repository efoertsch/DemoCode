package com.fisincorporated.democode.demoui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fisincorporated.democode.R;
import com.fisincorporated.interfaces.IDemoCallbacks;
import com.fisincorporated.utility.Utility;

/**
 * Master demo code activity that can implement fragment based on
 * fragment class passed in
 */
public abstract class DemoMasterActivity extends AppCompatActivity implements IDemoCallbacks  {
    private static final String TAG = DemoMasterActivity.class.getSimpleName();
    protected String fragmentClassName;
    protected String toolBarTitle;
    private AlertDialog mErrorAlertDialog;
    protected int mExitAnimation;
    protected int mEnterAnimation;

    protected ActionBar mActionBar;
    protected Toolbar mToolbar;
    //private SearchView searchView = null;
    //private SearchManager searchManager = null;

    //added for tablet

    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // added for master/detail fragments as on tablet
        setContentView(getLayoutResId());

    }


    /**
     * Save important values over orientation change
     *
     * @param savedInstanceState  important stuff
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // nothing yet
        super.onSaveInstanceState(savedInstanceState);

    }


//    /**
//     * IDemoCallBacks method
//     * Create and display the fragment saved in the bundle. The fragment is displayed in the
//     * 'master' fragment
//     * @param fragmentBundle
//     */
//    @Override
//    public void createAndDisplayFragment(Bundle fragmentBundle) {
//        Fragment fragment;
//        String fragmentClassName = fragmentBundle.getString(DemoDrillDownFragment.NEXT_FRAGMENT);
//        if (fragmentClassName == null) {
//            Log.d(TAG, "createAndDisplayFragment called but no DemoDrillDownFragment.NEXT_FRAGMENT string found in bundle");
//            return;
//        }
//
//        try {
//            Class<?> demoClass = Class.forName(fragmentClassName);
//            fragment = (Fragment) demoClass.newInstance();
//            fragment.setArguments(fragmentBundle);
//        } catch (ClassNotFoundException cnfe) {
//            return;
//        } catch (InstantiationException e) {
//            return;
//        } catch (IllegalAccessException e) {
//            return;
//        }
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        Fragment oldDetail = fm.findFragmentById(R.id.fragmentContainer);
//        setFragmentTransition(ft);
//        if (oldDetail != null) {
//            ft.remove(oldDetail);
//        }
//        if (fragment != null) {
//            ft.add(R.id.fragmentContainer, fragment).addToBackStack(null);
//        }
//        ft.commit();
//    }

    @Override
    public void onBackPressed() {

        doExitTransition();
        super.onBackPressed();
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//
//        if (count <= 1) {
//            super.onBackPressed();
//
//        } else {
//            getSupportFragmentManager().popBackStack();
//        }

    }

    // TODO add 5.0+ transitions
    // For nice overview of 5.0+ transitions
    // http://www.androiddesignpatterns.com/2014/12/activity-fragment-transitions-in-android-lollipop-part1.html
    protected void doStartTransition() {
        overridePendingTransition(mEnterAnimation,
                mExitAnimation);
    }



    //TODO add option to change transitions
    // TODO add 5.0+ transitions
    protected void doExitTransition() {
        overridePendingTransition(mEnterAnimation,
                mExitAnimation);
    }

    protected void setFragmentTransition(FragmentTransaction ft) {
        ft.setCustomAnimations(mEnterAnimation, mExitAnimation);
    }

// TODO refactor this activity with other demo activities with common code

    /**
     * An Ooops occurred. Display AlertDialog with error msg.
     *
     * @param title       dialog title
     * @param description error/msg description
     */
    protected void displayError(String title, String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(description)
                .setTitle(title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mErrorAlertDialog.dismiss();
                        mErrorAlertDialog = null;
                    }
                });
        mErrorAlertDialog = builder.create();
        mErrorAlertDialog.show();
        mErrorAlertDialog.setCanceledOnTouchOutside(false);
    }

    public void exitEnterTransitionsUpdated(){
        getAnimationsPreferences();
    }

    public void getAnimationsPreferences() {
        mExitAnimation = Utility.getAnimationPreference(this, Utility.EXIT_ANIMATION, 0);
        mEnterAnimation = Utility.getAnimationPreference(this, Utility.ENTER_ANIMATION, 0);
    }

    public void saveAnimationPreferences() {
        Utility.storeAnimation(this, Utility.EXIT_ANIMATION, mExitAnimation);
        Utility.storeAnimation(this, Utility.ENTER_ANIMATION, mEnterAnimation);
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
