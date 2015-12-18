package com.example.s_mizutani.milktime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.s_mizutani.milktime.R;
import com.example.s_mizutani.milktime.model.BreastFeeding;
import com.example.s_mizutani.milktime.view.BreastListView;

import java.util.ArrayList;

/**
 * Created by s_mizutani on 2015/09/06.
 */
public class BreastItemAdapter extends android.widget.BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<BreastFeeding> list;

    public BreastItemAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(ArrayList<BreastFeeding> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.breastfeedinglistitem, parent, false);

        BreastFeeding milk = list.get(position);
        ((TextView) convertView.findViewById(R.id.BreastTime)).setText(milk.toString());
        // 削除時のためのキー
        //convertView.setTag(milk.getId());
        ((Button)convertView.findViewById(R.id.deleteBreastTime)).setTag(milk.getId());

        return convertView;
    }

}
