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
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boutline.sports.R;
import com.boutline.sports.helpers.OnSwipeTouchListener;

public class Walkthrough0 extends Activity {

	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough0);

        //Set up fonts

        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
        getActionBar().hide();

        // Define the controls

        RelativeLayout container =(RelativeLayout) findViewById(R.id.container);
        TextView hdrWalkthrough0 = (TextView) findViewById(R.id.hdrWalkthrough0);
        TextView swiper = (TextView) findViewById(R.id.swiper);
        TextView lblWalkthrough0 = (TextView) findViewById(R.id.lblWalkthrough0);
        TextView subhdrWalkthrough0 = (TextView) findViewById(R.id.subhdrWalkthrough0);
        ImageView imgWalkthrough0 = (ImageView) findViewById(R.id.imgWalkthrough0);

        // Assign the font types
        swiper.setTypeface(tf);
        subhdrWalkthrough0.setTypeface(btf);
        hdrWalkthrough0.setTypeface(btf);
        lblWalkthrough0.setTypeface(btf);

        // Animations


        Animation walkthroughAnim = AnimationUtils.loadAnimation(this, R.anim.scalein);
        walkthroughAnim.setDuration(1000);
        walkthroughAnim.setRepeatCount(1);
        walkthroughAnim.setRepeatMode(1);
        imgWalkthrough0.startAnimation(walkthroughAnim);

        Animation walkthroughAnim2 = AnimationUtils.loadAnimation(this, R.anim.fadein);
        walkthroughAnim2.setDuration(500);
        walkthroughAnim2.setRepeatCount(1);
        walkthroughAnim2.setRepeatMode(1);
        lblWalkthrough0.startAnimation(walkthroughAnim2);
        hdrWalkthrough0.startAnimation(walkthroughAnim2);

        // Declare the function for gestures

        container.setOnTouchListener(new OnSwipeTouchListener(Walkthrough0.this) {
            @Override
            public void onSwipeLeft() {
                goToNext();
            }
            @Override
            public void onSwipeRight() {
            }
        });
	}

	protected void goToNext(){
		  Intent mainIntent = new Intent(Walkthrough0.this,Walkthrough1.class);
          startActivity(mainIntent);
          finish();
          overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}
}
