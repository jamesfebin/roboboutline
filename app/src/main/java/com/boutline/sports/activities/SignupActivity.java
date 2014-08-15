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
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;

import com.boutline.sports.R;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.helpers.OnSwipeTouchListener;
import android.content.BroadcastReceiver;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;

public class SignupActivity extends Activity {

    public String fontPath = "fonts/sharp.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/sharpsemibold.ttf";
    public Typeface btf;

    private ProgressDialog progress = null;
    BroadcastReceiver mReceiver = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //Set up fonts

        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
      //  getActionBar().hide();

        TextView login = (TextView) findViewById(R.id.lblLogin);

        final EditText usernameEditText = (EditText) findViewById(R.id.txtSignupUsername);
        final EditText passwordEditText = (EditText) findViewById(R.id.txtSignupPassword);
        final EditText emailEditText = (EditText) findViewById(R.id.txtSignupEmailId);
        progress = new ProgressDialog(this);



        usernameEditText.setText("jamesfebin");
        passwordEditText.setText("fbnonae()");
        emailEditText.setText("jamesfebin@gmail.com");
        Button signup = (Button) findViewById(R.id.SignupBtn);

        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           String username = usernameEditText.getText().toString();
           String password = passwordEditText.getText().toString();
           String email = emailEditText.getText().toString();


                if(username.length()==0)
                {

                    Toast.makeText(getApplicationContext(),"Please input your username",Toast.LENGTH_SHORT).show();

                }
                else if(password.length()==0)
                {

                    Toast.makeText(getApplicationContext(),"Please input password",Toast.LENGTH_SHORT).show();

                }
                else if(username.contains(" "))
                {

                    Toast.makeText(getApplicationContext(),"Username cannot contains spaces",Toast.LENGTH_SHORT).show();


                }
                else if(email.contains(" "))
                {

                    Toast.makeText(getApplicationContext(),"Email Id cannot contains spaces",Toast.LENGTH_SHORT).show();

                }
                else if(password.length()<8)
                {

                    Toast.makeText(getApplicationContext(),"Password must be a minimum of 8 characters",Toast.LENGTH_SHORT).show();
                }
                else if(email.length()==0)
                {

                    Toast.makeText(getApplicationContext(),"Please input email",Toast.LENGTH_SHORT).show();
                }
                else if(email.contains("@")==false||email.contains(".")==false)
                {

                    Toast.makeText(getApplicationContext(),"Please input valid email",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progress.setTitle("Signing Up");
                    progress.show();
                    MyDDPState.getInstance().createUser(username, password, email);
                }

            }
        });



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
    protected void onResume() {
        super.onResume();



        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {

            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);

                Bundle bundle = intent.getExtras();

                if(intent.getAction().equals("REGISTRATIONSUCCESS")) {

                    if(progress!=null)
                    {
                        progress.dismiss();
                    }

                    Intent profileIntent = new Intent(SignupActivity.this,CreateProfileActivity.class);
                    startActivity(profileIntent);
                    finish();

                }
                else if(intent.getAction().equals("REGISTRATIONFAILED"))
                {
                    if(progress!=null)
                    {
                        progress.dismiss();
                    }

                    Toast.makeText(getApplicationContext(),intent.getExtras().getString("Error"),Toast.LENGTH_SHORT).show();

                }

            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("REGISTRATIONSUCCESS"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("REGISTRATIONFAILED"));
    }
}
