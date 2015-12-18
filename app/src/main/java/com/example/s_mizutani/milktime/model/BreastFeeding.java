package com.example.s_mizutani.milktime.model;

import com.example.s_mizutani.milktime.common.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by s_mizutani on 2015/09/06.
 */
public class BreastFeeding {
    private long id;
    private String order_id;
    private Date start;
    private Date end;
    private String memo;

    public BreastFeeding() {
        this.order_id = UUID.randomUUID().toString();
        this.start = Util.MINDATE;
        this.end = Util.MINDATE;
    }

    public BreastFeeding(long id, String order_id, Date start, Date end, String memo) {
        this.id = id;
        this.order_id = order_id;
        this.start = start;
        if (this.start == null) {
            this.start = Util.MINDATE;
        }
        this.end = end;
        if (this.end == null) {
            this.end = Util.MINDATE;
        }
        if (memo != null) {
            this.memo = memo;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStart() {
        if (start == null) start = Util.MINDATE;
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        if (end == null) end = Util.MINDATE;
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }


    public String toString() {

        StringBuffer s = new StringBuffer();
        //SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat f = new SimpleDateFormat("MM/dd HH:mm");
        if (start != null && start.getTime() > Util.MINDATE.getTime()) {
            s.append(f.format(start.getTime()));
        } else {
            s.append("未記入");
        }
        s.append(" → ");
        if (end != null && end.getTime() > Util.MINDATE.getTime()) {
            s.append(f.format(end.getTime()));
        } else {
            s.append("未記入");
        }
        // TODO:一覧にはメモは入りきれないので一時的にコメントアウトしている
//        if (memo != null && memo.trim().length() > 0) {
//            if (s.length() > 0) {
//                s.append(System.getProperty("line.separator"));
//            }
//            s.append(memo);
//        }
        return s.toString();
    }
}
