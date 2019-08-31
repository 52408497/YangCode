package com.example.administrator.jianshang.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.jianshang.Tools.Constants;
import com.example.administrator.jianshang.bean.DBDaHuoInfoBean;
import com.example.administrator.jianshang.sqlite.MyOpenHelper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/6/2.
 */

public class TbDahuoInfoDao {

    private MyOpenHelper myOpenHelper = null;
    private SQLiteDatabase db = null;
private int dbVersion;

    public TbDahuoInfoDao(Context context) {
        dbVersion = Constants.DBVERSION;
        myOpenHelper = new MyOpenHelper(context, "jianshang.db", null, dbVersion);
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

        if (cursor != null && cursor.getCount() > 0) {
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

    /**
     * 根据年份信息查询大货信息列表
     *
     * @param year_info
     * @return
     */
    public ArrayList<DBDaHuoInfoBean> getDBDaHuoInfoBeansForYears(String year_info) {
        ArrayList<DBDaHuoInfoBean> dbDaHuoInfoBeans = new ArrayList<DBDaHuoInfoBean>();

        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_dahuo_info",
                null,
                "year_info = ?",
                new String[]{year_info},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                //datas.add(cursor.getString(cursor.getColumnIndex("year_info")));
                DBDaHuoInfoBean dbDaHuoInfoBean = new DBDaHuoInfoBean();
                dbDaHuoInfoBean.setId(cursor.getInt(cursor.getColumnIndex("_id_dahuo")));
                dbDaHuoInfoBean.setChengbenImg(cursor.getString(cursor.getColumnIndex("chengben_img")));
                dbDaHuoInfoBean.setFengmianImg(cursor.getString(cursor.getColumnIndex("fengmian_img")));
                dbDaHuoInfoBean.setTag(cursor.getString(cursor.getColumnIndex("tag")));
                dbDaHuoInfoBean.setBeizhu(cursor.getString(cursor.getColumnIndex("beizhu")));
                dbDaHuoInfoBean.setYangbanhao(cursor.getString(cursor.getColumnIndex("yangbanhao")));
                dbDaHuoInfoBean.setKuanshimingcheng(cursor.getString(cursor.getColumnIndex("kuanshimingcheng")));
                dbDaHuoInfoBean.setKuanhao(cursor.getString(cursor.getColumnIndex("kuanhao")));
                dbDaHuoInfoBean.setYearInfo(cursor.getString(cursor.getColumnIndex("year_info")));

                dbDaHuoInfoBeans.add(dbDaHuoInfoBean);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dbDaHuoInfoBeans;
    }


    public boolean removeDahuoInfoForDahuoId(int id, SQLiteDatabase db) {
        int n = 0;
        n = db.delete("tb_dahuo_info",
                "_id_dahuo = ?",
                new String[]{String.valueOf(id)});
        if (n > 0) {
            return true;
        } else {
            return false;
        }

    }


    public DBDaHuoInfoBean getDBDaHuoInfoBeanForClothesID(int clothesInfoID) {
        DBDaHuoInfoBean dbDaHuoInfoBean = new DBDaHuoInfoBean();

        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_dahuo_info",
                null,
                "_id_dahuo=?",
                new String[]{String.valueOf(clothesInfoID)},
                null,
                null,
                null);



        if (cursor.moveToFirst()) {
            do {
                dbDaHuoInfoBean.setId(cursor.getInt(cursor.getColumnIndex("_id_dahuo")));
                dbDaHuoInfoBean.setChengbenImg(cursor.getString(cursor.getColumnIndex("chengben_img")));
                dbDaHuoInfoBean.setFengmianImg(cursor.getString(cursor.getColumnIndex("fengmian_img")));
                dbDaHuoInfoBean.setTag(cursor.getString(cursor.getColumnIndex("tag")));
                dbDaHuoInfoBean.setBeizhu(cursor.getString(cursor.getColumnIndex("beizhu")));
                dbDaHuoInfoBean.setYangbanhao(cursor.getString(cursor.getColumnIndex("yangbanhao")));
                dbDaHuoInfoBean.setKuanshimingcheng(cursor.getString(cursor.getColumnIndex("kuanshimingcheng")));
                dbDaHuoInfoBean.setKuanhao(cursor.getString(cursor.getColumnIndex("kuanhao")));
                dbDaHuoInfoBean.setYearInfo(cursor.getString(cursor.getColumnIndex("year_info")));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        myOpenHelper.close();

        return dbDaHuoInfoBean;
    }

    public int getLastID(SQLiteDatabase db) {


        return 0;
    }


    public int updateDahuoInfo(DBDaHuoInfoBean bean) {
        db = myOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("kuanhao",bean.getKuanhao());
        values.put("kuanshimingcheng",bean.getKuanshimingcheng());
        values.put("yangbanhao",bean.getYangbanhao());
        values.put("year_info",bean.getYearInfo());
        values.put("fengmian_img",bean.getFengmianImg());
        values.put("chengben_img",bean.getChengbenImg());
        values.put("tag",bean.getTag());
        values.put("beizhu",bean.getBeizhu());

        int n = db.update(
                "tb_dahuo_info",
                values,
                "_id_dahuo = ?",
                new String[]{String.valueOf(bean.getId())});

        return n;
    }
}
