package com.fisincorporated.democode.demoui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fisincorporated.interfaces.IDemoCallbacks;

/**
 * Display a list of demo topics. Selecting a topic causes the corresponding demo code fragment to
 * be displayed
 * When the app first starts it will display the list of topics based on MainDemoTopicList. From there
 * on the fragment should display a list of topics that are passed to it via the calling activity
 */
public class DemoListFragment extends ListFragment  {


    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";


    /**
     * The fragment's current callback object, which is notified of list item
     * clicks and other sundry happenings.
     */
    private IDemoCallbacks mCallbacks = null;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;


    private String mDemoListClassName = null;
    private DemoTopicList mDemoTopicList = new DemoTopicList();



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

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListAdapter(new ArrayAdapter<DemoTopicInfo>(getActivity(),
                android.R.layout.simple_list_item_activated_1, android.R.id.text1,
                mDemoTopicList.ITEMS));

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState
                    .getInt(STATE_ACTIVATED_POSITION));
        }
    }

    /**
     * Look for passed in arguments.
     * @param savedInstanceState
     */
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
            mDemoListClassName = bundle.getString(DemoTopicList.DEMO_LIST);
            mDemoTopicList = null;
            if (mDemoListClassName != null) {
                try {
                    Class<?> demoClass = Class.forName(mDemoListClassName);
                    mDemoTopicList = (DemoTopicList) demoClass.newInstance();
                } catch (ClassNotFoundException cnfe) {
                    mDemoTopicList = null;
                } catch (java.lang.InstantiationException e) {
                    mDemoTopicList = null;
                } catch (IllegalAccessException e) {
                    mDemoTopicList = null;
                }
            } else {
                mDemoTopicList = new MainDemoTopicList();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities cocallbackntaining this fragment must implement its callbacks.
        if (!(activity instanceof IDemoCallbacks)) {
            throw new IllegalStateException(
                    "Activity must implement IDemoCallbacks.");
        }
            mCallbacks = (IDemoCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        DemoTopicInfo demoTopicInfo = (DemoTopicInfo) mDemoTopicList.ITEMS.get(position);
        mCallbacks.onItemSelected(demoTopicInfo);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DemoTopicList.DEMO_LIST, mDemoListClassName );
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
