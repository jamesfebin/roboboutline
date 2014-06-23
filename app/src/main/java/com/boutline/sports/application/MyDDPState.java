package com.boutline.sports.application;

import com.boutline.sports.models.Tweet;
import com.keysolutions.ddpclient.android.DDPStateSingleton;
import android.content.Context;

import com.keysolutions.ddpclient.DDPClient;
import android.util.Log;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.keysolutions.ddpclient.DDPClient.DdpMessageField;
import com.keysolutions.ddpclient.DDPClient.DdpMessageType;

/**
 * Created by Febin John James on 21/06/14.
 */
public class MyDDPState extends DDPStateSingleton {

    private Map<String, Tweet> tweets;
    public String TAG="MY DDP State";
    private DDPSTATE mDDPState;

    private MyDDPState(Context context) {
        // Constructor hidden because this is a singleton
        super(context);
        tweets = new ConcurrentHashMap<String, Tweet>();
    }

    public static void initInstance(Context context) {
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


    @Override
    public void broadcastSubscriptionChanged(String collectionName,
                                             String changetype, String docId) {
/*
        try {
            if (collectionName.equals("tweets")) {
                if (changetype.equals(DdpMessageType.ADDED)) {
                 //Tweet newTweet=new Tweet(docId,(Map<String, Object>) getCollection(collectionName).get(docId));

                tweets.put(docId, new Tweet(docId, (Map<String, Object>) getCollection(collectionName).get(docId)));

                } else if (changetype.equals(DdpMessageType.REMOVED)) {
                   tweets.remove(docId);
                } else if (changetype.equals(DdpMessageType.UPDATED)) {
                    tweets.get(docId).refreshFields();
                }
            }
       }
       catch (Exception E)
        {
           E.printStackTrace();
        }
*/
        // do the broadcast after we've taken care of our parties wrapper
        super.broadcastSubscriptionChanged(collectionName, changetype, docId);
    }


}
