package com.example.administrator.jianshang.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.Constants;
import com.example.administrator.jianshang.bean.DBFuliaoInfoBean;
import com.example.administrator.jianshang.bean.DBGongyinshangInfoBean;
import com.example.administrator.jianshang.bean.GongYinShangBean;
import com.example.administrator.jianshang.sqlite.MyOpenHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2019/6/2.
 */

public class TbGongyinshangInfoDao {

    private MyOpenHelper myOpenHelper = null;
    private SQLiteDatabase db = null;
    private Context context;
    private int dbVersion;

    public TbGongyinshangInfoDao(Context context) {
        this.context = context;
        dbVersion = Constants.DBVERSION;
        myOpenHelper = new MyOpenHelper(context, "jianshang.db", null, dbVersion);
    }

    public boolean addGongyinshangInfo(int dahuoId, DBGongyinshangInfoBean dbGongyinshangInfoBean, SQLiteDatabase db) {

//
//        ContentValues values = new ContentValues();
//        values.put("id_dahuo", dahuoId);
//        values.put("fuliao_name", dbFuliaoInfoBean.getFuliaoName());
//        values.put("fuliao_img", dbFuliaoInfoBean.getFuliaoImg());
//        values.put("jiage", dbFuliaoInfoBean.getJiage());
//        values.put("id_gongyingshang", dbFuliaoInfoBean.getGongyinshangId());
//        values.put("beizhu", dbFuliaoInfoBean.getBeizhu());
//
//        long n = db.insert("tb_fuliao_info",
//                null,
//                values);
//        if (n > 0) {
//            return true;
//        } else {
//            return false;
//        }

        return false;
    }

    public boolean removeGongyinshangInfoForId(int id, SQLiteDatabase db) {
        this.db = db;
        return removeGongyinshangInfoForId(id);
    }

    public boolean removeGongyinshangInfoForId(int id){
        if (db == null) {
            db = myOpenHelper.getWritableDatabase();
        }
        int n=0;
        n = db.delete("tb_gongyinshang_info",
                "_id_gongyinshang = ?",
                new String[]{String.valueOf(id)});

        db.close();

        if (n>0){
            return true;
        }else {
            return false;
        }
    }



    public boolean removeGongyinshangInfoForBean(DBGongyinshangInfoBean bean){
        boolean removeDataIsSuccess = false;
        //删除数据库数据
        removeDataIsSuccess = removeGongyinshangInfoForId(bean.getId());

        if (removeDataIsSuccess) {
            //删除本地图片
            String folderName = context.getString(R.string.my_photo_folder_name);
            File file_mingpian_zm = new File(Environment.getExternalStorageDirectory().getPath() +
                    "/" + folderName + "/" + bean.getMingPianImgZM());
            File file_mingpian_fm = new File(Environment.getExternalStorageDirectory().getPath() +
                    "/" + folderName + "/" + bean.getMingPianImgFM());

            if (file_mingpian_zm.exists()) {
                file_mingpian_zm.delete();//删除正面名片图片
            }

            if (file_mingpian_fm.exists()) {
                file_mingpian_fm.delete();//删除反面名片图片
            }
        }

        return removeDataIsSuccess;
    }





    public boolean isHaveGongyinshangInfoWithId(int id) {
        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_gongyinshang_info",
                null,
                "_id_gongyinshang=?",
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

    public ArrayList<DBGongyinshangInfoBean> getGongyinshangInfoBeansWithType(String gongyinshang_type) {
        ArrayList<DBGongyinshangInfoBean> dbGongyinshangInfoBeans = new ArrayList<>();
        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_gongyinshang_info",
                null,
                "gongyinshang_type = ?",
                new String[]{String.valueOf(gongyinshang_type)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                DBGongyinshangInfoBean dbGongyinshangInfoBean = new DBGongyinshangInfoBean();

                dbGongyinshangInfoBean.setId(cursor.getInt(cursor.getColumnIndex("_id_gongyinshang")));
                dbGongyinshangInfoBean.setGongYinShangType(gongyinshang_type);
                dbGongyinshangInfoBean.setDangKouAddress(cursor.getString(cursor.getColumnIndex("dangkou_address")));
                dbGongyinshangInfoBean.setCangKuAddress(cursor.getString(cursor.getColumnIndex("cangku_address")));
                dbGongyinshangInfoBean.setCangKuTelephone(cursor.getString(cursor.getColumnIndex("cangku_telephone")));
                dbGongyinshangInfoBean.setDangKouTelephone(cursor.getString(cursor.getColumnIndex("dangkou_telephone")));
                dbGongyinshangInfoBean.setGongYinShangName(cursor.getString(cursor.getColumnIndex("gongyinshang_name")));
                dbGongyinshangInfoBean.setMingPianImgZM(cursor.getString(cursor.getColumnIndex("mingpian_img_zm")));
                dbGongyinshangInfoBean.setMingPianImgFM(cursor.getString(cursor.getColumnIndex("mingpian_img_fm")));

                dbGongyinshangInfoBeans.add(dbGongyinshangInfoBean);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dbGongyinshangInfoBeans;
    }

    public ArrayList<GongYinShangBean> getGongyinshangBeansWithType(String gongyinshang_type){
        ArrayList<GongYinShangBean> gongYinShangBeans = new ArrayList<>();
        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_gongyinshang_info",
                null,
                "gongyinshang_type = ?",
                new String[]{String.valueOf(gongyinshang_type)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                GongYinShangBean bean = new GongYinShangBean();

                bean.setId(cursor.getInt(cursor.getColumnIndex("_id_gongyinshang")));
                bean.setName(cursor.getString(cursor.getColumnIndex("gongyinshang_name")));

                gongYinShangBeans.add(bean);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return gongYinShangBeans;
    }


    public ArrayList<GongYinShangBean> getGongyinshangBeans() {
        ArrayList<GongYinShangBean> gongYinShangBeans = new ArrayList<>();
        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_gongyinshang_info",
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                GongYinShangBean bean = new GongYinShangBean();

                bean.setId(cursor.getInt(cursor.getColumnIndex("_id_gongyinshang")));
                bean.setName(cursor.getString(cursor.getColumnIndex("gongyinshang_name")));

                gongYinShangBeans.add(bean);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return gongYinShangBeans;
    }

    public boolean addGongyinshangInfo(DBGongyinshangInfoBean bean) {
        db = myOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("gongyinshang_type", bean.getGongYinShangType());
        values.put("gongyinshang_name", bean.getGongYinShangName());
        values.put("dangkou_address", bean.getDangKouAddress());
        values.put("cangku_address", bean.getCangKuAddress());
        values.put("dangkou_telephone", bean.getDangKouTelephone());
        values.put("cangku_telephone", bean.getCangKuTelephone());
        values.put("mingpian_img_zm", bean.getMingPianImgZM());
        values.put("mingpian_img_fm", bean.getMingPianImgFM());

        long n = db.insert("tb_gongyinshang_info",
                null,
                values);
        if (n > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getGongyinshangNameWithId(int gongyinshangID) {
        String name = "";

        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_gongyinshang_info",
                null,
                "_id_gongyinshang = ?",
                new String[]{String.valueOf(gongyinshangID)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex("gongyinshang_name"));
        }

        cursor.close();
        db.close();
        return name;
    }


//    public ArrayList<DBFuliaoInfoBean> getDBGongyinshangInfoBeansForClothesID(int clothesInfoID) {
//        ArrayList<DBFuliaoInfoBean> dbFuliaoInfoBeans = new ArrayList<DBFuliaoInfoBean>();
//
//        db = myOpenHelper.getWritableDatabase();
//        Cursor cursor = db.query("tb_fuliao_info",
//                null,
//                "id_dahuo = ?",
//                new String[]{String.valueOf(clothesInfoID)},
//                null,
//                null,
//                null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                //datas.add(cursor.getString(cursor.getColumnIndex("year_info")));
//                DBFuliaoInfoBean dbFuliaoInfoBean = new DBFuliaoInfoBean();
//                dbFuliaoInfoBean.setId(cursor.getInt(cursor.getColumnIndex("_id_fuliao")));
//                dbFuliaoInfoBean.setDahuoId(cursor.getInt(cursor.getColumnIndex("id_dahuo")));
//                dbFuliaoInfoBean.setFuliaoName(cursor.getString(cursor.getColumnIndex("fuliao_name")));
//                dbFuliaoInfoBean.setFuliaoImg(cursor.getString(cursor.getColumnIndex("fuliao_img")));
//                dbFuliaoInfoBean.setJiage(cursor.getString(cursor.getColumnIndex("jiage")));
//                dbFuliaoInfoBean.setGongyinshangId(cursor.getInt(cursor.getColumnIndex("id_gongyingshang")));
//                dbFuliaoInfoBean.setBeizhu(cursor.getString(cursor.getColumnIndex("beizhu")));
//
//                dbFuliaoInfoBeans.add(dbFuliaoInfoBean);
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return dbFuliaoInfoBeans;
//    }


//    public ArrayList<String> getImgsWithDahuoIdHaveDb(int id, SQLiteDatabase db) {
//        ArrayList<String> imgs = new ArrayList<>();
//        //db = myOpenHelper.getWritableDatabase();
//        Cursor cursor = db.query("tb_fuliao_info",
//                null,
//                "id_dahuo=?",
//                new String[]{String.valueOf(id)},
//                null,
//                null,
//                null);
//
//        if (cursor != null && cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                imgs.add(cursor.getString(cursor.getColumnIndex("fuliao_img")));
//            }
//            cursor.close();
//        }
//        return imgs;
//    }
}
