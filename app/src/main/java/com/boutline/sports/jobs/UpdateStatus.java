package com.boutline.sports.jobs;

import com.boutline.sports.application.Constants;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by user on 22/07/14.
 */
public class UpdateStatus extends Job {

    String AccessToken;
    String AccessTokenSecret;
    String tweet;

    public UpdateStatus(String AccessToken, String AccessTokenSecret, String tweet) {

        super(new Params(Priority.MID).requireNetwork().persist().groupBy("tweet"));
        this.AccessToken = AccessToken;
        this.AccessTokenSecret = AccessTokenSecret;
        this.tweet=tweet;

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

            twitter.updateStatus(tweet);


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
        return 25;
    }
}
