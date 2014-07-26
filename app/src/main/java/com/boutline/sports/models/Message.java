package com.boutline.sports.models;

import android.content.ContentValues;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.database.SQLController;

import java.util.Map;

public class Message {


    public static final String TABLE_NAME = "Messages";

    // These fields can be anything you want.
    public  static final String COL_ID = "_id";
    public static final String COL_SENDERNAME = "name";
    public static final String COL_MESSAGE= "message";
    public static final String COL_BANTERID= "banterId";
    public static final String COL_USERPICURL = "userPicUrl";
    public static final String COL_SENDERID="sender";
    public static final String COL_TIME="time";


    public static final String[] FIELDS = { COL_ID, COL_SENDERNAME,COL_MESSAGE,COL_BANTERID,COL_USERPICURL,COL_SENDERID,COL_TIME};

    public static final String CREATE_TABLE = "CREATE TABLE Messages (_id Text PRIMARY KEY,"
                                               + COL_SENDERNAME + " Text NOT NULL, "
                                               + COL_MESSAGE + " TEXT NOT NULL, "
                                               + COL_BANTERID + " TEXT NOT NULL, "
                                        + COL_USERPICURL + " TEXT NOT NULL,"
                                        + COL_SENDERID + " TEXT NOT NULL,"
                                        + COL_TIME + " LONG NOT NULL ) " ;
    public long id = -1;
    public String mDocId="";
    public String banterId;
    public String message;

    public Map<String, Object> fields;

    SQLController dbController;

    BoutDBHelper dbHelper;
    public Message()
    {

    }

   public Message(String mDocId,Map<String,Object> fields)
   {
       this.mDocId = mDocId;
       this.fields = fields;
   }

    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        values.put(COL_ID,mDocId);
        values.put(COL_SENDERNAME, fields.get(COL_SENDERNAME).toString());
        values.put(COL_MESSAGE, fields.get(COL_MESSAGE).toString());
        values.put(COL_BANTERID, fields.get(COL_BANTERID).toString());
        values.put(COL_USERPICURL, fields.get(COL_USERPICURL).toString());
        values.put(COL_SENDERID, fields.get(COL_SENDERID).toString());
        values.put(COL_TIME, fields.get(COL_TIME).toString());
        return values;
    }

}