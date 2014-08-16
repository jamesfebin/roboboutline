package com.boutline.sports.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.boutline.sports.ContentProviders.BanterMessageProvider;
import com.boutline.sports.ContentProviders.ConversationProvider;
import com.boutline.sports.ContentProviders.MatchProvider;
import com.boutline.sports.ContentProviders.MessageProvider;
import com.boutline.sports.ContentProviders.SportProvider;
import com.boutline.sports.ContentProviders.TournamentProvider;
import com.boutline.sports.ContentProviders.TweetProvider;
import com.boutline.sports.application.MyApplication;
import com.boutline.sports.jobs.Favorite;
import com.boutline.sports.jobs.Retweet;
import com.boutline.sports.models.BanterMessage;
import com.boutline.sports.models.Conversation;
import com.boutline.sports.models.Match;
import com.boutline.sports.models.Message;
import com.boutline.sports.models.Sport;
import com.boutline.sports.models.Tournament;
import com.boutline.sports.models.Tweet;
import com.path.android.jobqueue.JobManager;

/**
 * Created by user on 07/07/14.
 */

public class BoutDBHelper extends SQLiteOpenHelper {


    final private static String dbname="boutdb";
    final private static Integer version=1;
    private static BoutDBHelper singleton;

    JobManager jobManager;

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
        db.execSQL(Tweet.CreateTable);
        db.execSQL(Conversation.CREATE_TABLE);
        db.execSQL(Message.CREATE_TABLE);
        db.execSQL(BanterMessage.CREATE_TABLE);


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



    public synchronized boolean putTweet(Tweet tweet) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Tweet.TABLE_NAME, new String[] { "_id" },"_id" + "=?",
                new String[] { tweet.mDocId }, null, null, null, null);


        if (cursor.getCount() > 0) {
            // There is no need to update tweets. It will work against the retweet cache logic

/*
            result += db.update(Tweet.TABLE_NAME, tweet.getContent(),
                    Tweet.COL_ID + " IS ?",
                    new String[] { tweet.mDocId });*/
            result = 1;
        }


        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(Tweet.TABLE_NAME, null,
                    tweet.getContent());

            success = true;
        }

        if(success)
        {
            notifyProviderOnTweetChange(tweet.type);

        }
        return success;
    }



    public synchronized boolean putRetweet(String mDocId,String AccessToken,String AccessTokenSecret,Long statusId) {
        int value;
        final SQLiteDatabase db = this.getWritableDatabase();

        Boolean status;
        Cursor cursor = db.query(Tweet.TABLE_NAME, Tweet.FIELDS, " _id ='"+mDocId+"' AND user_retweeted=1"
                , null, null, null, null);


        if(cursor.getCount()==0)
        {
            value = 1;
            status = true;
            Log.e("Retweeted","0");
        }
        else
        {
            value = 0;
            status = false;
            Log.e("Retweeted","1");

        }
        db.execSQL("UPDATE Tweets SET user_retweeted="+value+" WHERE _id='"+mDocId+"'");

        jobManager = MyApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(new Retweet(statusId,AccessToken,AccessTokenSecret,status));
        notifyProviderOnTweetChange("");
        notifyProviderOnBanterMessageChange();

        return true;
    }


    public synchronized boolean putFavorite(String mDocId,String AccessToken,String AccessTokenSecret,Long statusId) {
        int value;
        final SQLiteDatabase db = this.getWritableDatabase();

        Boolean status;
        Cursor cursor = db.query(Tweet.TABLE_NAME, Tweet.FIELDS, " _id ='"+mDocId+"' AND user_favorited=1"
                , null, null, null, null);


        if(cursor.getCount()==0)
        {
            value = 1;
            status = true;
            Log.e("Favorite","0");
        }
        else
        {
            value = 0;
            status = false;
            Log.e("Favorite","1");
        }
        db.execSQL("UPDATE Tweets SET user_favorited="+value+" WHERE _id='"+mDocId+"'");

        jobManager = MyApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(new Favorite(statusId,AccessToken,AccessTokenSecret,status));

        notifyProviderOnTweetChange("");
        notifyProviderOnBanterMessageChange();

        return true;
    }


    public synchronized int removeTweet(final Tweet tweet) {

        final SQLiteDatabase db = this.getWritableDatabase();

        final int result = db.delete(Tweet.TABLE_NAME,
                Tweet.COL_ID + " IS ?",
                new String[] { tweet.mDocId });

        notifyProviderOnTweetChange(tweet.type);
        notifyProviderOnBanterMessageChange();

        return result;
    }

    private void notifyProviderOnTweetChange(String type) {


        // Type must be implemented later

        mcontext.getContentResolver().notifyChange(
                TweetProvider.URI_Tweets_Normal, null, false);


        mcontext.getContentResolver().notifyChange(
                TweetProvider.URI_Tweets_Media, null, false);


        mcontext.getContentResolver().notifyChange(
                TweetProvider.URI_Tweets_Expert, null, false);

    }


    public synchronized boolean putConversation(Conversation conversation) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Conversation.TABLE_NAME, new String[] { "_id" },"_id" + "=?",
                new String[] { conversation.mDocId }, null, null, null, null);


        if (cursor.getCount() > 0) {
            // Then update
            result += db.update(Conversation.TABLE_NAME, conversation.getContent(),
                    Match.COL_ID + " IS ?",
                    new String[] { conversation.mDocId });
        }


        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(Conversation.TABLE_NAME, null,
                    conversation.getContent());

            success = true;
        }

        if(success)
        {
            notifyProviderOnConversationChange();

        }
        return success;
    }

    public synchronized int removeConversation(final Conversation conversation) {
        final SQLiteDatabase db = this.getWritableDatabase();

        final int result = db.delete(Conversation.TABLE_NAME,
                Conversation.COL_ID + " IS ?",
                new String[] { conversation.mDocId });

        notifyProviderOnConversationChange();
        return result;
    }

    private void notifyProviderOnConversationChange() {
        mcontext.getContentResolver().notifyChange(
                ConversationProvider.URI_CONVERSATIONS, null, false);
    }

    public synchronized boolean putMessage(Message message) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Message.TABLE_NAME, new String[] { "_id" },"_id" + "=?",
                new String[] { message.mDocId }, null, null, null, null);


        if (cursor.getCount() > 0) {
            // Then update
            result += db.update(Message.TABLE_NAME, message.getContent(),
                    Message.COL_ID + " IS ?",
                    new String[] { message.mDocId });
        }


        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(Message.TABLE_NAME, null,
                    message.getContent());

            success = true;
        }

        if(success)
        {
            notifyProviderOnMessageChange();
            notifyProviderOnBanterMessageChange();


        }
        return success;
    }

    public synchronized int removeMessage(final Message message) {
        final SQLiteDatabase db = this.getWritableDatabase();

        final int result = db.delete(Message.TABLE_NAME,
                Message.COL_ID + " IS ?",
                new String[] { message.mDocId });

        notifyProviderOnMessageChange();
        notifyProviderOnBanterMessageChange();

        return result;
    }

    private void notifyProviderOnMessageChange() {
        mcontext.getContentResolver().notifyChange(
                MessageProvider.URI_FILTERMESSAGES, null, false);
    }



    private void notifyProviderOnBanterMessageChange() {
        mcontext.getContentResolver().notifyChange(
                BanterMessageProvider.URI_FILTERMESSAGES, null, false);
    }




}
