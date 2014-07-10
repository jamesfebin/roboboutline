package com.boutline.sports.application;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.database.SQLController;
import com.boutline.sports.models.FacebookUserInfo;
import com.boutline.sports.models.Sport;
import com.boutline.sports.models.Tweet;
import com.keysolutions.ddpclient.DDPListener;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

import android.app.AlertDialog;
import android.content.Context;

import com.keysolutions.ddpclient.DDPClient;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.keysolutions.ddpclient.DDPClient.DdpMessageField;
import com.keysolutions.ddpclient.DDPClient.DdpMessageType;
import com.keysolutions.ddpclient.DDPListener;

/**
 * Created by Febin John James on 21/06/14.
 */
public class MyDDPState extends DDPStateSingleton {

    private Map<String, Sport> sports;
    public String TAG="MY DDP State";
    private DDPSTATE mDDPState;
    private static Context mContext;

    SQLiteDatabase boutdb;
    static SQLController dbController;
    BoutDBHelper dbHelper;

    private MyDDPState(Context context) {
        // Constructor hidden because this is a singleton
        super(context);
        this.mContext = context;
        sports = new ConcurrentHashMap<String, Sport>();


    }

    public static void initInstance(Context context)  {
        // only called by MyApplication
        if (mInstance == null) {
            // Create the instance
            mInstance = new MyDDPState(context);
            dbController = new SQLController(context);
             mContext = context;

                try {
                    dbController.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        }


    }

    @Override
    public void broadcastDDPError(String errorMsg) {
       super.broadcastDDPError(errorMsg);



        Log.e("ERROR IS",errorMsg);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MESSAGE_ERROR);
        broadcastIntent.putExtra(MESSAGE_EXTRA_MSG, "Unable to connect");



        LocalBroadcastManager.getInstance(
                MyApplication.getAppContext()).sendBroadcast(
                broadcastIntent);


    }


    public static MyDDPState getInstance() {
        // Return the instance
        return (MyDDPState) mInstance;
    }
    @Override
    public void createDDPCLient()
    {
        String sMeteorServer = "boutline.com";
        Integer sMeteorPort = 80;
        try {
            mDDP = new DDPClient(sMeteorServer, sMeteorPort);

        } catch (URISyntaxException e) {
            Log.e(TAG, "Invalid Websocket URL connecting to " + sMeteorServer
                    + ":" + sMeteorPort);
        }
        getDDP().addObserver(this);
        mDDPState = DDPSTATE.NotLoggedIn;

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

    public void boutlineLogin(FacebookUserInfo fbUser)
    {
        Object[] parameters = new Object[1];
        parameters[0]=fbUser;

        mDDP.call("connectFacebookUser",parameters,new DDPListener(){

            @Override
            public void onResult(Map<String, Object> resultFields) {
                super.onResult(resultFields);
                if (resultFields.containsKey("result")) {

                    Map<String, Object> result = (Map<String, Object>) resultFields
                            .get(DdpMessageField.RESULT);

                    if(result.containsKey("userId")) {

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("LOGINSUCCESS");
                        broadcastIntent.putExtra("userId",
                                result.get("userId").toString());
                        LocalBroadcastManager.getInstance(
                                MyApplication.getAppContext()).sendBroadcast(
                                broadcastIntent);
                    }
                    else
                    {
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("LOGINFAILED");
                        broadcastIntent.putExtra("Error",
                               "Unable to Login");
                        LocalBroadcastManager.getInstance(
                                MyApplication.getAppContext()).sendBroadcast(
                                broadcastIntent);

                    }


                }


            }


        });


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
