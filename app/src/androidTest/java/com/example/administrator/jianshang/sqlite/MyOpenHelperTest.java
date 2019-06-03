package com.example.administrator.jianshang.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2019/5/30.
 */
public class MyOpenHelperTest extends AndroidTestCase {

    private MyOpenHelper myOpenHelper = null;
    private SQLiteDatabase db = null;


    @Before
    public void setUp() throws Exception {
        Log.i("test","所有测试方法执行之前执行");
        //System.out.println("所有测试方法执行之前执行");
        //配置要创建的数据库信息
        myOpenHelper = new MyOpenHelper(this.getContext(),"jianshang.db",null,1);

        //创建数据库
        db = myOpenHelper.getWritableDatabase();

    }

    @After
    public void tearDown() throws Exception {
        Log.i("test","所有测试方法执行之后执行");
        //System.out.println("所有测试方法执行之后执行");

        //关闭数据库及相关资源
        myOpenHelper.close();
        db.close();
    }

    @Test
    public void test02(){
        Log.i("test","此处编写测试代码");
        //System.out.println("此处编写测试代码");
    }
}