package com.boutline.sports.jobs;

import com.boutline.sports.application.Constants;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by user on 22/07/14.
 */
public class Reply extends Job {

    String AccessToken;
    String AccessTokenSecret;
    String tweet;
    Long replyStatusId;

    public Reply(String AccessToken, String AccessTokenSecret, String tweet,Long replyStatusId) {

        super(new Params(Priority.MID).requireNetwork().persist().groupBy("reply"));
        this.AccessToken = AccessToken;
        this.AccessTokenSecret = AccessTokenSecret;
        this.tweet=tweet;
        this.replyStatusId = replyStatusId;

    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {


        if(AccessToken!=null && AccessTokenSecret!=null) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(Constants.CONSUMER_KEY);
            builder.setOAuthConsumerSecret(Constants.CONSUMER_SECRET);
            builder.setOAuthAccessToken(AccessToken);
            builder.setOAuthAccessTokenSecret(AccessTokenSecret);
            Configuration config = builder.build();
            Twitter twitter = new TwitterFactory(config).getInstance();

            twitter.updateStatus(new StatusUpdate(tweet).inReplyToStatusId(replyStatusId));


        }



    }

    @Override
    protected void onCancel() {



    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return true;
    }

    @Override
    protected int getRetryLimit() {
        return 1;
    }
}
