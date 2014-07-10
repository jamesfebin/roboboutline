package com.boutline.sports.ContentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.models.Sport;

public class SportProvider extends ContentProvider {

    public static final String AUTHORITY = "com.boutline.sports.provider";
    public static final String SCHEME = "content://";

    // URIs
    // Used for all persons
    public static final String SPORTS = SCHEME + AUTHORITY + "/sport";
    public static final Uri URI_SPORTS = Uri.parse(SPORTS);
    // Used for a single person, just add the id to the end
    public static final String SPORT_BASE = SPORTS + "/";


    public SportProvider() {
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

        if (uri.toString().startsWith(SPORT_BASE)) {


            final String id = uri.getLastPathSegment();
            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Sport.TABLE_NAME, Sport.FIELDS,
                            Sport.COL_ID + " IS ?",
                            new String[] { id }, null, null,
                            null, null);

            result.setNotificationUri(getContext().getContentResolver(), URI_SPORTS);


        }
        else if (URI_SPORTS.equals(uri)) {

            result = BoutDBHelper
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Sport.TABLE_NAME, Sport.FIELDS, null, null, null,
                            null, null, null);

            result.moveToFirst();

            result.setNotificationUri(getContext().getContentResolver(), URI_SPORTS);

        }
        else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        return result;
    }



    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
