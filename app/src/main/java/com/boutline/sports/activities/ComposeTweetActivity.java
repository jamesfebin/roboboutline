/** 
 * Tests:
 *   Activity slides in from the bottom -
 *   Ensure activity loads within 3 seconds -
 *   Button, EditText, tweetUsername, tweetHandle, wordcount exist -
 *   ActionBar does not exist and activity in full screen mode -
 *   Blank tweet message does not get submitted -
 *   charcount is correct and deducts the hashtag length -
 *   Back button slides activity down to board -
 *   Keyboard is open on activity load - 
 *   Submit button slides activity to the right
 *  EditText prepopulated with hashtag of game 
 *  Correct attributes of Tweet class exist 
 *  Post button sends tweet to Twitter 
 *   Toast is shown after tweet -
 *  Correct error message is displayed if not tweeted
 *   Check limit on characters -
 *  Drawer menu opens and is visible
 *  All links in drawer menu lead to correct pages
 *  Take a screenshot of the activity with and without drawer  
 */


package com.boutline.sports.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.boutline.sports.models.Tweet;
import com.boutline.sports.R;

public class ComposeTweetActivity extends Activity {
	
	ActionBar actionBar;
    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// Set up the UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);

        // Declare the controls
		Button btnPostTweet = (Button) findViewById(R.id.btnPostToTwitter);
		final EditText txtTweetMessage = (EditText) findViewById(R.id.txtTweetMessage);
        TextView lblTweetCharCount = (TextView)findViewById(R.id.lblTweetCharCount);
        TextView lblTweetUsername = (TextView)findViewById(R.id.lblTweetUsername);
        TextView lblTweetHandle = (TextView)findViewById(R.id.lblTweetHandle);

        // Set up controls
		txtTweetMessage.setText(getMatchHashTag());
		lblTweetCharCount.setText(String.valueOf(140-getMatchHashTag().length()));

        // Set up the fonts
        tf = Typeface.createFromAsset(getAssets(), fontPath);
        btf = Typeface.createFromAsset(getAssets(), boldFontPath);
        btnPostTweet.setTypeface(btf);
        txtTweetMessage.setTypeface(btf);
        lblTweetCharCount.setTypeface(tf);
        lblTweetUsername.setTypeface(btf);
        lblTweetHandle.setTypeface(btf);

		//Set up the listeners
		
		txtTweetMessage.addTextChangedListener(mTextEditorWatcher);
		btnPostTweet.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0){	
					
					// Create Tweet object here					
					Tweet tweet = new Tweet(null, null, null, null, null, txtTweetMessage.getText().toString(), null, null); 
					Boolean isValid = validateTweet(tweet);
					if(isValid) {
						postToTwitter(tweet);
						Intent mainIntent = new Intent(ComposeTweetActivity.this, BoardActivity.class);
				        startActivity(mainIntent);
				        overridePendingTransition(R.anim.pushdownin, R.anim.pushdownout);
					}
				}	 
		});
	}	
	
	
	
	public Boolean validateTweet(Tweet tweet){
		Boolean isValid = true;
		if(tweet.tweetMessage.length()==0 || tweet.tweetMessage==""||tweet.tweetMessage==null){
			Toast.makeText(this, "Tweet cannot be blank.", Toast.LENGTH_SHORT).show();
			isValid=false;
		}
		return isValid;
	}
	
	public Boolean postToTwitter(Tweet tweet){
		Boolean tweetPosted = false;
		try{
			// Do the post to Twitter work here
		}
		catch(Exception e){
			String errorMessage = "Unable to post to Twitter. Try again.";
			// error handling happens here
			Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
		}
		return tweetPosted;
	}
	
	// Show character count for limiting tweet length
	
	private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
           //This sets a textview to the current length
           TextView lblTweetCharCount = (TextView) findViewById(R.id.lblTweetCharCount);
           lblTweetCharCount.setText(String.valueOf(140-(s.length())));
           if(140-(s.length())<1){
        	   lblTweetCharCount.setTextColor(Color.RED);
           }
        }

        public void afterTextChanged(Editable s) {
        }
    };
    
    // This function gets the match hash tag
    
    public String getMatchHashTag(){
    	String matchHashTag = "#AvsB";
    	//Get the match hashtag here
    	return matchHashTag;
    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.pushdownin, R.anim.pushdownout);
	}
}




