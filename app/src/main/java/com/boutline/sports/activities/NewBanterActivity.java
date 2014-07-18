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
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.boutline.sports.adapters.BanterTopicsAdapter;
import com.boutline.sports.adapters.SportsAdapter;
import com.boutline.sports.models.Tournament;
import com.boutline.sports.R;

import java.util.ArrayList;

public class NewBanterActivity extends Activity {

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_banter);

        TextView hdrNewBanter = (TextView)findViewById(R.id.hdrNewBanter);
        TextView lblGiveName = (TextView)findViewById(R.id.lblGiveName);
        EditText txtBanterName = (EditText)findViewById(R.id.txtBanterName);
        TextView lblChooseTopic = (TextView)findViewById(R.id.lblChooseTopic);
        Button btnCreateBanter = (Button)findViewById(R.id.btnCreateBanter);

        // Set up the fonts

        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
        hdrNewBanter.setTypeface(btf);
        lblGiveName.setTypeface(btf);
        txtBanterName.setTypeface(btf);
        lblChooseTopic.setTypeface(btf);
        btnCreateBanter.setTypeface(btf);
		
		// Populate the List View
		
		//Sport sport = new Sport("123","123","123", true);
		//arrayOfSports.add(sport);
		//SportsAdapter adapter = null; // = new SportsAdapter(this, arrayOfSports);
	//	ArrayList<Tournament> arrayOfTournaments = new ArrayList<Tournament>();
	//	Tournament tournament = new Tournament("123","FIFA World Cup","May 13th, 4:30 PM","May 13th, 7:30 PM","#WorldCup", 1, true);
	//	arrayOfTournaments.add(tournament);
	  //  BanterTopicsAdapter adapter = new BanterTopicsAdapter(this, arrayOfTournaments);
		//ListView listView = (ListView) findViewById(R.id.lvSports);
		//listView.setAdapter(adapter);
	}	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushdownin, R.anim.pushdownout);
	}
}
