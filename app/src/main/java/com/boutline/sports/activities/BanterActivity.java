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
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.ContentProviders.ConversationProvider;
import com.boutline.sports.adapters.ConversationsAdapter;
import com.boutline.sports.adapters.SportsAdapter;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.helpers.Mayday;
import com.boutline.sports.models.Conversation;
import com.boutline.sports.R;
import com.boutline.sports.models.Match;
import com.boutline.sports.models.Sport;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

import java.util.ArrayList;

public class BanterActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	SimpleCursorAdapter conversationsAdapter;
    Cursor c;
    ListView listView;
    LoaderManager loadermanager;
    BroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banter);
        TextView hdrConversations = (TextView)findViewById(R.id.hdrConversations);

		// Declare and Assign the fonts
		
		tf = Typeface.createFromAsset(getAssets(), fontPath);
    	btf = Typeface.createFromAsset(getAssets(), boldFontPath);
    	hdrConversations.setTypeface(btf);

		// Populate the List View

        loadermanager = getLoaderManager();
        populateListViewFromDb();
        loadermanager.initLoader(1, null, this);


		
	}

    public void populateListViewFromDb()
    {

        String[] fromFieldNames = new String[] {Conversation.COL_NAME,Conversation.COL_TOURNAMENTNAME,Conversation.COL_LASTMESSAGE};
        int[] toViewIDs = new int[]
                {R.id.lblConversationName,R.id.lblConversationTopic,R.id.lblLastMessage};
        conversationsAdapter = new ConversationsAdapter(this,R.layout.item_conversation,c,fromFieldNames,toViewIDs, 0);
        listView = (ListView) findViewById(R.id.lvConversations);
        listView.setAdapter(conversationsAdapter);



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
    protected void onResume() {
        super.onResume();
        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {

            @Override
            protected void onDDPConnect(DDPStateSingleton ddp) {
                super.onDDPConnect(ddp);


                Object[] parameters = new Object[1];
                parameters[0] = 20;

                //jobManager = MyApplication.getInstance().getJobManager();
                //jobManager.addJobInBackground(new Subscribe(ddp,"associatedBanters",parameters));

                ddp.subscribe("associatedBanters",parameters);

            }

            @Override
            public void onReceive(Context context, Intent intent) {

                super.onReceive(context, intent);

                Bundle bundle = intent.getExtras();

                if(intent.getAction().equals(MyDDPState.MESSAGE_ERROR))
                {

                    Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT).show();

                }


            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(MyDDPState.MESSAGE_ERROR));


        // we want connection state change messages so we know we're logged in
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(MyDDPState.MESSAGE_CONNECTION));

        if (MyDDPState.getInstance().getState() == MyDDPState.DDPSTATE.Closed) {
        //    Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null) {

            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
            mReceiver = null;
        }

    }

    @Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                ConversationProvider.URI_CONVERSATIONS, Conversation.FIELDS, null, null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

       conversationsAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

        conversationsAdapter.swapCursor(null);

    }
}
