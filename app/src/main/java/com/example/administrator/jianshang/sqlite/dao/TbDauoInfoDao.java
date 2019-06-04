package com.example.administrator.jianshang.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.jianshang.sqlite.MyOpenHelper;

/**
 * Created by Administrator on 2019/6/2.
 */

public class TbDauoInfoDao {

    private MyOpenHelper myOpenHelper = null;
    private SQLiteDatabase db = null;

    public TbDauoInfoDao(Context context) {
        myOpenHelper = new MyOpenHelper(context, "jianshang.db", null, 3);
    }



}
