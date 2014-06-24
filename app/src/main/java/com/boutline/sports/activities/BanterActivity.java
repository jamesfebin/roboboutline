/** 
 * Tests:
 *  Activity slides in from the right -
 *  Ensure activity loads within 3 seconds -
 *  Loading bar is displayed till the view is populated
 *  List of conversations populated in most-recent-first order
 *  Each conversation has name, topic, scrollable members list, last message -
 *  ActionBar exists with icons for new convo, board, schedule, caret - 
 *  Each convo when clicked leads to the correct convo activity
 *  Back button slides activity right to board -
 *  Correct attributes of Conversation class exist -
 *  New Conversation section slides newconvo activity from bottom -
 *  Correct error message is displayed if no internet on activity load -
 *  Drawer menu opens and is visible
 *  All links in drawer menu lead to correct pages
 *  Fonts are defined and assigned -
 *  Take a screenshot of the activity with and without drawer  
 *  Cache convo list and keep in local storage
 *  Horizontal swipeable listview of members??
 */

package com.boutline.sports.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.boutline.sports.adapters.ConversationsAdapter;
import com.boutline.sports.helpers.Mayday;
import com.boutline.sports.models.Conversation;
import com.boutline.sports.R;

import java.util.ArrayList;

public class BanterActivity extends Activity {

	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banter);
		checkInternetConnected();
		
		// Declare and Assign the fonts
		
		tf = Typeface.createFromAsset(getAssets(), fontPath);
    	btf = Typeface.createFromAsset(getAssets(), boldFontPath);
    	

		// Populate the List View
		
		ArrayList<Conversation> arrayOfConversations = new ArrayList<Conversation>();
		ArrayList<String> abc = new ArrayList<String>();
		abc.add("123");
		Conversation conversation = new Conversation("123","123","123",abc,"123");
		arrayOfConversations.add(conversation);
		ConversationsAdapter adapter = new ConversationsAdapter(this, arrayOfConversations);
		ListView listView = (ListView) findViewById(R.id.lvConversations);
		listView.setAdapter(adapter);
		
	}
	
	public void checkInternetConnected(){
		
		// Mayday helper class finds out if internet connected
		
		Mayday chk = new Mayday(this);
		if(!chk.isConnectingToInternet())
		{
			Toast.makeText(this, "Unable to connect to internet. Try again.", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	// inflate the actionbar assigned for this page and set click listeners

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	       getMenuInflater().inflate(R.menu.banter, menu);
	       return true;
	}
		 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	 switch (item.getItemId()) {
	       case R.id.board:
	         Intent boardIntent = new Intent(BanterActivity.this, BoardActivity.class);
		     startActivity(boardIntent);
			 overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
		     return true;
	       case R.id.newconvo:
		     Intent newconvoIntent = new Intent(BanterActivity.this, NewBanterActivity.class);
			 startActivity(newconvoIntent);
			 overridePendingTransition(R.anim.pushupin, R.anim.pushupout);
			 return true;
		   default:
		     return super.onOptionsItemSelected(item);
		  }
	}
		
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}
}