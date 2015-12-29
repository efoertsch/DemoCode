package com.fisincorporated.democode;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

//from Wrox Professional Android 4 Professional Development book with some modifications 
//This verison supports version 8(?) through current versions via support library
// If code just for verison 11 on then android.support.v4.... support library not needed
// as fragment logic added to activity
public  class ActivityWithFragmentTemplate extends MasterActivity {

	FragmentManager fragmentManager;
	
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		return FragmentTemplate.newInstance(bundle);
		// or can do something like
		// fragment = FragmentTemplate.newInstance(args) or fragment = new FragmentTemplate() (no arguments)
		//fragment.setArguments(bundle);
		//return fragment;
 
	}

	// Called at the start of the full lifetime.
	// Initialize Activity
	// Inflate the UI (handled in superclass.
	// Get references to fragments
	// Allocate references to class variables
	// Bind data to controls
	// Start Services and Timers
	// Use Bundle as needed to restore the UI to its previous state (or wait to
	// do it in onRestoreInstanceState)
	// Create any objects needed during life of activity
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// do whatever needed
		getIntentExtras();
		getWidgetsSetListeners();
	}

	// get all info passed by calling activity
	private void getIntentExtras() {
		Intent intent = getIntent();
		// get extras
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_with_fragment_template, menu);
		return true;
	}

	private void getWidgetsSetListeners() {
		// get all views from layout
		// assign any listeners using anonymous listerner classes
		// within a listener you may startActivity(Intent intent)
		// or perhaps startActivityForResults(Intent intent, int RequestCode)
	}

	// call by the system after the user presses back button on child activity
	// that was called
	// via startActvitiyForResults
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// requestCode will be value that you originally called child actvitiy
		// with
		// resultCode will be something like Activity.RESULT_OK, .RESULT_CANCELED,
		// RESULT_FIRST_USER
		//
	}

	// Child activity can return results back to parent activity via
	// one of 2 methods. This is not required by child activity
	private void settingChildActivityResults() {
		// setResult(int resultCode);
		// setResult(int resultCode, Intent data);
	}

	// Called after onCreate has finished
	// Restore UI state from the savedInstanceState.
	// This bundle has also been passed to onCreate.
	// Will only be called if the Activity has been
	// killed by the system since it was last visible.
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

	}

	// Called prior to all but first call to onStart
	// (2nd and subsequent visible lifetimes for an activity process.)
	// Load changes knowing that the Activity has already
	// been visible within this process.
	// Restart animations, threads, Sensor listeners, GPS lookups,
	// Timers,Services or other processes that are used to exclusively update the
	// UI
	// May also be done in onStart
	@Override
	public void onRestart() {
		super.onRestart();
	}

	// Called at the start of the *visible* lifetime.
	// Apply any required UI change now that the Activity is visible.
	// Register Broadcase Receivers
	@Override
	public void onStart() {
		super.onStart();
	}

	// Called at the start of the *active* lifetime.
	// The activity is in the foreground and receiving user input events
	// Resume any paused UI updates, threads, or processes required
	// by the Activity but suspended when it was inactive.
	// Use to reregister any Broadcast Receivers or other processes suspended in
	// onPause
	@Override
	public void onResume() {
		super.onResume();
	}
	
	//This is the fragment-orientated version of onResume() that you can override 
	//to perform operations in the Activity at the same point where its fragments are resumed.
	@Override
	protected void onResumeFragments() {
	    super.onResumeFragments();
	    //....
	}

	// Called immediately before onPause
	// Called to save UI state changes (checkbox states, user focus, entered but
	// uncommitted data)
	// at the end of the active lifecycle.
	// Save UI state changes to the savedInstanceState.
	// This bundle will be passed to onCreate and
	// onRestoreInstanceState if the process is
	// killed and restarted by the run time.
	// You can safely assume this (and onPause) will be called before process
	// terminated
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// add needed values to bundle here
	}

	// Called at the end of the active lifetime.
	@Override
	public void onPause() {
		// Suspend UI updates, threads, or CPU intensive processes
		// that don't need to be updated when the Activity isn't
		// the active foreground Activity.
		super.onPause();
	}

	// Called at the end of the visible lifetime.
	// Suspend remaining UI updates, threads, or processing
	// that aren't required when the Activity isn't visible.
	// Persist all edits or state changes
	// as after this call the process is likely to be killed.
	// Pause or stop animations, threads, Sensor listeners, GPS lookups,
	// Timers,Services or other processes that are used to exclusively update the
	// UI
	// Unregister Broadcast Receivers
	@Override
	public void onStop() {
		super.onStop();
	}

	// Sometimes called at the end of the full lifetime.
	@Override
	public void onDestroy() {
		// Clean up any resources including ending threads,
		// Close all external connections (network/database links)
		super.onDestroy();
	}

	
	// From IHandleSelectedAction interface
	// handle callback
	@Override
	public void onSelectedAction(Bundle args) {
		// TODO Auto-generated method stub
		
	}


}
