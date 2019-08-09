package com.example.administrator.jianshang.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.activity.DaHuoListActivity;
import com.example.administrator.jianshang.activity.GongyinshangInfoActivity;
import com.example.administrator.jianshang.activity.NewDaHuoClothesActivity;
import com.example.administrator.jianshang.activity.NewGongYinShangActivity;
import com.example.administrator.jianshang.adapters.GongYinShangInfoRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.ImageAndTestRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.RecycleViewDivider;
import com.example.administrator.jianshang.base.BaseFragment;
import com.example.administrator.jianshang.bean.DBGongyinshangInfoBean;
import com.example.administrator.jianshang.sqlite.dao.TbGongyinshangInfoDao;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/5/25.
 */

public class GongYingShangFragment extends BaseFragment {

    private static final String TAG = GongYingShangFragment.class.getSimpleName();
    private TextView textView;

    private ImageButton btnAdd;
    private RecyclerView recyclerviewGongYingShangWithML;
    private RecyclerView recyclerviewGongYingShangWithFL;
    private LinearLayout ll_gongyinshang_title_ml;
    private LinearLayout ll_gongyinshang_title_fl;
    private LinearLayout ll_gongyinshang_main;

    private ArrayList<DBGongyinshangInfoBean> gongyinshangInfoBeansWithML;
    private ArrayList<DBGongyinshangInfoBean> gongyinshangInfoBeansWithFL;
    private GongYinShangInfoRecyclerViewAdapter adapterML;
    private GongYinShangInfoRecyclerViewAdapter adapterFL;

    private TbGongyinshangInfoDao tbGongyinshangInfoDao;

    @Override
    protected View initView() {
        Log.e(TAG,"供应商Fragment页面被初始化了！");

        View view = View.inflate(mContext, R.layout.activity_gongyinshang, null);
        btnAdd = view.findViewById(R.id.btnAdd);
        recyclerviewGongYingShangWithML = view.findViewById(R.id.recyclerview_gongyinshang_ml);
        recyclerviewGongYingShangWithFL = view.findViewById(R.id.recyclerview_gongyinshang_fl);
        ll_gongyinshang_title_ml = view.findViewById(R.id.ll_gongyinshang_title_ml);
        ll_gongyinshang_title_fl = view.findViewById(R.id.ll_gongyinshang_title_fl);
        ll_gongyinshang_main = view.findViewById(R.id.ll_gongyinshang_main);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();

        Log.e(TAG,"供应商Fragment页面数据被初始化了！");


        tbGongyinshangInfoDao = new TbGongyinshangInfoDao(mContext);
        gongyinshangInfoBeansWithML = tbGongyinshangInfoDao.getGongyinshangInfoBeansWithType(this.getString(R.string.gongyinshang_type_ml));
        gongyinshangInfoBeansWithFL = tbGongyinshangInfoDao.getGongyinshangInfoBeansWithType(this.getString(R.string.gongyinshang_type_fl));


        //使用RecyclerView显示数据
        if (gongyinshangInfoBeansWithML.size() > 0) {
            useRecyclerViewToShowML();

            //添加每项点击事件监听
            adapterML.setOnItemClickListener(new GongYinShangInfoRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, DBGongyinshangInfoBean data, int position) {

                    DBGongyinshangInfoBean gongyinshangInfoBean =  gongyinshangInfoBeansWithML.get(position);

                    Intent intent = new Intent();
                    intent.setClass(mContext, GongyinshangInfoActivity.class);
                    intent.putExtra("gongyinshangInfoBean", gongyinshangInfoBean);
                    startActivity(intent);



//                    String name =  gongyinshangInfoBeansWithML.get(position).getGongYinShangName();
//                    Toast.makeText(mContext, "item==" + name, Toast.LENGTH_SHORT).show();

                   // Toast.makeText(mContext, "item==" + data, Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            recyclerviewGongYingShangWithML.setVisibility(View.GONE);
            ll_gongyinshang_title_ml.setVisibility(View.GONE);
        }
        if (gongyinshangInfoBeansWithFL.size() > 0) {
            useRecyclerViewToShowFL();

            //添加每项点击事件监听
            adapterFL.setOnItemClickListener(new GongYinShangInfoRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, DBGongyinshangInfoBean data, int position) {

                    DBGongyinshangInfoBean gongyinshangInfoBean =  gongyinshangInfoBeansWithFL.get(position);

                    Intent intent = new Intent();
                    intent.setClass(mContext, GongyinshangInfoActivity.class);
                    intent.putExtra("gongyinshangInfoBean", gongyinshangInfoBean);
                    startActivity(intent);



//                   String name =  gongyinshangInfoBeansWithFL.get(position).getGongYinShangName();
//                    Toast.makeText(mContext, "item==" + name, Toast.LENGTH_SHORT).show();


                   // Toast.makeText(mContext, "item==" + data, Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            recyclerviewGongYingShangWithFL.setVisibility(View.GONE);
            ll_gongyinshang_title_fl.setVisibility(View.GONE);
        }

        if (gongyinshangInfoBeansWithML.size()<1 && gongyinshangInfoBeansWithFL.size() < 1){
            textView = new TextView(mContext);
            textView.setText("暂无数据");
            textView.setTextColor(Color.RED);
            textView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
            textView.setTextSize(20);
            ll_gongyinshang_main.addView(textView);
        }


        //添加按钮的点击事件监听
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(mContext, "添加新供应商", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClass(mContext, NewGongYinShangActivity.class);
                //intent.putExtra("yearInfo", timeData);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("data");
                if ("refresh".equals(msg)){
                    refresh();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver,intentFilter);

    }

    private void refresh() {
//        initView();
//        initData();
        gongyinshangInfoBeansWithML = tbGongyinshangInfoDao.getGongyinshangInfoBeansWithType(this.getString(R.string.gongyinshang_type_ml));
        gongyinshangInfoBeansWithFL = tbGongyinshangInfoDao.getGongyinshangInfoBeansWithType(this.getString(R.string.gongyinshang_type_fl));

        if (gongyinshangInfoBeansWithML.size()>1) {
            adapterML.updateData(gongyinshangInfoBeansWithML);
        recyclerviewGongYingShangWithML.scrollToPosition(gongyinshangInfoBeansWithML.size()-1);
        }else{
            textView.setVisibility(View.GONE);
            recyclerviewGongYingShangWithML.setVisibility(View.VISIBLE);
            ll_gongyinshang_title_ml.setVisibility(View.VISIBLE);
            useRecyclerViewToShowML();
            //添加每项点击事件监听
            adapterML.setOnItemClickListener(new GongYinShangInfoRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, DBGongyinshangInfoBean data, int position) {
                    DBGongyinshangInfoBean gongyinshangInfoBean =  gongyinshangInfoBeansWithML.get(position);
                    Intent intent = new Intent();
                    intent.setClass(mContext, GongyinshangInfoActivity.class);
                    intent.putExtra("gongyinshangInfoBean", gongyinshangInfoBean);
                    startActivity(intent);
                }
            });
        }
        if (gongyinshangInfoBeansWithFL.size()>1) {
            adapterFL.updateData(gongyinshangInfoBeansWithFL);
            recyclerviewGongYingShangWithFL.scrollToPosition(gongyinshangInfoBeansWithFL.size() - 1);
        }else {
            textView.setVisibility(View.GONE);
            recyclerviewGongYingShangWithFL.setVisibility(View.VISIBLE);
            ll_gongyinshang_title_fl.setVisibility(View.VISIBLE);
            useRecyclerViewToShowFL();
            //添加每项点击事件监听
            adapterFL.setOnItemClickListener(new GongYinShangInfoRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, DBGongyinshangInfoBean data, int position) {
                    DBGongyinshangInfoBean gongyinshangInfoBean =  gongyinshangInfoBeansWithFL.get(position);
                    Intent intent = new Intent();
                    intent.setClass(mContext, GongyinshangInfoActivity.class);
                    intent.putExtra("gongyinshangInfoBean", gongyinshangInfoBean);
                    startActivity(intent);
                }
            });
        }
    }

    private void useRecyclerViewToShowML() {
        //设置RecyclerView的适配器
        adapterML = new GongYinShangInfoRecyclerViewAdapter(mContext, gongyinshangInfoBeansWithML,0);
        recyclerviewGongYingShangWithML.setAdapter(adapterML);

        //LayoutManager
        //new GridLayoutManager 参数 1、上下文 2、列数 3、方向 4、是否倒序
        recyclerviewGongYingShangWithML.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //倒序后设置选显示倒序第一行
        //recyclerviewTime.scrollToPosition(datas.size()-1);

        //添加默认分割线：高度为2px，颜色为灰色
        recyclerviewGongYingShangWithML.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
    }

    private void useRecyclerViewToShowFL() {
        //设置RecyclerView的适配器
        adapterFL = new GongYinShangInfoRecyclerViewAdapter(mContext, gongyinshangInfoBeansWithFL,0);
        recyclerviewGongYingShangWithFL.setAdapter(adapterFL);

        //LayoutManager
        //new GridLayoutManager 参数 1、上下文 2、列数 3、方向 4、是否倒序
        recyclerviewGongYingShangWithFL.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //倒序后设置选显示倒序第一行
        //recyclerviewTime.scrollToPosition(datas.size()-1);

        //添加默认分割线：高度为2px，颜色为灰色
        recyclerviewGongYingShangWithFL.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
    }
}
