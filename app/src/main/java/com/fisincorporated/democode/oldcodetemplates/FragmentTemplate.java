package com.fisincorporated.democode.oldcodetemplates;

import com.fisincorporated.democode.R;
import com.fisincorporated.interfaces.IHandleSelectedAction;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

//From Wrox Android 4 Application Development with code/comment modifications
//for backward compatibility (using support library)

// Note that Fragment manager responsible for call Fragment's lifecycle methods
// If parent Activity is already running, FragmentManager will call
// Fragments onAttach,onCreate,onCreateView,onActivityCreated,onStart, and onResume
// so fragment is 'caught up' with parent Activity
// If support library used, if fragment added after Activity.onCreate, then onActivityCreated not 
// called immediately after Activity.onCreate but is called as Activity.onStart is being performed
//
// If fragment removed from activity via remove() then fragments methods called in this order
// onPause(), onStop(), onDestroyView(), onDestroy(), onDetach()

public class FragmentTemplate extends MasterFragment {
	private final static String TAG = "FragmentTemplate";
	// for callbacks to activity 
	protected IHandleSelectedAction callBacks;
	private Activity parentActivity;
	
	// pass startup info to fragment if needed, in this case just pass on bundle
	// to fragment. 
	// If other arguments passed to fragment, put args in bundle and use as setArguments/getArguments logic
	
	public static FragmentTemplate newInstance(Bundle bundle) {
		Log.i(TAG, "newInstance");
		FragmentTemplate fragment = new FragmentTemplate();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	// a different way on passing args
	public static FragmentTemplate newInstance(String arg0, int arg1, Parcelable arg3) {
		Log.i(TAG, "newInstance");
		Bundle bundle = new Bundle();
		FragmentTemplate fragment = new FragmentTemplate();
		bundle.putString("Arg0", arg0);
		bundle.putInt("Arg1", arg1);
		bundle.putParcelable("Arg2", arg3);
		fragment.setArguments(bundle);
		return fragment;
	}

	
	public void onAttach(Activity activity){
		Log.i(TAG, "onAttach");
		super.onAttach(activity);
		// Save a reference to the parent activity
		 parentActivity = activity;
		// also check to see if parent has implemented any Listeners required by this Fragment
		try {
			callBacks = (IHandleSelectedAction) parentActivity;
		}
		 catch (ClassCastException e){
			 throw new ClassCastException(activity.toString() + " must implement IHandleSelectedAction");
		 }
	}
	
	//This is called after onDestroy()
	public void onDetach(){
			Log.i(TAG, "onDetach");
			super.onDetach();
			callBacks = null;
	}
	
	// callback to activity (of course could be any parms based on definition of callback)
	private void doCallBack(Bundle bundle){
		Log.i(TAG, "doCallBack");
		callBacks.onSelectedAction(bundle);
	}

  // Called (after onAttach) when the Fragment is attached to its parent Activity.
	// Create any class scoped objects here
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		// Initialize the Fragment.
		//	Set if you have menus for fragment
		//  setHasOptionsMenu(true);
		// I think in general you want to setRetaininstance to save data over orientation change 
		// setRetainInstance(true);
		// for a fragment without a UI that is used for background processing you can create background worker threads and tasks
	}

   // Called (after onCreate) when the Fragment is attached to its parent Activity (first time or on orientation change)
	// Create, or inflate the Fragment's UI, and return it.
	// Wait till this point if fragment needs to interact with UI of parent
	// Activity
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get views from fragment layout
		// Bind data to views (array adapters etc
		// Create/Assign listeners
		// Create services/timers
		// If this Fragment has no UI then return null.
		Log.i(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.fragment_template, container, false);
		getReferencedViews(view);
		getSavedInstanceState(savedInstanceState);
		return view;
	}
	
	// get TextViews, Buttons etc from fragment view
	// Set button onClickListeners etc.
	private void getReferencedViews(View view){
		//spnrUnits = (Spinner) view.findViewById(R.id.program_options_spnrUnits);
		//spnrUnits.setOnItemSelectedListener(this);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		//spnrAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.displayUnits,
		//		android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		//spnrAdapter
		//		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply the adapter to the spinner
		//spnrUnits.setAdapter(spnrAdapter);

		//btnSave = (Button) view.findViewById(R.id.program_options_btnSave);
		//btnSave.setOnClickListener(new View.OnClickListener() {
		//	@SuppressLint("DefaultLocale")
		//	public void onClick(View v) {
		//		updateDisplayUnits();
		//	}
		//});
	}

	// Called after Activity onCreate method completes
	// This is the fragments version of onRestoreInstanceState
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.i(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		// Complete the Fragment initialization - particularly anything
		// that requires the parent Activity to be initialized or the
		// Fragment's view to be fully inflated.
		// For a fragment without a UI used for background tasks start worker threads and tasks
	}

	// Called at the start of the visible lifetime.
	@Override
	public void onStart() {
		Log.i(TAG, "onStart");
		super.onStart();
		// Apply any required UI change now that the Fragment is visible.
	}

	// Called at the start of the active lifetime.
	@Override
	public void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
		// Resume any paused UI updates, threads, or processes required
		// by the Fragment but suspended when it became inactive.
	}

	// Called at the end of the active lifetime.
	@Override
	public void onPause() {
		Log.i(TAG, "onPause");
		// Suspend UI updates, threads, or CPU intensive processes
		// that don't need to be updated when the Activity isn't
		// the active foreground activity.
		// Persist all edits or state changes
		// as after this call the process is likely to be killed.
		super.onPause();
	}

	// Called to save UI state changes at the
	// end of the active lifecycle.
	// save Fragment specific info so if you change orientation you have the current
	// value. This is called before activity destroy (perhaps due to orientation changes
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "onSaveInstanceState");
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate, onCreateView, and
		// onCreateView if the parent Activity is killed and restarted.
		super.onSaveInstanceState(savedInstanceState);
		//savedInstanceState.putString("StringKey", stringvalue);
	}
	
	// get Fragment specific info previously saved by onSaveInstanceState
	private void getSavedInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "getSavedInstanceState");
		if (savedInstanceState != null){
			//someValue  = savedInstanceState.getString("StringKey");
		}
	}
	
	// This needs some checking but in general
	// 1. See if arguments passed to Fragment via newInstance setArguments(bundle) and if so get via getArguments
	// 2. See if any info in savedInstnaceState bundle
	// Need to be careful as logic below may need modification per the circumstances
	private void lookForArguments(Bundle savedInstanceState) {
		Bundle bundle = null;
		if (getArguments() != null) {
			bundle = getArguments();
		}
// If fragment destroyed but then later recreated, 
// the savedInstanceState will hold info (assuming saved via onSaveInstanceState(... )
		if (savedInstanceState != null ){
				bundle = savedInstanceState;
		}
		if (bundle != null){
			// get values by key name
		  //locationExerciseId = bundle.getLong(LocationExercise._ID, -1);
		  // title = bundle.getString(GlobalValues.TITLE);
		   //description = bundle.getString(LocationExercise.DESCRIPTION);
		}
		
	}
	
	// Add the menu - Will add below any menu items added by parent activity
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.i(TAG, "onCreateOptionsMenu");
		super.onCreateOptionsMenu(menu, inflater);
		// Add the menu
		//inflater.inflate(R.menu.activity_map_menu, menu);
	}
	
	// handle the selected menu option
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "onOptionsItemSelected");
		switch (item.getItemId()) {
		//case R.id.xxxxs:
			// do something
			// indicate menu selection was handled
			//return true;
		//case R.id.yyyy:
			// do something else
			//return true;
		default:
			// pass up to superclass
			return super.onOptionsItemSelected(item);
		}
	}

	
	

	// Called at the end of the visible lifetime.
	// Suspend remaining UI updates, threads, or processing
	// that aren't required when the Fragment isn't visible.
	@Override
	public void onStop() {
		Log.i(TAG, "onStop");
		super.onStop();
	}

	// Called when the Fragment's View has been detached.
	@Override
	public void onDestroyView() {
		Log.i(TAG, "onDestroyView");
		// Clean up resources related to the View.
		super.onDestroyView();
	}

	// Called at the end of the full lifetime.
	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		// Clean up any resources including ending threads,
		// closing database connections etc.
		super.onDestroy();
	}

	
}
