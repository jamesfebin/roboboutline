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
public class GetSASURL extends Job  {
    public static final int PRIORITY = 1;
    private String TAG="Generate SAS URL";
    JobManager jobManager;
    String filename;
    String filepath;

    public GetSASURL(String filename,String filepath)
    {

        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy("GETSASURL"));
        this.filename= filename;
        this.filepath = filepath;
    }


    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        if(MyDDPState.getInstance().getDDPState()== DDPStateSingleton.DDPSTATE.LoggedIn)
        {

            MyDDPState.getInstance().getSASURLBackground(filename,filepath);


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
