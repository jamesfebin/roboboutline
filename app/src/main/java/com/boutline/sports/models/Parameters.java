package com.boutline.sports.models;

import android.content.ContentValues;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.database.SQLController;

import java.util.Map;

/**
 * Created by user on 21/08/14.
 */
public class Parameters {

    public static final String TABLE_NAME = "Parameters";

    // These fields can be anything you want.
    public  static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_TYPE = "type";
    public static final String COL_TABLE = "table_name";
    public static final String COL_COLUMNNAMES = "columnNames";
    public static final String COL_QUERYID="query_id";



    public static final String[] FIELDS = { COL_ID, COL_NAME,COL_TYPE,COL_TABLE,COL_COLUMNNAMES,COL_QUERYID};

    public static final String CREATE_TABLE = "CREATE TABLE Parameters (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " Text NOT NULL, "
            + COL_TYPE + " TEXT NOT NULL, "
            +COL_TABLE + " TEXT ,"
            +COL_COLUMNNAMES + " TEXT ,"
            +COL_QUERYID + " TEXT NOT NULL"
            +" ) " ;

    public long id = -1;


    public Map<String, Object> fields;

    SQLController dbController;

    BoutDBHelper dbHelper;

    public Parameters(Map<String,Object> fields)
    {
        this.fields = fields;
    }

    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        values.put(COL_NAME, fields.get(COL_NAME).toString());
        values.put(COL_TYPE, fields.get(COL_TYPE).toString());
        values.put(COL_TABLE, fields.get("table").toString());
        values.put(COL_COLUMNNAMES, fields.get(COL_COLUMNNAMES).toString());
        values.put(COL_QUERYID,fields.get(COL_QUERYID).toString());
        return values;
    }

}
