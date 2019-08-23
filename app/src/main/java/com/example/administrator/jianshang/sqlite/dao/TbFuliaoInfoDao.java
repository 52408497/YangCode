package com.example.administrator.jianshang.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.jianshang.Tools.Constants;
import com.example.administrator.jianshang.bean.DBFuliaoInfoBean;
import com.example.administrator.jianshang.sqlite.MyOpenHelper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/6/2.
 */

public class TbFuliaoInfoDao {

    private MyOpenHelper myOpenHelper = null;
    private SQLiteDatabase db = null;
private int dbVersion;
    public TbFuliaoInfoDao(Context context) {
        dbVersion = Constants.DBVERSION;
        myOpenHelper = new MyOpenHelper(context, "jianshang.db", null, dbVersion);
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

    public boolean removeFuliaoInfoForDahuoId(int id, SQLiteDatabase db) {

        int n = 0;
        n = db.delete("tb_fuliao_info",
                "id_dahuo = ?",
                new String[]{String.valueOf(id)});
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHaveFuliaoInfoWithDahuoId(int id) {
        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_fuliao_info",
                null,
                "id_dahuo=?",
                new String[]{String.valueOf(id)},
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

    public ArrayList<DBFuliaoInfoBean> getDBDaHuoFuliaoInfoBeansForClothesID(int clothesInfoID) {
        ArrayList<DBFuliaoInfoBean> dbFuliaoInfoBeans = new ArrayList<DBFuliaoInfoBean>();

        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_fuliao_info",
                null,
                "id_dahuo = ?",
                new String[]{String.valueOf(clothesInfoID)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                //datas.add(cursor.getString(cursor.getColumnIndex("year_info")));
                DBFuliaoInfoBean dbFuliaoInfoBean = new DBFuliaoInfoBean();
                dbFuliaoInfoBean.setId(cursor.getInt(cursor.getColumnIndex("_id_fuliao")));
                dbFuliaoInfoBean.setDahuoId(cursor.getInt(cursor.getColumnIndex("id_dahuo")));
                dbFuliaoInfoBean.setFuliaoName(cursor.getString(cursor.getColumnIndex("fuliao_name")));
                dbFuliaoInfoBean.setFuliaoImg(cursor.getString(cursor.getColumnIndex("fuliao_img")));
                dbFuliaoInfoBean.setJiage(cursor.getString(cursor.getColumnIndex("jiage")));
                dbFuliaoInfoBean.setGongyinshangId(cursor.getInt(cursor.getColumnIndex("id_gongyingshang")));
                dbFuliaoInfoBean.setBeizhu(cursor.getString(cursor.getColumnIndex("beizhu")));

                dbFuliaoInfoBeans.add(dbFuliaoInfoBean);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dbFuliaoInfoBeans;
    }


    public ArrayList<String> getImgsWithDahuoIdHaveDb(int id, SQLiteDatabase db) {
        ArrayList<String> imgs = new ArrayList<>();
        //db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_fuliao_info",
                null,
                "id_dahuo=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                imgs.add(cursor.getString(cursor.getColumnIndex("fuliao_img")));
            }
            cursor.close();
        }
        return imgs;
    }
}
