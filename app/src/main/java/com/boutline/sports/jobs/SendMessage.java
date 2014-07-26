package com.boutline.sports.jobs;

import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.exceptions.NotLoggedIn;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;

/**
 * Created by user on 13/07/14.
 */
public class SendMessage extends Job  {
    public static final int PRIORITY = 1;
    private String TAG="SendMessage Job";
    JobManager jobManager;
    Object[] parameters;
    public SendMessage(Object[] parameters)
    {

        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy("sendMessage"));
        this.parameters = parameters;

    }


    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        if(MyDDPState.getInstance().getDDPState()== DDPStateSingleton.DDPSTATE.LoggedIn)
        {
         MyDDPState.getInstance().sendMessage(parameters);
        }
        else
            throw new NotLoggedIn();

    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {

        MyDDPState.getInstance().connectIfNeeded();


    try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

        return true;
    }

    @Override
    protected int getRetryLimit() {

        return 25;
    }


}
