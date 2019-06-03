package com.example.administrator.jianshang.fragment;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.adapters.ImageAndTestRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.RecycleViewDivider;
import com.example.administrator.jianshang.adapters.TimeRecyclerViewAdapter;
import com.example.administrator.jianshang.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/5/25.
 */

public class YangYiFragment extends BaseFragment {

    private static final String TAG = YangYiFragment.class.getSimpleName();

    private ImageButton btnAdd;
    private RecyclerView recyclerviewYangyi;

    private ArrayList<String> datas;

    private ImageAndTestRecyclerViewAdapter adapter;

    @Override
    protected View initView() {
        Log.e(TAG,"样衣Fragment页面被初始化了！");

        View view = View.inflate(mContext, R.layout.activity_yangyi, null);
        btnAdd = view.findViewById(R.id.btnAdd);
        recyclerviewYangyi = view.findViewById(R.id.recyclerview_yangyi);

        return view;
    }



    @Override
    protected void initData() {
        super.initData();

        Log.e(TAG,"样衣Fragment页面数据被初始化了！");


        datas = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            datas.add("数据_" + i);
        }

        //使用RecyclerView显示数据
        useRecyclerViewToShow();

        //添加每项点击事件监听
        adapter.setOnItemClickListener(new ImageAndTestRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Toast.makeText(mContext, "item==" + data, Toast.LENGTH_SHORT).show();
            }
        });


        //添加按钮的点击事件监听
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {








                Toast.makeText(mContext, "新做一件", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void useRecyclerViewToShow() {
        //设置RecyclerView的适配器
        adapter = new ImageAndTestRecyclerViewAdapter(mContext, datas,1);
        recyclerviewYangyi.setAdapter(adapter);

        //LayoutManager
        //new GridLayoutManager 参数 1、上下文 2、列数 3、方向 4、是否倒序
        recyclerviewYangyi.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));
        //倒序后设置选显示倒序第一行
        //recyclerviewTime.scrollToPosition(datas.size()-1);

        //添加默认分割线：高度为2px，颜色为灰色
        recyclerviewYangyi.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
    }
}
