package com.example.s_mizutani.milktime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.s_mizutani.milktime.common.PreferenceManager;
import com.example.s_mizutani.milktime.common.Util;
import com.example.s_mizutani.milktime.model.BreastFeeding;
import com.example.s_mizutani.milktime.model.DBManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of App MyWidgetProvider functionality.
 */
public class MyWidgetProvider extends AppWidgetProvider {
    static final String AppBtnFilter = "com.example.s_mizutani.milktime.APP_BUTTON_CLICKED";
    static final String StartBtnFilter = "com.example.s_mizutani.milktime.START_BUTTON_CLICKED";
    static final String EndBtnFilter = "com.example.s_mizutani.milktime.END_BUTTON_CLICKED";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);


        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        Log.i(Util.PACAGE_NAME, action);
        Date now = new Date();
        if (action.equals(AppBtnFilter)) {
            Log.i(Util.PACAGE_NAME, "Button1 Clicked");
            Intent main = new Intent(context, MainActivity.class);
            main.setFlags(main.FLAG_ACTIVITY_NEW_TASK | main.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(main);
        } else if (action.equals(StartBtnFilter)) {
            Log.i(Util.PACAGE_NAME, "Button2 Clicked");
            startBreastFeeding(context, PreferenceManager.getCurrentBreastFeedingId(context), now);

            SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            remoteViews.setTextViewText(R.id.widget_label, sf.format(now) + "授乳中...");
            MyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
       } else if (action.equals(EndBtnFilter)) {
            Log.i(Util.PACAGE_NAME, "Button3 Clicked");
            endBreastFeeding(context, PreferenceManager.getCurrentBreastFeedingId(context));

            SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            remoteViews.setTextViewText(R.id.widget_label, sf.format(now) + "終了");
            MyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);

        // ボタンイベントを登録
        views.setOnClickPendingIntent(R.id.widget_app, clickAppButton(context));
        views.setOnClickPendingIntent(R.id.widget_start, clickStartButton(context));
        views.setOnClickPendingIntent(R.id.widget_end, clickEndButton(context));
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static PendingIntent clickAppButton(Context context) {
        Intent intent = new Intent();
        intent.setAction(AppBtnFilter);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent clickStartButton(Context context) {
        Intent intent = new Intent();
        intent.setAction(StartBtnFilter);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent clickEndButton(Context context) {
        Intent intent = new Intent();
        intent.setAction(EndBtnFilter);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    // アップデート
    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    private void startBreastFeeding(Context context, long id, Date now) {
        BreastFeeding b = null;
        if (id == 0) {
            // new!
            b = new BreastFeeding();
            b.setStart(now);
        } else {
            // 開始ボタンを２回押した場合は１つ前の授乳記録の終わりと
            // これからの開始を同じにして１レコードを完了させるものとする
            endBreastFeeding(context, id);
            DBManager db = new DBManager(context);
            b = db.findBreastFeedingById(id);
            if (b.getId() == 0) return; // err?
            b.setEnd(now);
            db.udpateBreastFeeding(b);
            db.close();
            // new!
            b = new BreastFeeding();
            b.setStart(now);
        }
        b.setEnd(Util.MINDATE);
        DBManager db = new DBManager(context);
        long addId = db.addBreastFeeding(b);
        PreferenceManager.setCurrentBreastFeedingId(context, addId);
        db.close();
    }

    private void endBreastFeeding(Context context, long id) {
        if (id == 0) return;
        DBManager db = new DBManager(context);
        BreastFeeding b = db.findBreastFeedingById(id);
        if (b.getId() == 0) return; // err?
        b.setEnd(new Date());
        db.udpateBreastFeeding(b);
        db.close();
        // 1レコード完了のため登録中のidは初期化
        PreferenceManager.setCurrentBreastFeedingId(context, 0);
    }
}

