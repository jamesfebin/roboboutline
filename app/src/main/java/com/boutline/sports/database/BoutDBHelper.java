package com.boutline.sports.database;

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
import com.boutline.sports.application.MyDDPState;
import com.boutline.sports.jobs.BoutBotQuery;
import com.boutline.sports.jobs.Favorite;
import com.boutline.sports.jobs.Retweet;
import com.boutline.sports.models.BanterMessage;
import com.boutline.sports.models.Conversation;
import com.boutline.sports.models.Match;
import com.boutline.sports.models.Message;
import com.boutline.sports.models.Parameters;
import com.boutline.sports.models.Query;
import com.boutline.sports.models.Sport;
import com.boutline.sports.models.Tags;
import com.boutline.sports.models.Team;
import com.boutline.sports.models.Tournament;
import com.boutline.sports.models.Tweet;
import com.path.android.jobqueue.JobManager;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

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
        db.execSQL(Query.CREATE_TABLE);
        db.execSQL(Tags.CREATE_TABLE);
        db.execSQL(Parameters.CREATE_TABLE);
        db.execSQL(Team.CREATE_TABLE);

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


        Log.d("DBPATH",db.getPath().toString());
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
            notifyProviderOnBanterMessageChange();
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
            Log.d("Retweeted","0");
        }
        else
        {
            value = 0;
            status = false;
            Log.d("Retweeted","1");

        }
        db.execSQL("UPDATE Tweets SET user_retweeted="+value+" WHERE _id='"+mDocId+"'");

        jobManager = MyApplication.getInstance().getJobManager();
        jobManager.addJobInBackground(new Retweet(statusId,AccessToken,AccessTokenSecret,status));
        notifyProviderOnTweetChange("");
        notifyProviderOnBanterMessageChange();

        return true;
    }

    public void putCachedMessage(Message message)
    {



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
            Log.d("Favorite","0");
        }
        else
        {
            value = 0;
            status = false;
            Log.d("Favorite","1");
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

    public synchronized boolean putTeam(Team team) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Team.TABLE_NAME, new String[] { "_id" },"_id" + "=?",
                new String[] { team.mDocId }, null, null, null, null);


        if (cursor.getCount() > 0) {
            // Then update
            result += db.update(Team.TABLE_NAME, team.getContent(),
                    Team.COL_ID + " IS ?",
                    new String[] { team.mDocId });
        }


        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(Team.TABLE_NAME, null,
                    team.getContent());

            success = true;
        }


        return success;
    }


    public synchronized boolean putQuery(Query query) {

        boolean success = false;

        try {
            int result = 0;
            final SQLiteDatabase db = this.getWritableDatabase();


            Cursor cursor = db.query(Query.TABLE_NAME, new String[]{"_id"}, "_id" + "=?",
                    new String[]{query.mDocId}, null, null, null, null);


            if (cursor.getCount() > 0) {
                // Then update
                result += db.update(Query.TABLE_NAME, query.getContent(),
                        Query.COL_ID + " IS ?",
                        new String[]{query.mDocId});
            }


            //Mining tags and inserting them into tags table

            if (query.fields.containsKey("tags")) {
                // Parsing non formatted JSON

                String tags_raw = query.fields.get("tags").toString();

                tags_raw = tags_raw.replaceAll("[\\[\\]]", "");
                tags_raw = tags_raw.replaceAll("[\\{\\}]", "");

                String[] tags_array = tags_raw.split(",");
                // For updating it with new tags
                Cursor tag_query = db.query(Tags.TABLE_NAME, new String[]{"query_id"}, "query_id" + "=?",
                        new String[]{query.mDocId}, null, null, null, null);
                if (tag_query.getCount() > 0) {
                    db.execSQL("DELETE  FROM Tags WHERE query_id=\"" + query.mDocId + "\"");
                }

                for (int i = 0; i < tags_array.length; i++) {

                    String tag_raw = tags_array[i];
                    String[] key_value = tag_raw.split("=");

                    if(key_value.length>1) {
                        String key = key_value[0];
                        String tag = key_value[1];
                        key = key.replaceAll(" ", "");
                        if (key.matches("name")) {

                            db.execSQL("INSERT INTO TAGS (name,query_id ) VALUES (\"" + tag + "\",\"" + query.mDocId + "\")");

                        }
                    }

                }


            }
            if (query.fields.containsKey("parameters")) {
                db.execSQL("DELETE  FROM Parameters WHERE query_id=\"" + query.mDocId + "\"");

                String raw_parameters = query.fields.get("parameters").toString();
                raw_parameters = raw_parameters.replace("[{", "");
                raw_parameters = raw_parameters.replace("}]", "");
                raw_parameters = raw_parameters.replaceAll("[\\[\\]]", "");
                raw_parameters = raw_parameters.replaceAll(" ", "");

                if (raw_parameters.matches("") == false) {
                    String[] parameter_array = raw_parameters.split(Pattern.quote("},{"));


                    for (int i = 0; i < parameter_array.length; i++) {
                        String[] parameterFields = parameter_array[i].split(",");

                        String name = "";
                        String type = "";
                        String table = "";
                        String columnNames = "";
                        String queryId = query.mDocId;

                        for (int j = 0; j < parameterFields.length; j++) {

                            String[] key_value = parameterFields[j].split("=");

                            if (key_value.length >= 2) {
                                String key = key_value[0];
                                String value = key_value[1];
                                if (key.matches("name"))
                                    name = value;
                                else if (key.matches("type"))
                                    type = value;
                                else if (key.matches("columnNames"))
                                    columnNames = value;
                                else if (key.matches("table"))
                                    table = value;
                            }
                        }

                        db.execSQL("INSERT INTO " + Parameters.TABLE_NAME + " (name,type,table_name,columnNames,query_id)" +
                                " VALUES (\"" + name + "\",\"" + type + "\",\"" + table + "\",\"" + columnNames + "\",\"" + queryId + "\")");

                    }


                }

                if (result > 0) {
                    success = true;
                } else {
                    // Update failed or wasn't possible, insert instead
                    final long id = db.insert(Query.TABLE_NAME, null,
                            query.getContent());

                    success = true;
                }
            }

        }

        catch (Exception E)
        {
            E.printStackTrace();
        }

        return success;

    }

    public synchronized boolean putMessage(Message message,String uId) {
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


        // Check if it's a cached message , Then Update it

        if(uId.matches("")==false) {
            cursor = db.query(Message.TABLE_NAME, new String[]{"_id"}, "_id" + "=?",
                    new String[]{uId}, null, null, null, null);

            if (cursor.getCount() > 0) {
                // Then update
                result += db.update(Message.TABLE_NAME, message.getContent(),
                        Message.COL_ID + " IS ?",
                        new String[]{uId});
            }
        }




        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(Message.TABLE_NAME, null,
                    message.getContent());

            success = true;
        }

        // Update messages if profile picture of a user changed


        cursor = db.query(Message.TABLE_NAME,Message.FIELDS,Message.COL_SENDERID +  "='"+message.fields.get(Message.COL_SENDERID)
                                                                                  + "' ORDER BY time DESC LIMIT 1",null,null, null, null, null);
        String lastProfileUrl="";
        String lastName = "";

        if(cursor.getCount()>0)

        {
            cursor.moveToFirst();
             lastProfileUrl = cursor.getString(cursor.getColumnIndex(Message.COL_USERPICURL));
             lastName = cursor.getString(cursor.getColumnIndex(Message.COL_SENDERNAME));

            Log.d("Last Message is ",cursor.getString(cursor.getColumnIndex(Message.COL_MESSAGE)));


        }

        if(lastProfileUrl!="") {
            cursor = db.query(Message.TABLE_NAME, Message.FIELDS, Message.COL_SENDERID + "='" + message.fields.get(Message.COL_SENDERID) + "' AND "
                    + Message.COL_USERPICURL + "!='" + lastProfileUrl + "'" ,
                    null, null, null, null, null);

            Log.d("Cursor Count Profile", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    db.execSQL("UPDATE "+Message.TABLE_NAME + " SET "+Message.COL_USERPICURL + " ='" + lastProfileUrl + "' WHERE "+Message.COL_ID +"='"+ cursor.getString(cursor.getColumnIndex(Message.COL_ID))+"'");

                }
            }
        }

        if(lastName!="")
        {
            cursor = db.query(Message.TABLE_NAME, Message.FIELDS, Message.COL_SENDERID + "='" + message.fields.get(Message.COL_SENDERID) + "' AND "
                            + Message.COL_SENDERNAME + "!='" + lastName + "'" ,
                    null, null, null, null, null);

            Log.d("Cursor Count Profile", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    db.execSQL("UPDATE "+Message.TABLE_NAME + " SET "+Message.COL_SENDERNAME + " ='" + lastName + "' WHERE "+Message.COL_ID +"='"+ cursor.getString(cursor.getColumnIndex(Message.COL_ID))+"'");

                }
            }


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

    public void sendStructuredQuery(String query_id,String parameters_raw,String originalQuery)
    {

    try {

        ArrayList<String> parameterList = new ArrayList<String>();
        HashMap<String,String> arguments = new HashMap<String,String>();

        arguments.put("originalQuery","originalQuery");
        String methodName = "";
        final SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Parameters WHERE query_id=\""+query_id+"\"",null);

        if(c.getCount()>0)
        {

            while (c.moveToNext())
            {

                parameterList.add(c.getString(c.getColumnIndex(Parameters.COL_NAME)));

            }

        String[] words = parameters_raw.split(" ");
            for(int i=0;i<words.length;i++)
            {

                if(parameterList.size()>0) {

                    for(int j=0;j<parameterList.size();j++)
                    {
                        Cursor parameterQuery = db.rawQuery("SELECT * FROM "+Parameters.TABLE_NAME + " WHERE "+Parameters.COL_NAME+ " =\"" +
                                                      parameterList.get(j) + "\" AND "+Parameters.COL_QUERYID+ " = \""+ query_id + "\" LIMIT 1",null);
                        parameterQuery.moveToFirst();

                        String tableName = parameterQuery.getString(c.getColumnIndex(Parameters.COL_TABLE));
                        if(tableName.matches("NULL")==false)
                        {
                            String[] columnNames = parameterQuery.getString(c.getColumnIndex(Parameters.COL_COLUMNNAMES)).split(";");
                            String WhereQuery = "";

                            for(int k=0;k<columnNames.length;k++)
                            {

                              if(WhereQuery.matches("")==false && words[i].matches("")==false)
                              {
                                  WhereQuery = WhereQuery + " OR LOWER(" + columnNames[k] + ") LIKE \""+words[i] +"%\"";
                              }
                                else if(words[i].matches("")==false)
                              {
                                  WhereQuery = " LOWER(" + columnNames[k] + ") LIKE \""+words[i]+"%\"";
                              }


                            }
                            if(WhereQuery.matches("")==false) {
                                Log.e("ARG QUERy ", "SELECT _id FROM " + tableName + " WHERE " + WhereQuery + " LIMIT 1");
                                Cursor agCursor = db.rawQuery("SELECT _id FROM " + tableName + " WHERE " + WhereQuery + " LIMIT 1", null);


                                if (agCursor.getCount() > 0) {

                                    agCursor.moveToFirst();

                                    arguments.put(parameterList.get(j), agCursor.getString(c.getColumnIndex("_id")));

                                    parameterList.remove(j);
                                }
                            }

                        }
                        else
                        {
                            Integer value =0;
                            if(parameterQuery.getString(parameterQuery.getColumnIndex(Parameters.COL_TYPE)).matches("INTEGER")) {
                                Boolean isInt = true;
                                try {

                                   value = Integer.parseInt(words[i]);

                                } catch (NumberFormatException e) {
                                    isInt = false;
                                }

                                if(isInt)
                                {

                                    arguments.put(parameterList.get(j),value+"");
                                    parameterList.remove(j);
                                }
                            }


                        }


                    }

                }
                else
                {
                    break;
                }


            }

         Cursor methodQuery = db.rawQuery("SELECT methodName FROM Queries WHERE _id =\""+query_id+"\" LIMIT 1",null);

            if(methodQuery.getCount()>0)
            {
                methodQuery.moveToFirst();
                methodName = methodQuery.getString(methodQuery.getColumnIndex(Query.COL_FUNCTIONNAME));
            }
        }
        else
        {
          c = db.rawQuery("SELECT methodName FROM Queries WHERE _id =\""+query_id+"\" LIMIT 1",null);
            if(c.getCount()>0)
            {
                c.moveToFirst();

                 methodName = c.getString(c.getColumnIndex(Query.COL_FUNCTIONNAME));


            }


        }

        if(methodName.matches("")==false)
        {

            Log.e("Added Job in background","Added");
            jobManager = MyApplication.getInstance().getJobManager();

            Object[] parameters = new Object[1];
            parameters[0]=arguments;
            arguments.put("tournament_id","KsrzMzFd6uHckAeb5");
            Log.e("method name,arfument" , methodName+" "+arguments.toString());
            jobManager.addJobInBackground(new BoutBotQuery(methodName,parameters));

        }
        else
        {


        }

    }
    catch (Exception E)
    {
        E.printStackTrace();
    }

    }

    public String findQuery(String query)
    {
        String result = null;


        try {

            final SQLiteDatabase db = this.getWritableDatabase();

            String parameterQuery = query;


            ArrayList<String> QueryList = new ArrayList<String>();
            ArrayList<Integer> ScoresList = new ArrayList();

            String[] words = query.split(" ");


            for (int i = 0; i < words.length; i++) {

                words[i] = words[i].toLowerCase();
                Cursor c = db.rawQuery("SELECT _id FROM Queries WHERE _id IN (SELECT query_id FROM Tags WHERE LOWER(name) =\"" + words[i] + "\" ) ORDER BY preference ", null);
                if (c.getCount() > 0) {

                    parameterQuery = parameterQuery.replaceAll(words[i], "");

                    while (c.moveToNext()) {


                        String query_id = c.getString(c.getColumnIndex(Query.COL_ID));

                        if (QueryList.contains(query_id)) {

                            int index = QueryList.indexOf(query_id);
                            int score = ScoresList.get(index);
                            score = score + 1;
                            ScoresList.set(index, score);
                            Log.e("QUERY", "Array  contains value");
                        } else {
                            QueryList.add(query_id);
                            ScoresList.add(1);
                            Log.e("QUERY", "Array Doesn't contain value,pushing it");

                        }
                    }
                }


            }


            if (ScoresList.size() > 0) {

                int max = Collections.max(ScoresList);

                int highestIndex = ScoresList.indexOf(max);

               result = QueryList.get(highestIndex) + ";";
               result = result + parameterQuery;
                Log.e("RESULT was",result);
            }


        }

        catch (Exception E)

        {
            E.printStackTrace();
        }

        return result;

    }

    public void clearTables()
    {

        final SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+Message.TABLE_NAME);
        db.execSQL("DELETE FROM "+Tweet.TABLE_NAME);
        db.execSQL("DELETE FROM "+Query.TABLE_NAME);

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
