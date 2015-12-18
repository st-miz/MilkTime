package com.example.s_mizutani.milktime.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.s_mizutani.milktime.R;
import com.example.s_mizutani.milktime.common.Util;

/**
 * Created by s_mizutani on 2015/09/06.
 */
public class DBManager {
    private Context context;
    private MyDbHelper helper;
    private SQLiteDatabase db;

    private final Map<String, String> sqlMap = new HashMap<String, String>(40);

    @SuppressWarnings("StatementWithEmptyBody")
    public DBManager(Context context) {
        this.context = context;

        //parse xml first!
        XmlResourceParser parser = context.getResources().getXml(R.xml.dml);

        String sqlQueryName = null;
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                } else if (eventType == XmlPullParser.START_TAG) {
                    if ("string".equals(parser.getName())) {
                        sqlQueryName = parser.getAttributeValue(null, "name");
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                } else if (eventType == XmlPullParser.TEXT) {
                    if (sqlQueryName != null) sqlMap.put(sqlQueryName, parser.getText());
                } else if (eventType == XmlPullParser.CDSECT) {
                } else if (eventType == XmlPullParser.COMMENT) {
                } else if (eventType == XmlPullParser.ENTITY_REF) {
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            Log.e(Util.PACAGE_NAME, "BAD DML xml file:" + sqlQueryName + "." + e.getMessage() + "." + e.getStackTrace());
        } catch (IOException e) {
            Log.e(Util.PACAGE_NAME, "Error Reading DML xml file:" + sqlQueryName + "." + e.getMessage() + "." + e.getStackTrace());
            e.printStackTrace();
        } finally {
            parser.close();
        }
        helper = new MyDbHelper(context);
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
        db.close();

        context = null;
        helper = null;
        db = null;
    }

    public long addBreastFeeding(BreastFeeding b) {
        ContentValues values = new ContentValues();
        values.put("order_id", b.getOrder_id());
        values.put("start_time", b.getStart().getTime());
        values.put("end_time", b.getEnd().getTime());
        values.put("memo", b.getMemo());
        long now = new Date().getTime();
        values.put("create_at", now);
        values.put("update_at", now);
        return db.insert("breastfeeding", null, values);
    }

    public ArrayList<BreastFeeding> findBreastFeedingAll() {
        String[] columns = {"_id", "order_id", "start_time", "end_time", "memo"};
        Cursor c = db.query("breastfeeding", columns, null, null, null, null, "create_at desc");
        int capacity = c.getCount() > 50 ? c.getCount() : 10;
        ArrayList<BreastFeeding> list = new ArrayList<BreastFeeding>(capacity);
        while (c.moveToNext()) {
            BreastFeeding b = new BreastFeeding();
            b.setId(c.getLong(c.getColumnIndex("_id")));
            b.setOrder_id(c.getString(c.getColumnIndex("order_id")));
            b.setStart(new Date(c.getLong(c.getColumnIndex("start_time"))));
            b.setEnd(new Date(c.getLong(c.getColumnIndex("end_time"))));
            b.setMemo(c.getString(c.getColumnIndex("memo")));
            list.add(b);
        }
        c.close();
        return list;
    }

    public BreastFeeding findBreastFeedingById(long id) {
        String[] columns = {"_id", "order_id", "start_time", "end_time", "memo"};
        Cursor c = db.query("breastfeeding", columns, "_id = ?", new String[]{String.valueOf(id)}, null, null, "create_at desc");
        BreastFeeding b = new BreastFeeding();
        while (c.moveToNext()) {
            b.setId(c.getLong(c.getColumnIndex("_id")));
            b.setOrder_id(c.getString(c.getColumnIndex("order_id")));
            b.setStart(new Date(c.getLong(c.getColumnIndex("start_time"))));
            b.setEnd(new Date(c.getLong(c.getColumnIndex("end_time"))));
            b.setMemo(c.getString(c.getColumnIndex("memo")));
            break; // 1record
        }
        c.close();
        return b;
    }

    public int udpateBreastFeeding(BreastFeeding b) {
        ContentValues values = new ContentValues();
        long now = new Date().getTime();
        values.put("update_at", now);
        values.put("start_time", b.getStart().getTime());
        values.put("end_time", b.getEnd().getTime());
        values.put("memo", b.getMemo());
        return db.update("breastfeeding", values, "_id = ?", new String[]{String.valueOf(b.getId())});
    }

    public int deleteOldBreastFeeding(long id) {
        return db.delete("breastfeeding", "_id = ?", new String[]{String.valueOf(id)});
    }

    public int deleteOldBreastFeeding() {
        Date d = Util.getDBStoredDay();
        String where[] = {String.valueOf(d.getTime())};
        return db.delete("breastfeeding", "create_at < ?", where);
    }


    private class MyDbHelper extends SQLiteOpenHelper {

        //initialize database with version number
        private MyDbHelper(Context context) {
            super(context, "order.db", null, 9);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(sqlMap.get("sql_create_table_breastfeeding"));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //database upgrade
//            if (oldVersion < 2 && newVersion >= 2) {
//                db.execSQL(sqlMap.get("sql_update_bookmark_20130604")); //add biko
//            }
            // do nothing
        }
    }

}
