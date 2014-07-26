/** 
 * Tests:
 *   Activity slides in from the right -
 *   Swiping right slides current activity to right to bring in last walkthrough - 
 *   Ensure activity loads within 3 seconds -
 *   Loading bar is displayed when login button is clicked -
 *   ActionBar does not exist -
 *  Activity in fullscreenmode 
 *   Back button slides activity right to last walkthrough -
 *   Correct error message is shown when fblogin clicked and no internet -
 *  Take a screenshot of the activity
 *   Fonts are defined and assigned -
 *   Correct readable error message shown if fblogin fails -
 *  fblogin works on 2g
 *   links to pp and tos take to new webview -
 *   fbdisclaimer exists -
 *  fblogin click logins in user and sets the sharedpref
 *  If first time user go to choosesport else go to choose tour
 *   fbloginbtn changes label and shows transition for click -
 *   Finish this activity if login successful
 */

package com.boutline.sports.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.application.Constants;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.helpers.Mayday;
import com.boutline.sports.helpers.OnSwipeTouchListener;
import com.boutline.sports.helpers.SmoothProgressBar;
import com.boutline.sports.R;
import com.boutline.sports.jobs.Login;
import com.boutline.sports.models.FacebookUserInfo;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keysolutions.ddpclient.DDPListener;

import android.content.SharedPreferences.Editor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import com.boutline.sports.application.MyDDPState;

import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.path.android.jobqueue.JobManager;

public class FacebookLogin extends Activity {

	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
    private Facebook facebook;
    private String TAG = "FacebookLogin";
    Animation mouseAnim;
	SmoothProgressBar mProgressBar;

    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback statusCallback =
    new SessionStatusCallback();
    public FacebookUserInfo fbUser = new FacebookUserInfo();
    SharedPreferences preferences;
    private BroadcastReceiver mReceiver;

    private Context context;

    public MixpanelAPI mixpanel = null ;

    JobManager jobManager;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        // Set up UI

		setContentView(R.layout.activity_facebooklogin);
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		btf = Typeface.createFromAsset(getAssets(), boldFontPath);
		getActionBar().hide();

		// Define the controls
		RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
		final Button btnFacebookLogin = (Button) findViewById(R.id.btnFacebookLogin);
		TextView tosnpp = (TextView) findViewById(R.id.tosnpp);
		TextView fbDisclaimer = (TextView) findViewById(R.id.fbDisclaimer);
		tosnpp.setText(Html.fromHtml(getString(R.string.tosnpp)));
		tosnpp.setMovementMethod(LinkMovementMethod.getInstance());
        preferences = this.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
		// Assign the font types

		btnFacebookLogin.setTypeface(btf);
		tosnpp.setTypeface(tf);
		fbDisclaimer.setTypeface(btf);

		//Declare the listeners for swipe right and click

		container.setOnTouchListener(new OnSwipeTouchListener(FacebookLogin.this) {
		    @Override
		    public void onSwipeRight() {
		       goToPrev();
		    }
		});

		btnFacebookLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				btnFacebookLogin.setText("Logging in...");
				loginWithFacebook(btnFacebookLogin);
			}

		});

	}

	// perform FB login checks here

	protected void loginWithFacebook(Button btnFacebookLogin) {

		// Show Progress Bar

		mProgressBar = (SmoothProgressBar) findViewById(R.id.progressBar);
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.progressiveStart();

		Boolean error=false;
		String errMessage = "Problems signing in. Try again.";



        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this)
                    .setPermissions(Arrays.asList("public_profile","email"))
                    .setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }


	}

	protected void loginError(Button btnFacebookLogin, String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		btnFacebookLogin.setText("LOGIN WITH FACEBOOK");
	}

	protected void goToPrev(){
		Intent mainIntent = new Intent(FacebookLogin.this,Walkthrough3.class);
        startActivity(mainIntent);
        finish();
        overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
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

     jobManager = MyApplication.getInstance().getJobManager();

     context = getApplicationContext();
     mixpanel=MixpanelAPI.getInstance(getApplicationContext(), Constants.MIXPANEL_TOKEN);

        mReceiver = new BroadcastReceiver() {


            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getExtras();
                if (intent.getAction().equals("LOGINSUCCESS"))
                {




                        if(mixpanel!=null) {
                            mixpanel.identify(intent.getStringExtra("userId"));
                            mixpanel.track("Boutline Login Success on FB Login Screen", Constants.info);
                        }

                        Intent mainIntent = new Intent(FacebookLogin.this,ChooseSportsActivity.class);
                        startActivity(mainIntent);
                        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                        finish();



                }
                else if(intent.getAction().equals("LOGINFAILED"))
                {

                    Toast.makeText(getApplicationContext(),"Unable to login via facebook",Toast.LENGTH_SHORT);

                    if(mixpanel!=null) {

                        mixpanel.track("Boutline Login Failed on FB Login Screen", Constants.info);

                    }

                }

                else if(intent.getAction().equals(MyDDPState.MESSAGE_ERROR))
                {
                    Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT);
                }


            }



        };



        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("LOGINSUCCESS"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("LOGINFAILED"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(MyDDPState.MESSAGE_ERROR));


        // we want connection state change messages so we know we're logged in
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(MyDDPState.MESSAGE_CONNECTION));

        LoginButton btnFacebookLogin = (LoginButton) findViewById(R.id.btnFacebookLogin);
		btnFacebookLogin.setText("LOGIN WITH FACEBOOK");
        btnFacebookLogin.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));
		mProgressBar = (SmoothProgressBar) findViewById(R.id.progressBar);
		mProgressBar.setVisibility(View.INVISIBLE);
		mProgressBar.progressiveStop();

        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
            mReceiver = null;
        }

    }




    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {


            long currentUnixTime = System.currentTimeMillis() / 1000L;


            fbUser.accessToken = session.getAccessToken();
            fbUser.expiresIn  = session.getExpirationDate().getTime() / 1000L -currentUnixTime;
            LoginButton btnFacebookLogin = (LoginButton) findViewById(R.id.btnFacebookLogin);

            btnFacebookLogin.setText("Please wait , logging in");


            makeFacebookRequests(session);




        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            // Respond to session state changes, ex: updating the view
        }
    }

    private void getFbUserInfo(GraphUser user) {


        if (user.getProperty("email") != null) {
            Log.i("Email is ",user.getProperty("email").toString());
            fbUser.email = user.getProperty("email").toString();
        }
        else
        {
            fbUser.email = "";
        }
        fbUser.id = user.getId();
        fbUser.link = user.getLink();
        fbUser.name = user.getName();
        fbUser.first_name = user.getFirstName();
        fbUser.last_name = user.getLastName();
        fbUser.gender = user.asMap().get("gender").toString();
        fbUser.locale = user.getProperty("locale").toString();



    }

    private Bundle getUserPicRequestParameters() {
        Bundle parameters = new Bundle(4);
        parameters.putBoolean("redirect", false);
        parameters.putString("type", "normal");
        parameters.putInt("width", 200);
        parameters.putInt("height", 200);

        return parameters;
    }

    private Bundle getUserAgeParameters() {
        Bundle parameters = new Bundle(1);
        parameters.putString("fields", "age_range");
        return parameters;
    }
    private void makeFacebookRequests(final Session session)
    {


        RequestBatch requestBatch = new RequestBatch();

        requestBatch.add(new Request(
                session,
                "/me/picture",
                getUserPicRequestParameters(),
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {


                        try {
                            JSONObject x = response.getGraphObject().getInnerJSONObject();

                            JSONObject datObj = x.has("data") ? (JSONObject) x.get("data"):null;
                            fbUser.userPicUrl = datObj.getString("url");

                        } catch (JSONException e1) {

                            e1.printStackTrace();
                        }
                        catch(Exception E)
                        {
                            E.printStackTrace();

                        }
                    }
                }
        ));



         requestBatch.add(new Request(
                 session,
                 "/me",
                 getUserAgeParameters(),
                 HttpMethod.GET,
                 new Request.Callback() {
                     public void onCompleted(Response response) {


                         try {
                             JSONObject x = response.getGraphObject().getInnerJSONObject();
                             fbUser.age_range = x.get("age_range");

                         } catch (JSONException e1) {
                             e1.printStackTrace();
                         }
                         catch (Exception e)
                         {
                             e.printStackTrace();
                         }
                     }
                 }
         ));


        Request userInfoRequest = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        try
                        {
                            if (session == Session.getActiveSession()) {
                                if (user != null) {

                                    getFbUserInfo(user);
                                    storeFbInfo();

                                }
                            }
                            if (response.getError() != null) {

                                Toast.makeText(getApplicationContext(),"Facebook Connection Error, Try again",Toast.LENGTH_SHORT);

                            }
                        }
                        catch (Exception E)
                        {
                            E.printStackTrace();
                        }
                    }
                });
        requestBatch.add(userInfoRequest);
        requestBatch.executeAsync();



    }


    private void storeFbInfo()
    {

        Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(fbUser);
        editor.putString("fbUserInfo",json);
        editor.commit();

        jobManager.addJobInBackground(new Login());


    }



}
