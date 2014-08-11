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
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Loader;
import com.boutline.sports.ContentProviders.SportProvider;
import com.boutline.sports.adapters.SportsAdapter;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.helpers.Mayday;
import com.boutline.sports.models.Sport;
import com.boutline.sports.R;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.path.android.jobqueue.JobManager;

public class ChooseSportsActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>  {
	

    public String fontPath = "fonts/sharp.ttf";
    public Typeface tf;
    public String proxiFontPath = "fonts/proxinova.ttf";
    public Typeface ptf;
	public String boldFontPath = "fonts/sharpbold.ttf";
	public Typeface btf;
	ActionBar actionBar;
    BroadcastReceiver mReceiver;
    SimpleCursorAdapter sportAdapter;
    ListView listview;
    Cursor c;
    LoaderManager loadermanager;
    JobManager jobManager;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sports);
		actionBar = getActionBar();
		final Button btnSubmitSportsSelection = (Button) findViewById(R.id.btnSubmitSportsSelection);

        // define the controls

		TextView lblChooseSport = (TextView)findViewById(R.id.lblChooseSport);
        TextView lblChooseSportDesc = (TextView)findViewById(R.id.lblChooseSportDesc);

		//Set up fonts
		
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		btf = Typeface.createFromAsset(getAssets(), boldFontPath);
        ptf = Typeface.createFromAsset(getAssets(), proxiFontPath);
		lblChooseSport.setTypeface(btf);
		btnSubmitSportsSelection.setTypeface(btf);
        lblChooseSportDesc.setTypeface(ptf);
		
		// Populate the Grid View

        loadermanager = getLoaderManager();
        populateListViewFromDb();
        loadermanager.initLoader(1,null,this);

        // Set up the animations

        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        Animation fadeinAnim = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeinAnim.setDuration(1000);
        fadeinAnim.setRepeatCount(1);
        fadeinAnim.setRepeatMode(1);
        container.startAnimation(fadeinAnim);

        // Set all the listeners
		
		btnSubmitSportsSelection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
                int i;
                int flag=0;
                CheckBox cb;

                for(i=0;i<listview.getCount();i++)
                {
                    cb = (CheckBox) listview.getChildAt(i).findViewById(R.id.chkFollowStatus);
                    if(cb.isChecked())
                    {
                        flag = 1;
                    }
                }
                if(flag == 1)
                {
                    btnSubmitSportsSelection.setText("Saving, Please Wait.");
                    Intent mainIntent = new Intent(ChooseSportsActivity.this, ChooseTournamentActivity.class);
                    startActivity(mainIntent);
                    overridePendingTransition(R.anim.pushleftin, R.anim.pushleftout);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please choose atleast one sport to continue",Toast.LENGTH_SHORT).show();
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
         listview = (ListView) findViewById(R.id.lvSports);
        listview.setAdapter(sportAdapter);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        sportAdapter.swapCursor(cursor);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, SportProvider.URI_SPORTS, Sport.FIELDS, null, null, null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        sportAdapter.swapCursor(null);
        }

	@Override
	protected void onResume() {
        super.onResume();

        final Button btnSubmitSportsSelection = (Button) findViewById(R.id.btnSubmitSportsSelection);
        btnSubmitSportsSelection.setText("Continue");
        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {
            @Override
            protected void onDDPConnect(DDPStateSingleton ddp) {
                super.onDDPConnect(ddp);
                Object[] parameters = new Object[1];
                parameters[0] = 100;
                //jobManager = MyApplication.getInstance().getJobManager();
                //jobManager.addJobInBackground(new Subscribe(ddp,"userSportPreferences",parameters));
                ddp.subscribe("userSportPreferences",parameters);
            }

            @Override
            protected void onError(String title, String msg) {

            }

            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                if(intent.getAction().equals(MyDDPState.MESSAGE_ERROR))
                {
                    Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void onSubscriptionUpdate(String changeType, String subscriptionName, String docId) {
                super.onSubscriptionUpdate(changeType, subscriptionName, docId);
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,new IntentFilter(MyDDPState.MESSAGE_ERROR));
        // we want connection state change messages so we know we're logged in
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,new IntentFilter(MyDDPState.MESSAGE_CONNECTION));
    }



    @Override
    protected void onPause() {
        super.onPause();
        if(mReceiver!=null) {
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
	public boolean onCreateOptionsMenu(Menu menu) {
	     getMenuInflater().inflate(R.menu.choosesport, menu);
	     return true;
	}

    //TODO set up actionbar click listeners
}
