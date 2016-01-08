package com.fisincorporated.democode.demoui;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} demo 'drill down' class.
 *
 */
public abstract class DemoDrillDownFragment extends Fragment {

    public IDemoCallbacks mDemoCallbacks;
    /**
     * Used in bundle to id the name of the fragment to instantiate for drill down
     */
    public static final String NEXT_FRAGMENT = "com.fisincorporated.democode.demoui.NEXT_FRAGMENT";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IDemoCallbacks) {
            mDemoCallbacks = (IDemoCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IDemoCallbacks interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDemoCallbacks = null;
    }

    /**
     * Get the package name this fragment resides in
     * @return package name
     * Concatenate package name with class name to create fully qualified name of
     * fragment to instantiate.
     */
    public String getClassPackageName(){
        return this.getClass().getPackage().getName();
    }


}
