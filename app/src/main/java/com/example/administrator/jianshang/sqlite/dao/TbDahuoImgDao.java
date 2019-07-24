package com.example.administrator.jianshang.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.jianshang.bean.DBDaHuoInfoBean;
import com.example.administrator.jianshang.bean.DBDahuoImgBean;
import com.example.administrator.jianshang.sqlite.MyOpenHelper;

import java.util.ArrayList;

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

    public boolean removeDahuoImgForDahuoId(int id, SQLiteDatabase db) {
        int n = 0;
        n = db.delete("tb_dahuo_img",
                "id_dahuo = ?",
                new String[]{String.valueOf(id)});
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHaveImgWithDahuoId(int id) {
        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_dahuo_img",
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

    public ArrayList<String> getImgsWithDahuoId(int id) {
        ArrayList<String> imgs = new ArrayList<>();
        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_dahuo_img",
                null,
                "id_dahuo=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                imgs.add(cursor.getString(cursor.getColumnIndex("img_name")));
            }
            cursor.close();
        }

        db.close();
        myOpenHelper.close();
        return imgs;
    }

    public ArrayList<String> getImgsWithDahuoIdHaveDb(int id, SQLiteDatabase db) {
        ArrayList<String> imgs = new ArrayList<>();
        //db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_dahuo_img",
                null,
                "id_dahuo = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                imgs.add(cursor.getString(cursor.getColumnIndex("img_name")));
            }
            cursor.close();
        }
        return imgs;
    }


    public ArrayList<DBDahuoImgBean> getDBDaHuoImgBeansForClothesID(int clothesInfoID) {

        ArrayList<DBDahuoImgBean> dbDahuoImgBeans = new ArrayList<DBDahuoImgBean>();

        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_dahuo_img",
                null,
                "id_dahuo = ?",
                new String[]{String.valueOf(clothesInfoID)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                //datas.add(cursor.getString(cursor.getColumnIndex("year_info")));
                DBDahuoImgBean dbDahuoImgBean = new DBDahuoImgBean();
                dbDahuoImgBean.setId(cursor.getInt(cursor.getColumnIndex("_id_dahuo_img")));
                dbDahuoImgBean.setDahuoId(cursor.getInt(cursor.getColumnIndex("id_dahuo")));
                dbDahuoImgBean.setImgType(cursor.getString(cursor.getColumnIndex("img_type")));
                dbDahuoImgBean.setImgName(cursor.getString(cursor.getColumnIndex("img_name")));

                dbDahuoImgBeans.add(dbDahuoImgBean);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dbDahuoImgBeans;

    }
}
