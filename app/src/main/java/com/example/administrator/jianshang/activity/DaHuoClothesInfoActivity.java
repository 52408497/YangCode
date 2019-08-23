package com.example.administrator.jianshang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.ApplictionWidthAndHeight;
import com.example.administrator.jianshang.Tools.Constants;
import com.example.administrator.jianshang.adapters.DaHuoClothesInfoViewPagerAdapter;
import com.example.administrator.jianshang.adapters.FuLiaoAddRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.FuLiaoInfoRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.RecycleViewDivider;
import com.example.administrator.jianshang.bean.DBDaHuoInfoBean;
import com.example.administrator.jianshang.bean.ClothesInfoBean;
import com.example.administrator.jianshang.bean.DBDahuoImgBean;
import com.example.administrator.jianshang.bean.DBFuliaoInfoBean;
import com.example.administrator.jianshang.bean.FuLiaoInfoBean;
import com.example.administrator.jianshang.sqlite.dao.ClothesInfoDao;
import com.example.administrator.jianshang.sqlite.dao.TbGongyinshangInfoDao;

import java.io.File;
import java.util.ArrayList;

public class DaHuoClothesInfoActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ClothesInfoBean clothesBean;
    private DBDaHuoInfoBean dbDaHuoInfoBean;
    private DBFuliaoInfoBean dbFuliaoInfoBean;
//    private  DBDahuoImgBean dbDahuoImgBean;
    private ArrayList<DBDahuoImgBean> dbDahuoImgBeans;
    private ArrayList<DBFuliaoInfoBean> dbFuliaoInfoBeans;

    private DaHuoClothesInfoViewPagerAdapter daHuoClothesInfoViewPagerAdapter;

    private ArrayList<View> myViewPager;
    private View viewClothesInfo;
    private View viewClothesFuliaoInfo;
//    private int defoutImg_width;
//    private int defoutImg_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_huo_clothes_info);


        ApplictionWidthAndHeight applictionWidthAndHeight = new ApplictionWidthAndHeight(DaHuoClothesInfoActivity.this);
//         defoutImg_width = applictionWidthAndHeight.getWidth() - 100;
//         defoutImg_height = defoutImg_width;

        getClothesBean();
        initView();


    }

    /**
     * 初始化数据
     */
    private void getClothesBean() {
        Intent intent = getIntent();
        DBDaHuoInfoBean daHuoInfoBean = (DBDaHuoInfoBean) intent.getSerializableExtra("DBDaHuoInfoBean");

        ClothesInfoDao dao = new ClothesInfoDao(DaHuoClothesInfoActivity.this);
        clothesBean = dao.getClothesInfoBeanForClothesInfoID(daHuoInfoBean.getId());
        dbDaHuoInfoBean = clothesBean.getDbDaHuoInfoBean();
        dbDahuoImgBeans = clothesBean.getDbDahuoImgBeans();
        dbFuliaoInfoBeans = clothesBean.getDbFuliaoInfoBeans();

    }

    /**
     * 初始化View
     */
    private void initView() {
        viewClothesInfo = View.inflate(DaHuoClothesInfoActivity.this, R.layout.viewpager_clothes_info, null);
        viewClothesFuliaoInfo = View.inflate(DaHuoClothesInfoActivity.this, R.layout.viewpager_clothes_fuliao, null);


        initViewClothesInfo();          //初始化款式信息页
        initViewClothesFuliaoInfo();    //初始化辅料信息页


        myViewPager = new ArrayList<>();
        myViewPager.add(viewClothesInfo);
        myViewPager.add(viewClothesFuliaoInfo);

        daHuoClothesInfoViewPagerAdapter = new DaHuoClothesInfoViewPagerAdapter(
                DaHuoClothesInfoActivity.this,
                myViewPager
        );

        viewPager = findViewById(R.id.vp_clothes_info);
        viewPager.setAdapter(daHuoClothesInfoViewPagerAdapter);


    }



    private ArrayList<FuLiaoInfoBean> getFuLiaoInfoBeans(ArrayList<DBFuliaoInfoBean> dbFuliaoInfoBeans) {
        ArrayList<FuLiaoInfoBean> fuLiaoInfoBeans = new ArrayList<>();
        TbGongyinshangInfoDao tbGongyinshangInfoDao = new TbGongyinshangInfoDao(DaHuoClothesInfoActivity.this);
        for (DBFuliaoInfoBean bean :
                dbFuliaoInfoBeans) {
            //根据供应商ID查询供应商名字
            String gongyinshang = tbGongyinshangInfoDao.getGongyinshangNameWithId(bean.getGongyinshangId());


//            String gongyinshang = "贵一点";

            FuLiaoInfoBean fuLiaoInfoBean = new FuLiaoInfoBean();
            fuLiaoInfoBean.setId(bean.getId());
            fuLiaoInfoBean.setDahuoId(bean.getDahuoId());
            fuLiaoInfoBean.setBeizhu(bean.getBeizhu());
            fuLiaoInfoBean.setFuliao_name(bean.getFuliaoName());
            fuLiaoInfoBean.setFuliao_img_name(bean.getFuliaoImg());
            fuLiaoInfoBean.setJiage(Float.parseFloat(bean.getJiage()));
            fuLiaoInfoBean.setGongyingshang(gongyinshang);
            fuLiaoInfoBean.setGongyinshangId(bean.getGongyinshangId());

            fuLiaoInfoBeans.add(fuLiaoInfoBean);
        }

        return fuLiaoInfoBeans;
    }

    private void useRecyclerViewToShow(RecyclerView recyclerView,ArrayList<FuLiaoInfoBean> fuLiaoInfoBeans) {
        //设置RecyclerView的适配器
        FuLiaoInfoRecyclerViewAdapter fuLiaoInfoRecyclerViewAdapter = new FuLiaoInfoRecyclerViewAdapter(DaHuoClothesInfoActivity.this, fuLiaoInfoBeans, 0);
        recyclerView.setAdapter(fuLiaoInfoRecyclerViewAdapter);
        //LayoutManager
        //new LinearLayoutManager 参数 1、上下文 2、方向 3、是否倒序
        recyclerView.setLayoutManager(new LinearLayoutManager(DaHuoClothesInfoActivity.this, LinearLayoutManager.VERTICAL, false));
        //倒序后设置选显示倒序第一行
        recyclerView.scrollToPosition(dbFuliaoInfoBeans.size() - 1);
        //添加默认分割线：高度为2px，颜色为灰色
        recyclerView.addItemDecoration(new RecycleViewDivider(DaHuoClothesInfoActivity.this, LinearLayoutManager.VERTICAL));



    }


    private void initViewClothesFuliaoInfo() {
        LinearLayout linearLayout = viewClothesFuliaoInfo.findViewById(R.id.ll_info_fuliao);


        if(dbFuliaoInfoBeans.size()>0){
            //RecyclerView recyclerView = viewClothesFuliaoInfo.findViewById(R.id.rv_fuliao_info);
            RecyclerView recyclerView = new RecyclerView(DaHuoClothesInfoActivity.this);
            recyclerView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    )
            );
            ArrayList<FuLiaoInfoBean> fuLiaoInfoBeans = getFuLiaoInfoBeans(dbFuliaoInfoBeans);

            //使用RecyclerView显示数据
            useRecyclerViewToShow(recyclerView,fuLiaoInfoBeans);

            linearLayout.addView(recyclerView);

//            //添加每项点击事件监听
//            setOnItemClickListener();


        }else {
        TextView textView = new TextView(DaHuoClothesInfoActivity.this);
        textView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );
        textView.setText("该款式暂无辅料信息");
        textView.setTextColor(Color.RED);
        textView.setTextSize(20);
        linearLayout.addView(textView);
        }

    }

    private void initViewClothesInfo(){
        //初始化基本信息
        TextView tv_kh_value = viewClothesInfo.findViewById(R.id.tv_kh_value);
        TextView tv_ksmc_value = viewClothesInfo.findViewById(R.id.tv_ksmc_value);
        TextView tv_ybh_value = viewClothesInfo.findViewById(R.id.tv_ybh_value);
        TextView tv_bz_value = viewClothesInfo.findViewById(R.id.tv_bz_value);
        tv_kh_value.setText(dbDaHuoInfoBean.getKuanhao());
        tv_ksmc_value.setText(dbDaHuoInfoBean.getKuanshimingcheng());
        tv_ybh_value.setText(dbDaHuoInfoBean.getYangbanhao());
        tv_bz_value.setText(dbDaHuoInfoBean.getBeizhu());

        //初始化成本图片
        LinearLayout ll_kscb_kscb = viewClothesInfo.findViewById(R.id.ll_kscb_kscb);
        ImageView iv_kscb_value = new ImageView(DaHuoClothesInfoActivity.this);
        iv_kscb_value.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
        iv_kscb_value.setAdjustViewBounds(true);

        String chengBenImgName = dbDaHuoInfoBean.getChengbenImg();
        final Uri fileUriForContent = getUriForContent(chengBenImgName);
        Glide.with(DaHuoClothesInfoActivity.this)
                .load(fileUriForContent)
                .placeholder(R.drawable.default_no_img)     //占位图
                .error(R.drawable.default_no_img)           //出错的占位图
                //.override(defoutImg_width, getDefoutImg_height)
                .dontAnimate()
                .into(iv_kscb_value);

        iv_kscb_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DaHuoClothesInfoActivity.this,ImageDetailActivity.class);
                intent.putExtra(Constants.ID , fileUriForContent.toString());
                startActivity(intent);
            }
        });

       ll_kscb_kscb.addView(iv_kscb_value);

                //初始化款式图片
        LinearLayout ll_kstp_kstp = viewClothesInfo.findViewById(R.id.ll_kstp_kstp);
        if (dbDahuoImgBeans.size() > 0) {
            for (DBDahuoImgBean bean : dbDahuoImgBeans) {
                ImageView imageView = new ImageView(DaHuoClothesInfoActivity.this);
                LinearLayout.LayoutParams layoutParams =  new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0,0,0,5);
                imageView.setLayoutParams( layoutParams );
                imageView.setAdjustViewBounds(true);

                String ksImgName = bean.getImgName();
                final Uri uri = getUriForContent(ksImgName);
                Glide.with(DaHuoClothesInfoActivity.this)
                        .load(uri)
                        .placeholder(R.drawable.default_no_img)     //占位图
                        .error(R.drawable.default_no_img)           //出错的占位图
                       // .override(defoutImg_width, getDefoutImg_height)
                        .dontAnimate()
                        .into(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DaHuoClothesInfoActivity.this,ImageDetailActivity.class);
                        intent.putExtra(Constants.ID , uri.toString());
                        startActivity(intent);
                    }
                });

                ll_kstp_kstp.addView(imageView);


                //这里可添加长按删除照片功能
//                imageView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//
//                        return false;
//                    }
//                });

            }
        } else {
            ImageView imageView = new ImageView(DaHuoClothesInfoActivity.this);
            imageView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );
            imageView.setAdjustViewBounds(true);

            Glide.with(DaHuoClothesInfoActivity.this)
                    .load("***")
                    .placeholder(R.drawable.default_no_img)     //占位图
                    .error(R.drawable.default_no_img)           //出错的占位图
                    .into(imageView);
            ll_kstp_kstp.addView(imageView);
        }

    }



    private Uri getUriForContent(String chengbenImg) {
        String folderName = this.getString(R.string.my_photo_folder_name);
        File fileUri = new File(Environment.getExternalStorageDirectory().getPath() +
                "/" + folderName + "/" + chengbenImg);
        return Uri.fromFile(fileUri);
    }

    public void updateJbxx(View view) {

    }

    public void updateKscbImg(View view) {

    }

    public void addKstpImg(View view) {

    }

    public void addFlxx(View view) {

    }
}
