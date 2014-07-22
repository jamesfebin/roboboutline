package com.boutline.sports.jobs;

import android.content.Context;
import android.content.SharedPreferences;

import com.boutline.sports.application.Constants;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by user on 22/07/14.
 */
public class Retweet extends Job {

    Long statusId;
    String AccessToken;
    String AccessTokenSecret;

    public Retweet(Long statusId,String AccessToken,String AccessTokenSecret) {

        super(new Params(Priority.MID).requireNetwork().persist().groupBy("retweetRequests"));
        this.statusId=statusId;
        this.AccessToken = AccessToken;
        this.AccessTokenSecret = AccessTokenSecret;
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
            twitter.retweetStatus(statusId);



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
