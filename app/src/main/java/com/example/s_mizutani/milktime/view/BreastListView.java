package com.example.s_mizutani.milktime.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.example.s_mizutani.milktime.common.Util;

/**
 * 授乳記録用のListView
 * <p/>
 * Created by s_mizutani on 2015/09/06.
 */
public class BreastListView extends ListView implements View.OnClickListener {

    /**
     * コンストラクタ
     *
     * @param ctx
     */
    public BreastListView(Context ctx) {
        super(ctx);
    }

    /**
     * コンストラクタ
     *
     * @param ctx
     * @param attrs
     */
    public BreastListView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    /**
     * リスト内のボタンがクリックされたら呼ばれる
     */
    public void onClick(View view) {

//        android.util.Log.i(Util.PACAGE_NAME, "onClick, BreastListView");
//
//        int pos = (Integer) view.getTag();
//        this.performItemClick(view, pos, view.getId());
    }


}
