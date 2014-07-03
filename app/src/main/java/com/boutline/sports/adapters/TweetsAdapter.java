package com.boutline.sports.adapters;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boutline.sports.models.Tweet;
import com.boutline.sports.R;

import java.util.ArrayList;

public class TweetsAdapter extends ArrayAdapter<Tweet> {

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;
    
	// View lookup cache
    private static class ViewHolder {
        RelativeLayout tweetContainer;
        TextView lblTweetUsername;
        TextView lblTweetHandle;
        TextView lblTweetMessage;
        ImageView imgImageExists;
    }

    public TweetsAdapter(Context context, ArrayList<Tweet> tweets) {
       super(context, R.layout.item_tweet, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
       // Get the data item for this position
    	
       Tweet tweet = getItem(position);    
       
       // Check if an existing view is being reused, otherwise inflate the view
       
       ViewHolder viewHolder; // view lookup cache stored in tag
       if (convertView == null) {
          viewHolder = new ViewHolder();
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.item_tweet, parent, false);
          viewHolder.tweetContainer = (RelativeLayout) convertView.findViewById(R.id.tweetContainer);
          viewHolder.lblTweetUsername = (TextView) convertView.findViewById(R.id.lblTweetUsername);
          viewHolder.lblTweetHandle = (TextView) convertView.findViewById(R.id.lblTweetHandle);
          viewHolder.lblTweetMessage = (TextView) convertView.findViewById(R.id.lblTweetMessage);
          viewHolder.imgImageExists = (ImageView) convertView.findViewById(R.id.imgExists);
          convertView.setTag(viewHolder);
       } else {
           viewHolder = (ViewHolder) convertView.getTag();
       }
       
       // Populate the data into the template view using the data object
       
       viewHolder.lblTweetUsername.setText(tweet.getTweetUsername());
       viewHolder.lblTweetHandle.setText(tweet.getTweetHandle());
       viewHolder.lblTweetMessage.setText(tweet.getTweetMessage());

       // Set up the fonts

       tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);
       btf = Typeface.createFromAsset(getContext().getAssets(), boldFontPath);
       viewHolder.lblTweetUsername.setTypeface(btf);
       viewHolder.lblTweetHandle.setTypeface(tf);
       viewHolder.lblTweetMessage.setTypeface(btf);

       if(tweet.getTweetPhotoUrl()==null){
    	   viewHolder.imgImageExists.setVisibility(View.INVISIBLE);
       }
       else{
    	   viewHolder.imgImageExists.setVisibility(View.VISIBLE);
       }
        Animation walkthroughAnim = AnimationUtils.loadAnimation(this.getContext(), R.anim.pushdownin);
        walkthroughAnim.setDuration(1000);
        walkthroughAnim.setRepeatCount(1);
        walkthroughAnim.setRepeatMode(1);
        walkthroughAnim.setZAdjustment(1);
        convertView.startAnimation(walkthroughAnim);
       return convertView;
   }
}