package com.boutline.sports.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.storage.StorageManager;
import android.util.Log;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.database.SQLController;

import java.util.Map;

public class Sport {

    public static final String TABLE_NAME = "SPORTS";

    // These fields can be anything you want.
    public  static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_FOLLOWED = "followed";
    public static final String COL_DESC = "description";
    public static final String COL_ICON = "icon";


    public static final String[] FIELDS = { COL_ID, COL_NAME,COL_ICON,COL_FOLLOWED};

    public static final String CREATE_TABLE = "CREATE TABLE SPORTS (_id Text PRIMARY KEY,name Text NOT NULL,icon Text NOT NULL, followed BOOLEAN NOT NULL)";

    public long id = -1;
    public String mDocId="";
    public String name="";
    public Boolean followed=false;
    public String icon="";

    public Map<String, Object> fields;

    SQLController dbController;

    BoutDBHelper dbHelper;

    // Constructor

    public Sport()
    {

    }

    public Sport(final Cursor cursor)
    {
        this.mDocId = cursor.getString(0);
        this.name = cursor.getString(1);
        this.icon = cursor.getString(2);
        this.followed = Boolean.parseBoolean(cursor.getString(3));


    }

    public ContentValues getContent() {
        final ContentValues values = new ContentValues();

        values.put(COL_ID,mDocId);
        values.put(COL_NAME, fields.get("name").toString());
        values.put(COL_ICON, fields.get("icon").toString());
        values.put(COL_FOLLOWED, (Boolean) fields.get("followed"));
        return values;
    }

    public Sport(String docId, Map<String, Object> fields) {


        this.mDocId = docId;
        this.fields = fields;


    }



    // Getters and Setters

    public String getId() {
        return mDocId;
    }


}