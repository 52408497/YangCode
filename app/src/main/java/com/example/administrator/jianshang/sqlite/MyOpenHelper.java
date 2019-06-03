package com.example.administrator.jianshang.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2019/5/30.
 */

public class MyOpenHelper extends SQLiteOpenHelper {

    /**
     * 实例化该类时，根据提供的数据库文件名创建数据库
     * @param context
     * @param name  数据库文件名
     * @param factory  游标 如为null使用的是默认的方式
     * @param version   版本
     */
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 在第一次使用时，当数据库不存在，创建数据库文件时执行此方法
     * 此方法一般用来创建数据库表
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        creatTables(sqLiteDatabase);
        Log.i("test:","数据库初始化已完成");
    }

    /**
     * 当数据库版本更新时执行此方法
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("test:","数据库版本已更新，数据已清空！");
        //删除原有的表
        dropTables(sqLiteDatabase);
        //重新创建表
        creatTables(sqLiteDatabase);
    }

    private void dropTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("drop table if exists tb_year_info");
        sqLiteDatabase.execSQL("drop table if exists tb_dahuo_info");
        sqLiteDatabase.execSQL("drop table if exists tb_dahuo_img");
        sqLiteDatabase.execSQL("drop table if exists tb_yangyi_info");
        sqLiteDatabase.execSQL("drop table if exists tb_yangyi_img");
        sqLiteDatabase.execSQL("drop table if exists tb_gongyinshang_info");
        sqLiteDatabase.execSQL("drop table if exists tb_mianliao_info");
        sqLiteDatabase.execSQL("drop table if exists tb_fuliao_info");
    }




    private void creatTables(SQLiteDatabase sqLiteDatabase) {
        //创建年份表
        sqLiteDatabase.execSQL("create table tb_year_info(_id_year integer primary key autoincrement,year_info text)");
        //创建大货信息表
        sqLiteDatabase.execSQL("create table tb_dahuo_info(" +
                "_id_dahuo integer primary key autoincrement," +
                "kuanhao text," +
                "kuanshimingcheng text," +
                "yangbanhao text," +
                "id_year integer," +
                "tag text," +
                "beizhu text)");

        //创建大货图片表
        sqLiteDatabase.execSQL("create table tb_dahuo_img(" +
                "_id_dahuo_img integer primary key autoincrement," +
                "id_dahuo integer," +
                "img_type text," +
                "img_name text)");

        //创建样衣信息表
        sqLiteDatabase.execSQL("create table tb_yangyi_info(" +
                "_id_yangyi integer primary key autoincrement," +
                "kuanshimingcheng text," +
                "yangbanhao text," +
                "creat_time text," +
                "beizhu text)");

        //创建样衣图片表
        sqLiteDatabase.execSQL("create table tb_yangyi_img(" +
                "_id_yangyi_img integer primary key autoincrement," +
                "id_yangyi integer," +
                "img_type text," +
                "img_name text)");

        //创建供应商信息表
        sqLiteDatabase.execSQL("create table tb_gongyinshang_info(" +
                "_id_gongyinshang integer primary key autoincrement," +
                "gongyinshang_type text," +
                "gongyinshang_name text," +
                "dangkou_address text," +
                "cangku_address text," +
                "dangkou_telephone integer," +
                "cangku_telephone integer," +
                "mingpian_img text)");

        //创建面料信息表
        sqLiteDatabase.execSQL("create table tb_mianliao_info(" +
                "_id_mianliao integer primary key autoincrement," +
                "id_dahuo integer," +
                "mianliao_name text," +
                "mianliao_img text," +
                "jiage text," +
                "id_gongyingshang integer," +
                "beizhu text)");

        //创建辅料信息表
        sqLiteDatabase.execSQL("create table tb_fuliao_info(" +
                "_id_fuliao integer primary key autoincrement," +
                "id_dahuo integer," +
                "fuliao_name text," +
                "fuliao_img text," +
                "jiage text," +
                "id_gongyingshang integer," +
                "beizhu text)");
    }
}
