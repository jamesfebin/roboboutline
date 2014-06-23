/** 
 * Tests:
 *  Activity slides in from the right 
 *  Ensure activity loads within 3 seconds 
 *  Loading bar is displayed till the view is populated
 *  List of messages populated in most-recent-at-bottom order
 *  Each message has text, sendername, time, profilePic
 *  ActionBar exists with icons for new member, board, schedule, caret
 *  Messages copiable
 *  Back button slides activity right to Conversations
 *  Correct attributes of Message class exist 
 *  Compose message section contains edittext and send button
 *  Err msg toast displayed if no internet when message send try & dont clear edittext
 *  Drawer menu opens and is visible
 *  All links in drawer menu lead to correct pages
 *  Take a screenshot of the activity with and without drawer  
 *  Cache 20 messages for each conversation and keep in local storage
 *  New messages come in with a smooth transition
 *  Message send button transition
 *  Fonts are defined and assigned
 *  Infinite scroll if time permits
 *  Blank slate message if no messages are found
 *  Topic of conversation titlebar
 *  New member fb popup appears seemlessly as overlay
 *  Toast message after fb request is sent
 *  Error Toast if invite could not be sent and fb dialog not opening
 *  Right align messages from self and left align others
 *  Mute/unmute in drawer works 
 *  new msg push notifications are not sent when this activity is open
 */

package com.boutline.sports.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.boutline.sports.adapters.MessagesAdapter;
import com.boutline.sports.models.Message;
import com.boutline.sports.R;

import java.util.ArrayList;

public class ConversationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation);
		
		// Populate the List View
		
		ArrayList<Message> arrayOfMessages = new ArrayList<Message>();
		ArrayList<String> abc = new ArrayList<String>();
		abc.add("123");
		Message message = new Message("123","123","123","123","123","123","123");
		arrayOfMessages.add(message);
		MessagesAdapter adapter = new MessagesAdapter(this, arrayOfMessages);
		ListView listView = (ListView) findViewById(R.id.lvMessages);
		listView.setAdapter(adapter);
				
	}	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}
}
