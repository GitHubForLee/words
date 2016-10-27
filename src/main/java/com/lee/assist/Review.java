package com.lee.assist;

/**
 * Created by Administrator on 2016/9/18.
 */
public class Review {
    String table;
    String date;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Review(String table, String date) {

        this.table = table;
        this.date = date;
    }
}
