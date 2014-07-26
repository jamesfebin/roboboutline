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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.ContentProviders.TournamentProvider;
import com.boutline.sports.adapters.BanterTopicsAdapter;
import com.boutline.sports.adapters.SportsAdapter;
import com.boutline.sports.adapters.TournamentsAdapter;
import com.boutline.sports.adapters.TournamentsSelectAdapter;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.database.SQLController;
import com.boutline.sports.jobs.CreateBanter;
import com.boutline.sports.models.Conversation;
import com.boutline.sports.models.Tournament;
import com.boutline.sports.R;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.path.android.jobqueue.JobManager;

import java.util.ArrayList;

public class NewBanterActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;
    LoaderManager loadermanager;
    TournamentsSelectAdapter tournamenentAdapter;
    ListView listView;
    Cursor c;
    BroadcastReceiver mReceiver;
    String selectedTournament=null;
    JobManager jobManager;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_banter);

        TextView hdrNewBanter = (TextView)findViewById(R.id.hdrNewBanter);
        TextView lblGiveName = (TextView)findViewById(R.id.lblGiveName);
        final EditText txtBanterName = (EditText)findViewById(R.id.txtBanterName);
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

        loadermanager = getLoaderManager();
        populateListViewFromDb();
        loadermanager.initLoader(1, null, this);

        jobManager = MyApplication.getInstance().getJobManager();
        txtBanterName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                String str = charSequence.toString();
                if(str.length() > 0 && str.startsWith(" ")){

                    Toast.makeText(getApplicationContext(),"Banter Name cannot begin with a whitespace",Toast.LENGTH_SHORT).show();
                    txtBanterName.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnCreateBanter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



        if(tournamenentAdapter.getSelectedTournament()!=null)
        {

            selectedTournament = tournamenentAdapter.getSelectedTournament();

            if(txtBanterName.getText().toString().equals("")==false)
            {
                String banterName = txtBanterName.getText().toString();

                Object[] parameters = new Object[2];
                parameters[0]=txtBanterName.getText().toString();
                parameters[1]=tournamenentAdapter.getSelectedTournament();

                jobManager.addJobInBackground(new CreateBanter(parameters));

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please provide Banter Name",Toast.LENGTH_SHORT).show();
            }

        }
                else
        {
            Toast.makeText(getApplicationContext(),"Please select one tournament to continue",Toast.LENGTH_SHORT).show();
        }

            }
        });



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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushdownin, R.anim.pushdownout);
	}


    public void populateListViewFromDb()
    {
        String[] fromFieldNames = new String[] {"name","from_date","till_date"};

        int[] toViewIDs = new int[]
                {R.id.lblTournamentName,R.id.lblTournamentStartTime,R.id.lblTournamentStartTime};

        tournamenentAdapter = new TournamentsSelectAdapter(this,R.layout.item_tournament_select,c,fromFieldNames,toViewIDs, 0);
        listView = (ListView) findViewById(R.id.lvTournaments);
        listView.setAdapter(tournamenentAdapter);
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
