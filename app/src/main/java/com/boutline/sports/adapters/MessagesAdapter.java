package com.boutline.sports.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.androidquery.util.AQUtility;
import com.boutline.sports.activities.ComposeTweetActivity;
import com.boutline.sports.application.Constants;
import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.helpers.FormateTime;
import com.boutline.sports.helpers.Mayday;
import com.boutline.sports.models.BanterMessage;
import com.boutline.sports.models.Message;
import com.boutline.sports.R;
import com.boutline.sports.models.Tweet;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;

public class MessagesAdapter extends SimpleCursorAdapter {


    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;
    SharedPreferences preferences;
    Context context;
    BoutDBHelper dbHelper;
    Mayday mayday;
    public MixpanelAPI mixpanel = null;


    String lastDate = "";

    public MessagesAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        mayday = new Mayday(context);
        mixpanel = MixpanelAPI.getInstance(context, Constants.MIXPANEL_TOKEN);
        preferences = context.getSharedPreferences("boutlineData",
                Context.MODE_PRIVATE);
        String userId = preferences.getString("boutlineUserId", "");
        mixpanel.identify(userId);

    }

    // View lookup cache
    private static class ViewHolder {
        TextView lblMessage;
        TextView lblSenderName;
        TextView lblMessageTime;
        TextView lblConjunction;
        ImageView imgProPic;
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


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Set up fonts

        preferences = context.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);
        String currentUserId = preferences.getString("boutlineUserId", null);

        FormateTime timeformatter = new FormateTime();

        Cursor c = getCursor();

        if (c.moveToPosition(position)) {
            tf = Typeface.createFromAsset(context.getAssets(), fontPath);
            btf = Typeface.createFromAsset(context.getAssets(), boldFontPath);

            // Get the data item for this position


            String botId = "bouty";

            // Check if an existing view is being reused, otherwise inflate the view

            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);

                Log.d("Message", c.getString(c.getColumnIndex(Message.COL_MESSAGE)) + "");

                if (c.getType(c.getColumnIndex(Message.COL_MESSAGE)) != 0) {

                    String cursorUnixtime = c.getString(c.getColumnIndex(Message.COL_TIME));
                    Log.d("Unixtime", cursorUnixtime);
                    String cursorDate = timeformatter.formatUnixtime(cursorUnixtime, "dd MMM");

                    if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(botId) == true) {
                        convertView = inflater.inflate(R.layout.item_botmessage, parent, false);
                    } else if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(currentUserId) == false) {

                        convertView = inflater.inflate(R.layout.item_leftmessage, parent, false);
                        viewHolder.imgProPic = (ImageView) convertView.findViewById(R.id.messageProPicLeft);
                        AQuery aq = new AQuery(context);
                        ImageOptions options = new ImageOptions();
                        options.round = 50;
                        options.memCache = true;
                        options.fileCache = true;
                        options.targetWidth = 50;
                        options.fallback = R.drawable.anon;

                        String image_url = c.getString(c.getColumnIndex(Message.COL_USERPICURL));
                        aq.id(viewHolder.imgProPic).image(image_url, options);
                        viewHolder.lblSenderName = (TextView) convertView.findViewById(R.id.lblSenderName);
                        viewHolder.lblMessageTime = (TextView) convertView.findViewById(R.id.lblMessageTime);
                        viewHolder.lblConjunction = (TextView) convertView.findViewById(R.id.lblConjunction);
                        convertView.setTag(viewHolder);

                        // Assign the fonts

                        viewHolder.lblSenderName.setTypeface(tf);
                        viewHolder.lblMessageTime.setTypeface(tf);
                        viewHolder.lblConjunction.setTypeface(tf);


                        viewHolder.lblMessage = (TextView) convertView.findViewById(R.id.lblMessage);
                        viewHolder.lblMessage.setTypeface(btf);

                        if (cursorDate.matches(lastDate) == false) {
                            // Add to the view
                            lastDate = cursorDate;
                        }


                    } else if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(currentUserId) == true) {
                        convertView = inflater.inflate(R.layout.item_rightmessage, parent, false);
                        viewHolder.imgProPic = (ImageView) convertView.findViewById(R.id.messageProPicRight);

                        AQuery aq = new AQuery(context);
                        ImageOptions options = new ImageOptions();
                        options.round = 50;
                        options.memCache = true;
                        options.fileCache = true;
                        options.targetWidth = 50;
                        options.fallback = R.drawable.anon;

                        String image_url = c.getString(c.getColumnIndex(Message.COL_USERPICURL));
                        aq.id(viewHolder.imgProPic).image(image_url, options);
                        viewHolder.lblSenderName = (TextView) convertView.findViewById(R.id.lblSenderName);
                        viewHolder.lblMessageTime = (TextView) convertView.findViewById(R.id.lblMessageTime);
                        viewHolder.lblConjunction = (TextView) convertView.findViewById(R.id.lblConjunction);
                        convertView.setTag(viewHolder);

                        // Assign the fonts

                        viewHolder.lblSenderName.setTypeface(tf);
                        viewHolder.lblMessageTime.setTypeface(tf);
                        viewHolder.lblConjunction.setTypeface(tf);
                        viewHolder.lblMessage = (TextView) convertView.findViewById(R.id.lblMessage);
                        viewHolder.lblMessage.setTypeface(btf);

                        if (cursorDate.matches(lastDate) == false) {
                            // Add to the view
                            lastDate = cursorDate;
                        }
                    }
                } else if (c.getType(c.getColumnIndex(Tweet.COL_Tweet)) != 0) {

                    Log.d("Tweet ConvertView", "ConverView");
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

                }

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                LayoutInflater inflater = LayoutInflater.from(context);

                if (c.getType(c.getColumnIndex(Message.COL_MESSAGE)) != 0) {

                    String cursorUnixtime = c.getString(c.getColumnIndex(Message.COL_TIME));
                    String cursorDate = timeformatter.formatUnixtime(cursorUnixtime, "dd MMM");


                    if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(botId) == true) {
                        convertView = inflater.inflate(R.layout.item_botmessage, parent, false);
                    } else if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(currentUserId) == false) {

                        convertView = inflater.inflate(R.layout.item_leftmessage, parent, false);
                        viewHolder.imgProPic = (ImageView) convertView.findViewById(R.id.messageProPicLeft);
                        AQuery aq = new AQuery(context);
                        ImageOptions options = new ImageOptions();
                        options.round = 50;
                        options.memCache = true;
                        options.fileCache = true;
                        options.targetWidth = 200;
                        options.fallback = R.drawable.anon;

                        String image_url = c.getString(c.getColumnIndex(Message.COL_USERPICURL));
                        aq.id(viewHolder.imgProPic).image(image_url, options);

                        viewHolder.lblSenderName = (TextView) convertView.findViewById(R.id.lblSenderName);
                        viewHolder.lblMessageTime = (TextView) convertView.findViewById(R.id.lblMessageTime);
                        viewHolder.lblConjunction = (TextView) convertView.findViewById(R.id.lblConjunction);
                        convertView.setTag(viewHolder);

                        // Assign the fonts

                        viewHolder.lblSenderName.setTypeface(tf);
                        viewHolder.lblMessageTime.setTypeface(tf);
                        viewHolder.lblConjunction.setTypeface(tf);


                        viewHolder.lblMessage = (TextView) convertView.findViewById(R.id.lblMessage);
                        viewHolder.lblMessage.setTypeface(btf);
                        if (cursorDate.matches(lastDate) == false) {
                            // Add to the view
                            lastDate = cursorDate;
                        }

                    } else if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(currentUserId) == true) {
                        convertView = inflater.inflate(R.layout.item_rightmessage, parent, false);
                        viewHolder.imgProPic = (ImageView) convertView.findViewById(R.id.messageProPicRight);

                        AQuery aq = new AQuery(context);
                        ImageOptions options = new ImageOptions();
                        options.round = 50;
                        options.memCache = true;
                        options.fileCache = true;
                        options.targetWidth = 200;
                        options.fallback = R.drawable.anon;

                        String image_url = c.getString(c.getColumnIndex(Message.COL_USERPICURL));
                        aq.id(viewHolder.imgProPic).image(image_url, options);
                        viewHolder.lblSenderName = (TextView) convertView.findViewById(R.id.lblSenderName);
                        viewHolder.lblMessageTime = (TextView) convertView.findViewById(R.id.lblMessageTime);
                        viewHolder.lblConjunction = (TextView) convertView.findViewById(R.id.lblConjunction);
                        convertView.setTag(viewHolder);

                        // Assign the fonts

                        viewHolder.lblSenderName.setTypeface(tf);
                        viewHolder.lblMessageTime.setTypeface(tf);
                        viewHolder.lblConjunction.setTypeface(tf);
                        viewHolder.lblMessage = (TextView) convertView.findViewById(R.id.lblMessage);
                        viewHolder.lblMessage.setTypeface(btf);

                        if (cursorDate.matches(lastDate) == false) {
                            // Add to the view
                            lastDate = cursorDate;
                        }
                    }
                } else if (c.getType(c.getColumnIndex(Tweet.COL_Tweet)) != 0) {

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
                }

            }

            // Populate the data into the template view using the data object
            if (c.getType(c.getColumnIndex(Message.COL_MESSAGE)) != 0) {

                String cursorUnixtime = c.getString(c.getColumnIndex(Message.COL_TIME));
                String cursorDate = timeformatter.formatUnixtime(cursorUnixtime, "dd MMM");

                if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(botId) == true) {

                } else if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(currentUserId) == false) {
                    viewHolder.lblSenderName.setText(c.getString(c.getColumnIndex(Message.COL_SENDERNAME)));
                    viewHolder.lblMessageTime.setText(timeformatter.formatUnixtime(c.getString(c.getColumnIndex(Message.COL_TIME)), "hh:mm a"));
                    viewHolder.lblMessage.setText(c.getString(c.getColumnIndex(Message.COL_MESSAGE)));
                    viewHolder.lblMessage.setText(c.getString(c.getColumnIndex(Message.COL_MESSAGE)));
                } else if (c.getString(c.getColumnIndex(Message.COL_SENDERID)).matches(currentUserId) == true) {
                    viewHolder.lblSenderName.setText(c.getString(c.getColumnIndex(Message.COL_SENDERNAME)));
                    viewHolder.lblMessageTime.setText(timeformatter.formatUnixtime(c.getString(c.getColumnIndex(Message.COL_TIME)), "hh:mm a"));
                    viewHolder.lblMessage.setText(c.getString(c.getColumnIndex(Message.COL_MESSAGE)));
                }

                // Return the completed view to render on screen
            } else if (c.getType(c.getColumnIndex(Tweet.COL_Tweet)) != 0) {

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

                if (c.getInt(c.getColumnIndex("user_retweeted")) == 1) {
                    viewHolder.retweet.setImageResource(R.drawable.retweeted);

                } else {
                    viewHolder.retweet.setImageResource(R.drawable.retweet);

                }
                if (c.getInt(c.getColumnIndex("user_favorited")) == 1) {
                    viewHolder.favorite.setImageResource(R.drawable.favorited);
                } else {
                    viewHolder.favorite.setImageResource(R.drawable.favorite);
                }

                final String media_url = c.getString(c.getColumnIndex("media_url"));

                if (media_url.matches("")) {
                    viewHolder.imgTweetImage.setVisibility(View.GONE);
                } else {
                    viewHolder.imgTweetImage.setVisibility(View.VISIBLE);
                    options = new ImageOptions();
                    String tweet_image_url = c.getString(c.getColumnIndex("media_url"));
                    aq.id(viewHolder.imgTweetImage).image(tweet_image_url, true, true, 500, 0);
                }


                viewHolder.favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mayday.hasTwitterCredentials()) {
                            mixpanel.track("Status Favorited", Constants.info);

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
                            if (mixpanel != null)
                                mixpanel.track("Status Retweeted", Constants.info);

                            retweet(status_id, mDocId);


                        } else {
                            mayday.askForTwitterCredentials();
                        }
                    }
                });


                viewHolder.reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mixpanel != null)
                            mixpanel.track("Status Replied", Constants.info);

                        Intent intent = new Intent(context, ComposeTweetActivity.class);

                        intent.putExtra("replyToTweetStatusId", status_id);
                        intent.putExtra("replyToUserProPicUrl", image_url);
                        intent.putExtra("replyToUserFullname", userFullname);
                        intent.putExtra("replyToUserHandle", userHanlde);
                        intent.putExtra("replyToTweet", tweet);
                        intent.putExtra("replyToTweetImage", media_url);
                        context.startActivity(intent);


                    }
                });
            }
        }
        return convertView;
    }

    public void retweet(String statusId, String mDocId) {


        Long tweet_status_id = Long.parseLong(statusId);
        dbHelper.getInstance(context).putRetweet(mDocId, mayday.getTwitterAccessToken(), mayday.getTwitterAccessTokenSecret(), tweet_status_id);
    }

    public void favortite(String statusId, String mDocId) {


        Long tweet_status_id = Long.parseLong(statusId);
        dbHelper.getInstance(context).putFavorite(mDocId, mayday.getTwitterAccessToken(), mayday.getTwitterAccessTokenSecret(), tweet_status_id);

    }

    public void reply(String statusId, String handle, String message) {

    }
}