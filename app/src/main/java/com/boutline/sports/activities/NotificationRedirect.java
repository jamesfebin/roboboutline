package com.boutline.sports.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.boutline.sports.R;
import com.boutline.sports.application.Constants;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.helpers.SmoothProgressBar;
import com.boutline.sports.models.Conversation;
import com.boutline.sports.models.FacebookUserInfo;
import com.google.gson.Gson;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.path.android.jobqueue.JobManager;

/**
 * Created by user on 24/07/14.
 */
public class NotificationRedirect extends Activity {

    private static final String TAG = "Notification Redirecter";
    private BroadcastReceiver mReceiver = null;
    SharedPreferences preferences;
    public FacebookUserInfo fbUser = null;
    public MixpanelAPI mixpanel = null;
    JobManager jobManager;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SmoothProgressBar mProgressBar;
        mProgressBar = (SmoothProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.progressiveStart();
//        getActionBar().hide();

    }


    protected void goToWalkthrough0() {
        Intent mainIntent = new Intent(NotificationRedirect.this, Walkthrough0.class);
        startActivity(mainIntent);
        finish();
        overridePendingTransition(R.anim.pushupin, R.anim.pushupout);
    }


    @Override
    protected void onResume() {
        super.onResume();

        context = getApplicationContext();
        preferences = this.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);

        String storedFbInfoString = preferences.getString("fbUserInfo", null);
        Gson gson = new Gson();
        if (storedFbInfoString != null) {
            fbUser = gson.fromJson(storedFbInfoString, FacebookUserInfo.class);
        }

        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {
            @Override
            protected void onDDPConnect(DDPStateSingleton ddp) {
                super.onDDPConnect(ddp);

                if (fbUser == null) {

                    goToWalkthrough0();
                    finish();

                } else {
                    // Commented code are precious..
                    // MyDDPState.getInstance().boutlineLogin();
                }

            }


            @Override
            protected void onError(String title, String msg) {
                // Do Nothing
            }

            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);


                Bundle bundle = intent.getExtras();

                Toast.makeText(getApplicationContext(), "GOT ACTION" + intent.getAction().toString(), Toast.LENGTH_SHORT).show();

                Log.e("This is what i got", intent.getAction().toString());

                if (intent.getAction().equals("LOGINSUCCESS")) {

                    if (intent.hasExtra("userId") == true) {

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("boutlineUserId", intent.getStringExtra("userId"));
                        editor.commit();


                        if (mixpanel != null) {
                            mixpanel.identify(intent.getStringExtra("userId"));
                            mixpanel.track("Boutline Login Success on SplashScreen", Constants.info);
                        }

                        boolean hasChoseSport = true;

                        if (!hasChoseSport) {

                            Intent mainIntent = new Intent(NotificationRedirect.this, ChooseSportsActivity.class);
                            startActivity(mainIntent);
                            overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                            finish();

                        } else {


                            if (getIntent().getExtras().getString("activity").matches("conversations")) {
                                Intent mainIntent = new Intent(NotificationRedirect.this, ConversationActivity.class);
                                mainIntent.putExtra("conversationId", getIntent().getExtras().getString("conversationId"));
                                startActivity(mainIntent);
                                overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                                finish();
                            } else if (getIntent().getExtras().getString("activity").matches("board")) {

                                Intent mainIntent = new Intent(NotificationRedirect.this, BoardActivity.class);
                                mainIntent.putExtra("type", "match");
                                mainIntent.putExtra("mtId", getIntent().getExtras().getString("mtId"));
                                mainIntent.putExtra("hashtag", getIntent().getExtras().getString("hashtag"));
                                startActivity(mainIntent);
                                overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                                finish();


                            }


                        }


                    }
                } else if (intent.getAction().equals("LOGINFAILED")) {

                    Toast.makeText(getApplicationContext(), "Unable to connect to facebook", Toast.LENGTH_SHORT);

                    if (mixpanel != null) {
                        mixpanel.track("Boutline Login Failed on SplashScreen", Constants.info);
                    }


                } else if (intent.getAction().equals(MyDDPState.MESSAGE_ERROR)) {


                    Intent mainIntent = new Intent(NotificationRedirect.this, ChooseSportsActivity.class);
                    startActivity(mainIntent);
                    overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                    finish();


                    Toast.makeText(getApplicationContext(), "Internet connection not avaialable", Toast.LENGTH_SHORT).show();

                }
            }
        };


        // There are two ways of doing this..
        // jobManager = MyApplication.getInstance().getJobManager();
        // jobManager.addJobInBackground(new Connect());

        MyDDPState.getInstance().connectIfNeeded();


        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("LOGINSUCCESS"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("LOGINFAILED"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(MyDDPState.MESSAGE_ERROR));


        // we want connection state change messages so we know we're logged in
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(MyDDPState.MESSAGE_CONNECTION));


        if (MyDDPState.getInstance().getDDPState() == MyDDPState.DDPSTATE.Closed) {

            // Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT).show();

        } else if (MyDDPState.getInstance().getDDPState() == DDPStateSingleton.DDPSTATE.LoggedIn) {
            boolean hasChoseSport = true;

            if (!hasChoseSport) {

                Intent mainIntent = new Intent(NotificationRedirect.this, ChooseSportsActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                finish();

            } else {

                if (getIntent().getExtras().getString("type").matches("redirect"))

                {

                    if (getIntent().getExtras().getString("activity").matches("conversations")) {
                        Intent mainIntent = new Intent(NotificationRedirect.this, ConversationActivity.class);
                        mainIntent.putExtra("conversationId", getIntent().getExtras().getString("conversationId"));
                        startActivity(mainIntent);
                        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                        finish();
                    } else if (getIntent().getExtras().getString("activity").matches("board")) {

                        Intent mainIntent = new Intent(NotificationRedirect.this, BoardActivity.class);
                        mainIntent.putExtra("type", "match");
                        mainIntent.putExtra("mtId", getIntent().getExtras().getString("mtId"));
                        mainIntent.putExtra("hashtag", getIntent().getExtras().getString("hashtag"));
                        startActivity(mainIntent);
                        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                        finish();


                    }

                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


        if (mReceiver != null) {

            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {

        if (mixpanel != null)
            mixpanel.flush();
        super.onDestroy();

    }


}
