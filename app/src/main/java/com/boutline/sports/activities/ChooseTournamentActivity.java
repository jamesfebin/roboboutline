/** 
 * Tests:
 *   Activity slides in from the right - 
 *   Ensure activity loads within 3 seconds -  
 *  Loading bar is displayed till the view is populated
 *  List of tournaments populated in most-recent-first order
 *   Each tournament has name, start date to end date, pic, follow buttons -
 *   ActionBar exists with icons for banter, settings -
 *  Each tournament when clicked leads to the correct matches activity
 *   Back button slides activity right to matches -
 *   Correct attributes of Tournament class exist - 
 *  Correct error message toast is displayed if no internet on activity load
 *  Drawer menu opens and is visible and has caret
 *  All links in drawer menu lead to correct pages
 *  Take a screenshot of the activity with and without drawer  
 *  Cache tournaments for each section and keep in local storage
 *   Toggling follow buttons changes icon -
 *   Fonts are defined and assigned -
 *   Blank slate message if no tournaments are found -
 *   Each tournament has the corresponding sport icon -
 */

package com.boutline.sports.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.ContentProviders.SportProvider;
import com.boutline.sports.ContentProviders.TournamentProvider;
import com.boutline.sports.adapters.SportsAdapter;
import com.boutline.sports.adapters.TournamentsAdapter;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.database.SQLController;
import com.boutline.sports.helpers.Mayday;
import com.boutline.sports.models.Sport;
import com.boutline.sports.models.Tournament;
import com.boutline.sports.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.path.android.jobqueue.JobManager;
//import com.tjeannin.apprate.AppRate;

import java.io.IOException;
import java.util.ArrayList;

public class ChooseTournamentActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    public String fontPath = "fonts/sharp.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/sharpbold.ttf";
    public Typeface btf;
    public String proxiFontPath = "fonts/proxinova.ttf";
    public Typeface ptf;
	ActionBar actionBar;
    String SENDER_ID = "265718256788";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    GoogleCloudMessaging gcm;
    private String TAG ="Choose Tournaments";
    Context context;
    private BroadcastReceiver mReceiver;
    SQLController dbController;
    Cursor c;
    LoaderManager loadermanager;
    JobManager jobManager;
    SimpleCursorAdapter tournamenentAdapter;
    ListView listView;



    @Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tournaments);

        //set up the action bar
		actionBar = getActionBar();
        int actionBarHeight=0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        /*
        ImageView actionBg = (ImageView) findViewById(R.id.actionBarBG);
        actionBg.getLayoutParams().height = actionBarHeight + 10;
        actionBg.requestLayout();
        */
		TextView lblChooseTournament = (TextView)findViewById(R.id.lblChooseTournament);
        TextView lblChooseTourDesc = (TextView)findViewById(R.id.lblChooseTourDesc);
		TextView lblBlankSlate = (TextView)findViewById(R.id.lblBlankSlate);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        // Set up the animations

        Animation fadeinAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeinAnim.setDuration(1000);
        fadeinAnim.setRepeatCount(1);
        fadeinAnim.setRepeatMode(1);
        container.startAnimation(fadeinAnim);

        // Set up the fonts
        tf = Typeface.createFromAsset(getAssets(), fontPath);
        ptf = Typeface.createFromAsset(getAssets(), proxiFontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
		lblChooseTournament.setTypeface(btf);
        lblChooseTourDesc.setTypeface(ptf);


        loadermanager = getLoaderManager();
        populateListViewFromDb();
        loadermanager.initLoader(1,null,this);
/*
		new AppRate(this)
	    .setMinDaysUntilPrompt(7)
	    .setMinLaunchesUntilPrompt(20)
	    .setShowIfAppHasCrashed(false)
	    .init();*/
		
	}

    public void populateListViewFromDb()
    {




        String[] fromFieldNames = new String[] {"name","followed","from_date","till_date"};

        int[] toViewIDs = new int[]
                {R.id.lblTournamentName,R.id.chkFollowStatus,R.id.lblTournamentStartTime,R.id.lblTournamentStartTime};

        tournamenentAdapter = new TournamentsAdapter(this,R.layout.item_tournament,c,fromFieldNames,toViewIDs, 0);
        listView = (ListView) findViewById(R.id.lvTournaments);
        listView.setAdapter(tournamenentAdapter);




    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("GCM", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences("BoutlineData",
                Context.MODE_PRIVATE);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


    private void registerInBackground() {




        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }

                    String regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    storeRegistrationId(getApplicationContext(), regid);
                    MyDDPState.getInstance().tagDevideId(regid);

                    Log.i("Registered",msg.toString());
                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            registerInBackground();
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    @Override
    protected void onResume() {
        super.onResume();

        context = getApplicationContext();

        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {

            @Override
            protected void onDDPConnect(DDPStateSingleton ddp) {
                super.onDDPConnect(ddp);

                String regid = getRegistrationId(context);

                if(regid.matches("")==false)
                {

                    MyDDPState.getInstance().tagDevideId(regid);


                }


                Object[] parameters = new Object[1];
                parameters[0] = 20;

                //jobManager = MyApplication.getInstance().getJobManager();
                //jobManager.addJobInBackground(new Subscribe(ddp,"userTournamentPreferences",parameters));

                ddp.subscribe("userTournamentPreferences",parameters);



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
            Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT);
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
		       	Intent banterIntent = new Intent(ChooseTournamentActivity.this, BanterActivity.class);
		        startActivity(banterIntent);
		        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
		        return true;
		    case R.id.settings:
		       	Intent settingsIntent = new Intent(ChooseTournamentActivity.this, SettingsActivity.class);
		        startActivity(settingsIntent);
		        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
		    }
	}

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                TournamentProvider.URI_TOURNAMENTS, Tournament.FIELDS, null, null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        tournamenentAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        tournamenentAdapter.swapCursor(null);

    }
}
