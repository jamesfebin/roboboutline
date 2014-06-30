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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import com.boutline.sports.helpers.SmoothProgressBar;
import com.boutline.sports.R;
import android.content.BroadcastReceiver;

import android.support.v4.content.LocalBroadcastManager;
import com.boutline.sports.application.MyDDPState;

import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;


public class MainActivity extends Activity {

    private BroadcastReceiver mReceiver;


    // TODO Declare Shared Preferences Check for User Details
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
		//TODO Do the background connection to server and check if user logged in

	}


	protected void goToWalkthrough1(){
		  Intent mainIntent = new Intent(MainActivity.this,Walkthrough1.class);
          startActivity(mainIntent);
          finish();
          overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
	}
	
	protected void goToChooseTournament(){
		Intent mainIntent = new Intent(MainActivity.this,ChooseTournamentActivity.class);
        startActivity(mainIntent);
        finish();
        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
	}

    @Override
    protected void onResume() {
        super.onResume();

        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {
            @Override
            protected void onDDPConnect(DDPStateSingleton ddp) {
                super.onDDPConnect(ddp);

                final Boolean isUserLoggedIn = false;

                if(!isUserLoggedIn){
                   goToWalkthrough1();
                }
                else{
                    goToChooseTournament();
                }

            }

        };


        if (MyDDPState.getInstance().getState() == MyDDPState.DDPSTATE.Closed) {
            showError( "Internet connection not available");
        }

        }

    private void showError(String msg) {


        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();



    }




}
