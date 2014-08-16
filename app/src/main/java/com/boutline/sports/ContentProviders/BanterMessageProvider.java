package com.boutline.sports.ContentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.models.BanterMessage;
import com.boutline.sports.models.Message;
import com.boutline.sports.models.Tweet;

/**
 * Created by user on 15/07/14.
 */
public class BanterMessageProvider extends ContentProvider {


    public static final String AUTHORITY = "com.boutline.bantermessages.provider";
    public static final String SCHEME = "content://";

    // URIs
    public static final String MESSAGES = SCHEME + AUTHORITY + "/bantermessage";
    public static final String FILTERMESSAGES = SCHEME + AUTHORITY + "/banterconvomessages";

    public static final Uri URI_MESSAGES = Uri.parse(MESSAGES);
    public static final Uri URI_FILTERMESSAGES = Uri.parse(FILTERMESSAGES);

    public static final String MESSAGE_BASE = MESSAGES + "/";
    public static final String MESSAGESFILTER = URI_FILTERMESSAGES + "/";



    public BanterMessageProvider()
    {

    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        Cursor result = null;

        if (uri.toString().startsWith(MESSAGE_BASE)) {
            final String id = uri.getLastPathSegment();
            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(BanterMessage.TABLE_NAME, BanterMessage.FIELDS,
                            BanterMessage.COL_ID + " IS ?",
                            new String[] { id }, null, null,
                            null, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_MESSAGES);
        }
        else if (uri.toString().startsWith(MESSAGESFILTER)) {

            String[] parameters = uri.getLastPathSegment().split(",");
            String conversationId = parameters[0];
            String mtId = parameters[1];


            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .rawQuery("SELECT _id,name,message,banterId,userPicUrl,sender,time,Null as status_id,Null as mtId,Null as user_handle,Null as user_profile_image,Null as tweet,Null as retweeted,Null as user_twitter_id,Null as has_image ,Null as type,Null as user_full_name,Null as media_url, Null as user_retweeted,Null as user_favorited FROM " + Message.TABLE_NAME+ " WHERE banterId = '"+conversationId +"'  UNION SELECT _id,Null as name,Null as message,Null as banterId,Null as userPicUrl,Null as sender,time,status_id,mtId,user_handle,user_profile_image,tweet,retweeted,user_twitter_id,has_image,type,user_full_name,media_url,user_retweeted,user_favorited FROM " + Tweet.TABLE_NAME + " WHERE "+ Tweet.COL_MTID + " = '"+mtId+"' ORDER BY time",null);



             Log.e("Total cursor count",result.getCount()+"");


            result.setNotificationUri(getContext().getContentResolver(), URI_FILTERMESSAGES);



        }

        else {
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
