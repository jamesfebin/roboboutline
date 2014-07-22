package com.boutline.sports.jobs;

import android.util.Log;

import com.boutline.sports.application.Constants;
import com.boutline.sports.application.MyDDPState;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by user on 22/07/14.
 */
public class GetTwitterUserInfo extends Job {

    String AccessToken;
    String AccessTokenSecret;

    public GetTwitterUserInfo( String AccessToken, String AccessTokenSecret) {

        super(new Params(Priority.HIGH).requireNetwork().persist());
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

            User user = twitter.showUser(twitter.getId());
            String profileImageUrl = user.getProfileImageURL();
            String userHandle = user.getScreenName();
            String fullName = user.getName();
            Long twitterId = user.getId();
            Log.e("User Details ", profileImageUrl + userHandle + twitterId);
            MyDDPState.getInstance().storeTwitterUserInfo(twitterId, userHandle, profileImageUrl, fullName);


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
