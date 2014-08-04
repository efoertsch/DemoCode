package com.fisincorporated.democode;

//import de.greenrobot.daoexample.DaoMaster;
//import de.greenrobot.daoexample.DaoSession;
//import de.greenrobot.daoexample.DaoMasterOpenHelper;
import com.fisincorporated.interfaces.IHandleSelectedAction;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;


public class MasterFragment extends Fragment {
	private final static String TAG = "MasterFragment";
	//protected DatabaseHelperTemplate databaseHelper = null;
	//protected SQLiteDatabase database = null;
	 

	protected IHandleSelectedAction callBacks;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		//getDatabaseSetup();
	}
		
   // Called when the Fragment is attached to its parent Activity.
	// Called *before* fragments UI has been created, and before fragment
	// itself or its parent Activity have finished initialization
	// so in general just get reference to parent activity
	@Override
	public void onAttach(Activity activity) {
		Log.i(TAG, "onAttach");
		super.onAttach(activity);
		// Get a reference to the parent Activity and keep it for callbacks.
		callBacks = (IHandleSelectedAction) activity;
	}
	
			
   // Called when the Fragment has been detached from its parent Activity.
	// May not be called if parent Activity's process is terminated without
	// completing it's lifecycle. Also remove reference to callback activity
	@Override
	public void onDetach(){
		Log.i(TAG, "onDetach");
		super.onDetach();
		callBacks = null;
	}
	

// this is for 'regular' SQLite database and database helper
//	public void getDatabaseSetup() {
//		if (databaseHelper == null)
//			databaseHelper = TrackerDatabaseHelper
//					.getTrackerDatabaseHelper(getActivity().getApplicationContext());
//		if (database == null)
//			database = databaseHelper.getWritableDatabase();
//		if (!database.isOpen())
//			database = databaseHelper.getWritableDatabase();
//	}
	
// this is for GreenDAO SQLite setup
//	public void getDatabaseSetup() {
//   DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
//   db = helper.getWritableDatabase();
//   daoMaster = new DaoMaster(db);
//   daoSession = daoMaster.newSession();
//	}
   
// Put database in Application object so don't worry about closing
//	public void onDestroy() {
//		Log.i(TAG, "onDestroyView");
//		if (database != null) {
//			if (database.isOpen())
//				database.close();
//			database = null;
//		}
//		super.onDestroy();
//	}
//
//	@Override
//	public void finalize() {
//		Log.i(TAG, "finalize");
//		if (database != null) {
//			if (database.isOpen())
//					database.close();
//			database = null;
//		}
//	}
//
//	public DatabaseHelperTemplate getDatabaseHelper() {
//		return databaseHelper;
//	}
//
//	public SQLiteDatabase getDatabase() {
//		return database;
//	}

	 
	
}
