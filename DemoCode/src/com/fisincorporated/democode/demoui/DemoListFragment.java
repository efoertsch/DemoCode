package com.fisincorporated.democode.demoui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fisincorporated.democode.dummy.DummyContent;
import com.google.gson.Gson;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class DemoListFragment extends ListFragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	private DemoList demoList = new DemoList();

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 * 
		 * @throws ClassNotFoundException
		 */
		public void onItemSelected(FragmentItem fragmentItem)
				throws ClassNotFoundException;
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {

		@Override
		public void onItemSelected(FragmentItem FragmentItem) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public DemoListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lookForArguments(savedInstanceState);
		// TODO: replace with a real list adapter.
		setListAdapter(new ArrayAdapter<FragmentItem>(getActivity(),
				android.R.layout.simple_list_item_activated_1, android.R.id.text1,
				demoList.ITEMS));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	// In general
	// 1. See if arguments passed to Fragment via newInstance
	// setArguments(bundle) and if so get via getArguments
	// 2. See if any info in savedInstanceState bundle
	// Need to be careful as logic below may need modification per the
	// circumstances
	private void lookForArguments(Bundle savedInstanceState) {
		Bundle bundle = null;
		if (getArguments() != null) {
			bundle = getArguments();
		}
		// If fragment destroyed but then later recreated,
		// the savedInstanceState will hold info (assuming saved via
		// onSaveInstanceState(... )
		if (savedInstanceState != null) {
			bundle = savedInstanceState;
		}
		if (bundle != null) {
			// String demoListJson = bundle.getString(DemoList.DEMO_LIST);
			String demoListClassName = bundle.getString(DemoList.DEMO_LIST);
			// Gson gson = new Gson();
			// if (demoListJson != null) {
			// demoList = gson.fromJson(demoListJson, DemoList.class);
			demoList = null;
			if (demoListClassName != null) {
				try {
					Class<?> demoClass = Class.forName(demoListClassName);
					// This should always be the case, but...
					//if(DemoList.class.isInstance(demoClass)){
						demoList = (DemoList) demoClass.newInstance();
					//}
				} catch (ClassNotFoundException cnfe) {
					demoList = null;
				} catch (java.lang.InstantiationException e) {
					// TODO Auto-generated catch block
					demoList = null;
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					demoList = null;
				}
			} else {
				demoList = new MainDemoList();
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		FragmentItem fragmentItem = (FragmentItem) demoList.ITEMS.get(position);
		try {
			mCallbacks.onItemSelected(fragmentItem);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
}
