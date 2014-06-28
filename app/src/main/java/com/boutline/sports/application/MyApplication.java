package com.boutline.sports.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.boutline.sports.R;

/**
 * Created by user on 21/06/14.
 */
public class MyApplication extends Application {

    private static Context sContext = null;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.sContext = getApplicationContext();

        // Initialize the singletons so their instances
        // are bound to the application process.
        initSingletons();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * Initializes any singleton classes
     */
    protected void initSingletons() {
        // Initialize App DDP State Singleton
        MyDDPState.initInstance(MyApplication.sContext);



        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                MyDDPState.getInstance().connectIfNeeded();


            }
        }; new Thread(runnable).start();

    }

    public static Context getAppContext() {
        return MyApplication.sContext;
    }

}
