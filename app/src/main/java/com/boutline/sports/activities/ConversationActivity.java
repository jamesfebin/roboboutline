package com.boutline.sports.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.boutline.sports.ContentProviders.BanterMessageProvider;
import com.boutline.sports.adapters.MessagesAdapter;
import com.boutline.sports.application.Constants;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.jobs.SendMessage;
import com.boutline.sports.models.BanterMessage;
import com.boutline.sports.models.Message;
import com.boutline.sports.R;

import com.boutline.sports.models.Query;
import com.boutline.sports.receivers.GcmBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.path.android.jobqueue.JobManager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConversationActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;
    BroadcastReceiver mReciever;
    MessagesAdapter messageAdapter;
    LoaderManager loadermanager;
    Cursor c;
    BroadcastReceiver mReceiver;
    ListView listView;
    JobManager jobManager;
    //  private UiLifecycleHelper uiHelper;
    SharedPreferences mSharedPreferences;
    BoutDBHelper dbHelper;
    ProgressDialog progress;
    public MixpanelAPI mixpanel = null;
    private PendingIntent pendingIntent;

    /*
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
       // uiHelper.onDestroy();
        mSharedPreferences = this.getSharedPreferences("boutlineData",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("currentScreen","");
        editor.putString("banterId","");
        editor.commit();

        if(mixpanel!=null)
        {
            mixpanel.flush();
        }
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation);
        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
        final EditText txtCompose = (EditText) findViewById(R.id.txtCompose);
        ImageButton sendMessage = (ImageButton) findViewById(R.id.btnCompose);
		txtCompose.setTypeface(tf);
        jobManager = MyApplication.getInstance().getJobManager();
        progress = new ProgressDialog(this);
        mixpanel= MixpanelAPI.getInstance(getApplicationContext(), Constants.MIXPANEL_TOKEN);

        mSharedPreferences = this.getSharedPreferences("boutlineData",
                Context.MODE_PRIVATE);
        String userId = mSharedPreferences.getString("boutlineUserId","");

        mixpanel.identify(userId);
        mixpanel.track("User on Muli Purpose Room",Constants.info);
        // Populate the List View
        loadermanager = getLoaderManager();
		populateListViewFromdb();
        loadermanager.initLoader(1,null,this);
        if(MyDDPState.getInstance().getDDPState() == DDPStateSingleton.DDPSTATE.Closed)
        {
            Toast.makeText(getApplicationContext(), "Internet connection not available", Toast.LENGTH_SHORT).show();

        }
        //   uiHelper = new UiLifecycleHelper(this, callback);
        // uiHelper.onCreate(savedInstanceState);

        txtCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String str = charSequence.toString();
                if(str.length() > 0 && str.startsWith(" ")){
                    txtCompose.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSharedPreferences = this.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
        final String userPicUrl = mSharedPreferences.getString("profileImageUrl","");
        final String senderName = mSharedPreferences.getString("fullName","");
        final String senderId = mSharedPreferences.getString("boutlineUserId","");
         long unixTime = System.currentTimeMillis() / 1000L;
        jobManager = MyApplication.getInstance().getJobManager();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* if (txtCompose.getText().toString().matches("invite") == true) {
                    sendRequestDialog();
                } else */if (txtCompose.getText().toString().matches("") == false) {


                    if(txtCompose.getText().toString().matches("settings"))
                    {

                        Intent intent = new Intent(ConversationActivity.this,SettingsActivity.class);
                        intent.putExtra("from","conversations");
                        startActivity(intent);
                        return;

                }

                    mixpanel.track("User send a message",Constants.info);

                    UUID uniqueKey = UUID.randomUUID();
                    String mDocId = uniqueKey.toString();
                    Object[] parameters = new Object[3];
                    parameters[0] = getIntent().getExtras().getString("conversationId");
                    parameters[1] = txtCompose.getText().toString();
                    parameters[2] = mDocId;
                   // jobManager.addJobInBackground(new SendMessage(parameters));
                    Map<String,Object> fields = new HashMap<String, Object>();
                    fields.put("name",senderName);
                    fields.put("message",txtCompose.getText().toString());
                    fields.put("banterId",getIntent().getExtras().getString("conversationId"));
                    fields.put("userPicUrl",userPicUrl);
                    fields.put("sender",senderId);
                    long unixTime = System.currentTimeMillis() / 1000L;
                    fields.put("time",unixTime);
                    processBotQuery(txtCompose.getText().toString());
                    Message message = new Message(mDocId,fields);
                    txtCompose.setText("");
                    dbHelper.getInstance(getApplicationContext()).putMessage(message,"");
                }
            }
        });
	}

    public void populateListViewFromdb()
    {
        String[] fromFieldNames = new String[] {Message.COL_MESSAGE};
        int[] toViewIDs = new int[]
                {R.id.lblMessage};
        messageAdapter = new MessagesAdapter(this,R.layout.item_leftmessage,c,fromFieldNames,toViewIDs,0);
        listView = (ListView) findViewById(R.id.lvMessages);
        listView.setAdapter(messageAdapter);
    }
/*
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {

        if (state.isOpened()) {
        }
        else if(state.isClosed())
        {
        }
    }

    private Session.StatusCallback callback =
            new SessionStatusCallback();
    private Session.StatusCallback statusCallback =
            new SessionStatusCallback();

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    }
*/

        @Override
    protected void onResume() {
        super.onResume();
            mSharedPreferences = this.getSharedPreferences("boutlineData",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("currentScreen","banter");
            editor.putString("banterId",getIntent().getExtras().getString("conversationId"));
            editor.commit();
        /* Needed when facebook is integrated
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();

            if (!session.isOpened() && !session.isClosed()) {
                MyDDPState.getInstance().mDDPState = DDPStateSingleton.DDPSTATE.Connected;
                Intent intent = new Intent(this,FacebookLogin.class);
                startActivity(intent);
            } else if(session.isClosed()){
                MyDDPState.getInstance().mDDPState = DDPStateSingleton.DDPSTATE.Connected;
                Intent intent = new Intent(this,FacebookLogin.class);
                startActivity(intent);
            }
*/
    mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {
            @Override
            protected void onDDPConnect(DDPStateSingleton ddp) {
                super.onDDPConnect(ddp);

                Object[] parameters = new Object[2];

            /*
                Log.d("ConversationId", getIntent().getExtras().getString("conversationId"));
                parameters[0] = getIntent().getExtras().getString("conversationId");
                parameters[1] = 20;
                //jobManager = MyApplication.getInstance().getJobManager();
                //jobManager.addJobInBackground(new Subscribe(ddp,"messages",parameters));
                ddp.subscribe("messages",parameters);
*/
                parameters[0] = "KsrzMzFd6uHckAeb5";
                parameters[1] = 20;
                ddp.subscribe("mobileTournamentsInfluencerTweets",parameters);



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
                    Toast.makeText(getApplicationContext(), "Internet connection not available", Toast.LENGTH_SHORT).show();
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


    public void processBotQuery(String query)
    {

        try {
         String originalQuery = query;
            query = query.replaceAll("//'s", "");
            query = query.replaceAll("[^a-zA-Z0-9 ]", "");
            query = query.toLowerCase();
        String result = dbHelper.getInstance(getApplicationContext()).findQuery(query);

        if(result!=null)
        {

            String[] QueryResult =   result.split(";");
            if(QueryResult.length>1) {
                String query_id = QueryResult[0];
                String parameterQuery = QueryResult[1];
                Log.e("CALLING ", "CALLED");
                dbHelper.getInstance(getApplicationContext()).sendStructuredQuery(query_id, parameterQuery,originalQuery);
            }
        }




            }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.banter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(ConversationActivity.this, SettingsActivity.class);
                intent.putExtra("from","conversations");
                startActivity(intent);
                overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSharedPreferences = this.getSharedPreferences("boutlineData",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("currentScreen","");
        editor.putString("banterId","");
        editor.commit();
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        //   uiHelper.onPause();
    }

    @Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String conversationId = getIntent().getExtras().getString("conversationId");
        return new CursorLoader(this, Uri.parse(BanterMessageProvider.URI_FILTERMESSAGES+"/"+conversationId+","+"KsrzMzFd6uHckAeb5") , BanterMessage.FIELDS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        messageAdapter.swapCursor(cursor);
        listView.setSelection(messageAdapter.getCount()-1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        messageAdapter.swapCursor(null);
    }

    private void sendRequestDialog() {
    /*
        Session session = Session.getActiveSession();
        if(session.isOpened()) {
            Bundle parameters = new Bundle();
            parameters.putString("message", "Sent by Boutline");
            WebDialog.Builder builder = new WebDialog.Builder(ConversationActivity.this, Session.getActiveSession(),
                    "apprequests", parameters);
            builder.setOnCompleteListener(new WebDialog.OnCompleteListener() {
                @Override
                public void onComplete(Bundle values, FacebookException error) {

                    if (error != null) {
                        if (error instanceof FacebookOperationCanceledException) {
                            Toast.makeText(ConversationActivity.this, "Request cancelled", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ConversationActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        final String requestId = values.getString("request");
                        Log.d("Rewuests", "This is the request ID" + requestId);
                        if (requestId != null) {
                            Toast.makeText(ConversationActivity.this, "Request sent", Toast.LENGTH_SHORT).show();
                            Object[] parameters = new Object[2];
                            parameters[0] = getIntent().getExtras().getString("conversationId");
                            parameters[1] = requestId;
                            jobManager.addJobInBackground(new RequestFbUser(parameters));
                        } else {
                            Toast.makeText(ConversationActivity.this, "Request cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            WebDialog webDialog = builder.build();
            webDialog.show();
        }
        */
    }
}
