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
import android.view.animation.AnimationUtils;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 
		// Set up UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_walkthrough1);

		//Set up fonts		
		
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		btf = Typeface.createFromAsset(getAssets(), boldFontPath);
		getActionBar().hide();
		
		// Define the controls
		
		RelativeLayout container =(RelativeLayout) findViewById(R.id.container);
		TextView hdrWalkthrough1 = (TextView) findViewById(R.id.hdrWalkthrough1);
		TextView lblWalkthrough1 = (TextView) findViewById(R.id.lblWalkthrough1);
		
		// Assign the font types
		
		hdrWalkthrough1.setTypeface(btf);
		lblWalkthrough1.setTypeface(btf);
		
		// Animations

        ImageView imgWalkthrough1 = (ImageView) findViewById(R.id.imgWalkthrough1);
        Animation walkthroughAnim = AnimationUtils.loadAnimation(this, R.anim.walkthroughanim);
        walkthroughAnim.setDuration(500);
        walkthroughAnim.setRepeatCount(1);
        walkthroughAnim.setRepeatMode(1);
        walkthroughAnim.setZAdjustment(1);
        imgWalkthrough1.startAnimation(walkthroughAnim);

        Animation walkthroughAnim2 = AnimationUtils.loadAnimation(this, R.anim.fadein);
        walkthroughAnim2.setDuration(1000);
        walkthroughAnim2.setRepeatCount(1);
        walkthroughAnim2.setRepeatMode(1);
        lblWalkthrough1.startAnimation(walkthroughAnim2);
        hdrWalkthrough1.startAnimation(walkthroughAnim2);

		// Declare the function for gestures

		container.setOnTouchListener(new OnSwipeTouchListener(Walkthrough1.this) {
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

    protected void goToPrev(){
        Intent mainIntent = new Intent(Walkthrough1.this,Walkthrough0.class);
        startActivity(mainIntent);
        finish();
        overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
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
