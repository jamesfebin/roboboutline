package com.boutline.sports.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.boutline.sports.ContentProviders.MatchProvider;
import com.boutline.sports.ContentProviders.SportProvider;
import com.boutline.sports.ContentProviders.TournamentProvider;
import com.boutline.sports.models.Match;
import com.boutline.sports.models.Sport;
import com.boutline.sports.models.Tournament;

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
        db.execSQL(Tournament.CREATE_TABLE);
        db.execSQL(Match.CREATE_TABLE);
        int i=0;

        for(i=0;i<34;i++) {

            ContentValues values = new ContentValues();
           /* values.put(Match.COL_ID,i+"");
            values.put(Match.COL_NAME,i+"");
            values.put(Match.COL_SHORTNAME,i+"");
            values.put(Match.COL_TEAMFIRSTID,i+"");
            values.put(Match.COL_TEAMSECONDID, i+"");
            values.put(Match.COL_MATCHSTARTTIME,10);
            values.put(Match.COL_MATCHENDTIME, 3040);
            values.put(Match.COL_HASHTAGS, i+"");
            values.put(Match.COL_MATCHVENUE, i+"");
            values.put(Match.COL_MATCHCITY,i+"");
            values.put(Match.COL_PRIORITY, i+"");
            values.put(Match.COL_TOURNAMENTID, i+"");
            values.put(Match.COL_SPORTSID, i+"");



            values.put(Tournament.COL_ID,i+"");
            values.put(Tournament.COL_NAME, i+"");
            values.put(Tournament.COL_FOLLOWED, i+"");
            values.put(Tournament.COL_SportId, i+"");
            values.put(Tournament.COL_FromDate,i+"");
            values.put(Tournament.COL_TillDate, i+"");
            values.put(Tournament.COL_Hashtag,i+"");
            values.put(Tournament.COL_Priority,i+"");
            values.put(Tournament.COL_UnixTimeStart,i);
            values.put(Tournament.COL_UnixTimeEnd,i);

            db.insert(Tournament.TABLE_NAME,null,values);

*/
        }

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


        Log.e("DBPATH",db.getPath().toString());
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


    public synchronized int updateFollowSport(boolean follow_status,String id)
    {

        final SQLiteDatabase db = this.getWritableDatabase();

        int follow;

        if(follow_status)
        {
            follow = 1;
        }
        else
        {
            follow = 0;

        }
        db.execSQL("UPDATE SPORTS SET followed="+follow+" WHERE _id='"+id+"'");
        return 1;

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


   //  Tournament Block Begin


    public synchronized Tournament getTournament(final String id) {

        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(Tournament.TABLE_NAME,
                Tournament.FIELDS, Tournament.COL_ID + " IS ?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor == null || cursor.isAfterLast()) {
            return null;
        }

        Tournament item = null;
        if (cursor.moveToFirst()) {
            item = new Tournament(cursor);
        }
        cursor.close();

        return item;

    }

    public synchronized boolean putTournament(Tournament tournament) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Tournament.TABLE_NAME, new String[] { "_id" },"_id" + "=?",
                new String[] { tournament.mDocId }, null, null, null, null);


        Log.i("Recieved Tournament", tournament.mDocId);
        if (cursor.getCount() > 0) {
            // Then update
            Log.i("Recieved Tournament", "This exists");


            result += db.update(Tournament.TABLE_NAME, tournament.getContent(),
                    Tournament.COL_ID + " IS ?",
                    new String[] { tournament.mDocId });
        }


        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(Tournament.TABLE_NAME, null,
                    tournament.getContent());

            success = true;
        }

        if(success)
        {
            notifyProviderOnTournamentChange();

        }
        return success;
    }

    ///data/user/10/com.boutline.sports/databases/boutdb
    public synchronized int updateFollowTorunament(boolean follow_status,String id)
    {

        final SQLiteDatabase db = this.getWritableDatabase();

        int follow;

        if(follow_status)
        {
            follow = 1;
        }
        else
        {
            follow = 0;

        }
        db.execSQL("UPDATE Tournaments SET followed="+follow+" WHERE _id='"+id+"'");
        return 1;

    }
    public synchronized int removeTournament(final Tournament tournament) {

        final SQLiteDatabase db = this.getWritableDatabase();

        final int result = db.delete(Tournament.TABLE_NAME,
                Tournament.COL_ID + " IS ?",
                new String[] { tournament.mDocId });

        notifyProviderOnTournamentChange();

        return result;
    }

    private void notifyProviderOnTournamentChange() {
        mcontext.getContentResolver().notifyChange(
                TournamentProvider.URI_TOURNAMENTS, null, false);
    }



    public synchronized boolean putMatch(Match match) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Match.TABLE_NAME, new String[] { "_id" },"_id" + "=?",
                new String[] { match.mDocId }, null, null, null, null);


        if (cursor.getCount() > 0) {
            // Then update


            result += db.update(Match.TABLE_NAME, match.getContent(),
                    Match.COL_ID + " IS ?",
                    new String[] { match.mDocId });
        }


        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(Match.TABLE_NAME, null,
                    match.getContent());

            success = true;
        }

        if(success)
        {
            notifyProviderOnMatchChange();

        }
        return success;
    }

    public synchronized int removeMatch(final Match match) {

        final SQLiteDatabase db = this.getWritableDatabase();

        final int result = db.delete(Match.TABLE_NAME,
                Match.COL_ID + " IS ?",
                new String[] { match.mDocId });

        notifyProviderOnMatchChange();

        return result;
    }

    private void notifyProviderOnMatchChange() {
        mcontext.getContentResolver().notifyChange(
                MatchProvider.URI_LIVE_MATCHES, null, false);

        mcontext.getContentResolver().notifyChange(
                MatchProvider.URI_UPCOMING_MATCHES, null, false);


    }


}
