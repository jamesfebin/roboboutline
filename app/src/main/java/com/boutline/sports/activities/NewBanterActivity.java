/** 
 * Tests:
 *  Activity slides in from the bottom 
 *  Ensure activity loads within 3 seconds 
 *  Loading bar is displayed till the view is populated
 *  List of tournaments populated in most-recent-first order
 *  Activity must have edittext, listview of tours
 *  List of tours must have profile pic, name and startend dates
 *  Check if banter name is blank
 *  ActionBar exists with icons for banter, board, schedule, caret
 *  Each tour when clicked updates db and goes to the correct conversation activity
 *  Back button slides activity down to matches 
 *  Correct attributes of Conversation class exist 
 *  Correct error message toast is displayed if no internet on activity load
 *  Drawer menu opens and is visible
 *  All links in drawer menu lead to correct pages
 *  Take a screenshot of the activity with and without drawer  
 *  Cache 10 tours and keep in local storage
 *  Loading bar is displayed till the banter is created
 *  Fonts are defined and assigned
 *  Blank slate message if no tours are found
 *  Multiline banter name is not allowed
 *  Directions are given clearly with labels to achieve the task
 */

package com.boutline.sports.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.boutline.sports.adapters.SportsAdapter;
import com.boutline.sports.models.Sport;
import com.boutline.sports.R;

import java.util.ArrayList;

public class NewBanterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_banter);
		
		// Populate the List View
		
		ArrayList<Sport> arrayOfSports = new ArrayList<Sport>();
		//Sport sport = new Sport("123","123","123", true);
		//arrayOfSports.add(sport);
		SportsAdapter adapter = null; // = new SportsAdapter(this, arrayOfSports);
		ListView listView = (ListView) findViewById(R.id.lvSports);
		listView.setAdapter(adapter);
	}	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}
}
