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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.fragments.LiveMatchesFragment;
import com.boutline.sports.R;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

import org.w3c.dom.Text;


public class ChooseMatchActivity extends FragmentActivity  {

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;
    BroadcastReceiver mReceiver;
    Context context;
    String tournamentId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matches);

        String tournamentName;
        //Declare the controls
        TextView lblTournamentName = (TextView) findViewById(R.id.lblTournamentName);
        TextView lblTournamentStartTime = (TextView) findViewById(R.id.lblTournamentStartTime);
        /*
        TextView lblLiveMatches = (TextView) findViewById(R.id.lblLiveMatches);
        TextView lblUpcomingMatches = (TextView) findViewById(R.id.lblUpcomingMatches);
        */
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        TextView lblLeftSports = (TextView) findViewById(R.id.lblLeftSports);
        TextView lblLeftTournaments = (TextView) findViewById(R.id.lblLeftTournaments);
        TextView lblLeftSchedule = (TextView) findViewById(R.id.lblLeftSchedule);
        TextView lblLeftProfile = (TextView) findViewById(R.id.lblLeftProfile);
        TextView lblLeftLogout = (TextView) findViewById(R.id.lblLeftLogout);
        TextView lblProfileName = (TextView) findViewById(R.id.lblProfileName);
    TextView lblLiveMatches = (TextView) findViewById(R.id.lblLiveMatches);
        TextView lblUpcomingMatches = (TextView) findViewById(R.id.lblUpcomingMatches);

        // Set up the animations

        Animation fadeinAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeinAnim.setDuration(1000);
        fadeinAnim.setRepeatCount(1);
        fadeinAnim.setRepeatMode(1);
        container.startAnimation(fadeinAnim);

        //Set up fonts

        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);

        // Assign the font types

        lblTournamentName.setTypeface(btf);
        lblTournamentStartTime.setTypeface(btf);

        tournamentId = getIntent().getExtras().getString("tournamentId");
        tournamentName = getIntent().getExtras().getString("tournamentName");
        lblTournamentName.setText(tournamentName);


        lblLiveMatches.setTypeface(btf);
        lblLeftSports.setTypeface(btf);
        lblLeftTournaments.setTypeface(btf);
        lblLeftSchedule.setTypeface(btf);
        lblLeftProfile.setTypeface(btf);
        lblLeftLogout.setTypeface(btf);
        lblProfileName.setTypeface(btf);
        lblUpcomingMatches.setTypeface(btf);

        // Set all the listeners


		

		
	}	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}

    @Override
    protected void onResume() {
        super.onResume();

        context = getApplicationContext();

        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {

            @Override
            protected void onDDPConnect(DDPStateSingleton ddp) {
                super.onDDPConnect(ddp);



                Object[] parameters = new Object[2];
                parameters[0] = tournamentId;
                parameters[1] = 20;

                //jobManager = MyApplication.getInstance().getJobManager();
                //jobManager.addJobInBackground(new Subscribe(ddp,"userSportPreferences",parameters));

                ddp.subscribe("tournamentLiveMatches",parameters);
                ddp.subscribe("tournamenUpcomingMatches",parameters);


            }

            @Override
            protected void onError(String title, String msg)
            {

            }

            @Override
            public void onReceive(Context context, Intent intent) {

                super.onReceive(context, intent);

                Bundle bundle = intent.getExtras();

                if(intent.getAction().equals(MyDDPState.MESSAGE_ERROR))
                {
                    Toast.makeText(getApplicationContext(), "Internet connection not avaialable", Toast.LENGTH_SHORT);
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(MyDDPState.MESSAGE_ERROR));

        // we want connection state change messages so we know we're logged in
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(MyDDPState.MESSAGE_CONNECTION));

        if (MyDDPState.getInstance().getState() == MyDDPState.DDPSTATE.Closed) {
            Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
