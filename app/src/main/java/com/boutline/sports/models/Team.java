package com.boutline.sports.models;

import android.content.ContentValues;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.database.SQLController;

import java.util.Map;

/**
 * Created by user on 21/08/14.
 */
public class Team {

    public static final String TABLE_NAME = "Teams";

    // These fields can be anything you want.
    public  static final String COL_ID = "_id";
    public static final String COL_Name = "name";
    public static final String COL_SHORTNAME = "shortname";
    public static final String COL_SPORTSID = "sports_id";



    public static final String[] FIELDS = { COL_ID, COL_Name,COL_SHORTNAME,COL_SPORTSID};

    public static final String CREATE_TABLE = "CREATE TABLE Teams (_id Text PRIMARY KEY,"
            + COL_Name + " Text NOT NULL, "
            + COL_SHORTNAME + " Text NOT NULL, "
            + COL_SPORTSID + " Text NOT NULL"
            +" ) " ;
    public long id = -1;
    public String mDocId="";


    public Map<String, Object> fields;

    SQLController dbController;

    BoutDBHelper dbHelper;

    public Team(String mDocId, Map<String, Object> fields)
    {
        this.mDocId = mDocId;
        this.fields = fields;
    }

    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        values.put(COL_ID,mDocId);
        values.put(COL_Name, fields.get(COL_Name).toString());
        values.put(COL_SHORTNAME,fields.get(COL_SHORTNAME).toString());
        values.put(COL_SPORTSID,fields.get(COL_SPORTSID).toString());
        return values;
    }
}
