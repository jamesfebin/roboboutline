package com.boutline.sports.application;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.database.SQLController;
import com.boutline.sports.helpers.FormateTime;
import com.boutline.sports.jobs.Connect;
import com.boutline.sports.jobs.CreateBanter;
import com.boutline.sports.jobs.ImageUpload;
import com.boutline.sports.jobs.Login;
import com.boutline.sports.jobs.ProcessFbRequests;
import com.boutline.sports.jobs.RequestFbUser;
import com.boutline.sports.jobs.SendMessage;
import com.boutline.sports.jobs.SendSportPreferences;
import com.boutline.sports.jobs.SendTournamentPreferences;
import com.boutline.sports.models.BanterMessage;
import com.boutline.sports.models.Conversation;
import com.boutline.sports.models.ConversationParameter;
import com.boutline.sports.models.FacebookUserInfo;
import com.boutline.sports.models.Match;
import com.boutline.sports.models.Message;
import com.boutline.sports.models.MessageParameter;
import com.boutline.sports.models.MeteorUser;
import com.boutline.sports.models.Profile;
import com.boutline.sports.models.Query;
import com.boutline.sports.models.Sport;

import com.boutline.sports.models.Team;
import com.boutline.sports.models.Tournament;
import com.boutline.sports.models.Tweet;
import com.google.gson.Gson;
import com.keysolutions.ddpclient.DDPListener;
import com.keysolutions.ddpclient.DDPClient.DdpMessageField;
import com.keysolutions.ddpclient.DDPClient.DdpMessageType;
import com.keysolutions.ddpclient.EmailAuth;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;
import java.util.regex.Pattern;

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
       //String sMeteorServer = "boutrep0.cloudapp.net";
       //Integer sMeteorPort = 80;

       String sMeteorServer = "192.168.1.5";
        Integer sMeteorPort = 3000;

        try {

            mDDP = new DDPClient(sMeteorServer, sMeteorPort);

        } catch (URISyntaxException e) {
            Log.d(TAG, "Invalid Websocket URL connecting to " + sMeteorServer
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
                 Log.d("MSG", msg.toString());
                    reconnect();

                }




            } else if (msgtype.equals(DdpMessageType.CONNECTED)) {

                Log.d("Conencted","Its connected");
                mDDPState = DDPSTATE.Connected;


                jobManager = MyApplication.getInstance().getJobManager();

                jobManager.addJobInBackground(new Login());

                broadcastConnectionState(mDDPState);
            }
            else if(msgtype.equals(DdpMessageType.CLOSED))
            {
                mDDPState = DDPSTATE.Closed;
                Log.d("Connection Closed","connection closed");
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


    public void BoutQuery(String methodName,Object[] parameters)
    {

        Log.e("I was called","called");

        mDDP.call(methodName, parameters, new DDPListener() {


            @Override
            public void onResult(Map<String, Object> resultFields) {

                Log.d("RESPONSE", resultFields.toString());
                super.onResult(resultFields);
                if (resultFields.containsKey("result")) {


                String response = resultFields.get("result").toString();

                Log.e("Response from BoutBot is",response);


                        while (response.contains("convertUnixtime(") == true) {
                            if (response.contains("convertUnixtime(")) {
                                String[] words = response.split(Pattern.quote("convertUnixtime("));
                                if(words.length>1) {
                                    words[1] = words[1].replace(")", "");
                                    words[1] = words[1].replaceAll("[^0-9]","");
                                    Log.e("unxitime", words[1]);
                                    FormateTime formateTime = new FormateTime();
                                    String formattedTime = formateTime.formatUnixtime(words[1], "dd MMM yyyy, hh:mm a");
                                    response = response.replace("convertUnixtime(" + words[1] + ")", formattedTime);
                                    Log.e("RESPONSE", response);
                                }
                            }
                        }

                    if(response.contains("\"type\":\"reminder\""))
                    {
                        response = "Sure , i will remind you";
                    }


                    UUID uniqueKey = UUID.randomUUID();
                    String mDocId = "bot"+uniqueKey.toString();
                    final long unixTime = System.currentTimeMillis() / 1000L;
                    Map<String,Object> fields = new HashMap<String, Object>();
                    fields.put("name","Bout Bot");
                    fields.put("message",response);
                    fields.put("banterId","Q83GjTwRCk4FNTSEJ");
                    fields.put("userPicUrl","http://i1294.photobucket.com/albums/b615/jamesfebin/pasted_image_at_2014_08_21_03_42_pm_zps1a09f96b.png");
                    fields.put("sender","BotId");
                    fields.put("time",unixTime);
                    Message message = new Message(mDocId,fields);
                    dbHelper.getInstance(mContext).putMessage(message,"");

                }


            }


        });





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

                Log.d("RESPONSE", resultFields.toString());
                super.onResult(resultFields);
                if (resultFields.containsKey("result")) {


                    String response = resultFields.get("result").toString();

                    Log.d("Respnse", response.toString());
                    Log.d("Sport Preferences", response);

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

                Log.d("RESPONSE", resultFields.toString());
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

                Log.d("RESPONSE", resultFields.toString());
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

                Log.d("RESPONSE", resultFields.toString());
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
        editor.putString("twitterProfileImageUrl",profileImageUrl);
        editor.putString("twitterFullName",fullName);
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

                Log.d("RESPONSE", resultFields.toString());
               // super.onResult(resultFields);
                if (resultFields.containsKey("result")) {



                } else {

                  jobManager = MyApplication.getInstance().getJobManager();
                  jobManager.addJobInBackground(new CreateBanter(parameters));


                }


            }


        });


    }


    public void getSASURLBackground(final String filename, final String filepath)
    {
        Object[] parameters = new Object[1];
        parameters[0]=filename;

        mDDP.call("genSASURL", parameters, new DDPListener() {
            @Override
            public void onResult(Map<String, Object> jsonFields) {

                if(jsonFields.containsKey("result"))
                {

                   jobManager = MyApplication.getInstance().getJobManager();
                   jobManager.addJobInBackground(new ImageUpload(jsonFields.get("result").toString(),filepath));

                    preferences = mContext.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);

                    String[] imageUrlArray = jsonFields.get("result").toString().split("\\?");
                    String imageUrl = imageUrlArray[0];

                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putString("profileImageUrl",imageUrl);
                    edit.commit();

                }

            }
        });


    }


    public void getSASURL(String filename)
    {

        Object[] parameters = new Object[1];
        parameters[0]=filename;

         mDDP.call("genSASURL", parameters, new DDPListener() {
                    @Override
                    public void onResult(Map<String, Object> jsonFields) {

                    if(jsonFields.containsKey("result"))
                    {

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("SASURL");
                        broadcastIntent.putExtra("SASURL",
                                jsonFields.get("result").toString());

                        LocalBroadcastManager.getInstance(
                                MyApplication.getAppContext()).sendBroadcast(
                                broadcastIntent);


                    }

                    }
                });



    }

    public void forgotPassword(String email) {

        Object[] parameters = new Object[1];
        Map<String, Object> options = new HashMap<String, Object>();
        parameters[0] = options;
        options.put("email", email);

        mDDP.call("resetUserPassword", parameters, new DDPListener() {



            @Override
            public void onResult(Map<String, Object> resultFields) {


                if(resultFields.containsKey("result"))
                {
                    if(resultFields.get("result").toString().matches("success"))
                    {

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("EMAILSEND");
                        broadcastIntent.putExtra("Error",
                                "Email Id Doesn't Exist");
                        LocalBroadcastManager.getInstance(
                                MyApplication.getAppContext()).sendBroadcast(
                                broadcastIntent);

                    }
                    else
                    {
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("EMAILFAILED");
                        broadcastIntent.putExtra("Error",
                                "Email Id Doesn't Exist");
                        LocalBroadcastManager.getInstance(
                                MyApplication.getAppContext()).sendBroadcast(
                                broadcastIntent);


                    }
                }
                else
                {

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("EMAILFAILED");
                    broadcastIntent.putExtra("Error",
                            "Internet connection not available");
                    LocalBroadcastManager.getInstance(
                            MyApplication.getAppContext()).sendBroadcast(
                            broadcastIntent);


                }
                Log.d("FORGOT PASSWORD",resultFields.toString());
            }

        });
    }

    public void sendMessage(final Object[] parameters)  {



        MessageParameter message = new MessageParameter();
        message.banterId = parameters[0].toString();
        message.message=parameters[1].toString();
        message.uId = parameters[2].toString();


        Object[] messageParameters = new Object[1];
        messageParameters[0]=message;



        mDDP.call("sendMessage", messageParameters, new DDPListener() {


                @Override
                public void onResult (Map<String, Object> resultFields){

                Log.d("RESPONSE", resultFields.toString());
                super.onResult(resultFields);
                if (resultFields.containsKey("result")) {


                    String response = resultFields.get("result").toString();

                    Log.d("Respnse", response.toString());

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



    public void updateProfileDetails()
    {

        Object[] methodArgs = new Object[1];


        preferences = mContext.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
        String fullName = preferences.getString("fullName","");
        String ProfileImageUrl = preferences.getString("profileImageUrl","");

        Profile profile = new Profile();
        profile.name=fullName;
        profile.profilePictureUrl = ProfileImageUrl;
        methodArgs[0] = profile;

        mDDP.call("updateUserProfileDetails", methodArgs, new DDPListener() {
            @Override
            public void onResult(Map<String, Object> jsonFields) {


                if (jsonFields.containsKey("result")) {

                }
            }
        });



                }
    public void createUser(final String username,final String password,final String email )
    {

        Object[] methodArgs = new Object[1];
        Map<String,Object> options = new HashMap<String,Object>();

        options.put("username",username);
        options.put("email",email);
        options.put("password",password);

        methodArgs[0] = options;

        mDDP.call("createUser", methodArgs, new DDPListener() {
            @Override
            public void onResult(Map<String, Object> jsonFields) {


                if (jsonFields.containsKey("result")) {
                    Map<String, Object> result = (Map<String, Object>) jsonFields
                            .get(DdpMessageField.RESULT);



                    String mUserId = (String) result.get("id");
                    preferences = mContext.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("boutlineUserId", mUserId);
                    editor.putString("email",username);
                    editor.putString("password",password);
                    editor.commit();

                    mDDPState = mDDPState.LoggedIn;
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("REGISTRATIONSUCCESS");
                    broadcastIntent.putExtra("userId",
                            mUserId);
                    LocalBroadcastManager.getInstance(
                            MyApplication.getAppContext()).sendBroadcast(
                            broadcastIntent);
                    Log.d("UserId",mUserId);


                } else if (jsonFields.containsKey("error")) {



                    String error = jsonFields.get("error").toString();

                    String[] ErrorArray = error.split(",");

                    if(ErrorArray.length>=1)
                    {
                        error = ErrorArray[1];
                        error=error.replace("reason=","");
                    }

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("REGISTRATIONFAILED");
                    broadcastIntent.putExtra("Error",
                            error);
                    LocalBroadcastManager.getInstance(
                            MyApplication.getAppContext()).sendBroadcast(
                            broadcastIntent);


                }



            }
        });


    }

    public void boutlineFirstTimeLogin(final String email, final String password)  {


        if(email == null || password ==null)
        {
            return;
        }

        Object[] methodArgs = new Object[1];
        MeteorUser user = new MeteorUser(email,password);
        methodArgs[0] = user;
        mDDP.call("login", methodArgs, new DDPListener() {
            @Override
            public void onResult(Map<String, Object> jsonFields) {

                if (jsonFields.containsKey("result")) {
                    Map<String, Object> result = (Map<String, Object>) jsonFields
                            .get(DdpMessageField.RESULT);
                    String mUserId = (String) result.get("id");
                    preferences = mContext.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("boutlineUserId", mUserId);
                    editor.putString("email",email);
                    editor.putString("password",password);
                    editor.commit();

                    mDDPState = mDDPState.LoggedIn;
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("LOGINSUCCESS");
                    broadcastIntent.putExtra("userId",
                            mUserId);
                    LocalBroadcastManager.getInstance(
                            MyApplication.getAppContext()).sendBroadcast(
                            broadcastIntent);
                    Log.d("UserId",mUserId);

                } else if (jsonFields.containsKey("error")) {

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("LOGINFAILED");
                    broadcastIntent.putExtra("Error",
                            "Unable to Login");
                    LocalBroadcastManager.getInstance(
                            MyApplication.getAppContext()).sendBroadcast(
                            broadcastIntent);
                }


            }
        });

    }
    public void boutlineLogin() throws Throwable {


        String email="";
        String password="";
        preferences = mContext.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
       email = preferences.getString("email",null);
       password = preferences.getString("password",null);
        if(email == null || password ==null)
        {
            return;
        }
        Object[] methodArgs = new Object[1];
        MeteorUser user = new MeteorUser(email,password);
        methodArgs[0] = user;
        mDDP.call("login", methodArgs, new DDPListener() {
            @Override
            public void onResult(Map<String, Object> jsonFields) {

                if (jsonFields.containsKey("result")) {
                    Map<String, Object> result = (Map<String, Object>) jsonFields
                            .get(DdpMessageField.RESULT);
                    String mUserId = (String) result.get("id");
                    preferences = mContext.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("boutlineUserId", mUserId);
                    editor.commit();

                    mDDPState = mDDPState.LoggedIn;
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("LOGINSUCCESS");
                    broadcastIntent.putExtra("userId",
                            mUserId);
                    LocalBroadcastManager.getInstance(
                            MyApplication.getAppContext()).sendBroadcast(
                            broadcastIntent);
                 Log.d("UserId",mUserId);

                } else if (jsonFields.containsKey("error")) {

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("LOGINFAILED");
                    broadcastIntent.putExtra("Error",
                            "Unable to Login");
                    LocalBroadcastManager.getInstance(
                            MyApplication.getAppContext()).sendBroadcast(
                            broadcastIntent);

                }



            }
        });

    }

    public void boutlineFacebookLogin() throws Throwable {

        FacebookUserInfo fbUser;


        preferences = mContext.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);


        String storedFbInfoString = preferences.getString("fbUserInfo", null);
        Gson gson = new Gson();

        if(storedFbInfoString!=null) {

            fbUser = gson.fromJson(storedFbInfoString, FacebookUserInfo.class);

        }

        else
        {

            Log.d("It's null","Null");
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
            Log.d("MyDDPState","already logged In");
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

            if(collectionName.equals("teams"))
            {
                if (changetype.equals(DdpMessageType.ADDED)) {

                    Team team = new Team(docId,getCollection(collectionName).get(docId));


                    dbHelper.getInstance(mContext).putTeam(team);

                } else if (changetype.equals(DdpMessageType.REMOVED)) {


                } else if (changetype.equals(DdpMessageType.UPDATED)) {

                    Team team = new Team(docId,getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putTeam(team);

                }

            }

            else if(collectionName.equals("queries")){


                if (changetype.equals(DdpMessageType.ADDED)) {

                    Query query = new Query(docId,getCollection(collectionName).get(docId));


                    dbHelper.getInstance(mContext).putQuery(query);

                } else if (changetype.equals(DdpMessageType.REMOVED)) {


                } else if (changetype.equals(DdpMessageType.UPDATED)) {

                    Query query = new Query(docId,getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putQuery(query);

                }
            }
            else if (collectionName.equals("sports")) {

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

                    Log.d("Item added","Tournaments");

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

                    Log.d("Item added","Matches");

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

                    Log.d("Item added","Tweets");

                   /* BanterMessage message = new BanterMessage(docId, getCollection(collectionName).get(docId),"tweet");
                    dbHelper.getInstance(mContext).putBanterMessage(message);*/
                    Tweet tweet = new Tweet(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putTweet(tweet);


                }  else if (changetype.equals(DdpMessageType.REMOVED)) {


                } else if (changetype.equals(DdpMessageType.UPDATED)) {
                   /* BanterMessage message = new BanterMessage(docId, getCollection(collectionName).get(docId),"tweet");
                    dbHelper.getInstance(mContext).putBanterMessage(message);
             */
                    Tweet tweet = new Tweet(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putTweet(tweet);
                }



            }

            else if(collectionName.equals("banters"))
            {

                if (changetype.equals(DdpMessageType.ADDED)) {

                    Log.d("Item added","Conversations");

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

                    Log.d("Item added","Messages");


/*
                    BanterMessage message = new BanterMessage(docId, getCollection(collectionName).get(docId),"message");
                    dbHelper.getInstance(mContext).putBanterMessage(message);
                 */
                    Message message = new Message(docId, getCollection(collectionName).get(docId));
                    Map<String,Object> messageMap;
                    messageMap=getCollection(collectionName).get(docId);
                    String uId="";
                    if(messageMap.containsKey("uId"))
                    {
                        uId = messageMap.get("uId").toString();

                    }
                    dbHelper.getInstance(mContext).putMessage(message,uId);



                }  else if (changetype.equals(DdpMessageType.REMOVED)) {


                } else if (changetype.equals(DdpMessageType.UPDATED)) {
                   /* BanterMessage message = new BanterMessage(docId, getCollection(collectionName).get(docId),"message");
                    dbHelper.getInstance(mContext).putBanterMessage(message);
*/
                    Map<String,Object> messageMap;
                    messageMap=getCollection(collectionName).get(docId);
                    String uId="";
                    if(messageMap.containsKey("uId"))
                    {
                        uId = messageMap.get("uId").toString();

                    }
                    Message message = new Message(docId, getCollection(collectionName).get(docId));
                    dbHelper.getInstance(mContext).putMessage(message,uId);

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
