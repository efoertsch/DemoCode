package com.fisincorporated.democode.demoui;

import com.fisincorporated.democode.R;
import com.fisincorporated.democode.demoui.DemoListFragment.Callbacks;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class DemoListActivity extends FragmentActivity implements
		DemoListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private AlertDialog errorAlertDialog;

	
	//added for tablet

	protected int getLayoutResId() {
		return R.layout.activity_masterdetail;
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DemoList demoList = null ;
		super.onCreate(savedInstanceState);
		 setContentView(getLayoutResId());
		// See if intent had list of fragments (to be passed to list fragment)
		 // Note demoListJson may be null if just starting this as main activity
		 String demoListJson = getIntent().getStringExtra(DemoList.DEMO_LIST);

		 
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
		if (fragment == null){
			fragment = new DemoListFragment();
			// pass the list of demo fragments to the list fragment
			Bundle bundle = new Bundle();
			bundle.putString(DemoList.DEMO_LIST,demoListJson);
			fragment.setArguments(bundle);
	
			fm.beginTransaction().add(R.id.fragmentContainer, fragment)
			.commit();
		}
	
		
	}


	/**
	 * Callback method from {@link DemoListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 * @throws ClassNotFoundException 
	 */
	@Override
	public void onItemSelected(FragmentItem fragmentItem) throws ClassNotFoundException {
		// If selected Item is another list then call the activity passing it the list
		if (fragmentItem.getDemoListClassName() != null){
			Intent listIntent = new Intent(this, Class.forName(fragmentItem.getActivity()));
			listIntent.putExtra(DemoList.DEMO_LIST,fragmentItem.getDemoListClassName());
			startActivity(listIntent);
			return;
		}
		if (mTwoPane ) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			// Eventually set up to pass arguments (like a drill down list)
			//Bundle arguments = new Bundle();
			//arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
			//ItemDetailFragment fragment = new ItemDetailFragment();
			Fragment fragment;
			try {
				fragment = (Fragment) Class.forName(fragmentItem.getFragment()).newInstance();
				//fragment.setArguments(arguments);
				getSupportFragmentManager().beginTransaction()
				 	//.replace(R.id.item_detail_container, fragment).commit();
				.replace(R.id.detailFragmentContainer, fragment).commit();
			} catch (InstantiationException e) {
				displayError("InstantiationException", fragmentItem.getFragment() +  " " + e.toString());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				displayError("IllegalAccessException", fragmentItem.getFragment()  +  " " + e.toString());
			} catch (ClassNotFoundException cnfe){
				 displayError("ClassNotFoundException", fragmentItem.getFragment() + " could not be instantiated");
			}
			
			
		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent =  new Intent(this, Class.forName(fragmentItem.getActivity()));;
			// Eventually add arguments
			//detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
		
	private void displayError(String title, String description){
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
