/** 
 * Tests:
 *  Activity slides in from the right -
 *  Ensure activity loads within 3 seconds -
 *  Loading bar is displayed till the view is populated
 *  List of tweets populated in most-recent-first order -
 *  Each Tweet has name, handle, message, profilepic -
 *  ActionBar exists with icons for banter, board, playpause, caret
 *  Each tweet when clicked leads to the correct tweet detail activity
 *  Back button slides activity right to matches -
 *  Correct attributes of Tweet class exist -
 *  New Tweet section slides composetweet activity from bottom
 *  Correct error message toast is displayed if no internet on activity load
 *  Drawer menu opens and is visible
 *  All links in drawer menu lead to correct pages
 *  Take a screenshot of the activity with and without drawer  
 *  Cache 20 tweets for each section and keep in local storage
 *  Each tab shows the correct kind of tweets from diff fragments
 *  Sliding between fragments is smooth and seemless -
 *  New tweets come in with a smooth transition
 *  Play pause button is functional and icon changes correctly with toasts
 *  Fonts are defined and assigned
 *  Switching tabs sets target fragment to played state (also icon change)
 *  Infinite scroll if time permits
 *  Blank slate message if match not started or no tweets are found
 *  Match Hashtag titlebar
 *  Tweets are HTML encoded
 */

package com.boutline.sports.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.adapters.TabPagerAdapter;
import com.boutline.sports.R;
import com.boutline.sports.application.MyDDPState;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

public class BoardActivity extends FragmentActivity implements ActionBar.TabListener {

	ActionBar actionBar;
	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	Animation mouseAnim;
	Boolean isPlay = true;
    BroadcastReceiver mReceiver;
    Context context;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// Set up the UI

		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		actionBar = getActionBar();
		
		// Define the controls

		ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
		LinearLayout composeLink = (LinearLayout) findViewById(R.id.composeLink);
        Button btnComposeDummy = (Button)findViewById(R.id.btnComposeDummy);

        EditText txtComposeDummy = (EditText)findViewById(R.id.txtComposeDummy);
        actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//Set up the tabs

	    String[] tabs = { "Popular", "Media", "Fan Voice" };

        String mtId = getIntent().getExtras().getString("mtId");
        String type = getIntent().getExtras().getString("type");
        txtComposeDummy.setText("Tweet with #"+getIntent().getExtras().getString("hashtag"));
		TabPagerAdapter adapterViewPager = new TabPagerAdapter(getSupportFragmentManager(),mtId);
	    vpPager.setAdapter(adapterViewPager);
	    vpPager.setOffscreenPageLimit(3);
		
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

        // Set up the fonts

        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
        btnComposeDummy.setTypeface(btf);
        txtComposeDummy.setTypeface(btf);
	     
	     // Set up the click listeners
	     
	     composeLink.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {		
					Intent mainIntent = new Intent(BoardActivity.this, ComposeTweetActivity.class);
			        mainIntent.putExtra("hashtag",getIntent().getExtras().getString("hashtag"));
                    startActivity(mainIntent);

			        overridePendingTransition(R.anim.pushupin, R.anim.pushupout);
				}	 
		 });
	     
	     vpPager.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {				
					Intent mainIntent = new Intent(BoardActivity.this, TweetDetailsActivity.class);
                    mainIntent.putExtra("hashtag",getIntent().getExtras().getString("hashtag"));
                    startActivity(mainIntent);
			        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
				}	 
		 });
		
	}
	
	// On back pressed action
	 
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
        overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}
	
	// inflate the menu assigned for this page and set click listeners

	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.board, menu);
	        return true;
	    }
	 
	 // Action Bar icon click listeners
	 
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
            case R.id.schedule:
                 Intent scheduleIntent = new Intent(BoardActivity.this, ChooseMatchActivity.class);
                 startActivity(scheduleIntent);
                 overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
                 return true;
	        case R.id.banter:
	        	Intent mainIntent = new Intent(BoardActivity.this, BanterActivity.class);
		        startActivity(mainIntent);
		        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
	            return true;
	        case R.id.playpause:
	        	MenuItem menuItem = (MenuItem)findViewById(R.id.playpause);
	        	if(isPlay){	        	
	        		isPlay = false;
		        	menuItem.setIcon(getResources().getDrawable(R.drawable.ic_play));
		        	doPlay();
	        	}
	        	else{
	        		isPlay = true;
	        		menuItem.setIcon(getResources().getDrawable(R.drawable.ic_pause));
		        	doPause();
	        	}	        	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	 
	 // Do play pause action when icon clicked in action bar
	 
	 public void doPlay(){
		 	 //TODO do play action here
	 }
	 public void doPause(){
	 	 	//TODO do pause action here
	 }

	 // Tab methods


    @Override
    protected void onResume() {
        super.onResume();

        context = getApplicationContext();

        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {

            @Override
            protected void onDDPConnect(DDPStateSingleton ddp) {
                super.onDDPConnect(ddp);



                String type = getIntent().getExtras().getString("type");
                String mtId = getIntent().getExtras().getString("mtId");


                Object[] parameters = new Object[2];
                parameters[0] = mtId;
                parameters[1] = 20;

                if(type.matches("match"))
                {

                    ddp.subscribe("mobileMatchesInfluencerTweets",parameters);
                    ddp.subscribe("mobileMatchesMediaTweets",parameters);
                    ddp.subscribe("mobileMatchesFanTweets",parameters);

                    //jobManager = MyApplication.getInstance().getJobManager();
                    //jobManager.addJobInBackground(new Subscribe(ddp,"mobileMatchesFanTweets",parameters));


                }
                else
                {

                    ddp.subscribe("mobileTournamentsInfluencerTweets",parameters);
                    ddp.subscribe("mobileTournamentsMediaTweets",parameters);
                    ddp.subscribe("mobileTournamentsFanVoice",parameters);

                }




            }

            @Override
            protected void onError(String title, String msg) {

            }

            @Override
            public void onReceive(Context context, Intent intent) {

                super.onReceive(context, intent);

                Bundle bundle = intent.getExtras();

                if(intent.getAction().equals(MyDDPState.MESSAGE_ERROR))
                {

                    Toast.makeText(getApplicationContext(), "Internet connection not avaialable", Toast.LENGTH_SHORT).show();

                }


            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(MyDDPState.MESSAGE_ERROR));


        // we want connection state change messages so we know we're logged in
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(MyDDPState.MESSAGE_CONNECTION));

        if (MyDDPState.getInstance().getState() == MyDDPState.DDPSTATE.Closed) {
         //   Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT);
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
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
		vpPager.setCurrentItem(tab.getPosition());
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

}
