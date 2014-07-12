package com.boutline.sports.application;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.boutline.sports.R;
import com.boutline.sports.activities.ChooseSportsActivity;
import com.boutline.sports.activities.ChooseTournamentActivity;
import com.boutline.sports.activities.MainActivity;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

/**
 * Created by user on 21/06/14.
 */
public class MyApplication extends Application {

    private static Context sContext = null;
    private BroadcastReceiver mReceiver = null;



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





    }

    public static Context getAppContext() {
        return MyApplication.sContext;
    }

}
