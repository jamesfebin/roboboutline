package com.boutline.sports.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.boutline.sports.database.BoutDBHelper;
import com.boutline.sports.database.SQLController;

import java.util.Map;

public class Match {

    public static final String TABLE_NAME = "Matches";

    // These fields can be anything you want.
    public  static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_SHORTNAME = "matchshortname";
    public static final String COL_TEAMFIRSTID = "team_first_id";
    public static final String COL_TEAMSECONDID = "team_second_id";
    public static final String COL_MATCHSTARTTIME="unixtime";
    public static final String COL_MATCHENDTIME="end_unixtime";
    public static final String COL_HASHTAGS="hashtags";
    public static final String COL_MATCHVENUE="matchvenue";
    public static final String COL_MATCHCITY="matchcity";
    public static final String COL_PRIORITY="matchpriority";
    public  static final String COL_TOURNAMENTID = "tournament_id";
    public  static final String COL_SPORTSID = "sports_id";


    public static final String[] FIELDS = { COL_ID, COL_NAME,COL_SHORTNAME,COL_TEAMFIRSTID,COL_TEAMSECONDID,COL_MATCHSTARTTIME,COL_MATCHENDTIME,COL_HASHTAGS,COL_MATCHVENUE,COL_MATCHCITY,COL_PRIORITY,COL_TOURNAMENTID,COL_SPORTSID};

    public static final String CREATE_TABLE = "CREATE TABLE Matches (_id Text PRIMARY KEY,name Text NOT NULL,matchshortname TEXT NOT NULL,team_first_id TEXT NOT NULL,team_second_id TEXT NOT NULL,unixtime LONG NOT NULL,end_unixtime LONG NOT NULL,hashtags TEXT,matchvenue TEXT NOT NULL,matchcity TEXT NOT NULL,matchpriority TEXT NOT NULL,tournament_id TEXT NOT NULL,sports_id TEXT NOT NULL)";


    public long id = -1;
    public String mDocId="";
    public String name="";
    public String matchshortname;
    public String team_first_id;
    public String team_second_id;
    public Long unixtime;
    public Long end_unixtime;
    public String hashtags;
    public String matchvenue;
    public String matchcity;
    public String matchpriority;
    public String tournament_id;
    public String sports_id;



    public Map<String, Object> fields;

    SQLController dbController;

    BoutDBHelper dbHelper;


    public Match()
    {


    }

    public Match(final Cursor cursor)
    {
        this.mDocId = cursor.getString(0);
        this.name = cursor.getString(1);
        this.matchshortname = cursor.getString(2);
        this.team_first_id=cursor.getString(3);
        this.team_second_id=cursor.getString(4);
        this.unixtime= Long.parseLong(cursor.getString(5));
        this.end_unixtime= Long.parseLong(cursor.getString(6));
        this.hashtags=cursor.getString(7);
        this.matchvenue=cursor.getString(8);
        this.matchcity=cursor.getString(9);
        this.matchpriority=cursor.getString(10);
        this.tournament_id=cursor.getString(11);
        this.sports_id=cursor.getString(12);
    }


    public Match(String mDocId,Map<String, Object> fields)
    {
       this.mDocId = mDocId;
       this.fields = fields;
    }

    public ContentValues getContent() {
        final ContentValues values = new ContentValues();

        values.put(COL_ID,mDocId);
        values.put(COL_NAME, fields.get("name").toString());
        values.put(COL_SHORTNAME, fields.get("matchshortname").toString());
        values.put(COL_TEAMFIRSTID, fields.get("team_first_id").toString());
        values.put(COL_TEAMSECONDID, fields.get("team_second_id").toString());
        values.put(COL_MATCHSTARTTIME, fields.get("unixtime").toString());
        values.put(COL_MATCHENDTIME, fields.get("end_unixtime").toString());
        values.put(COL_HASHTAGS, fields.get("hashtags").toString());
        values.put(COL_MATCHVENUE, fields.get("matchvenue").toString());
        values.put(COL_MATCHCITY, fields.get("matchcity").toString());
        values.put(COL_PRIORITY, fields.get("matchpriority").toString());
        values.put(COL_TOURNAMENTID, fields.get("tournament_id").toString());
        values.put(COL_SPORTSID, fields.get("sports_id").toString());
        return values;

    }


}