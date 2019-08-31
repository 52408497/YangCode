package com.example.administrator.jianshang.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.Constants;
import com.example.administrator.jianshang.adapters.DaHuoListRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.RecycleViewDivider;
import com.example.administrator.jianshang.bean.DBDaHuoInfoBean;
import com.example.administrator.jianshang.sqlite.dao.ClothesInfoDao;
import com.example.administrator.jianshang.sqlite.dao.TbDahuoInfoDao;

import java.util.ArrayList;

public class DaHuoListActivity extends AppCompatActivity {

    private RecyclerView recyclerviewDahuoList;
    private DaHuoListRecyclerViewAdapter adapter;
    private TbDahuoInfoDao dauoInfoDao;
    private Cursor cursor = null;
    private ImageButton btnNewClothes;


private ArrayList<DBDaHuoInfoBean> dbDaHuoInfoBeans;
    private String timeData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_huo_list);

        Intent intent = getIntent();
        timeData = intent.getStringExtra("yearInfo");

        //初始化View
        initViews();

        //因为添加新款式后需要刷新本页，所以重写了onResume方法用来初始化数据
//        //初始化数据
//        initDatas();

    }

    private void initDatas() {

        //为recyclerview注册上下文菜单
        this.registerForContextMenu(recyclerviewDahuoList);


        //根据年份信息 查询数据库 获取款式列表
        TbDahuoInfoDao tbFuliaoInfoDao = new TbDahuoInfoDao(DaHuoListActivity.this);
        dbDaHuoInfoBeans = tbFuliaoInfoDao.getDBDaHuoInfoBeansForYears(timeData);

        if (dbDaHuoInfoBeans.size()>0) {
            //使用RecyclerView显示数据
            useDaHuoListRecyclerViewToShow();

            //添加每项点击事件监听
            setOnItemClickListener();

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,v,menuInfo);
        menu.add(0, 0, 0, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Toast.makeText(DaHuoListActivity.this,"点击删除",Toast.LENGTH_SHORT).show();
        String kh = dbDaHuoInfoBeans.get(adapter.getmPosition()).getKuanhao().toString();
        //int id = dbDaHuoInfoBeans.get(adapter.getmPosition()).getId();

        DBDaHuoInfoBean dbDaHuoInfoBean = dbDaHuoInfoBeans.get(adapter.getmPosition());



        //根据id删除内容，包括大货信息表/大货图片表/辅料信息表
        ClothesInfoDao clothesInfoDao = new ClothesInfoDao(DaHuoListActivity.this);
        boolean removeIsSuccess = clothesInfoDao.removeClothesForId(dbDaHuoInfoBean);

        if(removeIsSuccess){
            adapter.removeData(adapter.getmPosition());
            //重新添加每项点击事件监听
            setOnItemClickListener();

            Toast.makeText(DaHuoListActivity.this,"款式："+kh+"删除成功！",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(DaHuoListActivity.this,"款式："+kh+"删除失败！",Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
        //return true;
    }

    private void initViews() {
        recyclerviewDahuoList = findViewById(R.id.recyclerview_dahuo_list);
        btnNewClothes = findViewById(R.id.btnNewClothes);
    }

    private void setOnItemClickListener() {

        adapter.setOnItemClickListener(new DaHuoListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DBDaHuoInfoBean data) {
                //点击款式弹出款式信息详情
               // Toast.makeText(DaHuoListActivity.this,"点击款式",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClass(DaHuoListActivity.this, DaHuoClothesInfoActivity.class);
                //intent.putExtra("ClothesInfoID", data.getId());

                intent.putExtra("DBDaHuoInfoBean", data);
                startActivity(intent);


            }
        });
    }

    public void creatNewClothes(View view) {

        Intent intent = new Intent();
        intent.setClass(DaHuoListActivity.this, NewDaHuoClothesActivity.class);
        intent.putExtra("yearInfo", timeData);
        startActivity(intent);

    }

    private void useDaHuoListRecyclerViewToShow() {
        adapter = new DaHuoListRecyclerViewAdapter(DaHuoListActivity.this, dbDaHuoInfoBeans,0);
        recyclerviewDahuoList.setAdapter(adapter);
                //LayoutManager
        //new LinearLayoutManager 参数 1、上下文 2、方向 3、是否倒序
        recyclerviewDahuoList.setLayoutManager(new LinearLayoutManager(DaHuoListActivity.this, LinearLayoutManager.VERTICAL, false));
        //倒序后设置选显示倒序第一行
        recyclerviewDahuoList.scrollToPosition(dbDaHuoInfoBeans.size()-1);

        //添加默认分割线：高度为2px，颜色为灰色
        recyclerviewDahuoList.addItemDecoration(new RecycleViewDivider(DaHuoListActivity.this, LinearLayoutManager.VERTICAL));

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

    @Override
    public void onResume() {
        super.onResume();
        //初始化数据
        initDatas();
    }

    public void toSearchActivity(View view) {
        Intent intent = new Intent();
        intent.setClass(DaHuoListActivity.this, SearchActivity.class);
        intent.putExtra(Constants.TO_SEARCH_ACTIVITY,Constants.SEARCH_CLOTHESINFO);
        startActivity(intent);
    }
}
