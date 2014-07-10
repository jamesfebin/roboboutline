/** 
 * Tests:
 *   Activity slides in from the right - 
 *   Ensure activity loads within 3 seconds -
 *  Loading bar is displayed till the view is populated
 *  Tournament board link exists
 *  List of matches populated in coming-up-next order
 *  Each match has name, venue, datetime, teamApic, teamBpic
 *  ActionBar exists with icons for banter, settings, caret
 *  Each match when clicked leads to the correct board
 *  Back button slides activity right to matches 
 *  Correct attributes of Match class exist 
 *  Correct error message toast is displayed if no internet on activity load
 *  Drawer menu opens and is visible
 *  All links in drawer menu lead to correct pages
 *  Take a screenshot of the activity with and without drawer  
 *  Cache matches and show using java code only upcoming matches
 *  Fonts are defined and assigned
 *  Infinite scroll if time permits
 *  Tournament, Live matches, and upcoming matches in seperate sections
 *  Tournament Hashtag titlebar
 */

package com.boutline.sports.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boutline.sports.adapters.LiveMatchesAdapter;
import com.boutline.sports.models.Match;
import com.boutline.sports.R;

import java.util.ArrayList;


public class ChooseMatchActivity extends Activity {

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matches);

        //Declare the controls
        TextView lblTournamentName = (TextView) findViewById(R.id.lblTournamentName);
        TextView lblTournamentStartTime = (TextView) findViewById(R.id.lblTournamentStartTime);
        TextView lblLiveMatches = (TextView) findViewById(R.id.lblLiveMatches);
        TextView lblUpcomingMatches = (TextView) findViewById(R.id.lblUpcomingMatches);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        TextView lblLeftSports = (TextView) findViewById(R.id.lblLeftSports);
        TextView lblLeftTournaments = (TextView) findViewById(R.id.lblLeftTournaments);
        TextView lblLeftSchedule = (TextView) findViewById(R.id.lblLeftSchedule);
        TextView lblLeftProfile = (TextView) findViewById(R.id.lblLeftProfile);
        TextView lblLeftLogout = (TextView) findViewById(R.id.lblLeftLogout);
        TextView lblProfileName = (TextView) findViewById(R.id.lblProfileName);

        // Set up the animations

        Animation fadeinAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeinAnim.setDuration(1000);
        fadeinAnim.setRepeatCount(1);
        fadeinAnim.setRepeatMode(1);
        container.startAnimation(fadeinAnim);

        //Set up fonts

        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);

		// Populate the List View
		
		ArrayList<Match> arrayOfMatches = new ArrayList<Match>();
		Match match = new Match("123","GER vs POR","4th July 9:30PM","4th July 11:30PM","Emirates Stadium","GERvsPOR","Salvador","123","123");
		arrayOfMatches.add(match);
		LiveMatchesAdapter liveMatchesAdapter = new LiveMatchesAdapter(this, arrayOfMatches);
		ListView lvLiveMatches = (ListView) findViewById(R.id.lvLiveMatches);
		ListView lvUpcomingMatches = (ListView) findViewById(R.id.lvUpcomingMatches);
		RelativeLayout tourDetails = (RelativeLayout) findViewById(R.id.tourDetails);
		lvLiveMatches.setAdapter(liveMatchesAdapter);
		
		ArrayList<Match> arrayOfUpcomingMatches = new ArrayList<Match>();
		Match match2 = new Match("123","123","123","123","123","123","123","123","123");
		arrayOfUpcomingMatches.add(match2);
		LiveMatchesAdapter upcomingMatchesAdapter = new LiveMatchesAdapter(this, arrayOfMatches);
		lvLiveMatches.setAdapter(upcomingMatchesAdapter);

        // Assign the font types

        lblTournamentName.setTypeface(btf);
        lblTournamentStartTime.setTypeface(btf);
        lblLiveMatches.setTypeface(btf);
        lblLeftSports.setTypeface(btf);
        lblLeftTournaments.setTypeface(btf);
        lblLeftSchedule.setTypeface(btf);
        lblLeftProfile.setTypeface(btf);
        lblLeftLogout.setTypeface(btf);
        lblProfileName.setTypeface(btf);
        lblUpcomingMatches.setTypeface(btf);

        // Set all the listeners
		
		tourDetails.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ChooseMatchActivity.this,BoardActivity.class);
				//TODO take user to tour board
		        startActivity(intent);
		        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
			}
		});
		
		lvLiveMatches.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When clicked, show a toast with the TextView text
				Match match = (Match) parent.getItemAtPosition(position);
				Intent intent = new Intent(ChooseMatchActivity.this,BoardActivity.class);
				intent.putExtra("matchId", match.getMatchId());
				intent.putExtra("matchName",match.getMatchName());
		        startActivity(intent);
		        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
				
			}
		});
		
		lvUpcomingMatches.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When clicked, show a toast with the TextView text
				Match match = (Match) parent.getItemAtPosition(position);
				Intent intent = new Intent(ChooseMatchActivity.this,BoardActivity.class);
				intent.putExtra("matchId", match.getMatchId());
				intent.putExtra("matchName",match.getMatchName());
		        startActivity(intent);
		        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
				
			}
		});
		
	}	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}
}
