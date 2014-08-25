package com.boutline.sports.models;

import android.content.ContentValues;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.database.SQLController;

import java.util.Map;

/**
 * Created by user on 21/08/14.
 */
public class Query {

    public static final String TABLE_NAME = "Queries";

    // These fields can be anything you want.
    public  static final String COL_ID = "_id";
    public static final String COL_Query = "query";
    public static final String COL_PREFERENCE ="preference";
    public static final String COL_FUNCTIONNAME = "methodName";


    public static final String[] FIELDS = { COL_ID, COL_Query};

    public static final String CREATE_TABLE = "CREATE TABLE Queries (_id Text PRIMARY KEY,"
            + COL_Query + " Text NOT NULL, "
            + COL_PREFERENCE + " INTEGER NOT NULL ,"
            + COL_FUNCTIONNAME + " TEXT NOT NULL "
            +" ) " ;
    public long id = -1;
    public String mDocId="";


    public Map<String, Object> fields;

    SQLController dbController;

    BoutDBHelper dbHelper;


    public Query(String mDocId,Map<String,Object> fields)
    {
        this.mDocId = mDocId;
        this.fields = fields;
    }

    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        values.put(COL_ID,mDocId);
        values.put(COL_Query, fields.get(COL_Query).toString());
        values.put(COL_PREFERENCE,fields.get(COL_PREFERENCE).toString());
        values.put(COL_FUNCTIONNAME,fields.get(COL_FUNCTIONNAME).toString());
        return values;
    }


}
