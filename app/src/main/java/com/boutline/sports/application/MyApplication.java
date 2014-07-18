package com.boutline.sports.application;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.log.CustomLogger;
import com.path.android.jobqueue.config.Configuration;
//import android.content.res.Configuration;


/**
 * Created by user on 21/06/14.
 */
public class MyApplication extends Application {

    private static Context sContext = null;
    private BroadcastReceiver mReceiver = null;
    private static MyApplication instance;
    private JobManager jobManager;


    @Override
    public void onTerminate() {
        super.onTerminate();
        MyDDPState.getInstance().disconnect();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.sContext = getApplicationContext();

        // Initialize the singletons so their instances
        // are bound to the application process.


        initSingletons();
        configureJobManager();
        instance = this;


    }
/*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }*/




    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        jobManager = new JobManager(this, configuration);
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

    public JobManager getJobManager() {
        return jobManager;
    }
    public static MyApplication getInstance()
    {

        return instance;
    }

}
