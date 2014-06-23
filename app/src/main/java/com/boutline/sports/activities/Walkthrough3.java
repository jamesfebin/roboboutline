/** 
 * Tests:
 *  Activity slides in from the right -
 *  Ensure activity loads within 3 seconds -
 *  ActionBar does not exist -
 *  Activity in fullscreen mode
 *  Back button slide wt2 from the left - 
 *  Take a screenshot of the activity with and without drawer  
 *  Fonts are defined and assigned - 
 *  swiping left should bring next fblogin from right - 
 *  swiping right should bring prev wtactivity2 from left -
 */

package com.boutline.sports.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boutline.sports.helpers.OnSwipeTouchListener;
import com.boutline.sports.R;

public class Walkthrough3 extends Activity {

	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// Set up UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_walkthrough3);
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		btf = Typeface.createFromAsset(getAssets(), boldFontPath);
		getActionBar().hide();
		
		// Define the controls
		
		RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
		TextView hdrWalkthrough3 = (TextView) findViewById(R.id.hdrWalkthrough3);
		TextView lblWalkthrough3 = (TextView) findViewById(R.id.lblWalkthrough3);
		
		// Assign the font types
		
		hdrWalkthrough3.setTypeface(btf);
		lblWalkthrough3.setTypeface(tf);
		
		// Declare the function for swipe left action
		
		container.setOnTouchListener(new OnSwipeTouchListener(Walkthrough3.this) {
		    @Override
		    public void onSwipeLeft() {
		       goToNext();		        
		    }
		    public void onSwipeRight() {
			       goToPrev();		        
			    }
		});
	}
	
	protected void goToNext(){
		  Intent mainIntent = new Intent(Walkthrough3.this,FacebookLogin.class);
          startActivity(mainIntent);
          finish();
          overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
	}
	
	protected void goToPrev(){
		  Intent mainIntent = new Intent(Walkthrough3.this,Walkthrough2.class);
        startActivity(mainIntent);
        finish();
        overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		goToPrev();
	}

}
