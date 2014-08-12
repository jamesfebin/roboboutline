package com.boutline.sports.application;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.database.SQLController;
import com.boutline.sports.jobs.Connect;
import com.boutline.sports.jobs.CreateBanter;
import com.boutline.sports.jobs.Login;
import com.boutline.sports.jobs.ProcessFbRequests;
import com.boutline.sports.jobs.RequestFbUser;
import com.boutline.sports.jobs.SendMessage;
import com.boutline.sports.jobs.SendSportPreferences;
import com.boutline.sports.jobs.SendTournamentPreferences;
import com.boutline.sports.models.Conversation;
import com.boutline.sports.models.ConversationParameter;
import com.boutline.sports.models.FacebookUserInfo;
import com.boutline.sports.models.Match;
import com.boutline.sports.models.Message;
import com.boutline.sports.models.MessageParameter;
import com.boutline.sports.models.Sport;

import com.boutline.sports.models.Tournament;
import com.boutline.sports.models.Tweet;
import com.google.gson.Gson;
import com.keysolutions.ddpclient.DDPListener;
import com.keysolutions.ddpclient.DDPClient.DdpMessageField;
import com.keysolutions.ddpclient.DDPClient.DdpMessageType;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

import android.content.Context;
import com.keysolutions.ddpclient.DDPClient;
import com.path.android.jobqueue.JobManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Observable;

import de.greenrobot.event.EventBus;


/**
 * Created by Febin John James on 21/06/14.
 */
public class MyDDPState extends DDPStateSingleton {

    private Map<String, Sport> sports;
    public String TAG="MY DDP State";
    public DDPSTATE mDDPState;
    private static Context mContext;

    SQLiteDatabase boutdb;
    static SQLController dbController;
    BoutDBHelper dbHelper;
    EventBus eventBus;
    SharedPreferences preferences;
    JobManager jobManager;


    private MyDDPState(Context context) {
        // Constructor hidden because this is a singleton
        super(context);
        this.mContext = context;

    }

    public static void initInstance(Context context)  {
        // only called by MyApplication
        if (mInstance == null) {
            // Create the instance
            mInstance = new MyDDPState(context);

        }
    }




    public static MyDDPState getInstance() {
        // Return the instance
        return (MyDDPState) mInstance;
    }
    @Override
    public void createDDPCLient()
    {
        String sMeteorServer = "boutrep0.cloudapp.net";
        Integer sMeteorPort = 80;
        try {
            mDDP = new DDPClient(sMeteorServer, sMeteorPort);

        } catch (URISyntaxException e) {
            Log.e(TAG, "Invalid Websocket URL connecting to " + sMeteorServer
                    + ":" + sMeteorPort);
        }
        getDDP().addObserver(this);
        mDDPState = mDDPState.NotLoggedIn;

    }

    @Override
    public void update(Observable client, Object msg) {
        super.update(client, msg);


        if (msg instanceof Map<?, ?>) {
            Map<String, Object> jsonFields = (Map<String, Object>) msg;
            // handle msg types for DDP server->client msgs:
            // https://github.com/meteor/meteor/blob/master/packages/livedata/DDP.md
            String msgtype = (String) jsonFields
                    .get(DDPClient.DdpMessageField.MSG);

            String errormsg="";
            if(jsonFields.containsKey("errormsg"))
            {
                errormsg = (String) jsonFields.get("errormsg");
            }

            if (msgtype == null) {
                Log.i("NULL",msg.toString());
                // ignore {"server_id":"GqrKrbcSeDfTYDkzQ"} web socket msgs
                return;
            } else if (msgtype.equals(DdpMessageType.ERROR)) {

                if(errormsg.contains("Unknown websocket error (exception in callback?"))
                {
                    mDDPState = DDPSTATE.Closed;
                 Log.e("MSG", msg.toString());
                    reconnect();

                }




            } else if (msgtype.equals(DdpMessageType.CONNECTED)) {

                Log.e("Conencted","Its connected");
                mDDPState = DDPSTATE.Connected;


                jobManager = MyApplication.getInstance().getJobManager();

                jobManager.addJobInBackground(new Login());

                broadcastConnectionState(mDDPState);
            }
            else if(msgtype.equals(DdpMessageType.CLOSED))
            {
                mDDPState = DDPSTATE.Closed;
                Log.e("Connection Closed","connection closed");
                broadcastDDPError("Connection closed");

                reconnect();

            }
        }
    }

    public void reconnect()
    {

        jobManager = MyApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(new Connect());


    }


    public DDPSTATE getDDPState()
    {
        return mDDPState;
    }

    public void tagDevideId(String deviceId)
    {
        Object[] parameters = new Object[1];
        parameters[0]=deviceId;
        mDDP.call("addDeviceId",parameters,new DDPListener(){

            @Override
            public void onResult(Map<String, Object> resultFields) {
                super.onResult(resultFields);
                if (resultFields.containsKey("result")) {

                  Log.i(TAG,resultFields.toString());
                  Log.i(TAG,"DEVICE ID TAGGED");


                }


            }


        });

    }


    public void disconnect()
    {

        mDDP.disconnect();

    }

    public void followSport(final String sportId) throws Throwable
    {

        Object[] parameters = new Object[1];
        parameters[0] = sportId;


         mDDP.call("updateFollowSports", parameters, new DDPListener() {


            @Override
            public void onResult(Map<String, Object> resultFields) {

                Log.e("RESPONSE", resultFields.toString());
                super.onResult(resultFields);
                if (resultFields.containsKey("result")) {


                    String response = resultFields.get("result").toString();

                    Log.e("Respnse", response.toString());
                    Log.e("Sport Preferences", response);

                    if (response.matches("added") || response.matches("removed")) {


                        // Do nothing


                    } else {


                    }


                } else {


                    jobManager = MyApplication.getInstance().getJobManager();
                   jobManager.addJobInBackground(new SendSportPreferences(sportId));


                }


            }


        });


    }


    public void followTournament(final String tournamentId) throws Throwable
    {

        Object[] parameters = new Object[1];
        parameters[0] = tournamentId;


        mDDP.call("updateFollowTournament", parameters, new DDPListener() {


            @Override
            public void onResult(Map<String, Object> resultFields) {

                Log.e("RESPONSE", resultFields.toString());
                super.onResult(resultFields);
                if (resultFields.containsKey("result")) {


                    String response = resultFields.get("result").toString();


                    if (response.matches("added") || response.matches("removed")) {


                        // Do nothing


                    } else {

                    }


                } else {


                    jobManager = MyApplication.getInstance().getJobManager();
                   jobManager.addJobInBackground(new SendTournamentPreferences(tournamentId));


                }


            }


        });


    }



    public void processFbRequests(final Object[] parameters) throws Throwable
    {



        mDDP.call("processBanterRequests", parameters, new DDPListener() {


            @Override
            public void onResult(Map<String, Object> resultFields) {

                Log.e("RESPONSE", resultFields.toString());
                super.onResult(resultFields);
                if (resultFields.containsKey("result")) {


                    String response = resultFields.get("result").toString();


                    if (response.matches("added") || response.matches("removed")) {


                        // Do nothing


                    } else {

                    }


                } else {


                    jobManager = MyApplication.getInstance().getJobManager();
                    jobManager.addJobInBackground(new ProcessFbRequests(parameters));


                }


            }


        });


    }


    public void requestFbUser(final Object[] parameters) throws Throwable
    {



        mDDP.call("requestFbUser", parameters, new DDPListener() {


            @Override
            public void onResult(Map<String, Object> resultFields) {

                Log.e("RESPONSE", resultFields.toString());
                super.onResult(resultFields);
                if (resultFields.containsKey("result")) {


                    String response = resultFields.get("result").toString();


                    if (response.matches("added") || response.matches("removed")) {


                        // Do nothing


                    } else {

                    }


                } else {


                    jobManager = MyApplication.getInstance().getJobManager();
                    jobManager.addJobInBackground(new RequestFbUser(parameters));


                }


            }


        });


    }

    public void storeTwitterUserInfo(Long twitterId,String userHandle,String profileImageUrl,String fullName)
    {

        preferences = mContext.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong("twitterId",twitterId);
        editor.putString("userHandle",userHandle);
        editor.putString("profileImageUrl",profileImageUrl);
        editor.putString("fullName",fullName);
        editor.commit();


    }

    public void createBanter(final Object[] parameters)  {



        ConversationParameter conversation = new ConversationParameter();
        conversation.name = parameters[0].toString();
        conversation.tournamentId = parameters[1].toString();

        Object[] convoParameters = new Object[1];
        convoParameters[0]=conversation;

        mDDP.call("createBanter", convoParameters, new DDPListener() {


            @Override
            public void onResult(Map<String, Object> resultFields) {

                Log.e("RESPONSE", resultFields.toString());
               // super.onResult(resultFields);
                if (resultFields.containsKey("result")) {



                } else {

                    //This has a purpose.. Don't delete
                  jobManager = MyApplication.getInstance().getJobManager();
                  jobManager.addJobInBackground(new CreateBanter(parameters));


                }


            }


        });


    }

    public void sendMessage(final Object[] parameters)  {



        MessageParameter message = new MessageParameter();
        message.banterId = parameters[0].toString();
        message.message=parameters[1].toString();



        Object[] messageParameters = new Object[1];
        messageParameters[0]=message;



        mDDP.call("sendMessage", messageParameters, new DDPListener() {


                @Override
                public void onResult (Map<String, Object> resultFields){

                Log.e("RESPONSE", resultFields.toString());
                super.onResult(resultFields);
                if (resultFields.containsKey("result")) {


                    String response = resultFields.get("result").toString();

                    Log.e("Respnse", response.toString());

                    if (response.matches("Message was inserted") == true) {


                        // Do nothing


                    } else {


                    }


                } else {

                    //This has a purpose.. Don't delete
                    jobManager = MyApplication.getInstance().getJobManager();
                    jobManager.addJobInBackground(new SendMessage(parameters));


                }


            }




        });

    }


    public void boutlineLogin() throws Throwable {

        FacebookUserInfo fbUser;


        preferences = mContext.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);


        String storedFbInfoString = preferences.getString("fbUserInfo", null);
        Gson gson = new Gson();

        if(storedFbInfoString!=null) {

            fbUser = gson.fromJson(storedFbInfoString, FacebookUserInfo.class);

        }

        else
        {

            Log.e("It's null","Null");
            return;
        }

            if (mDDPState != mDDPState.LoggedIn)

        {

            Object[] parameters = new Object[1];
            parameters[0] = fbUser;

            mDDP.call("connectFacebookUser", parameters, new DDPListener() {

                @Override
                public void onResult(Map<String, Object> resultFields) {
                    super.onResult(resultFields);
                    if (resultFields.containsKey("result")) {

                        Map<String, Object> result = (Map<String, Object>) resultFields
                                .get(DdpMessageField.RESULT);
                        if (result.containsKey("userId")) {

                           preferences = mContext.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
                           SharedPreferences.Editor editor = preferences.edit();
                           editor.putString("boutlineUserId", result.get("userId").toString());
                           editor.commit();

                            mDDPState = mDDPState.LoggedIn;
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction("LOGINSUCCESS");
                            broadcastIntent.putExtra("userId",
                                    result.get("userId").toString());
                            LocalBroadcastManager.getInstance(
                                    MyApplication.getAppContext()).sendBroadcast(
                                    broadcastIntent);
                        } else {
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction("LOGINFAILED");
                            broadcastIntent.putExtra("Error",
                                    "Unable to Login");
                            LocalBroadcastManager.getInstance(
                                    MyApplication.getAppContext()).sendBroadcast(
                                    broadcastIntent);
                           mDDPState = mDDPState.NotLoggedIn;
                        }


                    }


                }


            });

        }

        else

            {
                Log.e("MyDDPState","already logged In");
                mDDPState = mDDPState.LoggedIn;
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("LOGINSUCCESS");
                LocalBroadcastManager.getInstance(
                        MyApplication.getAppContext()).sendBroadcast(
                        broadcastIntent);
            }

    }



    @Override
    public void broadcastSubscriptionChanged(String collectionName,
                                             String changetype, String docId) {



        try {
            if (collectionName.equals("sports")) {

                if (changetype.equals(DdpMessageType.ADDED)) {

                    Sport sport = new Sport(docId,getCollection(collectionName).get(docId));


                dbHelper.getInstance(mContext).putSport(sport);

                } else if (changetype.equals(DdpMessageType.REMOVED)) {


                } else if (changetype.equals(DdpMessageType.UPDATED)) {

                    Sport sport = new Sport(docId,getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putSport(sport);

                }
            }
            else if(collectionName.equals("tournaments"))
            {
                if (changetype.equals(DdpMessageType.ADDED)) {

                    Log.e("Item added","TOurnaments");

                    Tournament tournament = new Tournament(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putTournament(tournament);

                }  else if (changetype.equals(DdpMessageType.REMOVED)) {


                } else if (changetype.equals(DdpMessageType.UPDATED)) {

                    Tournament tournament = new Tournament(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putTournament(tournament);

                }


              }

            else if(collectionName.equals("matches"))
            {

                if (changetype.equals(DdpMessageType.ADDED)) {

                    Log.e("Item added","Matches");

                    Match match = new Match(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putMatch(match);

                }  else if (changetype.equals(DdpMessageType.REMOVED)) {


                } else if (changetype.equals(DdpMessageType.UPDATED)) {

                    Match match = new Match(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putMatch(match);

                }

            }
            else if(collectionName.equals("tweets"))
            {


                if (changetype.equals(DdpMessageType.ADDED)) {

                    Log.e("Item added","Tweets");



                    Tweet tweet = new Tweet(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putTweet(tweet);

                }  else if (changetype.equals(DdpMessageType.REMOVED)) {


                } else if (changetype.equals(DdpMessageType.UPDATED)) {

                    Tweet tweet = new Tweet(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putTweet(tweet);

                }



            }

            else if(collectionName.equals("banters"))
            {

                if (changetype.equals(DdpMessageType.ADDED)) {

                    Log.e("Item added","Conversations");

                    Conversation conversation = new Conversation(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putConversation(conversation);

                }  else if (changetype.equals(DdpMessageType.REMOVED)) {


                } else if (changetype.equals(DdpMessageType.UPDATED)) {

                    Conversation conversation = new Conversation(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putConversation(conversation);

                }

            }
            else if(collectionName.equals("messages"))
            {


                if (changetype.equals(DdpMessageType.ADDED)) {

                    Log.e("Item added","Messages");

                    Message message = new Message(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putMessage(message);

                }  else if (changetype.equals(DdpMessageType.REMOVED)) {


                } else if (changetype.equals(DdpMessageType.UPDATED)) {

                    Message message = new Message(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putMessage(message);

                }



            }
       }
       catch (Exception E)
        {
           E.printStackTrace();
        }

        // do the broadcast after we've taken care of our parties wrapper
        super.broadcastSubscriptionChanged(collectionName, changetype, docId);
    }


}
