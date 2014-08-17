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
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.ContentProviders.BanterMessageProvider;
import com.boutline.sports.ContentProviders.MatchProvider;
import com.boutline.sports.ContentProviders.MessageProvider;
import com.boutline.sports.ContentProviders.SportProvider;
import com.boutline.sports.adapters.MessagesAdapter;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.jobs.RequestFbUser;
import com.boutline.sports.jobs.SendMessage;
import com.boutline.sports.models.BanterMessage;
import com.boutline.sports.models.Conversation;
import com.boutline.sports.models.Message;
import com.boutline.sports.R;
import com.boutline.sports.models.Sport;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.WebDialog;
import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import com.path.android.jobqueue.JobManager;

import java.util.ArrayList;
import java.util.Arrays;
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

    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation);

        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
        final EditText txtCompose = (EditText) findViewById(R.id.txtCompose);
        Button sendMessage = (Button) findViewById(R.id.btnCompose);
		txtCompose.setTypeface(tf);
        jobManager = MyApplication.getInstance().getJobManager();

		// Populate the List View
        loadermanager = getLoaderManager();
		populateListViewFromdb();
        loadermanager.initLoader(1,null,this);

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
        final long unixTime = System.currentTimeMillis() / 1000L;
        jobManager = MyApplication.getInstance().getJobManager();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtCompose.getText().toString().matches("invite") == true) {
                    sendRequestDialog();
                } else if (txtCompose.getText().toString().matches("") == false) {


                    if(txtCompose.getText().toString().matches("settings"))
                    {

                        Intent intent = new Intent(ConversationActivity.this,CreateProfileActivity.class);
                        intent.putExtra("from","conversations");
                        startActivity(intent);
                       return;

                    }


                    UUID uniqueKey = UUID.randomUUID();

                    String mDocId = uniqueKey.toString();

                    Object[] parameters = new Object[3];
                    parameters[0] = getIntent().getExtras().getString("conversationId");
                    parameters[1] = txtCompose.getText().toString();
                    parameters[2] = mDocId;
                    jobManager.addJobInBackground(new SendMessage(parameters));


                    Map<String,Object> fields = new HashMap<String, Object>();
                    fields.put("name",senderName);
                    fields.put("message",txtCompose.getText().toString());
                    fields.put("banterId",getIntent().getExtras().getString("conversationId"));
                    fields.put("userPicUrl",userPicUrl);
                    fields.put("sender",senderId);
                    fields.put("time",unixTime);

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


                Log.e("ConversationId", getIntent().getExtras().getString("conversationId"));

                Object[] parameters = new Object[2];
                parameters[0] = getIntent().getExtras().getString("conversationId");
                parameters[1] = 20;

                //jobManager = MyApplication.getInstance().getJobManager();
                //jobManager.addJobInBackground(new Subscribe(ddp,"messages",parameters));

                ddp.subscribe("messages",parameters);


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
            //    Toast.makeText(getApplicationContext(),"Internet connection not avaialable",Toast.LENGTH_SHORT);
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
		finish();
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
                        Log.e("Rewuests", "This is the request ID" + requestId);
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
