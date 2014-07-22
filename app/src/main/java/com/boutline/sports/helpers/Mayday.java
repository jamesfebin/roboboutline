package com.boutline.sports.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.boutline.sports.activities.TwitterLogin;

public class Mayday {
    private Context context;
    SharedPreferences mSharedPreferences;
    public Mayday(Context context) {
        this.context = context;
    }


    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public boolean hasTwitterCredentials ()
    {

        mSharedPreferences = context.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);

        if(mSharedPreferences.getString("twitter_access_token",null)!=null && mSharedPreferences.getString("twitter_access_token_secret",null)!=null)
            return true;
        else
        return false;



    }

    public void askForTwitterCredentials()
    {

        Intent intent = new Intent(context, TwitterLogin.class);
        Activity activity = (Activity) context;
        activity.startActivity(intent);

    }

    public String getTwitterAccessToken()
    {
        mSharedPreferences = context.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
        return mSharedPreferences.getString("twitter_access_token",null);
    }

    public String getTwitterAccessTokenSecret()
    {
        mSharedPreferences = context.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
        return mSharedPreferences.getString("twitter_access_token_secret",null);

    }


}