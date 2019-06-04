package com.example.administrator.jianshang.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.adapters.ImageAndTestRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.RecycleViewDivider;
import com.example.administrator.jianshang.adapters.TimeRecyclerViewAdapter;
import com.example.administrator.jianshang.sqlite.dao.TbDauoInfoDao;

public class DaHuoListActivity extends AppCompatActivity {

//    private RecyclerView recyclerviewDahuoList;
//    private ImageAndTestRecyclerViewAdapter adapter;
//    private TbDauoInfoDao dauoInfoDao;
//    private Cursor cursor = null;
//    private ImageButton btnNewClothes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_huo_list);

        Intent intent = getIntent();
        String data = intent.getStringExtra("yearInfo");

//        recyclerviewDahuoList = findViewById(R.id.recyclerview_dahuo_list);
//        btnNewClothes = findViewById(R.id.btnNewClothes);

        //根据年份信息 查询数据库 获取款式列表

    }

    public void creatNewClothes(View view) {



    }




//    private void useRecyclerViewToShow() {
//        //设置RecyclerView的适配器
//        adapter = new TimeRecyclerViewAdapter(DaHuoListActivity.this, datas);
//        recyclerviewDahuoList.setAdapter(adapter);
//
//        //LayoutManager
//        //new LinearLayoutManager 参数 1、上下文 2、方向 3、是否倒序
//        recyclerviewDahuoList.setLayoutManager(new LinearLayoutManager(DaHuoListActivity.this, LinearLayoutManager.VERTICAL, false));
//        //倒序后设置选显示倒序第一行
//        //recyclerviewTime.scrollToPosition(datas.size()-1);
//
//        //添加默认分割线：高度为2px，颜色为灰色
//        recyclerviewDahuoList.addItemDecoration(new RecycleViewDivider(DaHuoListActivity.this, LinearLayoutManager.VERTICAL));
//
//
//    }
}
