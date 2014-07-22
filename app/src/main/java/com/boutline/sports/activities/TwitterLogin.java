package com.boutline.sports.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.boutline.sports.R;
import com.boutline.sports.application.Constants;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.jobs.GetTwitterUserInfo;
import com.boutline.sports.models.Tweet;
import com.path.android.jobqueue.JobManager;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by user on 21/07/14.
 */
public class TwitterLogin extends Activity {
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;
    private int TWITTER_AUTH=1;
    JobManager jobManager;


    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Shared Preferences
        mSharedPreferences = this.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loginToTwitter();



    }

    public void loginToTwitter()
    {
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(Constants.CONSUMER_KEY,Constants.CONSUMER_SECRET );
        try
        {
            requestToken = twitter.getOAuthRequestToken(Constants.TWITTER_CALLBACK_URL);
        }
        catch (TwitterException e)
        {
            e.printStackTrace();
        }
        Intent i = new Intent(this, TwitterWebViewLogin.class);
        i.putExtra("URL", requestToken.getAuthenticationURL());
        startActivityForResult(i, TWITTER_AUTH);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TWITTER_AUTH)
        {

            if (resultCode == Activity.RESULT_OK)
            {
                String oauthVerifier = (String) data.getExtras().get("oauth_verifier");

                AccessToken accessToken ;

                try
                {
                    // Pair up our request with the response
                    accessToken = twitter.getOAuthAccessToken(oauthVerifier);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("twitter_access_token",accessToken.getToken());
                    editor.putString("twitter_access_token_secret", accessToken.getTokenSecret());
                    Log.e("AccessToken", accessToken.getToken());
                    Log.e("Secret",accessToken.getTokenSecret());
                    jobManager = MyApplication.getInstance().getJobManager();
                    jobManager.addJobInBackground(new GetTwitterUserInfo(accessToken.getToken(),accessToken.getTokenSecret()));

                    editor.commit();
                    finish();
                }
                catch (TwitterException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                finish();
            }
        }
        else
        {

            finish();
        }



    }

    @Override
    protected void onResume() {
        super.onResume();

         }







}
