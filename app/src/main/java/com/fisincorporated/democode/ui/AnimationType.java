package com.fisincorporated.democode.ui;

import android.support.annotation.AnimRes;
import android.support.annotation.StringRes;

import com.fisincorporated.democode.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ericfoertsch on 1/21/16.
 */
public enum AnimationType {
    DEVICE_DEFAULT(R.string.device_default, 0),
    NO_SCALE_FADE_IN(R.string.no_scale_fade_in, R.anim.no_scale_fade_in),
    NO_SCALE_FADE_OUT(R.string.no_scale_fade_out, R.anim.no_scale_fade_out),
    SLIDE_DOWN_TO_BOTTOM(R.string.slide_down_to_bottom, R.anim.slide_down_to_bottom),
    SLIDE_IN_FROM_LEFT(R.string.slide_in_from_left, R.anim.slide_in_from_left),
    SLIDE_IN_FROM_RIGHT(R.string.slide_in_from_right, R.anim.slide_in_from_right),
    SLIDE_OUT_TO_LEFT(R.string.slide_out_to_left, R.anim.slide_out_to_left),
    SLIDE_OUT_TO_RIGHT(R.string.slide_out_to_right, R.anim.slide_out_to_right),
    SLIDE_UP_FROM_BOTTOM(R.string.slide_up_from_bottom, R.anim.slide_up_from_bottom);

    private final int mDescriptionStringResourceId;
    private final int mAnimationResourceId;

    private AnimationType(@StringRes int descriptionId, @AnimRes int animationId) {
        mDescriptionStringResourceId = descriptionId;
        mAnimationResourceId = animationId;
    }

    @StringRes
    public int getAnimationDescriptionResourceId() {
        return mDescriptionStringResourceId;
    }

    @StringRes
    public int getAnimationResourceId() {
        return mAnimationResourceId;
    }

    private static final Map<Integer, Integer> animationTypes = new HashMap<Integer, Integer>();

    static {
        for (AnimationType animationType : AnimationType.values()) {
            animationTypes.put(animationType.getAnimationDescriptionResourceId(),
                    animationType.getAnimationResourceId());
        }
    }

    public static Integer forValue(int value) {
        return animationTypes.get(value);
    }


}
