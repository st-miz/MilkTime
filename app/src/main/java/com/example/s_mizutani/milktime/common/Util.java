package com.example.s_mizutani.milktime.common;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 共通系処理のクラス
 * <p/>
 * Created by s_mizutani on 2015/09/06.
 */
public class Util {
    public static final String PACAGE_NAME = "BreastFeeding";
    // アプリ内部の日付最小値
    public static final Date MINDATE = new GregorianCalendar(1900, 1, 1).getTime();
    // DBに何日分のデータを保持しておくか
    public static final int DB_STORED_DAY = 5;

    public static Date toDateTime(String s) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date d = sdf.parse(s);
            return d;
        } catch (Exception e) {
            Log.e(PACAGE_NAME, e.getMessage() + ".." + e.getStackTrace());
            return null;
        }
    }

    public static Date getDBStoredDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -DB_STORED_DAY); // n日前とする
        return c.getTime();
    }

    public static Date toDate(String s) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date d = sdf.parse(s);
            return d;
        } catch (ParseException e) {
            Log.e(PACAGE_NAME, e.getMessage() + ".." + e.getStackTrace());
            return null;
        } catch (Exception e) {
            Log.e(PACAGE_NAME, e.getMessage() + ".." + e.getStackTrace());
            return null;
        }
    }

    public static String toString(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String s = sdf.format(date);
            return s;
        } catch (Exception e) {
            Log.e(PACAGE_NAME, e.getMessage() + ".." + e.getStackTrace());
            return null;
        }
    }

}
