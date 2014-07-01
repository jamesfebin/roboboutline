

package com.boutline.sports.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.adapters.SportsAdapter;
import com.boutline.sports.helpers.Mayday;
import com.boutline.sports.models.Sport;
import com.boutline.sports.R;

import java.util.ArrayList;

public class ChooseSportsActivity extends Activity {
	
	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sports);
		actionBar = getActionBar();
		final Button btnSubmitSportsSelection = (Button) findViewById(R.id.btnSubmitSportsSelection);
		TextView lblChooseSport = (TextView)findViewById(R.id.lblChooseSport);
		
		
		//Set up fonts
		
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		btf = Typeface.createFromAsset(getAssets(), boldFontPath);
		lblChooseSport.setTypeface(btf);
		btnSubmitSportsSelection.setTypeface(btf);
		
		// Populate the List View
		
		ArrayList<Sport> arrayOfSports = new ArrayList<Sport>();
		//TODO populate the arraylist with all sports and details
		Sport sport = new Sport("1","Cricket","5601 followers", false);
		arrayOfSports.add(sport);
        Sport sport2 = new Sport("1","Football","6801 followers", true);
        arrayOfSports.add(sport2);
		SportsAdapter adapter = new SportsAdapter(this, arrayOfSports);
		ListView listView = (ListView) findViewById(R.id.lvSports);
		listView.setAdapter(adapter);
						
		// Set all the listeners
		
		btnSubmitSportsSelection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				String errorMessage = "Something went wrong. Try again.";
				Boolean noSportSelected = false;
				Boolean isSportsCollectionUpdated = true; //Change this to false later
				btnSubmitSportsSelection.setText("Saving...");				
				Mayday chk = new Mayday(ChooseSportsActivity.this);
				
				//TODO find out if atleast one sport is selected and assign it to noSportSelected
				
				// Check if theres internet connection to update
				
				if(!chk.isConnectingToInternet()) 
				{
					errorMessage ="No internet connection. Try again.";
					isSportsCollectionUpdated = false;
					btnSubmitSportsSelection.setText("Save");
				}
				
				//Check if atleast one sport is selected
				
				if(noSportSelected){
					isSportsCollectionUpdated = false;
					errorMessage = "Select atleast one sport to continue.";
					isSportsCollectionUpdated = false;
					btnSubmitSportsSelection.setText("Save");
				}
				
				try {
					//TODO store sports selections in the database and update sportsCollectionUpdated
				}
				catch(Exception e) {
					isSportsCollectionUpdated = false;
				}
				
				//Success:go to next activity, else show errorMessage
			
				if(isSportsCollectionUpdated){
					btnSubmitSportsSelection.setText("Saved!");
					Intent mainIntent = new Intent(ChooseSportsActivity.this,ChooseTournamentActivity.class);
			        startActivity(mainIntent);
			        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
				}
				else{
					showError(errorMessage);
					btnSubmitSportsSelection.setText("Save");
				}
			}
 
		});
		
	}	
	
	@Override
	protected void onResume() {
		super.onResume();
		final Button btnSubmitSportsSelection = (Button) findViewById(R.id.btnSubmitSportsSelection);
		btnSubmitSportsSelection.setText("Save");
	}
	
	public void showError(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
		       	Intent banterIntent = new Intent(ChooseSportsActivity.this, BanterActivity.class);
		        startActivity(banterIntent);
		        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
		        return true;
		    case R.id.settings:
		       	Intent settingsIntent = new Intent(ChooseSportsActivity.this, SettingsActivity.class);
		        startActivity(settingsIntent);
		        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
		    }
	}
}
