/** 
 * Tests:
 *  Activity slides in from the right -
 *  Ensure activity loads within 3 seconds - 
 *  ActionBar does not exist -
 *  Activity in fullscreen mode
 *  Back button slide wt1 from the left -
 *  Take a screenshot of the activity with and without drawer  
 *  Fonts are defined and assigned -
 *  swiping left should bring next wtactivity3 from right -
 *  swiping right should bring prev wtactivity1 from left -
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

public class Walkthrough2 extends Activity {

	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// Set up UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_walkthrough2);
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		btf = Typeface.createFromAsset(getAssets(), boldFontPath);
		getActionBar().hide();
		
		// Define the controls
		
		RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
		TextView hdrWalkthrough2 = (TextView) findViewById(R.id.hdrWalkthrough2);
		TextView lblWalkthrough2 = (TextView) findViewById(R.id.lblWalkthrough2);
		
		// Assign the font types
		
		hdrWalkthrough2.setTypeface(btf);
		lblWalkthrough2.setTypeface(tf);
		
		// Declare the function for swipe left action
		
		container.setOnTouchListener(new OnSwipeTouchListener(Walkthrough2.this) {
		    @Override
		    public void onSwipeLeft() {
		       goToNext();
		        
		    }
		    @Override
		    public void onSwipeRight() {
		       goToPrev();
		        
		    }
		});
	
	}

	protected void goToNext(){
		  Intent mainIntent = new Intent(Walkthrough2.this,Walkthrough3.class);
          startActivity(mainIntent);
          finish();
          overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
	}
	
	protected void goToPrev(){
		  Intent mainIntent = new Intent(Walkthrough2.this,Walkthrough1.class);
        startActivity(mainIntent);
        finish();
        overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		 goToPrev();
	}

}