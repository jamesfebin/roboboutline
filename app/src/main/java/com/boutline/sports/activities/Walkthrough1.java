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
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boutline.sports.helpers.OnSwipeTouchListener;
import com.boutline.sports.R;

public class Walkthrough1 extends Activity implements OnTouchListener {

	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	Animation mouseAnim;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 
		// Set up UI
		super.onCreate(savedInstanceState);
		//TODO set activity to fullscreen mode
		setContentView(R.layout.activity_walkthrough1);
		
		//Set up fonts		
		
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		btf = Typeface.createFromAsset(getAssets(), boldFontPath);
		getActionBar().hide();
		
		// Define the controls
		
		RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
		TextView hdrWalkthrough1 = (TextView) findViewById(R.id.hdrWalkthrough1);
		TextView lblWalkthrough1 = (TextView) findViewById(R.id.lblWalkthrough1);
		final ImageView imgMouseImage = (ImageView) findViewById(R.id.imgMouseImage);
		
		
		// Assign the font types
		
		hdrWalkthrough1.setTypeface(btf);
		lblWalkthrough1.setTypeface(tf);
		
		// Animation of user's call to action
		
		TranslateAnimation animation = new TranslateAnimation(300.0f , 0.0f, 0.0f, 0.0f);
		animation.setDuration(1000);
		animation.setRepeatCount(2);
		animation.setRepeatMode(1);
		animation.setFillAfter(true);		
		imgMouseImage.startAnimation(animation);
				
		// Declare the function for gestures
		

		
		container.setOnTouchListener(new OnSwipeTouchListener(Walkthrough1.this) {
		    @Override
		    public void onSwipeLeft() {
		       goToNext();		        
		    }
		});

	}

	protected void goToNext(){
		  Intent mainIntent = new Intent(Walkthrough1.this,Walkthrough2.class);
          startActivity(mainIntent);
          finish();
          overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}
}
