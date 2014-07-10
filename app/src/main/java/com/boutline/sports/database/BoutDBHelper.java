package com.boutline.sports.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.boutline.sports.ContentProviders.SportProvider;
import com.boutline.sports.models.Sport;

/**
 * Created by user on 07/07/14.
 */

public class BoutDBHelper extends SQLiteOpenHelper {


    final private static String dbname="boutdb";
    final private static Integer version=1;
    private static BoutDBHelper singleton;


    private final Context mcontext;

    public static BoutDBHelper getInstance(final Context context) {
        if (singleton == null) {
            singleton = new BoutDBHelper(context);
        }
        return singleton;
    }


    public BoutDBHelper(Context context)
    {
        super(context,dbname,null, version);
        this.mcontext = context.getApplicationContext();

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Sport.CREATE_TABLE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {

        db.execSQL("DROP TABLE IF EXISTS SPORTS" );
        onCreate(db);

    }

     void deleteDatbase()
    {

        mcontext.deleteDatabase(dbname);

    }


    public synchronized Sport getSport(final String id) {

        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(Sport.TABLE_NAME,
                Sport.FIELDS, Sport.COL_ID + " IS ?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor == null || cursor.isAfterLast()) {
            return null;
        }

        Sport item = null;
        if (cursor.moveToFirst()) {
            item = new Sport(cursor);
        }
        cursor.close();

        return item;

    }

    public synchronized boolean putSport(Sport sport) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Sport.TABLE_NAME, new String[] { "_id" },"_id" + "=?",
                new String[] { sport.mDocId }, null, null, null, null);


        if (cursor.getCount() > 0) {
            // Then update
            result += db.update(Sport.TABLE_NAME, sport.getContent(),
                    Sport.COL_ID + " IS ?",
                    new String[] { sport.mDocId });
        }


        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(Sport.TABLE_NAME, null,
                    sport.getContent());

            success = true;
        }

        if(success)
        {
            notifyProviderOnSportChange();

        }
        return success;
    }

    public synchronized int removeSport(final Sport sport) {

        final SQLiteDatabase db = this.getWritableDatabase();

        final int result = db.delete(Sport.TABLE_NAME,
                Sport.COL_ID + " IS ?",
                new String[] { sport.mDocId });

        notifyProviderOnSportChange();

        return result;
    }
    private void notifyProviderOnSportChange() {
        mcontext.getContentResolver().notifyChange(
                SportProvider.URI_SPORTS, null, false);
    }

}
