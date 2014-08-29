package com.boutline.sports.receivers;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.boutline.sports.R;
import com.boutline.sports.activities.NotificationRedirect;
import com.boutline.sports.services.GCMIntentService;

/**
 * Created by user on 24/07/14.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        Log.e("YEAA", "RECIEVED");

        if(intent.hasExtra("SHEDULED"))
        {
            Log.e("YEAA", intent.getStringExtra("SHEDULED"));
        }

        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMIntentService.class.getName());

        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }

}
