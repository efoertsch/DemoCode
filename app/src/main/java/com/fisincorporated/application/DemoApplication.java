package com.fisincorporated.application;

import android.app.Application;
import android.content.Context;

import com.fisincorporated.utility.OttoBus;

/**
 * Created by ericfoertsch on 1/8/16.
 */
public class DemoApplication extends Application{
    private String TAG = "DemoApplication";
    private static DemoApplication mDemoApplication = null;
    private static Context sContext;

    // Use the Otto Bus to communicate upward
    private static OttoBus mOttoBus;


    @Override
    public void onCreate() {
        super.onCreate();
        mDemoApplication = this;
        mOttoBus = new OttoBus();
    }

    public static Context getContext() {
        return mDemoApplication.getApplicationContext();
    }


    public static OttoBus getOttoBus() {
        return mOttoBus;
    }

}
