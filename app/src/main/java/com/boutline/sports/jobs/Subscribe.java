package com.boutline.sports.jobs;

import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.exceptions.NotConnectedToBoutline;
import com.boutline.sports.exceptions.NotLoggedIn;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

/**
 * Created by user on 14/07/14.
 */
public class Subscribe extends Job {

    String subscriptionName;
    Object[] parameters;
    DDPStateSingleton ddp;
    public Subscribe(DDPStateSingleton ddp,String subscriptionName, Object[] parameters)
    {
        super(new Params(Priority.LOW).requireNetwork());
        this.subscriptionName=subscriptionName;
        this.parameters = parameters;
        this.ddp=ddp;

    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {


        if(MyDDPState.getInstance().getDDPState()== DDPStateSingleton.DDPSTATE.LoggedIn)
        ddp.subscribe(subscriptionName,parameters);
        else
        {
            throw new NotLoggedIn();
        }


    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {

        if(throwable instanceof NotLoggedIn) {
            MyDDPState.getInstance().connectIfNeeded();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected int getRetryLimit() {
        return 10;
    }
}
