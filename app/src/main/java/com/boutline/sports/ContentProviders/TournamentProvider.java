package com.boutline.sports.ContentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.models.Tournament;

/**
 * Created by user on 14/07/14.
 */
public class TournamentProvider extends ContentProvider {


    public static final String AUTHORITY = "com.boutline.tournaments.provider";
    public static final String SCHEME = "content://";

    // URIs
    public static final String TOURNAMENTS = SCHEME + AUTHORITY + "/tournament";
    public static final Uri URI_TOURNAMENTS= Uri.parse(TOURNAMENTS);
    public static final String TOURNAMENTS_BASE = TOURNAMENTS + "/";


    public TournamentProvider() {
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor result = null;

        if (uri.toString().startsWith(TOURNAMENTS_BASE)) {


            final String id = uri.getLastPathSegment();
            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Tournament.TABLE_NAME, Tournament.FIELDS,
                            Tournament.COL_ID + " IS ?",
                            new String[] { id }, null, null,
                            null, null);

            result.setNotificationUri(getContext().getContentResolver(), URI_TOURNAMENTS);


        }
        else if (URI_TOURNAMENTS.equals(uri)) {

            long unixTimeNow = System.currentTimeMillis() / 1000L;


          result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Tournament.TABLE_NAME, Tournament.FIELDS, Tournament.COL_SportId+" IN (SELECT _id FROM SPORTS WHERE followed=1) AND unixtime_end>="+unixTimeNow , null, null,null,
                             Tournament.COL_UnixTimeStart, null);

            result.setNotificationUri(getContext().getContentResolver(), URI_TOURNAMENTS);

        }
        else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        return result;
    }




    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
