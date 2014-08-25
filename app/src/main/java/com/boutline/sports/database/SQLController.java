package com.boutline.sports.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boutline.sports.ContentProviders.SportProvider;
import com.boutline.sports.models.Sport;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by user on 08/07/14.
 */
public class SQLController {

    private BoutDBHelper dbHelper;
    private Context mcontext;
    private SQLiteDatabase database = null;
    private Map<String, Object> mFields;


    public SQLController(Context c) {
        mcontext = c;
        dbHelper = new BoutDBHelper(c);

    }

    public SQLController open() throws SQLException {
        dbHelper = new BoutDBHelper(mcontext);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void newSportInsert(String mDocId,Map<String, Object> mFields) {


        Cursor cursor = database.query("SPORTS", new String[] { "_id" },"_id" + "=?",
                new String[] { mDocId }, null, null, null, null);


        ContentValues cv = new ContentValues();
        cv.put("name",mFields.get("name").toString());
        cv.put("icon",mFields.get("icon").toString());
        cv.put("followed",(Boolean) mFields.get("followed"));
        cv.put("_id",mDocId);

        if (cursor.getCount() > 0) {
            database.update(Sport.TABLE_NAME, cv,
                    Sport.COL_ID + " IS ?",
                    new String[] { mDocId });

            return;
        }




        database.insert("SPORTS", null, cv);

        notifyProviderOnSportChange();



    }


    public void clearSportsList()
    {

        database.execSQL("DELETE FROM SPORTS");


    }

    private void notifyProviderOnSportChange() {
        mcontext.getContentResolver().notifyChange(
                SportProvider.URI_SPORTS, null, false);
    }



}
