/**
 * Tests:
 *  Activity slides in from the right -
 *  Ensure activity loads within 3 seconds -
 *  ActionBar does not exist - 
 *  Activity in fullscreen mode 
 *  slide to next should be animated and shown -
 *  Back button closes the app -
 *  Take a screenshot of the activity with and without drawer  
 *  Fonts are defined and assigned -
 *  swiping left should bring next wtactivity2 from right -
 */

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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.R;
import com.boutline.sports.application.Constants;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.application.MyDDPState;

import com.boutline.sports.helpers.OnSwipeTouchListener;
import com.boutline.sports.jobs.Login;
import com.boutline.sports.models.Conversation;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.path.android.jobqueue.JobManager;

public class LoginActivity extends Activity {

    public String fontPath = "fonts/sharp.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/sharpsemibold.ttf";
    public Typeface btf;
    JobManager jobManager;
    SharedPreferences preferences;
    public MixpanelAPI mixpanel = null ;
    private BroadcastReceiver mReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Set up fonts

        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
//        getActionBar().hide();

        mixpanel=MixpanelAPI.getInstance(getApplicationContext(), Constants.MIXPANEL_TOKEN);


        Button loginButton = (Button) findViewById(R.id.LoginBtn);
        preferences = getApplicationContext().getSharedPreferences("boutlineData", Context.MODE_PRIVATE);

        final EditText emailEditText = (EditText) findViewById(R.id.txtUsername);
        final EditText passwordEditText = (EditText) findViewById(R.id.txtPassword);
        TextView signup = (TextView) findViewById(R.id.lblSignup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


             Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
             startActivity(intent);

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email =  emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();


                if(email=="")
                {

                    Toast.makeText(getApplicationContext(),"Enter Email Id",Toast.LENGTH_SHORT).show();
                }
                else if(password=="")
                {

                    Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_SHORT).show();
                }
                else
                {


                MyDDPState.getInstance().boutlineFirstTimeLogin(email,password);

                }



            }
        });


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

               // Toast.makeText(getApplicationContext(),"GOT ACTION"+intent.getAction().toString(),Toast.LENGTH_SHORT).show();

              //  Log.e("This is what i got", intent.getAction().toString());


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

                        Intent banterIntent = new Intent(LoginActivity.this,ConversationActivity.class);
                        banterIntent.putExtra("conversationId","Q83GjTwRCk4FNTSEJ");
                        startActivity(banterIntent);


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
