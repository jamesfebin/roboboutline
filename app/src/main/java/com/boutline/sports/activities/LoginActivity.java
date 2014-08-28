package com.boutline.sports.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.R;
import com.boutline.sports.application.Constants;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.helpers.OnSwipeTouchListener;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.path.android.jobqueue.JobManager;

public class LoginActivity extends Activity {

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;
    JobManager jobManager;
    SharedPreferences preferences;
    public MixpanelAPI mixpanel = null ;
    private BroadcastReceiver mReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mixpanel=MixpanelAPI.getInstance(getApplicationContext(), Constants.MIXPANEL_TOKEN);

        //define the controls
        ImageButton loginButton = (ImageButton) findViewById(R.id.LoginBtn);
        preferences = getApplicationContext().getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
        final EditText emailEditText = (EditText) findViewById(R.id.txtUsername);
        final EditText passwordEditText = (EditText) findViewById(R.id.txtPassword);
        TextView boutline = (TextView) findViewById(R.id.boutline);
        TextView signup = (TextView) findViewById(R.id.lblSignup);
        TextView login = (TextView) findViewById(R.id.lblLogin);
        TextView problems = (TextView) findViewById(R.id.problems);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        //Set up fonts
        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
        emailEditText.setTypeface(tf);
        passwordEditText.setTypeface(tf);
        signup.setTypeface(tf);
        problems.setTypeface(tf);
        login.setTypeface(tf);
        boutline.setTypeface(btf);

        Animation walkthroughAnim = AnimationUtils.loadAnimation(this, R.anim.hovering);
        walkthroughAnim.setZAdjustment(1);
        logo.startAnimation(walkthroughAnim);

        container.setOnTouchListener(new OnSwipeTouchListener(LoginActivity.this) {
            @Override
            public void onSwipeLeft() {  }
            @Override
            public void onSwipeRight() {
                goToPrev();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
             startActivity(intent);
             overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email =  emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(email.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter Email Id",Toast.LENGTH_SHORT).show();
                }
                else if(password.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    MyDDPState.getInstance().boutlineFirstTimeLogin(email,password);
                }
            }
        });
        problems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(mixpanel!=null)
            mixpanel.flush();
        super.onDestroy();
    }

    protected void goToPrev(){
        Intent mainIntent = new Intent(LoginActivity.this,Walkthrough3.class);
        startActivity(mainIntent);
        finish();
        overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {

            @Override
            protected void onError(String title, String msg) {
                // Do Nothing
            }

            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                Bundle bundle = intent.getExtras();

                if (intent.getAction().equals("LOGINSUCCESS"))
                {
                    if(intent.hasExtra("userId")==true) {

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("boutlineUserId", intent.getStringExtra("userId"));
                        editor.commit();
                        if(mixpanel!=null) {
                            mixpanel.identify(intent.getStringExtra("userId"));
                            mixpanel.track("Boutline Login Success on LoginScreen", Constants.info);
                        }

                        Intent banterIntent = new Intent(LoginActivity.this,CreateProfileActivity.class);
                        startActivity(banterIntent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        finish();
                    }
                }
                else if(intent.getAction().equals("LOGINFAILED"))
                {
                    Toast.makeText(getApplicationContext(), "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                    if(mixpanel!=null) {
                        mixpanel.track("Invalid Login Credentials", Constants.info);
                    }
                }

                else if(intent.getAction().equals(MyDDPState.MESSAGE_ERROR))
                {
                    Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT).show();
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
