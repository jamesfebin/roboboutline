package com.boutline.sports.models;

import android.content.ContentValues;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.database.SQLController;

import java.util.Map;

public class BanterMessage {


    public static final String TABLE_NAME = "BanterMessages";

    // These fields can be anything you want.
    public  static final String COL_ID = "_id";
    public static final String COL_SENDERNAME = "name";
    public static final String COL_MESSAGE= "message";
    public static final String COL_BANTERID= "banterId";
    public static final String COL_USERPICURL = "userPicUrl";
    public static final String COL_SENDERID="sender";
    public static final String COL_TIME="time";
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
    public static final String COL_BanterMessageType = "banterMessageType";


    public static final String[] FIELDS = { COL_ID, COL_SENDERNAME,COL_MESSAGE,COL_BANTERID,COL_USERPICURL,COL_SENDERID,COL_TIME,COL_StatusId,COL_MTID,COL_UserHandle,COL_ProfileImage,COL_Tweet,COL_Tweet,COL_Retweeted,COL_UserTwitterId,COL_HasImage,COL_Type,COL_UserFullName,COL_MediaUrl,COL_UserFavorited,COL_UserRetweeted,COL_BanterMessageType};

    public static final String CREATE_TABLE = "CREATE TABLE BanterMessages (_id Text PRIMARY KEY,"
                                               + COL_SENDERNAME + " Text , "
                                               + COL_MESSAGE + " TEXT , "
                                               + COL_BANTERID + " TEXT , "
                                        + COL_USERPICURL + " TEXT ,"
                                        + COL_SENDERID + " TEXT ,"
                                        + COL_TIME + " LONG ,"
                                        + COL_StatusId + " TEXT ,"
                                        + COL_MTID + " TEXT , "
                                        + COL_UserHandle + " TEXT ,"
                                        + COL_ProfileImage + " TEXT ,"
                                        + COL_Tweet + " TEXT ,"
                                        + COL_Retweeted + " BOOLEAN ,"
                                        +COL_UserTwitterId + " TEXT ,"
                                        +COL_HasImage + " BOOLEAN ,"
                                        +COL_Type + " TEXT , "
                                        +COL_UserFullName + " TEXT ,"
                                        +COL_MediaUrl + " TEXT ,"
                                        +COL_BanterMessageType + " TEXT ,"
                                        +COL_UserRetweeted + " BOOLEAN ,"
                                        +COL_UserFavorited +" BOOLEAN )";
    ;
    public long id = -1;
    public String mDocId="";
    public String banterId;
    public String message;
    public String banterMessageType;
public String type;
    public Map<String, Object> fields;

    SQLController dbController;

    BoutDBHelper dbHelper;
    public BanterMessage()
    {

    }

   public BanterMessage(String mDocId, Map<String, Object> fields,String banterMessageType)
   {
       this.mDocId = mDocId;
       this.fields = fields;
       this.banterMessageType=banterMessageType;

       if(this.fields.containsKey("tournament_id")) {
           this.type = "tournament";
       }
       else if(this.fields.containsKey("match_id"))
       {
           this.type="match";
       }

   }

    public ContentValues getContent() {
        final ContentValues values = new ContentValues();

        if(banterMessageType.matches("message")) {

            values.put(COL_ID, "m"+mDocId);
            values.put(COL_SENDERNAME, fields.get(COL_SENDERNAME).toString());
            values.put(COL_MESSAGE, fields.get(COL_MESSAGE).toString());
            values.put(COL_BANTERID, fields.get(COL_BANTERID).toString());
            values.put(COL_USERPICURL, fields.get(COL_USERPICURL).toString());
            values.put(COL_SENDERID, fields.get(COL_SENDERID).toString());
            values.put(COL_TIME, fields.get(COL_TIME).toString());
            values.put(COL_BanterMessageType, "message");


        }
        else if(banterMessageType.matches("tweet")) {
            if (type.matches("match")) {
                values.put(COL_ID, "t"+mDocId);
                values.put(COL_TIME, fields.get("unixtimenow").toString());
                values.put(COL_StatusId, fields.get("status_id").toString());
                values.put(COL_MTID, fields.get("match_id").toString());
                values.put(COL_UserHandle, fields.get("user_handle").toString());
                values.put(COL_ProfileImage, fields.get("user_profile_image").toString());
                values.put(COL_Tweet, fields.get("tweet").toString());
                values.put(COL_Retweeted, fields.get("retweeted").toString());
                values.put(COL_UserTwitterId, fields.get("user_twitter_id").toString());
                values.put(COL_HasImage, fields.get("has_image").toString());
                values.put(COL_Type, fields.get("type").toString());
                values.put(COL_UserFullName, fields.get("user_full_name").toString());
                values.put(COL_MediaUrl, fields.get("media_url").toString());
                values.put(COL_UserRetweeted, false);
                values.put(COL_UserFavorited, false);
                values.put(COL_BanterMessageType, "tweet");

            } else if(type.matches("tournament")) {

                values.put(COL_ID, "t"+mDocId);
                values.put(COL_TIME, fields.get("unixtimenow").toString());
                values.put(COL_StatusId, fields.get("status_id").toString());
                values.put(COL_MTID, fields.get("tournament_id").toString());
                values.put(COL_UserHandle, fields.get("user_handle").toString());
                values.put(COL_ProfileImage, fields.get("user_profile_image").toString());
                values.put(COL_Tweet, fields.get("tweet").toString());
                values.put(COL_Retweeted, fields.get("retweeted").toString());
                values.put(COL_UserTwitterId, fields.get("user_twitter_id").toString());
                values.put(COL_HasImage, fields.get("has_image").toString());
                values.put(COL_Type, fields.get("type").toString());
                values.put(COL_UserFullName, fields.get("user_full_name").toString());
                values.put(COL_MediaUrl, fields.get("media_url").toString());
                values.put(COL_UserRetweeted, false);
                values.put(COL_UserFavorited, false);
                values.put(COL_BanterMessageType, "tweet");

            }
        }
        return values;
    }

}