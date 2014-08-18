package com.boutline.sports.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.boutline.sports.R;
import com.boutline.sports.activities.NotificationRedirect;
import com.boutline.sports.receivers.GcmBroadcastReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by user on 24/07/14.
 */
public class GCMIntentService extends IntentService {

    public static  int NOTIFICATION_ID = 1;
    private static final String TAG = "GCM";
    private NotificationManager mNotificationManager;
    private static SharedPreferences mSharedPreferences;

String userId;

    NotificationCompat.Builder builder;

    public GCMIntentService() {

        super("GcmIntentService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {


        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        mSharedPreferences = getApplicationContext().getSharedPreferences(
                "boutlineData", 0);
        userId = mSharedPreferences.getString("boutlineUserId","");

        String messageType = gcm.getMessageType(intent);


        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
	            /*
	             * Filter messages based on message type. Since it is likely that GCM
	             * will be extended in the future with new message types, just ignore
	             * any message types you're not interested in, or that you don't
	             * recognize.
	             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(),null,null);
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString(),null,null);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.

                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                Log.i("Notification Recieved", extras.toString());

                String type=extras.getString("type",null);
                if(type.matches("banter"))
                {

                    if(mSharedPreferences.getBoolean("banterMute", false))
                    {

                        Log.i("Notification ","Banters are muted ,Notifications can't be displayed");
                        return;
                    }
                    String senderId = extras.getString("senderId",null);


                    if(senderId.matches(userId)==true)
                    {
                        Log.i("Notification ","This matches sender Id");

                        return;
                    }


                    String senderName = extras.getString("senderName",null);
                    String senderPic = extras.getString("senderPic",null);
                    String banterId = extras.getString("banterId",null);
                    String banterName = extras.getString("banterName",null);
                    String message = extras.getString("msg",null);
                    String time = extras.getString("time",null);
                    // Disabling notifications
                   // sendBanterNotification(senderId,senderName,senderPic,banterId,banterName,message,time);
                }

                if(type.matches("match"))
                {

                    if(mSharedPreferences.getBoolean("spike", true))
                    {

                        // sendNotification(extras.getString("msg", null),extras.getString("matchId",null),extras.getString("hashtag",null));
                        Log.i(TAG, "Received: " + extras.toString());
                    }
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    private void sendBanterNotification(String senderId,String senderName,String senderPic,String banterId,String banterName,String message,String time)
    {



        String screenName = mSharedPreferences.getString("currentScreen", null);
        String uniqId = mSharedPreferences.getString("banterId", null);
        Log.e("SCreen name",screenName+banterId);

        if(screenName!=null&&uniqId!=null)
        {
            if(screenName.matches("banter")&&uniqId.matches(banterId))
            {
                Log.e("SCreen name",screenName+banterId);
                return;
            }
        }



        Log.i("Banter MSG is",message.toString());

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent redirectIntent =  new Intent(this, NotificationRedirect.class);
        redirectIntent.putExtra("type", "redirect");
        redirectIntent.putExtra("activity","conversations");
        redirectIntent.putExtra("conversationId", banterId);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                redirectIntent, PendingIntent.FLAG_ONE_SHOT);

        //   final Intent contentIntent = new Intent(this, ChooseTournaments.class);
        // contentIntent.setAction(Intent.ACTION_MAIN);
        //contentIntent.addCategory(Intent.CATEGORY_LAUNCHER);



        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(banterName)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(senderName+" : " + message ))
                        .setContentText(senderName+" : " + message )
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true);


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        NOTIFICATION_ID++;


    }
    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg,String matchId,String hashtag) {

        Log.i("matchId is ",matchId);

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent=null;


        if(matchId!=null)
        {
            Intent redirectIntent =  new Intent(this, NotificationRedirect.class);
            redirectIntent.putExtra("type", "redirect");
            redirectIntent.putExtra("activity","board");
            redirectIntent.putExtra("mtId", matchId);
            redirectIntent.putExtra("hashtag", hashtag);

            contentIntent = PendingIntent.getActivity(this, 0,
                    redirectIntent,PendingIntent.FLAG_ONE_SHOT);


        }


if(contentIntent!=null) {
    NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Boutline Alert!")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true);

    mBuilder.setContentIntent(contentIntent);
    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    NOTIFICATION_ID++;
}
    }

}
