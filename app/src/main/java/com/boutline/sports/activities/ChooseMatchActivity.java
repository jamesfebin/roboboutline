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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.boutline.sports.adapters.LiveMatchesAdapter;
import com.boutline.sports.models.Match;
import com.boutline.sports.R;

import java.util.ArrayList;


public class ChooseMatchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matches);

		// Populate the List View
		
		ArrayList<Match> arrayOfMatches = new ArrayList<Match>();
		Match match = new Match("123","123","123","123","123","123","123","123","123");
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
