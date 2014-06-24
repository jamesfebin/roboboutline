package com.boutline.sports.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boutline.sports.models.Tweet;
import com.boutline.sports.R;

import java.util.ArrayList;

public class TweetsAdapter extends ArrayAdapter<Tweet> {
    
	// View lookup cache
    private static class ViewHolder {
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
       if(tweet.getTweetPhotoUrl()==null){
    	   viewHolder.imgImageExists.setVisibility(View.INVISIBLE);
       }
       else{
    	   viewHolder.imgImageExists.setVisibility(View.VISIBLE);
       }
    	   
       return convertView;
   }
}