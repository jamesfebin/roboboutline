/** 
 * Tests:
 *   Activity slides in from the right - 
 *   Ensure activity loads within 3 seconds -  
 *  Loading bar is displayed till the view is populated
 *  List of tournaments populated in most-recent-first order
 *   Each tournament has name, start date to end date, pic, follow buttons -
 *   ActionBar exists with icons for banter, settings -
 *  Each tournament when clicked leads to the correct matches activity
 *   Back button slides activity right to matches -
 *   Correct attributes of Tournament class exist - 
 *  Correct error message toast is displayed if no internet on activity load
 *  Drawer menu opens and is visible and has caret
 *  All links in drawer menu lead to correct pages
 *  Take a screenshot of the activity with and without drawer  
 *  Cache tournaments for each section and keep in local storage
 *   Toggling follow buttons changes icon -
 *   Fonts are defined and assigned -
 *   Blank slate message if no tournaments are found -
 *   Each tournament has the corresponding sport icon -
 */

package com.boutline.sports.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boutline.sports.R;
import com.boutline.sports.adapters.TournamentsAdapter;
import com.boutline.sports.models.Tournament;

import java.util.ArrayList;

//import com.tjeannin.apprate.AppRate;

public class DummyDesignActivity extends Activity {

	public String fontPath = "fonts/museo.otf";
	public Typeface tf;
	public String boldFontPath = "fonts/museobold.otf";
	public Typeface btf;
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dummydesign);

        //set up the action bar
		actionBar = getActionBar();
        int actionBarHeight=0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        ImageView actionBg = (ImageView) findViewById(R.id.actionBarBG);
        actionBg.getLayoutParams().height = actionBarHeight + 10;
        actionBg.requestLayout();

		tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
		
			

		
	}	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}
	
	// inflate the menu assigned for this page and set click listeners

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	     getMenuInflater().inflate(R.menu.main, menu);
	     return true;
	}
	
}
