package com.boutline.sports.jobs;

import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.exceptions.NotLoggedIn;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

/**
 * Created by user on 15/07/14.
 */
public class SendTournamentPreferences extends Job {
    String tournamentId;

   public SendTournamentPreferences(String tournamentId)
   {

       super(new Params(Priority.HIGH).requireNetwork().persist().groupBy("tournamentPreferences"));

       this.tournamentId = tournamentId;
   }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        if(MyDDPState.getInstance().getDDPState()== DDPStateSingleton.DDPSTATE.LoggedIn)
            MyDDPState.getInstance().followTournament(tournamentId);
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

        return true;    }

    @Override
    protected int getRetryLimit() {

        return 25;
    }


}
