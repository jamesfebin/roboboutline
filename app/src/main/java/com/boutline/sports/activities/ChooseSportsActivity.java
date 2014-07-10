

package com.boutline.sports.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.CursorLoader;
import android.content.Loader;
import com.boutline.sports.ContentProviders.SportProvider;
import com.boutline.sports.adapters.SportsAdapter;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.database.SQLController;
import com.boutline.sports.helpers.Mayday;
import com.boutline.sports.models.Sport;
import com.boutline.sports.R;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

import java.sql.SQLException;
import java.util.ArrayList;

public class ChooseSportsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>  {
	
	public String fontPath = "fonts/proxinova.ttf";
	public Typeface tf;
	public String boldFontPath = "fonts/proxinovabold.otf";
	public Typeface btf;
	ActionBar actionBar;
    BroadcastReceiver mReceiver;
    SimpleCursorAdapter sportAdapter;
    ListView listView;
    Cursor c;
    SQLController dbController;
    DataSetObserver mDataSetObserver;

    LoaderManager loadermanager;




    @Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sports);
		actionBar = getActionBar();
		final Button btnSubmitSportsSelection = (Button) findViewById(R.id.btnSubmitSportsSelection);

        // define the controls

		TextView lblChooseSport = (TextView)findViewById(R.id.lblChooseSport);
		RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        // Set up the animations

        Animation fadeinAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeinAnim.setDuration(1000);
        fadeinAnim.setRepeatCount(1);
        fadeinAnim.setRepeatMode(1);
        container.startAnimation(fadeinAnim);

		//Set up fonts
		
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		btf = Typeface.createFromAsset(getAssets(), boldFontPath);
		lblChooseSport.setTypeface(btf);
		btnSubmitSportsSelection.setTypeface(btf);
		
		// Populate the List View


        dbController = new SQLController(this);
        try {
            dbController.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        loadermanager = getLoaderManager();

        populateListViewFromDb();
        loadermanager.initLoader(1,null,this);

        // Set all the listeners
		
		btnSubmitSportsSelection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

                dbController.clearSportsList();
                c = dbController.getSportsData();
                sportAdapter.swapCursor(c);
                sportAdapter.notifyDataSetChanged();
                listView.invalidate();

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
			//        startActivity(mainIntent);
			        overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
				}
				else{
					showError(errorMessage);
					btnSubmitSportsSelection.setText("Save");
				}
			}
 
		});
		
	}


	public void populateListViewFromDb()
    {

      //  c = dbController.getSportsData();



        String[] fromFieldNames = new String[] {"name","followed"};

        int[] toViewIDs = new int[]
                {R.id.lblSportName,R.id.chkFollowStatus};

        sportAdapter = new SportsAdapter(this,R.layout.item_sport,c,fromFieldNames,toViewIDs, 0);
        listView = (ListView) findViewById(R.id.lvSports);
        listView.setAdapter(sportAdapter);




    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        sportAdapter.swapCursor(cursor);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(this,
                SportProvider.URI_SPORTS, Sport.FIELDS, null, null,
                null);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

        sportAdapter.swapCursor(null);

        }

	@Override
	protected void onResume() {
        super.onResume();
        final Button btnSubmitSportsSelection = (Button) findViewById(R.id.btnSubmitSportsSelection);
        btnSubmitSportsSelection.setText("Save");


        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {

            @Override
            protected void onDDPConnect(DDPStateSingleton ddp) {
                super.onDDPConnect(ddp);
                Object[] parameters = new Object[1];
                parameters[0] = 100;
                ddp.subscribe("userSportPreferences",parameters);


            }

            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);

                if(intent.getAction().equals(MyDDPState.MESSAGE_ERROR))
                {
                    Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT);
                }



            }

            @Override
            protected void onSubscriptionUpdate(String changeType, String subscriptionName, String docId) {
                super.onSubscriptionUpdate(changeType, subscriptionName, docId);


            }
        };


        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(MyDDPState.MESSAGE_ERROR));

        // we want connection state change messages so we know we're logged in
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                new IntentFilter(MyDDPState.MESSAGE_CONNECTION));





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
