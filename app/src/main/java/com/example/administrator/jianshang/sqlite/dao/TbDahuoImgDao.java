package com.example.administrator.jianshang.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.jianshang.bean.DBDaHuoInfoBean;
import com.example.administrator.jianshang.bean.DBDahuoImgBean;
import com.example.administrator.jianshang.sqlite.MyOpenHelper;

/**
 * Created by Administrator on 2019/6/2.
 */

public class TbDahuoImgDao {

    private MyOpenHelper myOpenHelper = null;
    private SQLiteDatabase db = null;
    public TbDahuoImgDao(Context context) {
        myOpenHelper = new MyOpenHelper(context, "jianshang.db", null, 3);
    }




    public boolean addDahuoImg(int dahuoId, DBDahuoImgBean dbDahuoImgBean, SQLiteDatabase db) {


        ContentValues values = new ContentValues();
        values.put("id_dahuo", dahuoId);
        values.put("img_type", dbDahuoImgBean.getImgType());
        values.put("img_name", dbDahuoImgBean.getImgName());


        long n = db.insert("tb_dahuo_img",
                null,
                values);
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }
}
