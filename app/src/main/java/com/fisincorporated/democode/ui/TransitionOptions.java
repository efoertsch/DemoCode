package com.fisincorporated.democode.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import com.fisincorporated.democode.R;
import com.fisincorporated.democode.demoui.DemoDrillDownFragment;
import com.fisincorporated.utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Options to set exit/enter animations
 */
public class TransitionOptions extends DemoDrillDownFragment {
    private RadioButton mRbtnForwardTransition;
    private RadioButton mRbtnBackTransition;
    private RadioButton mRbtnExitAnimation;
    private RadioButton mRbtnEnterAnimation;

    private ArrayList<AnimationType> mAnimationTypes;
    private AnimationOptionAdapter mAnimationOptionAdapter;
    private int mForwardExitAnimation;
    private int mForwardEnterAnimation;
    private int mBackExitAnimation;
    private int mBackEnterAnimation;


    public TransitionOptions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TransitionOptions.
     */
    public static TransitionOptions newInstance() {
        return new TransitionOptions();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAnimationsPreferences();
        getAnimationTypes();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_transition_options, container, false);
        mRbtnForwardTransition = (RadioButton) rootView.findViewById(R.id.btnForwardTransition);
        // Since you can't define onClick in XML used in Fragments, define onClickListener here
        mRbtnForwardTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        mRbtnBackTransition = (RadioButton) rootView.findViewById(R.id.rbtnBackTransition);
        mRbtnBackTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        mRbtnExitAnimation = (RadioButton) rootView.findViewById(R.id.rbtnExitAnimation);
        mRbtnExitAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        mRbtnEnterAnimation = (RadioButton) rootView.findViewById((R.id.rbtnEnterAnimation));
        mRbtnEnterAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });

        ListView lvAnimation = (ListView) rootView.findViewById(R.id.lvAnimation);
        lvAnimation.addFooterView(new View(getActivity().getBaseContext()),
                null, true);
        lvAnimation
                .setDescendantFocusability(ListView.FOCUS_AFTER_DESCENDANTS);
        lvAnimation.setItemsCanFocus(true);
        mAnimationOptionAdapter = new AnimationOptionAdapter(mAnimationTypes);
        lvAnimation.setAdapter(mAnimationOptionAdapter);
        lvAnimation.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mRbtnForwardTransition.isChecked()) {
                    // defining forward transition
                    if (mRbtnExitAnimation.isChecked()) {
                        // exit transition
                        mForwardExitAnimation = mAnimationTypes.get(position).getAnimationResourceId();
                    } else {
                        // enter transition
                        mForwardEnterAnimation = mAnimationTypes.get(position).getAnimationResourceId();
                    }
                } else {
                    // back transition
                    if (mRbtnExitAnimation.isChecked()) {
                        // exit transition
                        mBackExitAnimation = mAnimationTypes.get(position).getAnimationResourceId();
                    } else {
                        // forward transition
                        mBackEnterAnimation = mAnimationTypes.get(position).getAnimationResourceId();
                    }
                }

                mAnimationOptionAdapter.notifyDataSetChanged();

            }

        });

        Button btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnimationPreferences();
                mDemoCallbacks.exitEnterTransitionsUpdated();
            }
        });
        return rootView;
    }

    /**
     * Just case selected animations to be updated
     * @param view either exit or enter radio button
     */
    public void onRadioButtonClicked(View view) {

        mAnimationOptionAdapter.notifyDataSetChanged();
    }

    public void getAnimationTypes() {
        mAnimationTypes = new ArrayList<>(Arrays.asList(AnimationType.values()));
    }

    public void getAnimationsPreferences() {
        Context context = getActivity();
        mForwardExitAnimation = Utility.getAnimationPreference(context, Utility.FORWARD_EXIT_ANIMATION, 0);
        mForwardEnterAnimation = Utility.getAnimationPreference(context, Utility.FORWARD_ENTER_ANIMATION, 0);
        mBackExitAnimation = Utility.getAnimationPreference(context, Utility.BACK_EXIT_ANIMATION, 0);
        mBackEnterAnimation = Utility.getAnimationPreference(context, Utility.BACK_ENTER_ANIMATION, 0);
    }

    public void saveAnimationPreferences() {
        Context context = getActivity();
        Utility.storeAnimation(context, Utility.FORWARD_EXIT_ANIMATION, mForwardExitAnimation);
        Utility.storeAnimation(context, Utility.FORWARD_ENTER_ANIMATION, mForwardEnterAnimation);
        Utility.storeAnimation(context, Utility.BACK_EXIT_ANIMATION, mBackExitAnimation);
        Utility.storeAnimation(context, Utility.BACK_ENTER_ANIMATION, mBackEnterAnimation);
    }

    private class AnimationOptionAdapter extends
            ArrayAdapter<AnimationType> {
        public class AnimationOptionHolder {
            public RadioButton rbtnAnimationOption;
        }

        public AnimationOptionAdapter(ArrayList<AnimationType> optionList) {
            super(getActivity(), android.R.layout.simple_selectable_list_item,
                    optionList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            AnimationOptionHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                viewHolder = new AnimationOptionHolder();
                rowView = inflater.inflate(R.layout.animation_option,
                        parent, false);
                viewHolder.rbtnAnimationOption = (RadioButton) rowView
                        .findViewById(R.id.rbtnAnimation);
                rowView.setTag(viewHolder);
            }
            viewHolder = (AnimationOptionHolder) rowView
                    .getTag();
            AnimationType animationType = this.getItem(position);
            viewHolder.rbtnAnimationOption.setText(animationType.getAnimationDescriptionResourceId());

            if (mRbtnForwardTransition.isChecked()) {
                // Forward transition
                if (mRbtnExitAnimation.isChecked()) {
                    viewHolder.rbtnAnimationOption.setChecked(animationType.getAnimationResourceId() == mForwardExitAnimation);
                } else { // EnterAnimation checked
                    viewHolder.rbtnAnimationOption.setChecked(animationType.getAnimationResourceId() == mForwardEnterAnimation);
                }
            } else {
                // must be backward
                if (mRbtnExitAnimation.isChecked()) {
                    viewHolder.rbtnAnimationOption.setChecked(animationType.getAnimationResourceId() == mBackExitAnimation);
                } else { // EnterAnimation checked
                    viewHolder.rbtnAnimationOption.setChecked(animationType.getAnimationResourceId() == mBackEnterAnimation);
                }
            }

            return rowView;
        }

    }


}

