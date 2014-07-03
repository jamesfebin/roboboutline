/** 
 * Tests:
 *  Activity slides in from the right 
 *  Ensure activity loads within 3 seconds 
 *  Loading bar is displayed till the view is populated
 *  Activity has propic, name, handle, message, time, reply, retweet, fav, picture
 *  tweetpicture imageview appears only if tweet has a picture
 *  ActionBar exists with icons for banter, board, schedule, caret
 *  Reply when clicked loads compose activity with handle of target tweeter
 *  Retweet when clicked should give a toast and should change icon
 *  Favorite when clicked should give a toast and should change icon
 *  Back button slides activity right to board 
 *  Correct attributes of Tweet class exist 
 *  Correct error message toast is displayed if no internet on activity load
 *  Drawer menu opens and is visible
 *  All links in drawer menu lead to correct pages
 *  Take a screenshot of the activity with and without drawer  
 *  Fonts are defined and assigned
 *  toast if theres an error retrieving the tweet
 *  Tweet is html encoded
 */


package com.boutline.sports.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boutline.sports.models.Tweet;
import com.boutline.sports.R;

public class TweetDetailsActivity extends Activity {

    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// Set up the UI
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweetdetails);
		ImageView tweetPhoto = (ImageView)findViewById(R.id.imgTweetPicUrl);
		tweetPhoto.setVisibility(View.INVISIBLE);  //By default image doesnt exist in the tweet
		Tweet tweet = new Tweet("123",null,"Anand Satyan",null,"@anandsatyan","This is a tweet","30th July, 4:30 PM","123");
		TextView lblTweetUsername = (TextView)findViewById(R.id.lblTweetUsername);
		TextView lblTweetHandle = (TextView)findViewById(R.id.lblTweetHandle);
		TextView lblTweetMessage = (TextView)findViewById(R.id.lblTweetMessage);
		TextView lblTweetDateTime = (TextView)findViewById(R.id.lblTweetDateTime);
		
		lblTweetUsername.setText(tweet.getTweetUsername());
		lblTweetHandle.setText(tweet.getTweetHandle());
		lblTweetMessage.setText(tweet.getTweetMessage());
		lblTweetDateTime.setText(tweet.getTweetUnixtime()); //TODO Resolve to human readable time
		if(tweet.getTweetPhotoUrl()!=null)
		{
			// Set src of tweetphoto to the url given by getTweetPhoto() 
			tweetPhoto.setVisibility(View.VISIBLE);
		}

        // Set up the fonts

        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
        lblTweetUsername.setTypeface(btf);
        lblTweetHandle.setTypeface(tf);
        lblTweetMessage.setTypeface(btf);
        lblTweetDateTime.setTypeface(tf);
		
	}	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushrightin, R.anim.pushrightout);
	}
}
