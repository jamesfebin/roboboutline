package com.boutline.sports.ContentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.models.Tournament;
import com.boutline.sports.models.Tweet;

/**
 * Created by user on 21/07/14.
 */
public class TweetProvider extends ContentProvider {


    public static final String AUTHORITY = "com.boutline.tweets.provider";
    public static final String SCHEME = "content://";

    // URIs
    public static final String Tweets = SCHEME + AUTHORITY + "/tweet";
    public static final Uri URI_Tweets= Uri.parse(Tweets);
    public static final String Tweets_BASE = Tweets + "/";

    public static final String FANTWEETS = SCHEME + AUTHORITY  + "/fan";
    public static final String MEDIATWEETS = SCHEME + AUTHORITY + "/media";
    public static final String EXPERTTWEETS = SCHEME + AUTHORITY +  "/expert";


    public static final Uri URI_Tweets_Normal = Uri.parse(FANTWEETS);
    public static final Uri URI_Tweets_Media = Uri.parse(MEDIATWEETS);
    public static final Uri URI_Tweets_Expert = Uri.parse(EXPERTTWEETS);



    public static final String TWEET_FILTER_FAN = URI_Tweets_Normal +"/";
    public static final String TWEET_FILTER_MEDIA =URI_Tweets_Media + "/";
    public static final String TWEET_FILTER_EXPERT = URI_Tweets_Expert  +  "/";


    @Override
    public boolean onCreate() {
        return false;
    }




    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {

        Cursor result = null;

        if (uri.toString().startsWith(Tweets_BASE)) {


            final String id = uri.getLastPathSegment();
            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Tweet.TABLE_NAME, Tweet.FIELDS,
                            Tweet.COL_ID + " IS ?",
                            new String[] { id }, null, null,
                            null, null);

            result.setNotificationUri(getContext().getContentResolver(), URI_Tweets);


        }
        else if (uri.toString().startsWith(TWEET_FILTER_FAN)) {


            String MtId =  uri.getLastPathSegment();


            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Tweet.TABLE_NAME, Tweet.FIELDS, Tweet.COL_MTID+"='"+ MtId+ "' AND type='normal'", null, null,null,
                            Tweet.COL_Unixtimenow + " DESC", null);

            result.setNotificationUri(getContext().getContentResolver(), URI_Tweets_Normal);

            Log.e("Tweet Cursor Count", result.getCount()+"");

        }
        else if (uri.toString().startsWith(TWEET_FILTER_MEDIA)) {


            String MtId =  uri.getLastPathSegment();

            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Tweet.TABLE_NAME, Tweet.FIELDS, Tweet.COL_MTID+"='"+ MtId+ "' AND type='media'", null, null,null,
                            Tweet.COL_Unixtimenow + " DESC", null);

            result.setNotificationUri(getContext().getContentResolver(), URI_Tweets_Media);

        }
        else if (uri.toString().startsWith(TWEET_FILTER_EXPERT)) {


            String MtId =  uri.getLastPathSegment();

            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Tweet.TABLE_NAME, Tweet.FIELDS, Tweet.COL_MTID+"='"+ MtId+ "' AND type='influencer'", null, null,null,
                            Tweet.COL_Unixtimenow + " DESC", null);

            result.setNotificationUri(getContext().getContentResolver(), URI_Tweets_Expert);

        }
        else {
            Log.e("URI ERROR",uri.toString());
            throw new UnsupportedOperationException("Not yet implemented");
        }

        return result;

    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
