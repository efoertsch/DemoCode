package com.fisincorporated.democode.demoui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fisincorporated.democode.R;
import com.fisincorporated.interfaces.IDemoCallbacks;
import com.fisincorporated.utility.GlobalConstants;

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
    private TextView mTvListFragmentHeader;
    private String mDemoListDescription;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.demo_list, container, false);
        mTvListFragmentHeader = (TextView) view.findViewById(R.id.tvListFragmentHeader);
        updateSubActionBarHeader(mDemoListDescription);
        return view;
    }

    private void updateSubActionBarHeader(String headerText) {
        if (headerText != null && mTvListFragmentHeader != null) {
            mTvListFragmentHeader.setText(headerText);
            mTvListFragmentHeader.setVisibility(View.VISIBLE);
        }
        else {
            if (mTvListFragmentHeader != null) {
                mTvListFragmentHeader.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListAdapter(new ArrayAdapter<>(getActivity(),
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
     * @param savedInstanceState bundle
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
            mDemoListClassName = bundle.getString(GlobalConstants.DEMO_LIST_CLASS_NAME);
            mDemoListDescription = bundle.getString(GlobalConstants.DEMO_LIST_TITLE);
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

        // Activities containing this fragment must implement its callbacks.
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
        DemoTopicInfo demoTopicInfo = mDemoTopicList.ITEMS.get(position);
        mCallbacks.onItemSelected(demoTopicInfo);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GlobalConstants.DEMO_LIST_CLASS_NAME, mDemoListClassName );
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
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
