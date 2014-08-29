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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.boutline.sports.R;
import com.boutline.sports.application.Constants;
import com.boutline.sports.application.MyDDPState;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class ForgotPasswordActivity extends Activity {

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;
    BroadcastReceiver mReceiver;
    private ProgressDialog progress = null;
    public MixpanelAPI mixpanel=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        ImageButton go  = (ImageButton) findViewById(R.id.btnUpdate);
        final EditText forgotPasswordEditText = (EditText) findViewById(R.id.txtEmailId);
        TextView lblForgotPassword = (TextView)findViewById(R.id.lblForgotPassword);
        TextView forgotpwdguide = (TextView)findViewById(R.id.forgotpwdguide);
        mixpanel= MixpanelAPI.getInstance(getApplicationContext(), Constants.MIXPANEL_TOKEN);

        //set up fonts
        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
        forgotPasswordEditText.setTypeface(tf);
        forgotpwdguide.setTypeface(tf);
        lblForgotPassword.setTypeface(btf);
        progress = new ProgressDialog(this);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(MyDDPState.getInstance().mDDPState== DDPStateSingleton.DDPSTATE.Closed)
                {
                    Toast.makeText(getApplicationContext(),"Internet connection not available",Toast.LENGTH_SHORT).show();
                    MyDDPState.getInstance().connectIfNeeded();
                    return;
                }
                if(!forgotPasswordEditText.getText().toString().matches(""))
                {

                  if(forgotPasswordEditText.getText().toString().contains("@")&&forgotPasswordEditText.getText().toString().contains(".")) {


                      progress.show();
                      MyDDPState.getInstance().forgotPassword(forgotPasswordEditText.getText().toString());

                      }
                    else
                  {

                      Toast.makeText(getApplicationContext(),"Please Enter Valid Email Id",Toast.LENGTH_SHORT).show();

                  }



                }
                else
                {

                    Toast.makeText(getApplicationContext(),"Please provide your boutline email",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {

            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if(intent.getAction().equals("EMAILSEND")) {
                    if(progress!=null)
                    {
                        progress.dismiss();
                    }

                    if(mixpanel!=null)
                    {
                        mixpanel.track("Reset Password, Email Send",Constants.info);
                    }
                    Toast.makeText(getApplicationContext(),"Password reset instructions has been send to your email",Toast.LENGTH_SHORT).show();
                }
                else if(intent.getAction().equals("EMAILFAILED"))
                {
                    if(progress!=null)
                    {
                        progress.dismiss();
                    }
                    if(mixpanel!=null)
                    {
                        mixpanel.track("Reset Password , Email Address Doesn't Exist ",Constants.info);
                    }
                    Toast.makeText(getApplicationContext(),intent.getExtras().getString("Error"),Toast.LENGTH_SHORT).show();
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("EMAILSEND"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter("EMAILFAILED"));
    }


    @Override
    protected void onDestroy() {

        if(mixpanel!=null)
            mixpanel.flush();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
    }
}
