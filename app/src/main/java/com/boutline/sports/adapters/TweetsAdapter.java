package com.boutline.sports.adapters;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.boutline.sports.activities.TwitterLogin;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.helpers.Mayday;
import com.boutline.sports.jobs.Favorite;
import com.boutline.sports.jobs.Retweet;
import com.boutline.sports.models.Tweet;
import com.boutline.sports.R;
import com.path.android.jobqueue.JobManager;

import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class TweetsAdapter extends SimpleCursorAdapter {

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;
    Context context;
    Mayday mayday;
    JobManager jobManager;
    
	// View lookup cache
    private static class ViewHolder {
        RelativeLayout tweetContainer;
        TextView lblTweetUsername;
        TextView lblTweetHandle;
        TextView lblTweetMessage;
        TextView lblTweetTime;
        ImageView imgTweetImage;
        ImageView imgProfile;
        ImageView retweet;
        ImageView favorite;

    }


    public TweetsAdapter(Context context, int layout, Cursor c,
                         String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        mayday = new Mayday(context);
        jobManager = MyApplication.getInstance().getJobManager();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
       // Get the data item for this position
    	

        Cursor c = getCursor();



       // Check if an existing view is being reused, otherwise inflate the view
       if(c.moveToPosition(position)) {

           ViewHolder viewHolder; // view lookup cache stored in tag
           if (convertView == null) {
               viewHolder = new ViewHolder();
               LayoutInflater inflater = LayoutInflater.from(context);
               convertView = inflater.inflate(R.layout.item_tweet, parent, false);
               viewHolder.tweetContainer = (RelativeLayout) convertView.findViewById(R.id.tweetContainer);
               viewHolder.lblTweetUsername = (TextView) convertView.findViewById(R.id.lblTweetUsername);
               viewHolder.lblTweetHandle = (TextView) convertView.findViewById(R.id.lblTweetHandle);
               viewHolder.lblTweetMessage = (TextView) convertView.findViewById(R.id.lblTweetMessage);
               viewHolder.lblTweetTime = (TextView) convertView.findViewById(R.id.lblTweetTime);
               viewHolder.imgTweetImage = (ImageView) convertView.findViewById(R.id.imgTweetImage);
               viewHolder.imgProfile = (ImageView) convertView.findViewById(R.id.imgPropic);
               viewHolder.retweet = (ImageView) convertView.findViewById(R.id.retweet);
               viewHolder.favorite = (ImageView) convertView.findViewById(R.id.favourite);
               convertView.setTag(viewHolder);
           } else {
               viewHolder = (ViewHolder) convertView.getTag();
           }

           // Populate the data into the template view using the data object

           viewHolder.lblTweetUsername.setText(c.getString(c.getColumnIndex(Tweet.COL_UserFullName)));
           viewHolder.lblTweetHandle.setText("@" + c.getString(c.getColumnIndex(Tweet.COL_UserHandle)));
           viewHolder.lblTweetMessage.setText(c.getString(c.getColumnIndex(Tweet.COL_Tweet)));

           AQuery aq = new AQuery(context);
           ImageOptions options = new ImageOptions();
           final String status_id = c.getString(c.getColumnIndex(Tweet.COL_StatusId));
           String image_url = c.getString(c.getColumnIndex(Tweet.COL_ProfileImage));
           options.round = 35;

           aq.id(viewHolder.imgProfile).image(image_url, options);

           // Set up the fonts

           tf = Typeface.createFromAsset(context.getAssets(), fontPath);
           btf = Typeface.createFromAsset(context.getAssets(), boldFontPath);
           viewHolder.lblTweetUsername.setTypeface(btf);
           viewHolder.lblTweetHandle.setTypeface(tf);
           viewHolder.lblTweetTime.setTypeface(tf);
           viewHolder.lblTweetMessage.setTypeface(tf);

           if (c.getString(c.getColumnIndex("media_url")).matches("")) {
               viewHolder.imgTweetImage.setVisibility(View.GONE);
           } else {


               viewHolder.imgTweetImage.setVisibility(View.VISIBLE);

               options = new ImageOptions();

               String twee_image_url = c.getString(c.getColumnIndex("media_url"));

               aq.id(viewHolder.imgTweetImage).image(twee_image_url, options);


           }

           viewHolder.favorite.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   if (mayday.hasTwitterCredentials()) {

                       favortite(status_id);

                   } else {
                       mayday.askForTwitterCredentials();
                   }

               }
           });


           viewHolder.retweet.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {


                   if (mayday.hasTwitterCredentials()) {

                       retweet(status_id);


                   } else {
                       mayday.askForTwitterCredentials();
                   }
               }
           });

       }

        Animation walkthroughAnim = AnimationUtils.loadAnimation(context, R.anim.pushdownin);
        walkthroughAnim.setDuration(1000);
        walkthroughAnim.setRepeatCount(1);
        walkthroughAnim.setRepeatMode(1);
        walkthroughAnim.setZAdjustment(1);
        convertView.startAnimation(walkthroughAnim);
       return convertView;

   }

    public void retweet(String statusId)
    {
        Long tweet_status_id = Long.parseLong(statusId);
        jobManager.addJobInBackground(new Retweet(tweet_status_id,mayday.getTwitterAccessToken(),mayday.getTwitterAccessTokenSecret()));
        Toast.makeText(context,"Retweeted",Toast.LENGTH_SHORT).show();
    }

    public void favortite(String statusId)
    {
        Long tweet_status_id = Long.parseLong(statusId);
        jobManager.addJobInBackground(new Favorite(tweet_status_id,mayday.getTwitterAccessToken(),mayday.getTwitterAccessTokenSecret()));
        Toast.makeText(context,"Favorited",Toast.LENGTH_SHORT).show();


    }

    public void reply(String statusId,String handle, String message)
    {

    }
    public void tweet(String message)
    {

    }



}