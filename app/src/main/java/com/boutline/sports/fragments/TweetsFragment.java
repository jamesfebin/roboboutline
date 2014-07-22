package com.boutline.sports.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.boutline.sports.ContentProviders.MatchProvider;
import com.boutline.sports.ContentProviders.TweetProvider;
import com.boutline.sports.R;
import com.boutline.sports.adapters.TweetsAdapter;
import com.boutline.sports.adapters.UpcomingMatchesAdapter;
import com.boutline.sports.database.SQLController;
import com.boutline.sports.models.Match;
import com.boutline.sports.models.Tweet;
import com.path.android.jobqueue.JobManager;

/**
 * Created by user on 15/07/14.
 */
public class TweetsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    ListView listView;
    TextView text;
    Cursor c;
    SQLController dbController;

    LoaderManager loadermanager;

    JobManager jobManager;
    SimpleCursorAdapter tweetAdapter;

    public static TweetsFragment newInstance(String MtId, String type) {
        TweetsFragment tweetsFragment = new TweetsFragment();
        Bundle args = new Bundle();
        args.putString("mtId", MtId);
        args.putString("type", type);
        tweetsFragment.setArguments(args);
        return tweetsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    public void populateListViewFromDb(View v)
    {
        String[] fromFieldNames = new String[] {Tweet.COL_Tweet,Tweet.COL_UserHandle,Tweet.COL_UserFullName};

        int[] toViewIDs = new int[]
                {R.id.lblTweetMessage,R.id.lblTweetHandle,R.id.lblTweetUsername};

        tweetAdapter = new TweetsAdapter(getActivity(),R.layout.item_tweet,null,fromFieldNames,toViewIDs,0);
        listView = (ListView) v.findViewById(R.id.lvTweets);
        listView.setAdapter(tweetAdapter);

        loadermanager = getLoaderManager();
        loadermanager.initLoader(1,null,this);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fans,null);
        populateListViewFromDb(v);
        return v;
    }



    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

       Bundle arguments = this.getArguments();

        String MtId = arguments.getString("mtId");
        String type = arguments.getString("type");

        if(type.matches("normal")) {
            return new CursorLoader(getActivity(),
                    Uri.parse(TweetProvider.URI_Tweets_Normal +"/"+ MtId), Tweet.FIELDS, null, null,
                    null);
        }
        else if(type.matches("media"))
        {

            return new CursorLoader(getActivity(),
                    Uri.parse(TweetProvider.URI_Tweets_Media +"/"+ MtId), Tweet.FIELDS, null, null,
                    null);

        }
        else
        {
            return new CursorLoader(getActivity(),
                    Uri.parse(TweetProvider.URI_Tweets_Expert +"/" + MtId), Tweet.FIELDS, null, null,
                    null);
        }
    }




    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        tweetAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        tweetAdapter.swapCursor(null);
    }
}
