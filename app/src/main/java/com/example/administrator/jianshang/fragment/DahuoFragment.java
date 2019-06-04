package com.example.administrator.jianshang.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.adapters.RecycleViewDivider;
import com.example.administrator.jianshang.adapters.TimeRecyclerViewAdapter;
import com.example.administrator.jianshang.base.BaseFragment;
import com.example.administrator.jianshang.sqlite.dao.TbYearsInfoDao;

import java.util.ArrayList;

/**
 * Created by Administrator on 2019/5/25.
 */

public class DahuoFragment extends BaseFragment {

    private static final String TAG = DahuoFragment.class.getSimpleName();
    private ArrayList<String> datas;


    private ImageButton btnAdd;
    private RecyclerView recyclerviewTime;

    private TimeRecyclerViewAdapter adapter;

    private TbYearsInfoDao yearsInfoDao;

    private Dialog dlg;
    private long n = -1;
    private Cursor cursor = null;


    @Override
    protected View initView() {
        Log.e(TAG, "大货Fragment页面被初始化了！");

        View view = View.inflate(mContext, R.layout.activity_da_huo_time, null);
        btnAdd = view.findViewById(R.id.btnAdd);
        recyclerviewTime = view.findViewById(R.id.recyclerview_time);

        return view;
    }


    @Override
    protected void initData() {
        super.initData();

        //Log.e(TAG, "大货Fragment页面数据被初始化了！");

        registerForContextMenu(recyclerviewTime);//为recyclerview注册上下文菜单

        //创建弹出窗口
        createDialog();

        //查询数据库,添加数据
        datas = new ArrayList<String>();

        yearsInfoDao = new TbYearsInfoDao(mContext);
        cursor = yearsInfoDao.getAllYearsInfo();
        if (cursor.moveToFirst()) {
            do {
                datas.add(cursor.getString(cursor.getColumnIndex("year_info")));
            } while (cursor.moveToNext());
        }
        cursor.close();

        //使用RecyclerView显示数据
        useRecyclerViewToShow();

        //添加按钮的点击事件监听
        setOnClickListener();

        //添加每项点击事件监听
        setOnItemClickListener();


    }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 0, 0, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (getUserVisibleHint()){

            String sYearInfo = datas.get(adapter.getmPosition());
            //Toast.makeText(mContext, "删除："+sYearInfo, Toast.LENGTH_SHORT).show();

            //从数据库中删除
            n = yearsInfoDao.delYearInfo(sYearInfo);
            if(n>0){
                adapter.removeData(adapter.getmPosition());
                //重新添加每项点击事件监听
                setOnItemClickListener();
                Toast.makeText(mContext, "数据删除成功！", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(mContext, "数据删除过程中出现错误，请重试！", Toast.LENGTH_SHORT).show();
            }
            n = -1;
            return true;
        }else {
            return false;
        }

    }

    private void setOnClickListener() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.show();
                //使用RecyclerView显示数据
                useRecyclerViewToShow();
            }
        });
    }

    private void setOnItemClickListener() {
        adapter.setOnItemClickListener(new TimeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Toast.makeText(mContext, "item==" + data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_view, null);
        final EditText etYearInfo = view.findViewById(R.id.et_YearInfo);

        dlg = new AlertDialog.Builder(mContext)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String yearInfo = etYearInfo.getText().toString().trim();
                        //根据年份信息查询数据库是否存在
                        cursor = yearsInfoDao.getYearsInfoForYearInfo(yearInfo);
                        //若存在则提示该数据已存在，不能重复添加
                        if (cursor.moveToFirst()) {
                            Toast.makeText(mContext, "该数据已存在，不能重复添加", Toast.LENGTH_SHORT).show();
                        } else {
                            //若不存在则添加数据
                            n = yearsInfoDao.addYearInfo(yearInfo);
                            if (n > 0) {
                                //添加数据
                                adapter.addData(0, yearInfo);
                                //重新添加每项点击事件监听
                                setOnItemClickListener();
                                Toast.makeText(mContext, "数据添加成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "数据添加过程中出现错误，请重试！", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        n = -1;
                    }
                })
                .create();


    }


    private void useRecyclerViewToShow() {
        //设置RecyclerView的适配器
        adapter = new TimeRecyclerViewAdapter(mContext, datas);
        recyclerviewTime.setAdapter(adapter);

        //LayoutManager
        //new LinearLayoutManager 参数 1、上下文 2、方向 3、是否倒序
        recyclerviewTime.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        //倒序后设置选显示倒序第一行
        //recyclerviewTime.scrollToPosition(datas.size()-1);

        //添加默认分割线：高度为2px，颜色为灰色
        recyclerviewTime.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));


    }
}
