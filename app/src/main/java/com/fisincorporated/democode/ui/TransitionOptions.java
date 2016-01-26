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
    private RadioButton mRbtnExitAnimation;
    private RadioButton mRbtnEnterAnimation;
    private ArrayList<AnimationType> mAnimationTypes;
    private AnimationOptionAdapter mAnimationOptionAdapter;
    private int mExitAnimation;
    private int mEnterAnimation;


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
        View rootView =  inflater.inflate(R.layout.fragment_transition_options, container, false);
        mRbtnExitAnimation = (RadioButton) rootView.findViewById(R.id.rbtnExitAnimation);
        mRbtnExitAnimation.setChecked(true);
        mRbtnExitAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExitEnterButtonClick(v);
            }
        });
        mRbtnEnterAnimation = (RadioButton) rootView.findViewById((R.id.rbtnEnterAnimation));
        mRbtnEnterAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExitEnterButtonClick(v);
            }
        });

        mRbtnEnterAnimation = (RadioButton) rootView.findViewById(R.id.rbtnEnterAnimation);
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
                if (mRbtnExitAnimation.isChecked()) {
                    mExitAnimation = mAnimationTypes.get(position).getAnimationResourceId();
                } else {
                    mEnterAnimation = mAnimationTypes.get(position).getAnimationResourceId();
                }
                mAnimationOptionAdapter.notifyDataSetChanged();

            }

        });

        Button btnSave = (Button) rootView.findViewById(R.id.btnSave) ;
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
     * Called via xml defined in layout
     *
     * @param view  either exit or enter radio button
     */
    public void onExitEnterButtonClick(View view) {
        if (view == mRbtnExitAnimation) {
            mRbtnExitAnimation.setChecked(true);
            mRbtnEnterAnimation.setChecked(false);
        } else {
            mRbtnExitAnimation.setChecked(false);
            mRbtnEnterAnimation.setChecked(true);
        }
        mAnimationOptionAdapter.notifyDataSetChanged();
    }

    public void getAnimationTypes() {
        mAnimationTypes = new ArrayList<>(Arrays.asList(AnimationType.values()));
    }

    public void getAnimationsPreferences() {
        Context context = getActivity();
        mExitAnimation = Utility.getAnimationPreference(context, Utility.EXIT_ANIMATION, 0);
        mEnterAnimation = Utility.getAnimationPreference(context, Utility.ENTER_ANIMATION, 0);
    }

    public void saveAnimationPreferences() {
        Context context = getActivity();
        Utility.storeAnimation(context, Utility.EXIT_ANIMATION, mExitAnimation);
        Utility.storeAnimation(context, Utility.ENTER_ANIMATION, mEnterAnimation);
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


            if (mRbtnExitAnimation.isChecked()) {
                    viewHolder.rbtnAnimationOption.setChecked(animationType.getAnimationResourceId() == mExitAnimation);
            }
            else{ // EnterAnimation checked
                 viewHolder.rbtnAnimationOption.setChecked(animationType.getAnimationResourceId() == mEnterAnimation);
            }
            return rowView;
        }

    }


}

