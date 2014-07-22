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
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.helpers.Mayday;
import com.boutline.sports.jobs.GetTwitterUserInfo;
import com.boutline.sports.jobs.UpdateStatus;
import com.boutline.sports.R;
import com.path.android.jobqueue.JobManager;

public class ComposeTweetActivity extends Activity {
	
	ActionBar actionBar;
    public String fontPath = "fonts/proxinova.ttf";
    public Typeface tf;
    public String boldFontPath = "fonts/proxinovabold.otf";
    public Typeface btf;
    Mayday mayday;
    JobManager jobManager;
	
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
        ImageView profilePic = (ImageView) findViewById(R.id.imgPropic);
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

        jobManager = MyApplication.getInstance().getJobManager();

        mayday = new Mayday(getApplicationContext());

        if(mayday.hasTwitterCredentials()) {
            SharedPreferences preferences = this.getSharedPreferences("boutlineData", Context.MODE_PRIVATE);

            String userHandle = preferences.getString("userHandle",null);
            String fullName = preferences.getString("fullName",null);
            String profileImageUrl = preferences.getString("profileImageUrl",null);


            if(userHandle!= null && fullName !=null && profileImageUrl!=null) {
                lblTweetHandle.setText(userHandle);
                lblTweetUsername.setText(fullName);

                AQuery aq = new AQuery(getApplicationContext());
                ImageOptions options = new ImageOptions();
                options.round = 35;
                aq.id(profilePic).image(profileImageUrl, options);
            }
            else
            {

                jobManager.addJobInBackground(new GetTwitterUserInfo(mayday.getTwitterAccessToken(),mayday.getTwitterAccessTokenSecret()));


            }



        }
        else
        {
            mayday.askForTwitterCredentials();
        }


		//Set up the listeners


		txtTweetMessage.addTextChangedListener(mTextEditorWatcher);
		btnPostTweet.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0){	
					
					// Create Tweet object here
                String tweet = txtTweetMessage.getText().toString();
					Boolean isValid = validateTweet(tweet);
					if(isValid) {

                    jobManager.addJobInBackground(new UpdateStatus(mayday.getTwitterAccessToken(),mayday.getTwitterAccessTokenSecret(),tweet));
                    Toast.makeText(getApplicationContext(),"Tweet posted",Toast.LENGTH_SHORT).show();
                        finish();
					}
				}	 
		});




	}	
	
	
	
	public Boolean validateTweet(String tweet){
		Boolean isValid = true;
		if(tweet.length()==0 || tweet==""||tweet==null){
			Toast.makeText(this, "Tweet cannot be blank.", Toast.LENGTH_SHORT).show();
			isValid=false;
		}
		return isValid;
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




