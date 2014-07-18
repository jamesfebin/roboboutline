package com.boutline.sports.models;

import android.content.ContentValues;
import android.database.Cursor;

import java.math.BigInteger;
import java.util.Map;

public class Tournament {

    public static final String TABLE_NAME = "Tournaments";

    public  static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_FOLLOWED = "followed";
    public static final String COL_SportId = "sports_id";
    public static final String COL_FromDate= "from_date";
    public static final String COL_TillDate= "till_date";
    public static final String COL_Hashtag= "hashtag";
    public static final String COL_Priority= "priority";
    public static final String COL_UnixTimeStart= "unixtime_start";
    public static final String COL_UnixTimeEnd= "unixtime_end";
    public static final String COL_ICON = "icon";

    public static final String[] FIELDS = { COL_ID, COL_NAME,COL_FOLLOWED,COL_SportId,COL_FromDate,COL_TillDate,COL_Hashtag,COL_Priority,COL_UnixTimeStart,COL_UnixTimeEnd};

    public static final String CREATE_TABLE = "CREATE TABLE Tournaments (_id Text PRIMARY KEY,name Text NOT NULL,followed BOOLEAN NOT NULL,sports_id Text NOT NULL,from_date Text NOT NULL,till_date Text NOT NULL,hashtag Text NOT NULL,priority Text NOT NULL,unixtime_start LONG NOT NULL,unixtime_end LONG NOT NULL)";


    public long _id=-1;
    public String mDocId;
    public String name;
    public Boolean followed;
    public String sports_id;

    public String from_date;
    public String till_date;
    public String hashtag;
    public String priority;
    public Long unixtime_start;
    public Long unixtime_end;

    boolean tournamentFollow;

    public Map<String, Object> fields;

    // Constructor

    public Tournament()
    {


    }

    public Tournament(final Cursor cursor)
    {
        this.mDocId = cursor.getString(0);
        this.name = cursor.getString(1);
        this.followed = Boolean.parseBoolean(cursor.getString(3));
        this.sports_id = cursor.getString(4);
        this.from_date = cursor.getString(5);
        this.till_date = cursor.getString(6);
        this.hashtag = cursor.getString(7);
        this.priority = cursor.getString(8);
        this.unixtime_start= cursor.getLong(9);
        this.unixtime_end = cursor.getLong(10);

    }

    public ContentValues getContent() {
        final ContentValues values = new ContentValues();

        values.put(COL_ID,mDocId);
        values.put(COL_NAME, fields.get("name").toString());
        values.put(COL_FOLLOWED, (Boolean) fields.get("followed"));
        values.put(COL_SportId, fields.get("sports_id").toString());
        values.put(COL_FromDate, fields.get("from_date").toString());
        values.put(COL_TillDate, fields.get("till_date").toString());
        values.put(COL_Hashtag,fields.get("hashtag").toString());
        values.put(COL_Priority,fields.get("priority").toString());
        values.put(COL_UnixTimeStart, fields.get("unixtime_start").toString());
        values.put(COL_UnixTimeEnd,fields.get("unixtime_end").toString());


        return values;

    }

    public Tournament(String docId, Map<String, Object> fields) {


        this.mDocId = docId;
        this.fields = fields;


    }

    public String getId() {
        return mDocId;
    }


}
