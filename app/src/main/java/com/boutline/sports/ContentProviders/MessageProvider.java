package com.boutline.sports.ContentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.models.Match;
import com.boutline.sports.models.Message;

/**
 * Created by user on 15/07/14.
 */
public class MessageProvider extends ContentProvider {


    public static final String AUTHORITY = "com.boutline.messages.provider";
    public static final String SCHEME = "content://";

    // URIs
    public static final String MESSAGES = SCHEME + AUTHORITY + "/message";
    public static final String FILTERMESSAGES = SCHEME + AUTHORITY + "/convomessages";

    public static final Uri URI_MESSAGES = Uri.parse(MESSAGES);
    public static final Uri URI_FILTERMESSAGES = Uri.parse(FILTERMESSAGES);

    public static final String MESSAGE_BASE = MESSAGES + "/";
    public static final String MESSAGESFILTER = URI_FILTERMESSAGES + "/";



    public MessageProvider()
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
                    .query(Message.TABLE_NAME, Message.FIELDS,
                            Message.COL_ID + " IS ?",
                            new String[] { id }, null, null,
                            null, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_MESSAGES);
        }
        else if (uri.toString().startsWith(MESSAGESFILTER)) {

            String conversationId = uri.getLastPathSegment();
            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Message.TABLE_NAME, Message.FIELDS, Message.COL_BANTERID+ " = '"+conversationId+"'", null, null,
                            null, Message.COL_TIME, null);

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
