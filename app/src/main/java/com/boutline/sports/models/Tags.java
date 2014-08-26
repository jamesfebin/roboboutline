package com.boutline.sports.models;

import android.content.ContentValues;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.database.SQLController;

import java.util.Map;

/**
 * Created by user on 21/08/14.
 */
public class Tags {

    public static final String TABLE_NAME = "Tags";

    // These fields can be anything you want.
    public  static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_QUERYID="query_id";



    public static final String[] FIELDS = { COL_ID, COL_NAME,COL_QUERYID};

    public static final String CREATE_TABLE = "CREATE TABLE Tags (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " Text NOT NULL, "
            +COL_QUERYID + " TEXT NOT NULL"
            +" ) " ;

    public long id = -1;


    public Map<String, Object> fields;

    SQLController dbController;

    BoutDBHelper dbHelper;


    public Tags(Map<String,Object> fields)
    {
        this.fields = fields;
    }

    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        values.put(COL_NAME, fields.get(COL_NAME).toString());
        values.put(COL_QUERYID,fields.get(COL_QUERYID).toString());
        return values;
    }

}
