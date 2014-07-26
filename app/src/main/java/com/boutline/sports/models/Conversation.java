package com.boutline.sports.models;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Map;

public class Conversation {


    public static final String TABLE_NAME = "Conversations";

    // These fields can be anything you want.
    public  static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_TOURNAMENTNAME = "tournamentName";
    public static final String COL_TOURNAMENTID ="tournamentId";
    public static final String COL_RANDOMUSERPICS ="randomUserPics";
    public static final String COL_LASTMESSAGE ="message";



    public static final String[] FIELDS = { COL_ID, COL_NAME,COL_TOURNAMENTNAME,COL_TOURNAMENTID,COL_RANDOMUSERPICS,COL_LASTMESSAGE};

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                                              + COL_ID + " TEXT PRIMARY KEY,"
                                              + COL_NAME + " TEXT NOT NULL,"
                                              + COL_TOURNAMENTNAME + " TEXT ,"
                                              + COL_TOURNAMENTID + " TEXT ,"
                                              + COL_RANDOMUSERPICS + " TEXT,"
                                              + COL_LASTMESSAGE + " TEXT)";

    public long id = -1;
    public static String mDocId;
    public String name;
    public String tournamentName;
    public String tournamentId;
    public String randomUserPics;
    public String lastMessage;


    public Map<String, Object> fields;



    // Constructor

    public Conversation()
    {

    }
    
    public Conversation(String mDocId,Map<String, Object> fields) {

        this.mDocId = mDocId;
        this.fields = fields;

    }


    public ContentValues getContent() {
        final ContentValues values = new ContentValues();

        values.put(COL_ID,mDocId);
        values.put(COL_NAME, fields.get(COL_NAME).toString());
        values.put(COL_TOURNAMENTNAME, fields.get(COL_TOURNAMENTNAME).toString());
        values.put(COL_TOURNAMENTID, fields.get(COL_TOURNAMENTID).toString());
        values.put(COL_RANDOMUSERPICS, fields.get(COL_RANDOMUSERPICS).toString());
        values.put(COL_LASTMESSAGE, fields.get(COL_LASTMESSAGE).toString());
        return values;

    }
    
}