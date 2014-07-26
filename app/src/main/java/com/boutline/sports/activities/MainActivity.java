/** 
 * Tests:
 *  No initial actionbar screen
 *  Show splash for 4 seconds -
 *  check if user previously has logged in
 *  slide in walkthrough 1 from right if first time -
 *  slide in choose tour activity if already logged in 
 *  dont show fb activity if already logged in
 *  show progressbar through this activity -
 */

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

import com.boutline.sports.application.Constants;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.helpers.SmoothProgressBar;
import com.boutline.sports.R;
import android.content.SharedPreferences.Editor;
import com.boutline.sports.application.MyDDPState;

import com.boutline.sports.jobs.Connect;
import com.boutline.sports.models.FacebookUserInfo;
import com.google.gson.Gson;

import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.path.android.jobqueue.JobManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;



public class MainActivity extends Activity {

    private static final String TAG = "Splash Screen";
    private BroadcastReceiver mReceiver = null;
    SharedPreferences preferences;
    public FacebookUserInfo fbUser=null;
    public MixpanelAPI mixpanel = null ;
    JobManager jobManager;
    private Context context;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Setup Progress Bar
		
		SmoothProgressBar mProgressBar;
		mProgressBar = (SmoothProgressBar) findViewById(R.id.progressBar);
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.progressiveStart();
		getActionBar().hide();

    }




	protected void goToWalkthrough0(){
		  Intent mainIntent = new Intent(MainActivity.this,Walkthrough0.class);
          startActivity(mainIntent);
          finish();
          overridePendingTransition(R.anim.pushupin, R.anim.pushupout);
	}
	
	protected void goToChooseTournament(){
		Intent mainIntent = new Intent(MainActivity.this,ChooseTournamentActivity.class);
        startActivity(mainIntent);
        finish();
        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
	}

    @Override
    protected void onDestroy() {

        if(mixpanel!=null)
            mixpanel.flush();
        super.onDestroy();

    }




    @Override
    protected void onResume() {





        super.onResume();

    context = getApplicationContext();


        mixpanel=MixpanelAPI.getInstance(getApplicationContext(), Constants.MIXPANEL_TOKEN);

        preferences = this.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);


        String storedFbInfoString = preferences.getString("fbUserInfo", null);
        Gson gson = new Gson();

       if(storedFbInfoString!=null) {
            fbUser = gson.fromJson(storedFbInfoString, FacebookUserInfo.class);
       }
            mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {
            @Override
            protected void onDDPConnect(DDPStateSingleton ddp) {
                super.onDDPConnect(ddp);

                if(fbUser==null){

                   goToWalkthrough0();
                    finish();

                }
                else{
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

                    Toast.makeText(getApplicationContext(),"GOT ACTION"+intent.getAction().toString(),Toast.LENGTH_SHORT).show();

                    Log.e("This is what i got", intent.getAction().toString());

                    if (intent.getAction().equals("LOGINSUCCESS"))
                    {

                        if(intent.hasExtra("userId")==true) {

                            Editor editor = preferences.edit();
                            editor.putString("boutlineUserId", intent.getStringExtra("userId"));
                            editor.commit();


                            if(mixpanel!=null) {
                                mixpanel.identify(intent.getStringExtra("userId"));
                                mixpanel.track("Boutline Login Success on SplashScreen", Constants.info);
                            }

                            boolean hasChoseSport=false;

                            if(!hasChoseSport)
                            {

                                Intent mainIntent = new Intent(MainActivity.this, ChooseSportsActivity.class);
                                startActivity(mainIntent);
                                overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                                finish();

                            }
                            else {

                                Intent mainIntent = new Intent(MainActivity.this, ChooseTournamentActivity.class);
                                startActivity(mainIntent);
                                overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                                finish();
                            }


                        }
                    }
                    else if(intent.getAction().equals("LOGINFAILED"))
                    {

                        Toast.makeText(getApplicationContext(),"Unable to connect to facebook",Toast.LENGTH_SHORT);

                        if(mixpanel!=null) {
                            mixpanel.track("Boutline Login Failed on SplashScreen", Constants.info);
                        }


                    }

                    else if(intent.getAction().equals(MyDDPState.MESSAGE_ERROR))
                    {


                        Intent mainIntent = new Intent(MainActivity.this, ChooseSportsActivity.class);
                        startActivity(mainIntent);
                        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                        finish();


                        Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT).show();

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


        if (MyDDPState.getInstance().getDDPState()== MyDDPState.DDPSTATE.Closed) {

           // Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT).show();

        }
        else if(MyDDPState.getInstance().getDDPState() == DDPStateSingleton.DDPSTATE.LoggedIn)
        {
            boolean hasChoseSport=false;

            if(!hasChoseSport)
            {

                Intent mainIntent = new Intent(MainActivity.this, ChooseSportsActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                finish();

            }
            else {

                Intent mainIntent = new Intent(MainActivity.this, ChooseTournamentActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                finish();
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


}
