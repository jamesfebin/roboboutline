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
import com.boutline.sports.activities.ComposeTweetActivity;
import com.boutline.sports.activities.TwitterLogin;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.database.BoutDBHelper;
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
    BoutDBHelper dbHelper;
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
        ImageView reply;

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
               viewHolder.reply = (ImageView) convertView.findViewById(R.id.reply);
               convertView.setTag(viewHolder);
           } else {
               viewHolder = (ViewHolder) convertView.getTag();
           }

           final String mDocId = c.getString(c.getColumnIndex("_id"));
           // Populate the data into the template view using the data object

           final String userFullname = c.getString(c.getColumnIndex(Tweet.COL_UserFullName));
           final String userHanlde = c.getString(c.getColumnIndex(Tweet.COL_UserHandle));
           final String tweet = c.getString(c.getColumnIndex(Tweet.COL_Tweet));
           viewHolder.lblTweetUsername.setText(userFullname);
           viewHolder.lblTweetHandle.setText("@" + userHanlde);
           viewHolder.lblTweetMessage.setText(tweet);

           AQuery aq = new AQuery(context);
           ImageOptions options = new ImageOptions();
           final String status_id = c.getString(c.getColumnIndex(Tweet.COL_StatusId));
           final String image_url = c.getString(c.getColumnIndex(Tweet.COL_ProfileImage));
           options.round = 35;

           aq.id(viewHolder.imgProfile).image(image_url, options);

           // Set up the fonts

           tf = Typeface.createFromAsset(context.getAssets(), fontPath);
           btf = Typeface.createFromAsset(context.getAssets(), boldFontPath);
           viewHolder.lblTweetUsername.setTypeface(btf);
           viewHolder.lblTweetHandle.setTypeface(tf);
           viewHolder.lblTweetTime.setTypeface(tf);
           viewHolder.lblTweetMessage.setTypeface(tf);

           if(c.getInt(c.getColumnIndex("user_retweeted"))==1)
           {
               viewHolder.retweet.setImageResource(R.drawable.retweeted);

           }
           else
           {
               viewHolder.retweet.setImageResource(R.drawable.retweet);

           }
           if(c.getInt(c.getColumnIndex("user_favorited"))==1)
           {
               viewHolder.favorite.setImageResource(R.drawable.favorited);
           }
           else
           {
               viewHolder.favorite.setImageResource(R.drawable.favorite);
           }

           final String media_url=c.getString(c.getColumnIndex("media_url"));

           if (media_url.matches("")) {
               viewHolder.imgTweetImage.setVisibility(View.GONE);
           } else {


               viewHolder.imgTweetImage.setVisibility(View.VISIBLE);

               options = new ImageOptions();

              String  tweet_image_url = c.getString(c.getColumnIndex("media_url"));

               aq.id(viewHolder.imgTweetImage).image(tweet_image_url, options);


           }



           viewHolder.favorite.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   if (mayday.hasTwitterCredentials()) {

                       favortite(status_id, mDocId);

                   } else {
                       mayday.askForTwitterCredentials();
                   }

               }
           });


           viewHolder.retweet.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {


                   if (mayday.hasTwitterCredentials()) {

                       retweet(status_id, mDocId);


                   } else {
                       mayday.askForTwitterCredentials();
                   }
               }
           });


           viewHolder.reply.setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View view) {

                   Intent intent = new Intent(context, ComposeTweetActivity.class);

                   intent.putExtra("replyToTweetStatusId",status_id);
                   intent.putExtra("replyToUserProPicUrl",image_url);
                   intent.putExtra("replyToUserFullname",userFullname);
                   intent.putExtra("replyToUserHandle",userHanlde);
                   intent.putExtra("replyToTweet",tweet);
                   intent.putExtra("replyToTweetImage",media_url);
                   context.startActivity(intent);


               }
           });

       }
       return convertView;

   }

    public void retweet(String statusId,String mDocId)
    {
        Long tweet_status_id = Long.parseLong(statusId);
        dbHelper.getInstance(context).putRetweet(mDocId,mayday.getTwitterAccessToken(),mayday.getTwitterAccessTokenSecret(),tweet_status_id);
    }

    public void favortite(String statusId,String mDocId)
    {
        Long tweet_status_id = Long.parseLong(statusId);
        dbHelper.getInstance(context).putFavorite(mDocId, mayday.getTwitterAccessToken(), mayday.getTwitterAccessTokenSecret(),tweet_status_id);

    }

    public void reply(String statusId,String handle, String message)
    {

    }
    public void tweet(String message)
    {

    }



}