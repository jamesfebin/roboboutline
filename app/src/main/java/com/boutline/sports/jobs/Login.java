package com.boutline.sports.jobs;

import android.util.Log;

import com.boutline.sports.application.MyApplication;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.exceptions.NotConnectedToBoutline;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;

/**
 * Created by user on 13/07/14.
 */
public class Login extends Job {

    JobManager jobManager;

    public Login()
    {

        super(new Params(Priority.HIGH).requireNetwork());


    }
    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        Log.e("Running", "Login");
        Log.e("DDP STATE is ",MyDDPState.getInstance().getDDPState().toString());

        if(MyDDPState.getInstance().getDDPState()== DDPStateSingleton.DDPSTATE.Connected || MyDDPState.getInstance().getDDPState()== MyDDPState.getInstance().getState().NotLoggedIn || MyDDPState.getInstance().getDDPState()== DDPStateSingleton.DDPSTATE.LoggedIn ) {
            MyDDPState.getInstance().boutlineLogin();
       }
        else
       {
            throw new NotConnectedToBoutline();
        }
    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {

    jobManager = MyApplication.getInstance().getJobManager();
    jobManager.addJobInBackground(new Connect());
        return true;
    }

    @Override
    protected int getRetryLimit() {
        return 25;
    }
}
