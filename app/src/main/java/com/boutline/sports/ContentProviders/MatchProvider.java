package com.boutline.sports.ContentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.models.Match;
import com.boutline.sports.models.Sport;

/**
 * Created by user on 15/07/14.
 */
public class MatchProvider extends ContentProvider {


    public static final String AUTHORITY = "com.boutline.matches.provider";
    public static final String SCHEME = "content://";

    // URIs
    public static final String MATCHES = SCHEME + AUTHORITY + "/match";
    public static final String LIVEMATCHES = SCHEME + AUTHORITY + "/livematches";
    public static final String UPCOMINGMATCHES = SCHEME + AUTHORITY + "/upcommingmatches";

    public static final Uri URI_MATCHES = Uri.parse(MATCHES);
    public static final Uri URI_LIVE_MATCHES = Uri.parse(LIVEMATCHES);
    public static final Uri URI_UPCOMING_MATCHES = Uri.parse(UPCOMINGMATCHES);


    public static final String MATCH_BASE = MATCHES + "/";
    public static final String LIVE_MATCH_FILTER = URI_LIVE_MATCHES + "/";
    public static final String UPCOMING_MATCHES_FILTER = URI_UPCOMING_MATCHES + "/";



    public MatchProvider()
    {

    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        Cursor result = null;

        if (uri.toString().startsWith(MATCH_BASE)) {
            final String id = uri.getLastPathSegment();
            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Match.TABLE_NAME, Match.FIELDS,
                            Match.COL_ID + " IS ?",
                            new String[] { id }, null, null,
                            null, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_MATCHES);
        }
        else if (URI_MATCHES.equals(uri)) {
            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(true,Match.TABLE_NAME, Match.FIELDS, null, null, null,
                            null, Match.COL_MATCHSTARTTIME, null);

            Log.e("Total cursor count",result.getCount()+"");
            result.setNotificationUri(getContext().getContentResolver(), URI_MATCHES);

        }
        else if(uri.toString().startsWith(LIVE_MATCH_FILTER))
        {

            final String tournamentId = uri.getLastPathSegment();
            long unixTimeNow = System.currentTimeMillis() / 1000L;
            Log.e("Unixtime now",unixTimeNow+"");

            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Match.TABLE_NAME, Match.FIELDS, " tournament_id = '"+tournamentId+"' AND unixtime <="+unixTimeNow+" AND end_unixtime>="+unixTimeNow, null, null,
                            null, Match.COL_MATCHSTARTTIME, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_LIVE_MATCHES);

        }
        else if(uri.toString().startsWith(UPCOMING_MATCHES_FILTER))
        {

            final String tournamentId = uri.getLastPathSegment();
            long unixTimeNow = System.currentTimeMillis() / 1000L;

            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Match.TABLE_NAME, Match.FIELDS, " tournament_id = '"+tournamentId+"' AND unixtime >"+unixTimeNow, null, null,
                            null, Match.COL_MATCHSTARTTIME , null);

            result.setNotificationUri(getContext().getContentResolver(), URI_UPCOMING_MATCHES);

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
