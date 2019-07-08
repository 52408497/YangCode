package com.example.administrator.jianshang.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.jianshang.bean.DBDaHuoInfoBean;
import com.example.administrator.jianshang.sqlite.MyOpenHelper;

/**
 * Created by Administrator on 2019/6/2.
 */

public class TbDahuoInfoDao {

    private MyOpenHelper myOpenHelper = null;
    private SQLiteDatabase db = null;

    public TbDahuoInfoDao(Context context) {
        myOpenHelper = new MyOpenHelper(context, "jianshang.db", null, 3);
    }


    /**
     * 根据年份信息及款号查询该款式是否存在
     *
     * @param timeData
     * @param sKH
     * @return
     */
    public boolean getInfoForYearAndKH(String timeData, String sKH) {

        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_dahuo_info",
                null,
                "kuanhao=? and year_info=?",
                new String[]{sKH, timeData},
                null,
                null,
                null);

        if (cursor!=null && cursor.getCount() > 0) {
            cursor.close();
            db.close();
            myOpenHelper.close();
            return true;
        } else {
            db.close();
            myOpenHelper.close();
            return false;
        }
    }

    /**
     * 添加大货信息
     *
     * @param dbDaHuoInfoBean
     * @return
     */
    public boolean addDahuoInfo(DBDaHuoInfoBean dbDaHuoInfoBean, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("kuanhao", dbDaHuoInfoBean.getKuanhao());
        values.put("kuanshimingcheng", dbDaHuoInfoBean.getKuanshimingcheng());
        values.put("yangbanhao", dbDaHuoInfoBean.getYangbanhao());
        values.put("year_info", dbDaHuoInfoBean.getYearInfo());
        values.put("fengmian_img", dbDaHuoInfoBean.getFengmianImg());
        values.put("chengben_img", dbDaHuoInfoBean.getChengbenImg());
        values.put("tag", dbDaHuoInfoBean.getTag());
        values.put("beizhu", dbDaHuoInfoBean.getBeizhu());
        long n = db.insert("tb_dahuo_info",
                null,
                values);
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getIdWithLast(SQLiteDatabase db) {
        int _id_dahuo = 0;
        Cursor cursor = db.query("tb_dahuo_info",
                null,
                null,
                null,
                null,
                null,
                null);
        if (cursor.moveToLast()) {
            // 该cursor是最后一条数据
            _id_dahuo = cursor.getInt(cursor.getColumnIndex("_id_dahuo"));
            cursor.close();
        }
        return _id_dahuo;
    }
}
