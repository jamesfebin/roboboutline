package com.boutline.sports.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boutline.sports.adapters.TweetsAdapter;
import com.boutline.sports.models.Tweet;
import com.boutline.sports.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ExpertFragment extends Fragment {
	// Store instance variables

    // newInstance constructor for creating fragment with arguments
    public static ExpertFragment newInstance(int page, String title) {
        ExpertFragment fragmentExpert = new ExpertFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentExpert.setArguments(args);
        return fragmentExpert;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	
    }

    // Inflate the view for the fragment based on layout XML
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    	View rootView;
    	rootView = inflater.inflate(R.layout.fragment_experts, container, false);
    	Tweet dummyTweet = new Tweet("123", "http://boutline.com/cricket.png", "Anand Satyan", "http://facebook.com/anandsatyan", "@anandsatyan", "This is a tweet", "12345", "345");
    	Tweet dummyTweet2 = new Tweet("124", null, "Febin John James", "http://facebook.com/anandsatyan", "@anandsatyan", "This is a tweet", "123456", "345");
        ArrayList<Tweet> arrayOfTweets = new ArrayList<Tweet>();
        ArrayList<Tweet> arrayOfTweets2 = new ArrayList<Tweet>();
        
        arrayOfTweets.add(dummyTweet);
        arrayOfTweets.add(dummyTweet2);
        
        //Sort the arraylist
        
        Tweet tweets[] = arrayOfTweets.toArray(new Tweet[arrayOfTweets.size()]);
        
        Arrays.sort(tweets, new Comparator<Tweet>() {
        	   public int compare(Tweet t1, Tweet t2) {
        	      if(Long.parseLong(t1.tweetUnixtime)<Long.parseLong(t2.tweetUnixtime)){return 1;}
        	      else if(Long.parseLong(t2.tweetUnixtime)<Long.parseLong(t1.tweetUnixtime)){return -1;}
        	      else { return 0; }
        	   }
        	});
        
        for(int i=0;i<tweets.length;++i)
        {
        	arrayOfTweets2.add(tweets[i]);
        }
        
        TweetsAdapter dataAdapter = new TweetsAdapter(getActivity(), arrayOfTweets2);
		ListView expertTweetsListView= (ListView) rootView.findViewById(R.id.lvTweets);
		expertTweetsListView.setAdapter(dataAdapter);
		return rootView;
	}
    
    
}