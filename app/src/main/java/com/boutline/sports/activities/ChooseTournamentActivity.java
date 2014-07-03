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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boutline.sports.adapters.TournamentsAdapter;
import com.boutline.sports.models.Tournament;
import com.boutline.sports.R;
//import com.tjeannin.apprate.AppRate;

import java.util.ArrayList;

public class ChooseTournamentActivity extends Activity {

	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tournaments);

        //set up the action bar
		actionBar = getActionBar();
        int actionBarHeight=0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        /*
        ImageView actionBg = (ImageView) findViewById(R.id.actionBarBG);
        actionBg.getLayoutParams().height = actionBarHeight + 10;
        actionBg.requestLayout();
        */
		TextView lblChooseTournament = (TextView)findViewById(R.id.lblChooseTournament);
		TextView lblBlankSlate = (TextView)findViewById(R.id.lblBlankSlate);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        // Set up the animations

        Animation fadeinAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeinAnim.setDuration(1000);
        fadeinAnim.setRepeatCount(1);
        fadeinAnim.setRepeatMode(1);
        container.startAnimation(fadeinAnim);

        // Set up the fonts

        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
		lblChooseTournament.setTypeface(btf);
		
			
		// Populate the List View
		
		ArrayList<Tournament> arrayOfTournaments = new ArrayList<Tournament>();
		Tournament tournament = new Tournament("123","Indian Premier League","30th June","6th July","#PepsiIPL", 2, true);
		arrayOfTournaments.add(tournament);
        Tournament tournament2 = new Tournament("124","FIFA World Cup","30th June","6th July","#PepsiIPL", 2, true);
        arrayOfTournaments.add(tournament2);
		if(arrayOfTournaments.size()==0){
			lblChooseTournament.setVisibility(View.INVISIBLE);
			lblBlankSlate.setVisibility(View.VISIBLE);
		}
		TournamentsAdapter adapter = new TournamentsAdapter(this, arrayOfTournaments);
		ListView listView = (ListView) findViewById(R.id.lvTournaments);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When clicked, show a toast with the TextView text
				Tournament tournament = (Tournament) parent.getItemAtPosition(position);
				
				Intent intent = new Intent(ChooseTournamentActivity.this,ChooseMatchActivity.class);
				intent.putExtra("tournamentId", tournament.getTournamentId());
				intent.putExtra("tournamentName",tournament.getTournamentName());
		        startActivity(intent);
		        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
				
			}
		});
		/*
		new AppRate(this)
	    .setMinDaysUntilPrompt(7)
	    .setMinLaunchesUntilPrompt(20)
	    .setShowIfAppHasCrashed(false)
	    .init();*/
		
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
	     getMenuInflater().inflate(R.menu.choosesport, menu);
	     return true;
	}
		 
	// Action Bar icon click listeners
		 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
		    case R.id.banter:
		       	Intent banterIntent = new Intent(ChooseTournamentActivity.this, BanterActivity.class);
		        startActivity(banterIntent);
		        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
		        return true;
		    case R.id.settings:
		       	Intent settingsIntent = new Intent(ChooseTournamentActivity.this, DummyDesignActivity.class);
		        startActivity(settingsIntent);
		        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
		    }
	}
	
}
