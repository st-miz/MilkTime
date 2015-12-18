package com.example.s_mizutani.milktime.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferencesの管理
 *
 * Created by s_mizutani on 2015/09/06.
 */
public class PreferenceManager {

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("com.exmaole.s_mizutani.breastfeeding", Context.MODE_PRIVATE);
    }

    public static void setCurrentBreastFeedingId(Context context, long id) {
        SharedPreferences preferences = getPreferences(context);
        preferences.edit().putLong("currentId", id).apply();
    }

    public static long getCurrentBreastFeedingId(Context context) {
        return getPreferences(context).getLong("currentId", 0);
    }
}
