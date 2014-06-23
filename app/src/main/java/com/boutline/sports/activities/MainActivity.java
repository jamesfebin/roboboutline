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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.boutline.sports.helpers.SmoothProgressBar;
import com.boutline.sports.R;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Setup Progress Bar
		
		SmoothProgressBar mProgressBar;
		mProgressBar = (SmoothProgressBar) findViewById(R.id.progressBar);
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.progressiveStart();
				
		final Boolean isUserLoggedIn = false;
		getActionBar().hide();
		//TODO Do the background connection to server and check if user logged in
		new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
            	if(!isUserLoggedIn){
            		goToWalkthrough1(); 
            	}
            	else{
            		goToChooseTournament();
            	}
            }
        }, 4000);
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
}
