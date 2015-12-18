package com.example.s_mizutani.milktime;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.s_mizutani.milktime.adapter.BreastItemAdapter;
import com.example.s_mizutani.milktime.common.Util;
import com.example.s_mizutani.milktime.model.BreastFeeding;
import com.example.s_mizutani.milktime.model.DBManager;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBManager db = new DBManager(this);
        try {
            int deleteCnt = db.deleteOldBreastFeeding();

            // TODO:debug用、後で消すこと
//        Log.i(Util.PACAGE_NAME, "古いデータの削除件数：" + deleteCnt);
//        ArrayList<BreastFeeding> list = new ArrayList<BreastFeeding>();
//        list = getTestItems();
//        list.add(new BreastFeeding(1111, "sssss", new Date(), new Date(), "4444444"));
//        for (int i = 0; i < list.size(); i++) {
//            BreastFeeding b = list.get(i);
//            db.addBreastFeeding(b);
//        }

            //list = getTestItems();
            showMilkTimeList(db);
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                Log.e(Util.PACAGE_NAME, e.getMessage() + e.getStackTrace());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickDelete(View view) {
        Log.d(Util.PACAGE_NAME, "Activity.onClickDelete");
        //Log.i(Util.PACAGE_NAME, view.getTag().toString());
        Object o = view.getTag();
        if (o != null) {
            long id = (long) o;
            // 削除処理
            DBManager db = new DBManager(this);
            try {
                db.deleteOldBreastFeeding(id);
                showMilkTimeList(db);
            } finally {
                db.close();
            }
        }
    }

    private void showMilkTimeList(DBManager db) {
        ArrayList<BreastFeeding> list = db.findBreastFeedingAll();

        BreastItemAdapter adapter = new BreastItemAdapter(MainActivity.this);
        adapter.setList(list);
        ListView listView = (ListView) findViewById(R.id.listViewBreast);
        listView.setAdapter(adapter);
    }

    private ArrayList<BreastFeeding> getTestItems() {
        ArrayList<BreastFeeding> l = new ArrayList<BreastFeeding>();
        BreastFeeding b = new BreastFeeding();
        b.setId(1);
        b.setStart(Util.toDateTime("2015/09/05 09:00:00"));
        b.setEnd(Util.toDateTime("2015/09/05 09:10:00"));
        b.setMemo("test memo");
        l.add(b);

        b = new BreastFeeding();
        b.setId(2);
        b.setStart(Util.toDateTime("2015/09/05 09:15:00"));
        b.setEnd(Util.toDateTime("2015/09/05 09:20:00"));
        b.setMemo("test memo2");
        l.add(b);

        b = new BreastFeeding();
        b.setId(3);
        b.setStart(Util.toDateTime("2015/09/05 09:40:00"));
        b.setEnd(Util.toDateTime("2015/09/05 09:50:00"));
        b.setMemo("test memo3");
        l.add(b);

        return l;
    }
}
