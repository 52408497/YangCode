package com.example.administrator.jianshang.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.ApplictionWidthAndHeight;
import com.example.administrator.jianshang.Tools.CameraTool;
import com.example.administrator.jianshang.Tools.Constants;
import com.example.administrator.jianshang.Tools.FileUtils;
import com.example.administrator.jianshang.Tools.PhotoUtils;
import com.example.administrator.jianshang.adapters.DaHuoClothesInfoViewPagerAdapter;
import com.example.administrator.jianshang.adapters.FuLiaoAddRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.FuLiaoInfoRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.RecycleViewDivider;
import com.example.administrator.jianshang.bean.DBDaHuoInfoBean;
import com.example.administrator.jianshang.bean.ClothesInfoBean;
import com.example.administrator.jianshang.bean.DBDahuoImgBean;
import com.example.administrator.jianshang.bean.DBFuliaoInfoBean;
import com.example.administrator.jianshang.bean.FileBean;
import com.example.administrator.jianshang.bean.FuLiaoInfoBean;
import com.example.administrator.jianshang.bean.HandlerMessageBean;
import com.example.administrator.jianshang.bean.ImageBean;
import com.example.administrator.jianshang.myview.MyCamera;
import com.example.administrator.jianshang.sqlite.dao.ClothesInfoDao;
import com.example.administrator.jianshang.sqlite.dao.TbDahuoImgDao;
import com.example.administrator.jianshang.sqlite.dao.TbDahuoInfoDao;
import com.example.administrator.jianshang.sqlite.dao.TbGongyinshangInfoDao;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class DaHuoClothesInfoActivity extends AppCompatActivity {
//    public static final int CODE_CAMERA_REQUEST = 0xa2;            //拍照完成回调
//    private MyCamera myCamera;


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

    private ImageView iv_kscb_value;

    private MyCamera myCameraKSCB;
    private String newKscbImageFileName = "";
    private Uri newKscbImageFileUriForContent;
    private String newKstpImageFileName = "";
    private Uri newKstpImageFileUriForContent;

    private String imageKscbOld = "";
    private boolean copeFileIsOK = false;               //拷贝文件操作状态（用来判断progressDialog是否可关闭）
    private boolean insertDataBaseIsOK = false;         //将数据保存到数据库的状态（用来判断progressDialog是否可关闭）
    private ProgressDialog progressDialog;
    private boolean addIsSuccess = false;               //保存数据是否成功

//    private ArrayList<ImageBean> imageBeans;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //处理消息
            switch (msg.what) {
                case Constants.COPE_FILE:
                    copeFileIsOK = ((HandlerMessageBean) msg.obj).isCopeFileIsOK();

                    if (copeFileIsOK && insertDataBaseIsOK) {
                        progressDialog.dismiss();
                        copeFileIsOK = false;
                        insertDataBaseIsOK = false;

                        if (addIsSuccess) {
                            AlertDialog alertDialog1 = new AlertDialog.Builder(DaHuoClothesInfoActivity.this)
                                    .setTitle("提示")//标题
                                    .setMessage("款式图片添加成功！")//内容
                                    .setIcon(R.drawable.cg)//图标
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //DaHuoClothesInfoActivity.this.finish();
                                            TbDahuoImgDao tbDahuoImgDao = new TbDahuoImgDao(DaHuoClothesInfoActivity.this);
                                            dbDahuoImgBeans = tbDahuoImgDao.getDBDaHuoImgBeansForClothesID(dbDaHuoInfoBean.getId());

                                            initKSTP();
                                        }
                                    })
                                    .create();
                            alertDialog1.setCanceledOnTouchOutside(false);//触摸对话框边缘外部，对话框不消失
                            alertDialog1.show();
                            //finish();
                        } else {
                            AlertDialog alertDialog1 = new AlertDialog.Builder(DaHuoClothesInfoActivity.this)
                                    .setTitle("提示")//标题
                                    .setMessage("款式图片添加失败！")//内容
                                    .setIcon(R.drawable.jg)//图标
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //DaHuoClothesInfoActivity.this.finish();
                                        }
                                    })
                                    .create();
                            alertDialog1.setCanceledOnTouchOutside(false);//触摸对话框边缘外部，对话框不消失
                            alertDialog1.show();
                        }
                    }

                    ArrayList<String> copyErrorList = ((HandlerMessageBean) msg.obj).getCopyErrorList();
                    if (copyErrorList.size() > 0) {
                        String errorMessage = "拷贝相册文件的过程中出现错误，文件：";
                        for (String fileName :
                                copyErrorList) {
                            errorMessage = errorMessage + fileName + " ";
                        }
                        errorMessage = errorMessage + "拷贝失败！原因有三：1、源文件不存在 2、源文件路径和目标文件路径重复 3、该路径下已经有一个同名文件，该失败有可能会影响图片的显示！";
                        Toast.makeText(DaHuoClothesInfoActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                    break;




                case Constants.INSERT_DATABASE:
                    insertDataBaseIsOK = ((HandlerMessageBean) msg.obj).isInsertDataBaseIsOK();
                    addIsSuccess = ((HandlerMessageBean) msg.obj).isAddIsSuccess();
                    if (copeFileIsOK && insertDataBaseIsOK) {
                        progressDialog.dismiss();
                        copeFileIsOK = false;
                        insertDataBaseIsOK = false;

                        if (addIsSuccess) {
                            AlertDialog alertDialog1 = new AlertDialog.Builder(DaHuoClothesInfoActivity.this)
                                    .setTitle("提示")//标题
                                    .setMessage("款式图片添加成功！")//内容
                                    .setIcon(R.drawable.cg)//图标
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //DaHuoClothesInfoActivity.this.finish();
                                            TbDahuoImgDao tbDahuoImgDao = new TbDahuoImgDao(DaHuoClothesInfoActivity.this);
                                            dbDahuoImgBeans = tbDahuoImgDao.getDBDaHuoImgBeansForClothesID(dbDaHuoInfoBean.getId());

                                            initKSTP();
                                        }
                                    })
                                    .create();
                            alertDialog1.setCanceledOnTouchOutside(false);//触摸对话框边缘外部，对话框不消失
                            alertDialog1.show();
                            //finish();
                        } else {
                            AlertDialog alertDialog1 = new AlertDialog.Builder(DaHuoClothesInfoActivity.this)
                                    .setTitle("提示")//标题
                                    .setMessage("款式图片添加失败！")//内容
                                    .setIcon(R.drawable.jg)//图标
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //DaHuoClothesInfoActivity.this.finish();
                                        }
                                    })
                                    .create();
                            alertDialog1.setCanceledOnTouchOutside(false);//触摸对话框边缘外部，对话框不消失
                            alertDialog1.show();
                        }
                    }
                    break;
            }
        }
    };



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

        imageKscbOld = dbDaHuoInfoBean.getChengbenImg();
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


//        myCameraKSCB = viewClothesInfo.findViewById(R.id.myCamera);
////        myCameraKSCB.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Toast.makeText(DaHuoClothesInfoActivity.this,"你点击了按钮",Toast.LENGTH_SHORT).show();
////            }
////        });


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

    private void useRecyclerViewToShow(RecyclerView recyclerView, ArrayList<FuLiaoInfoBean> fuLiaoInfoBeans) {
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


        if (dbFuliaoInfoBeans.size() > 0) {
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
            useRecyclerViewToShow(recyclerView, fuLiaoInfoBeans);

            linearLayout.addView(recyclerView);

//            //添加每项点击事件监听
//            setOnItemClickListener();


        } else {
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

    private void initViewClothesInfo() {
        //初始化基本信息
        initJBXX();

        //初始化成本图片
        initKSCB();

        //初始化款式图片
        initKSTP();
    }

    //初始化款式图片
    private void initKSTP() {
        LinearLayout ll_kstp_kstp = viewClothesInfo.findViewById(R.id.ll_kstp_kstp);
        ll_kstp_kstp.removeAllViews();

        if (dbDahuoImgBeans.size() > 0) {

            for (DBDahuoImgBean bean : dbDahuoImgBeans) {
                ImageView imageView = new ImageView(DaHuoClothesInfoActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 0, 0, 5);
                imageView.setLayoutParams(layoutParams);
                imageView.setAdjustViewBounds(true);

                final String ksImgName = bean.getImgName();
                final int dahuoID = bean.getDahuoId();
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
                        Intent intent = new Intent(DaHuoClothesInfoActivity.this, ImageDetailActivity.class);
                        intent.putExtra(Constants.URI, uri.toString());
                        startActivity(intent);
                    }
                });

                imageView.setId(bean.getId());

                ll_kstp_kstp.addView(imageView);

                //给款式图片注册上下文菜单
                registerForContextMenu(imageView);

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

    //创建上下文菜单选项
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "删除");
    }

    //点击上下文菜单触发的事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {

                //删除图片数据
                TbDahuoImgDao dahuoImgDao = new TbDahuoImgDao(DaHuoClothesInfoActivity.this);
                boolean removeIsSuccess = dahuoImgDao.removeDahuoImgForId(item.getItemId());

        if (removeIsSuccess){

            TbDahuoImgDao tbDahuoImgDao = new TbDahuoImgDao(DaHuoClothesInfoActivity.this);
            dbDahuoImgBeans = tbDahuoImgDao.getDBDaHuoImgBeansForClothesID(dbDaHuoInfoBean.getId());

            initKSTP();

            Toast.makeText(DaHuoClothesInfoActivity.this,"图片删除成功！",Toast.LENGTH_SHORT).show();
        }




        return super.onContextItemSelected(item);
    }

    //初始化成本图片
    private void initKSCB() {
        LinearLayout ll_kscb_kscb = viewClothesInfo.findViewById(R.id.ll_kscb_kscb);
        iv_kscb_value = new ImageView(DaHuoClothesInfoActivity.this);
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
                Intent intent = new Intent(DaHuoClothesInfoActivity.this, ImageDetailActivity.class);
                intent.putExtra(Constants.URI, fileUriForContent.toString());
                startActivity(intent);
            }
        });

        ll_kscb_kscb.addView(iv_kscb_value);
    }

    //初始化基本信息
    private void initJBXX() {
        TextView tv_kh_value = viewClothesInfo.findViewById(R.id.tv_kh_value);
        TextView tv_ksmc_value = viewClothesInfo.findViewById(R.id.tv_ksmc_value);
        TextView tv_ybh_value = viewClothesInfo.findViewById(R.id.tv_ybh_value);
        TextView tv_bz_value = viewClothesInfo.findViewById(R.id.tv_bz_value);
        tv_kh_value.setText(dbDaHuoInfoBean.getKuanhao());
        tv_ksmc_value.setText(dbDaHuoInfoBean.getKuanshimingcheng());
        tv_ybh_value.setText(dbDaHuoInfoBean.getYangbanhao());
        tv_bz_value.setText(dbDaHuoInfoBean.getBeizhu());
    }


    private Uri getUriForContent(String chengbenImg) {
        String folderName = this.getString(R.string.my_photo_folder_name);
        File fileUri = new File(Environment.getExternalStorageDirectory().getPath() +
                "/" + folderName + "/" + chengbenImg);
        return Uri.fromFile(fileUri);
    }

    public void updateJbxx(View view) {
        Intent intent = new Intent(DaHuoClothesInfoActivity.this, ClothesInfoUpdateActivity.class);
        intent.putExtra("DBDaHuoInfoBean", dbDaHuoInfoBean);
        startActivityForResult(intent, 1);

    }

    public void updateKscbImg(View view) {
        MyCamera myCamera = (MyCamera) view;

        //拍照
        myCamera.takePicture(DaHuoClothesInfoActivity.this);

        //获取新照片的名称
        newKscbImageFileName = myCamera.getImageFileName();

        //获取新照片的Content类型URI
        newKscbImageFileUriForContent = myCamera.getFileUriForContent();
    }

    public void addKstpImg(View view) {
        PopupMenu popupMenu = new PopupMenu(DaHuoClothesInfoActivity.this, (ImageButton) view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_get_image, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_paizhao:
                        CameraTool cameraTool = new CameraTool(DaHuoClothesInfoActivity.this);

                        //拍照
                        cameraTool.takePicture(DaHuoClothesInfoActivity.this);

                        //获取新照片的名称
                        newKstpImageFileName = cameraTool.getImageFileName();

                        //获取新照片的Content类型URI
                        newKstpImageFileUriForContent = cameraTool.getFileUriForContent();


                        //Toast.makeText(DaHuoClothesInfoActivity.this, "你点击了拍照添加", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_item_xiangce:

                        String[] permissions;

                        permissions = new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, //写入SD卡的权限
                                Manifest.permission.READ_EXTERNAL_STORAGE   //读取SD卡的权限
                        };

                        if (EasyPermissions.hasPermissions(DaHuoClothesInfoActivity.this, permissions)) {

                            ApplictionWidthAndHeight applictionWidthAndHeight = new ApplictionWidthAndHeight(
                                    DaHuoClothesInfoActivity.this);

                            Matisse.from(DaHuoClothesInfoActivity.this)
                                    .choose(MimeType.allOf()) // 选择 mime 的类型
                                    .countable(true)           //显示选择的数量
                                    .maxSelectable(9) // 图片选择的最多数量
                                    .gridExpectedSize(applictionWidthAndHeight.getWidth() / 3 - 5)//图片显示在列表中的大小
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.85f) // 缩略图的比例
                                    .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                                    .theme(R.style.Matisse_Zhihu)//主题
                                    .capture(false)//是否提供拍照功能
                                    .forResult(Constants.CODE_GALLERY_REQUEST); // 设置作为标记的请求码

                        } else {
                            //权限还未申请，申请权限
                            EasyPermissions.requestPermissions(DaHuoClothesInfoActivity.this,
                                    "需要使用读写SD卡的权限",
                                    Constants.PERMISSION_SDCARD,
                                    permissions);
                        }

                        //Toast.makeText(DaHuoClothesInfoActivity.this, "你点击了从相册添加", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public void addFlxx(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 当otherActivity中返回数据的时候，会响应此方法
        // requestCode和resultCode必须与请求startActivityForResult()和返回setResult()的时候传入的值一致。
        if (requestCode == 1 && resultCode == ClothesInfoUpdateActivity.RESULT_CODE) {
            Bundle bundle = data.getExtras();
            DBDaHuoInfoBean resultBean = (DBDaHuoInfoBean) bundle.getSerializable("result");

            if (resultBean != null) {
                dbDaHuoInfoBean = resultBean;
                initJBXX();
                Toast.makeText(DaHuoClothesInfoActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DaHuoClothesInfoActivity.this, "对不起，修改过程出现错误!", Toast.LENGTH_SHORT).show();
            }

        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.CODE_CAMERA_REQUEST://款式成本拍照完成回调

                    //修改数据库数据
                    DBDaHuoInfoBean bean = getNewBean();
                    TbDahuoInfoDao dao = new TbDahuoInfoDao(DaHuoClothesInfoActivity.this);
                    int n = dao.updateDahuoInfo(bean);
                    if (n > 0) {
                        //删除旧的成本图片文件
                        delOldImageWithKscb();

                        dbDaHuoInfoBean = bean;
                        imageKscbOld = dbDaHuoInfoBean.getChengbenImg();

                        Glide.with(DaHuoClothesInfoActivity.this)
                                .load(newKscbImageFileUriForContent)
                                .placeholder(R.drawable.default_no_img)     //占位图
                                .error(R.drawable.default_no_img)           //出错的占位图
                                .dontAnimate()
                                .into(iv_kscb_value);

                        Toast.makeText(DaHuoClothesInfoActivity.this, "款式成本图片修改成功!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DaHuoClothesInfoActivity.this, "对不起，修改过程出现错误!", Toast.LENGTH_SHORT).show();
                    }

                    newKscbImageFileName = "";
                    newKscbImageFileUriForContent = null;

                    break;

                case Constants.CODE_CAMERA_TOOL_REQUEST:    //添加款式图片拍照完成回调
                    DBDahuoImgBean dbDahuoImgBean = new DBDahuoImgBean();
                    dbDahuoImgBean.setDahuoId(dbDaHuoInfoBean.getId());
                    dbDahuoImgBean.setImgName(newKstpImageFileName);
                    dbDahuoImgBean.setImgType(Constants.DB_DAHUO_IMG_TYPE_KS);

                    TbDahuoImgDao tbDahuoImgDao = new TbDahuoImgDao(DaHuoClothesInfoActivity.this);
                    boolean addAllDahuoImgIsSuccess = tbDahuoImgDao.addDahuoImg(dbDahuoImgBean);
                    if (addAllDahuoImgIsSuccess){
                        dbDahuoImgBeans = tbDahuoImgDao.getDBDaHuoImgBeansForClothesID(dbDahuoImgBean.getDahuoId());

                        initKSTP();

                        Toast.makeText(DaHuoClothesInfoActivity.this, "款式图片添加成功!", Toast.LENGTH_SHORT).show();
                    }

                    break;

                    case Constants.CODE_GALLERY_REQUEST: //访问相册完成回调
                        if (hasSdcard()) {

                            final ArrayList<FileBean> fileBeanListForKSToXC = new ArrayList<>();   //相册款式图片集合

                            List<Uri> mSelected = Matisse.obtainResult(data);

                            for (Uri u : mSelected) {
                                String sPath = PhotoUtils.getPath(DaHuoClothesInfoActivity.this, u);
                                String sName = getFileNameWithSuffix(sPath);    //获取文件名

                                FileBean fileBean = new FileBean();
                                fileBean.setFileName(sName);
                                fileBean.setFileUri(u);

                                fileBeanListForKSToXC.add(fileBean);
                            }


                            progressDialog = new ProgressDialog(DaHuoClothesInfoActivity.this);
                            progressDialog.setMessage("...请您稍等...");
                            progressDialog.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
                            progressDialog.show();



                            //拷贝相册里的照片到本应用专用的图片文件夹中
                            if (fileBeanListForKSToXC != null) {
                                final String folderName = this.getString(R.string.my_photo_folder_name);
                                final String newPath = Environment.getExternalStorageDirectory().getPath() + "/" + folderName + "/";
                                new Thread() {
                                    @Override
                                    public void run() {
                                        ArrayList<String> copyErrorList = new ArrayList<String>();
                                        for (FileBean bean :
                                                fileBeanListForKSToXC) {
                                            String sPath = PhotoUtils.getPathNoPathHead(                           //  绝对路径的URI
                                                    DaHuoClothesInfoActivity.this,
                                                    bean.getFileUri()
                                            );
                                            boolean isSuccess = FileUtils.copyFile(sPath, newPath);
                                            if (!isSuccess) {
                                                copyErrorList.add(bean.getFileName());
                                            }
                                        }
                                        HandlerMessageBean messageBean = new HandlerMessageBean();
                                        messageBean.setCopeFileIsOK(true);
                                        messageBean.setCopyErrorList(copyErrorList);
                                        Message msg = new Message();
                                        msg.what = Constants.COPE_FILE;
                                        msg.obj = messageBean;
                                        handler.sendMessage(msg);

                                    }
                                }.start();
                            } else {
                                copeFileIsOK = true;
                            }


                            //将数据添加到数据库中
                            new Thread() {
                                @Override
                                public void run() {
                                    HandlerMessageBean messageBean = new HandlerMessageBean();



                                    TbDahuoImgDao tbDahuoImgDao = new TbDahuoImgDao(DaHuoClothesInfoActivity.this);
                                    ArrayList<DBDahuoImgBean> beans = new ArrayList<>();
                                    for (FileBean bean:fileBeanListForKSToXC) {
                                        DBDahuoImgBean dbDahuoImgBean = new DBDahuoImgBean();
                                        dbDahuoImgBean.setDahuoId(dbDaHuoInfoBean.getId());
                                        dbDahuoImgBean.setImgName(bean.getFileName());
                                        dbDahuoImgBean.setImgType(Constants.DB_DAHUO_IMG_TYPE_KS);
                                        beans.add(dbDahuoImgBean);
                                    }


                                    boolean addAllDahuoImgIsSuccess = tbDahuoImgDao.addDahuoImgs(beans);


                                    messageBean.setAddIsSuccess(addAllDahuoImgIsSuccess);
                                    messageBean.setInsertDataBaseIsOK(true);
                                    Message msg = new Message();
                                    msg.what = Constants.INSERT_DATABASE;
                                    msg.obj = messageBean;
                                    handler.sendMessage(msg);

                                }
                            }.start();


                        }else {
                            Toast.makeText(DaHuoClothesInfoActivity.this, "设备没有SD卡!!", Toast.LENGTH_SHORT).show();
                        }
                        break;
            }
        }


    }


    //删除旧的成本图片文件
    private void delOldImageWithKscb() {
        String folderName = DaHuoClothesInfoActivity.this.getString(R.string.my_photo_folder_name);
        File file_img_kscb_old = new File(Environment.getExternalStorageDirectory().getPath() +
                "/" + folderName + "/" + imageKscbOld);
        if (file_img_kscb_old.exists()) {
            file_img_kscb_old.delete();//删除款式成本图片
        }
    }


    private DBDaHuoInfoBean getNewBean() {
        DBDaHuoInfoBean bean = dbDaHuoInfoBean;
        bean.setChengbenImg(newKscbImageFileName);
        return bean;
    }


    /**
     * 将图片显示在ImageView控件中
     *
     * @param uri
     * @param view
     */
    private void showImages(Uri uri, ImageView view) {

        Glide.with(DaHuoClothesInfoActivity.this).load(uri).into(view);

    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 保留文件名及后缀
     */
    public String getFileNameWithSuffix(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return null;
        }
    }
}
