package com.example.administrator.jianshang.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.jianshang.bean.DBFuliaoInfoBean;
import com.example.administrator.jianshang.sqlite.MyOpenHelper;

/**
 * Created by Administrator on 2019/6/2.
 */

public class TbFuliaoInfoDao {

    private MyOpenHelper myOpenHelper = null;
    private SQLiteDatabase db = null;

    public TbFuliaoInfoDao(Context context) {
        myOpenHelper = new MyOpenHelper(context, "jianshang.db", null, 3);
    }


    public boolean addFuliaoInfo(int dahuoId, DBFuliaoInfoBean dbFuliaoInfoBean, SQLiteDatabase db) {


        ContentValues values = new ContentValues();
        values.put("id_dahuo", dahuoId);
        values.put("fuliao_name", dbFuliaoInfoBean.getFuliaoName());
        values.put("fuliao_img", dbFuliaoInfoBean.getFuliaoImg());
        values.put("jiage", dbFuliaoInfoBean.getJiage());
        values.put("id_gongyingshang", dbFuliaoInfoBean.getGongyinshangId());
        values.put("beizhu", dbFuliaoInfoBean.getBeizhu());

        long n = db.insert("tb_fuliao_info",
                null,
                values);
        if (n > 0) {
            return true;
        } else {
            return false;
        }


    }
}
