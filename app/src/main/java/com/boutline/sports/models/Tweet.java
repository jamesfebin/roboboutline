package com.boutline.sports.models;

import android.content.ContentValues;

import java.util.Map;

public class Tweet {



    public static final String TABLE_NAME = "Tweets";

    // These fields can be anything you want.
    public  static final String COL_ID = "_id";
    public static final String COL_Unixtimenow = "unixtimenow";
    public static final String COL_StatusId= "status_id";
    public static final String COL_MTID = "mtId";
    public static final String COL_UserHandle= "user_handle";
    public static final String COL_ProfileImage= "user_profile_image";
    public static final String COL_Tweet = "tweet";
    public static final String COL_Retweeted = "retweeted";
    public static final String COL_UserTwitterId= "user_twitter_id";
    public static final String COL_HasImage= "has_image";
    public static final String COL_Type= "type";
    public static final String COL_UserFullName= "user_full_name";
    public static final String COL_MediaUrl="media_url";
    public static final String COL_UserRetweeted = "user_retweeted";
    public static final String COL_UserFavorited = "user_favorited";




    public static String CreateTable = "CREATE TABLE Tweets (_id TEXT PRIMARY KEY,"
                                  +COL_Unixtimenow + " LONG NOT NULL, "
                                  +COL_StatusId + " TEXT NOT NULL ,"
                                  + COL_MTID + " TEXT NOT NULL, "
                                  + COL_UserHandle + " TEXT NOT NULL,"
                                  + COL_ProfileImage + " TEXT NOT NULL,"
                                  + COL_Tweet + " TEXT NOT NULL,"
                                  + COL_Retweeted + " BOOLEAN NOT NULL,"
                                +COL_UserTwitterId + " TEXT NOT NULL,"
                                +COL_HasImage + " BOOLEAN NOT NULL,"
                                +COL_Type + " TEXT NOT NULL, "
                                +COL_UserFullName + " TEXT NOT NULL,"
                                +COL_MediaUrl + " TEXT NOT NULL,"
                                +COL_UserRetweeted + " BOOLEAN ,"
                                +COL_UserFavorited +" BOOLEAN )";

    public static final String[] FIELDS = { COL_ID,COL_Unixtimenow,COL_StatusId,COL_MTID,COL_UserHandle,COL_ProfileImage,COL_Tweet,COL_Tweet,COL_Retweeted,COL_UserTwitterId,COL_HasImage,COL_Type,COL_UserFullName,COL_MediaUrl,COL_UserFavorited,COL_UserRetweeted};



    public String mDocId;
    public String unixtimenow;
    public String status_id;
    public String mtId;
    public String user_handle;
    public String user_profile_image;
    public String tweet;
    public String retweeted;
    public String user_twitter_id;
    public String has_image;
    public String type;
    public String user_full_name;
    public String media_url;


    public Map<String, Object> fields;

    // Constructor

    public Tweet()
    {

    }
    // Getters and Setters



    public Tweet(String mDocId,Map<String, Object> fields)
    {
        this.mDocId = mDocId;
        this.fields = fields;

        if(this.fields.containsKey("tournament_id")) {
            this.type = "tournament";
        }
        else
        {
            this.type="match";
        }
    }


    public ContentValues getContent() {
        final ContentValues values = new ContentValues();

        if(type.matches("match")) {
            values.put(COL_ID, mDocId);
            values.put(COL_Unixtimenow, fields.get("unixtimenow").toString());
            values.put(COL_StatusId, fields.get("status_id").toString());
            values.put(COL_MTID, fields.get("match_id").toString());
            values.put(COL_UserHandle, fields.get("user_handle").toString());
            values.put(COL_ProfileImage,fields.get("user_profile_image").toString());
            values.put(COL_Tweet, fields.get("tweet").toString());
            values.put(COL_Retweeted, fields.get("retweeted").toString());
            values.put(COL_UserTwitterId, fields.get("user_twitter_id").toString());
            values.put(COL_HasImage, fields.get("has_image").toString());
            values.put(COL_Type, fields.get("type").toString());
            values.put(COL_UserFullName, fields.get("user_full_name").toString());
            values.put(COL_MediaUrl, fields.get("media_url").toString());
            values.put(COL_UserRetweeted, false);
            values.put(COL_UserFavorited, false);
            return values;
        }
        else
        {

            values.put(COL_ID, mDocId);
            values.put(COL_Unixtimenow, fields.get("unixtimenow").toString());
            values.put(COL_StatusId, fields.get("status_id").toString());
            values.put(COL_MTID, fields.get("tournament_id").toString());
            values.put(COL_UserHandle, fields.get("user_handle").toString());
            values.put(COL_ProfileImage,fields.get("user_profile_image").toString());
            values.put(COL_Tweet, fields.get("tweet").toString());
            values.put(COL_Retweeted, fields.get("retweeted").toString());
            values.put(COL_UserTwitterId, fields.get("user_twitter_id").toString());
            values.put(COL_HasImage, fields.get("has_image").toString());
            values.put(COL_Type, fields.get("type").toString());
            values.put(COL_UserFullName, fields.get("user_full_name").toString());
            values.put(COL_MediaUrl, fields.get("media_url").toString());
            values.put(COL_UserRetweeted, false);
            values.put(COL_UserFavorited, false);
            return values;



        }

    }



}