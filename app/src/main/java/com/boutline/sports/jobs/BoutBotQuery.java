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
public class BoutBotQuery extends Job  {
    public static final int PRIORITY = 1;
    private String TAG="BoutBot Query Job";

    JobManager jobManager;
    Object[] parameters;
    public String methodName;
    public BoutBotQuery(String methodName,Object[] parameters)
    {

        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy("BoutBotQueryJob"));
        this.parameters = parameters;
        this.methodName = methodName;
    }


    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        if(MyDDPState.getInstance().getDDPState()== DDPStateSingleton.DDPSTATE.LoggedIn)
        {

         MyDDPState.getInstance().BoutQuery(methodName,parameters);
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
