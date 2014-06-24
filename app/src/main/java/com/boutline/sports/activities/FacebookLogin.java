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
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.boutline.sports.helpers.Mayday;
import com.boutline.sports.helpers.OnSwipeTouchListener;
import com.boutline.sports.helpers.SmoothProgressBar;
import com.boutline.sports.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.widget.LoginButton;

import java.util.Arrays;


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

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        // Set up Facebook


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
		
		// Check for mayday and show error if no internet
		
		Mayday chk = new Mayday(this);
		if(!chk.isConnectingToInternet()){
			errMessage="No internet connection. Try again.";
			error = false; //TODO set this to true later when connecting
			mProgressBar.progressiveStop();
		}
		
		//TODO Do all the login work here

        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this)
                    .setPermissions(Arrays.asList("public_profile"))
                    .setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }

		/*
		if(!error)
		{
			Intent mainIntent = new Intent(FacebookLogin.this,ChooseSportsActivity.class);
	        startActivity(mainIntent);
	        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
		}
		else
		{
			loginError(btnFacebookLogin, errMessage);
		}*/
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
	protected void onResume() {
		super.onResume();

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
	public void onBackPressed() {
		super.onBackPressed();
		goToPrev();		
	}

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
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

}
