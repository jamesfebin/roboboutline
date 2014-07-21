package com.boutline.sports.jobs;

import android.content.Context;
import android.util.Log;

import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.exceptions.NotLoggedIn;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.ConnectionClosedException;

import java.net.ConnectException;

/**
 * Created by user on 13/07/14.
 */
public class Connect extends Job {


    public Connect()
    {
        super(new Params(Priority.HIGH).requireNetwork());

    }


    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        MyDDPState.getInstance().connectIfNeeded();

    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {

 return false;

    }


}
