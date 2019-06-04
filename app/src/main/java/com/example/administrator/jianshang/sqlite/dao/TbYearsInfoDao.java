package com.example.administrator.jianshang.sqlite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.jianshang.sqlite.MyOpenHelper;

/**
 * Created by Administrator on 2019/6/2.
 */

public class TbYearsInfoDao {

    private MyOpenHelper myOpenHelper = null;
    private SQLiteDatabase db = null;

    public TbYearsInfoDao(Context context) {
        myOpenHelper = new MyOpenHelper(context, "jianshang.db", null, 1);
    }

    /**
     * 删除年份
     * @param yearInfo
     * @return
     */
    public int delYearInfo(String yearInfo){
    db = myOpenHelper.getWritableDatabase();
    int n = db.delete("tb_year_info",
            "year_info=?",
            new String[]{yearInfo}
            );
    db.close();
    myOpenHelper.close();
    return n;
}


    /**
     * 添加新的年份
     *
     * @param yearInfo
     * @return
     */
    public long addYearInfo(String yearInfo) {
        db = myOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("year_info", yearInfo);
        long n = db.insert("tb_year_info",
                null,
                values);
        db.close();
        myOpenHelper.close();
        return n;
    }

    /**
     * 查询年份表
     * @return
     */
    public Cursor getAllYearsInfo() {
        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_year_info",
                null,
                null,
                null,
                null,
                null,
                null);
        return cursor;
    }

    /**
     * 查询年份表
     * @return
     */
    public Cursor getYearsInfoForYearInfo(String yearInfo) {
        db = myOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("tb_year_info",
                null,
                "year_info=?",
                new String[]{yearInfo},
                null,
                null,
                null);
        return cursor;
    }

}
