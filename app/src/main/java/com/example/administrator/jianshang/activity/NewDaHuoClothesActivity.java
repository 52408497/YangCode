package com.example.administrator.jianshang.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.ApplictionWidthAndHeight;
import com.example.administrator.jianshang.Tools.CommonPopupWindow;
import com.example.administrator.jianshang.Tools.Constants;
import com.example.administrator.jianshang.Tools.FileUtils;
import com.example.administrator.jianshang.Tools.ClothesTagType;
import com.example.administrator.jianshang.Tools.PhotoUtils;
import com.example.administrator.jianshang.adapters.FuLiaoAddRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.KuanShiImageListRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.RecycleViewDivider;
import com.example.administrator.jianshang.bean.DBDaHuoInfoBean;
import com.example.administrator.jianshang.bean.DBDahuoImgBean;
import com.example.administrator.jianshang.bean.DBFuliaoInfoBean;
import com.example.administrator.jianshang.bean.FileBean;
import com.example.administrator.jianshang.bean.FuLiaoInfoBean;
import com.example.administrator.jianshang.bean.GongYinShangBean;
import com.example.administrator.jianshang.bean.HandlerMessageBean;
import com.example.administrator.jianshang.bean.ClothesInfoBean;
import com.example.administrator.jianshang.sqlite.dao.ClothesInfoDao;
import com.example.administrator.jianshang.sqlite.dao.TbGongyinshangInfoDao;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.widget.Spinner;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class NewDaHuoClothesActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private String timeData;    //年份信息


    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int PERMISSION_CAMERA_SDCARD = 0xa2;
    private static final int PERMISSION_SDCARD = 0xa3;
    private static final int CODE_CAMERA_CB_REQUEST = 0xa4;
    private static final int CODE_CAMERA_FL_REQUEST = 0xa5;
    private static final int COPE_FILE = 0x0001;
    private static final int INSERT_DATABASE = 0x0002;


    private ClothesInfoDao clothesInfoDao;

    private View oldView = null;
    private TextView oldTvFmView = null;


    //--------主窗体控件----------
    private ScrollView scrollView;
    private EditText etKH;       //款号
    private EditText etKSMC;    //款式名称
    private EditText etYBH;     //样板号
    private EditText etBZ;      //备注
    private ImageView ivCB;     //成本图片

    private RecyclerView imgKsListRecyclerview;     //款式图片Recyclerview
    private RecyclerView fuliaoListRecyclerview;    //辅料列表Recyclerview

    private KuanShiImageListRecyclerViewAdapter kuanShiImageListRecyclerViewAdapter;
    private FuLiaoAddRecyclerViewAdapter fuLiaoAddRecyclerViewAdapter;
    //---------------------------

    //    //-----悬浮窗控件------------------
    CommonPopupWindow popupWindow;
    private EditText etName;            //辅料名称
    private EditText etJiage;           //价格
    private Spinner spGongyinshang;     //供应商
    private ImageButton ibPaiZhao;      //拍照按钮
    private ImageView ivTuPian;         //辅料图片
    private Button btnOk;               //确定按钮
    private Button btnQx;               //取消按钮
    private RadioGroup radioGroup;      //面辅料类型单选按钮组

//    //----------------------------------

    private FuLiaoInfoBean fuLiaoInfoBean;
    private String sfengmian = "";

    private String[] permissions;
    private String imageFileName;

    private File fileUri;
    private File fileUriCB;
    private File fileUriFL;

    private Uri fileUriForContent;
    private Uri fileUriCBForContent;
    private Uri fileUriFLForContent;

    private FileBean fileBean;
    private FileBean beDelFileBean = null;

    private String sType = "";

    private String oldPath = "";
    private String newPath = "";
    private String folderName = "";

    private ArrayList<FileBean> fileBeanListForKS;       //所有款式图片集合
    private ArrayList<FileBean> fileBeanListForKSToXC;   //相册款式图片集合
    private ArrayList<FuLiaoInfoBean> fuLiaoInfoBeans;  //添加的辅料信息集合


    private String fileNameForCB = "";               //成本图片名
    private String fileNameForFL = "";               //辅料图片名


    private ArrayList<DBFuliaoInfoBean> dbFuliaoInfoBeans;//辅料信息列表集合
    private ArrayList<DBDahuoImgBean> dbDahuoImgBeans;  //大货图片列表集
    private DBDaHuoInfoBean dbDaHuoInfoBean;            //数据库大货信息Bean
    private ClothesInfoBean clothesInfoBean;    //新添加的款式信息Bean
    private boolean copeFileIsOK = false;               //拷贝文件操作状态（用来判断progressDialog是否可关闭）
    private boolean insertDataBaseIsOK = false;         //将数据保存到数据库的状态（用来判断progressDialog是否可关闭）
    private ProgressDialog progressDialog;
    private boolean addIsSuccess = false;               //保存数据是否成功
    private float scrollViewY;


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //处理消息
            switch (msg.what) {
                case COPE_FILE:
                    copeFileIsOK = ((HandlerMessageBean) msg.obj).isCopeFileIsOK();

                    if (copeFileIsOK && insertDataBaseIsOK) {
                        progressDialog.dismiss();
                        copeFileIsOK = false;
                        insertDataBaseIsOK = false;

                        if (addIsSuccess) {
                            AlertDialog alertDialog1 = new AlertDialog.Builder(NewDaHuoClothesActivity.this)
                                    .setTitle("提示")//标题
                                    .setMessage("数据保存成功！")//内容
                                    .setIcon(R.drawable.cg)//图标
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            NewDaHuoClothesActivity.this.finish();
                                        }
                                    })
                                    .create();
                            alertDialog1.setCanceledOnTouchOutside(false);//触摸对话框边缘外部，对话框不消失
                            alertDialog1.show();
                            //finish();
                        } else {
                            AlertDialog alertDialog1 = new AlertDialog.Builder(NewDaHuoClothesActivity.this)
                                    .setTitle("提示")//标题
                                    .setMessage("数据保存失败！")//内容
                                    .setIcon(R.drawable.jg)//图标
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            NewDaHuoClothesActivity.this.finish();
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
                        Toast.makeText(NewDaHuoClothesActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                    break;
                case INSERT_DATABASE:
                    insertDataBaseIsOK = ((HandlerMessageBean) msg.obj).isInsertDataBaseIsOK();
                    addIsSuccess = ((HandlerMessageBean) msg.obj).isAddIsSuccess();
                    if (copeFileIsOK && insertDataBaseIsOK) {
                        progressDialog.dismiss();
                        copeFileIsOK = false;
                        insertDataBaseIsOK = false;

                        if (addIsSuccess) {
                            AlertDialog alertDialog1 = new AlertDialog.Builder(NewDaHuoClothesActivity.this)
                                    .setTitle("提示")//标题
                                    .setMessage("数据保存成功！")//内容
                                    .setIcon(R.drawable.cg)//图标
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            NewDaHuoClothesActivity.this.finish();
                                        }
                                    })
                                    .create();
                            alertDialog1.setCanceledOnTouchOutside(false);//触摸对话框边缘外部，对话框不消失
                            alertDialog1.show();
                            //finish();
                        } else {
                            AlertDialog alertDialog1 = new AlertDialog.Builder(NewDaHuoClothesActivity.this)
                                    .setTitle("提示")//标题
                                    .setMessage("数据保存失败！")//内容
                                    .setIcon(R.drawable.jg)//图标
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            NewDaHuoClothesActivity.this.finish();
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

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 改变滚动条的位置
            boolean scrollDownCB = getIntent().getBooleanExtra("SCROLL_DOWN_CB", false);
            RelativeLayout rl_kscb = findViewById(R.id.rl_kscb);

            scrollView.scrollTo(0, rl_kscb.getTop());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_da_huo_clothes);


        //查看SD卡中是否存在JianShangPhoto文件夹，若不存在，则创建
        folderName = this.getString(R.string.my_photo_folder_name);
        creatFolder(folderName);

        fuLiaoInfoBeans = new ArrayList<FuLiaoInfoBean>();
        fileBeanListForKS = new ArrayList<FileBean>();
        fileBeanListForKSToXC = new ArrayList<FileBean>();

        //获取View控件对象
        initView();

        //为recyclerview注册上下文菜单
        registerForContextMenu(imgKsListRecyclerview);

        //使用RecyclerView显示数据
        useRecyclerViewToShow();

        //使用RecyclerView显示辅料信息数据
        useRecyclerViewToShowFuliaoList();

        //添加每项点击事件监听
        setOnItemClickForCSListener();

        //为辅料RecyclerView添加点击事件
        setOnItemClickForFLListener();
        Intent intent = getIntent();
        timeData = intent.getStringExtra("yearInfo");

        //initPopupWindow();
        //initPop();
    }

    /**
     * 获取View控件对象
     */
    private void initView() {
        scrollView = findViewById(R.id.sv_scrollView);

        fuliaoListRecyclerview = findViewById(R.id.recyclerview_img_fl_list);
        imgKsListRecyclerview = findViewById(R.id.recyclerview_img_ks_list);
        ivCB = findViewById(R.id.iv_cb);
        etKH = findViewById(R.id.et_kh);
        etKSMC = findViewById(R.id.et_mc);
        etYBH = findViewById(R.id.et_ybh);
        etBZ = findViewById(R.id.et_bz);
    }

    /**
     * 辅料RecyclerView点击事件监听
     */
    private void setOnItemClickForFLListener() {
        fuLiaoAddRecyclerViewAdapter.setOnItemClickListener(new FuLiaoAddRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, FuLiaoInfoBean data, int position) {
                //点击后打开悬浮窗口重新赋值
                initPopupWindowHaveBean(view, data, position);
            }
        });

    }

    /**
     * 创建存放图片的文件夹
     *
     * @param folderName
     */
    private void creatFolder(String folderName) {
        if (hasSdcard()) {

            File appDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + folderName);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
        }
    }


    /**
     * 上下文菜单添加删除按钮
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 0, 0, "删除");
    }


    /**
     * 上下文菜单删除操作
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //初始化oldView
        reSetOldView();

        int delPosition = kuanShiImageListRecyclerViewAdapter.getmPosition();
        beDelFileBean = fileBeanListForKS.get(delPosition); //将要被删除的图片

        fileBeanListForKS.remove(beDelFileBean);

        for (FileBean b :
                fileBeanListForKSToXC) {
            if (b.getFileName().trim().equals(beDelFileBean.getFileName().trim())) {
                fileBeanListForKSToXC.remove(b);
                break;
            }

        }

        kuanShiImageListRecyclerViewAdapter.updateData(fileBeanListForKS);
        imgKsListRecyclerview.scrollToPosition(fileBeanListForKS.size() - 1);
        beDelFileBean = null;

        return true;
    }

    /**
     * 初始化oldView
     */
    private void reSetOldView() {
        if (oldView != null) {
            oldView.findViewById(R.id.id_linear_layout)
                    .setBackgroundColor(Color.WHITE);
            oldView = null;
            sfengmian = "";

            oldTvFmView = null;

        }
    }


    /**
     * 点击款式图某项
     */
    private void setOnItemClickForCSListener() {
        kuanShiImageListRecyclerViewAdapter.setOnItemClickListener(new KuanShiImageListRecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, FileBean data, int position) {

                if (oldView != null) {
                    if (view != oldView) {
                        view.findViewById(R.id.id_linear_layout)
                                .setBackgroundColor(getResources().getColor(R.color.blanchedalmond));
                        oldView.findViewById(R.id.id_linear_layout)
                                .setBackgroundColor(Color.WHITE);
                        oldView = view;
                    }
                    if (oldTvFmView != view.findViewById(R.id.tv_title)) {
                        ((TextView) view.findViewById(R.id.tv_title)).setText("设为封面");
                        ((TextView) view.findViewById(R.id.tv_title)).setTextColor(Color.RED);

                        oldTvFmView.setText("");
                        oldTvFmView.setTextColor(Color.WHITE);

                        oldTvFmView = view.findViewById(R.id.tv_title);
                    }

                } else {
                    view.findViewById(R.id.id_linear_layout)
                            .setBackgroundColor(getResources().getColor(R.color.blanchedalmond));
                    oldView = view;

                    oldTvFmView = view.findViewById(R.id.tv_title);
                    oldTvFmView.setText("设为封面");
                    oldTvFmView.setTextColor(Color.RED);
                }

                sfengmian = data.getFileName(); //设置封面图片
            }
        });
    }


    /**
     * 点击拍照获取款式图片按钮
     *
     * @param view
     */

    public void paiZhaoOnClickForKSTP(View view) {
        getKstpForPZ();
    }

    //@AfterPermissionGranted(PERMISSION_CAMERA_SDCARD)
    private void getKstpForPZ() {
        permissions = new String[]{
                Manifest.permission.CAMERA,                 //使用相机的权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE, //写入SD卡的权限
                Manifest.permission.READ_EXTERNAL_STORAGE   //读取SD卡的权限
        };


        if (EasyPermissions.hasPermissions(NewDaHuoClothesActivity.this, permissions)) {

            //有权限直接调用系统相机拍照
            if (hasSdcard()) {

                imageFileName = System.currentTimeMillis() + ".jpg";
                fileUri = new File(Environment.getExternalStorageDirectory().getPath() +
                        "/" + folderName + "/" + imageFileName);


                fileUriForContent = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //通过FileProvider创建一个content类型的Uri
                    fileUriForContent = FileProvider.getUriForFile(
                            NewDaHuoClothesActivity.this,
                            "com.zz.fileprovider",
                            fileUri);
                }


                //拍完照片自动执行onActivityResult回调方法
                PhotoUtils.takePicture(
                        NewDaHuoClothesActivity.this,
                        fileUriForContent,
                        CODE_CAMERA_REQUEST);

            } else {
                //ToastUtils.showShort(this, "设备没有SD卡！");
                Toast.makeText(
                        NewDaHuoClothesActivity.this,
                        "设备没有SD卡",
                        Toast.LENGTH_SHORT)
                        .show();
            }

        } else {
            //权限还未申请，申请权限
            EasyPermissions.requestPermissions(this,
                    "需要使用到相机和读写SD卡的权限",
                    PERMISSION_CAMERA_SDCARD,
                    permissions);
        }
    }

    /**
     * 点击从相册获取款式图片按钮
     *
     * @param view
     */
    public void xiangCeOnClickForKSTP(View view) {
        getKstpForXC();
    }

    //@AfterPermissionGranted(PERMISSION_SDCARD)
    private void getKstpForXC() {
        permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, //写入SD卡的权限
                Manifest.permission.READ_EXTERNAL_STORAGE   //读取SD卡的权限
        };

        if (EasyPermissions.hasPermissions(NewDaHuoClothesActivity.this, permissions)) {

            ApplictionWidthAndHeight applictionWidthAndHeight = new ApplictionWidthAndHeight(
                    NewDaHuoClothesActivity.this);

            Matisse.from(NewDaHuoClothesActivity.this)
                    .choose(MimeType.allOf()) // 选择 mime 的类型
                    .countable(true)           //显示选择的数量
                    .maxSelectable(9) // 图片选择的最多数量
                    .gridExpectedSize(applictionWidthAndHeight.getWidth() / 3 - 5)//图片显示在列表中的大小
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f) // 缩略图的比例
                    .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                    .theme(R.style.Matisse_Zhihu)//主题
                    .capture(false)//是否提供拍照功能
                    .forResult(CODE_GALLERY_REQUEST); // 设置作为标记的请求码

        } else {
            //权限还未申请，申请权限
            EasyPermissions.requestPermissions(this,
                    "需要使用读写SD卡的权限",
                    PERMISSION_SDCARD,
                    permissions);
        }
    }

    /**
     * 动态申请权限的回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //权限回调交付于EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, NewDaHuoClothesActivity.this);

    }

    /**
     * 拍照及访问相册的回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri fileUriForFile = null;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调

                    fileUriForFile = Uri.fromFile(fileUri);

                    if (fileUriForFile != null) {

                        fileBean = new FileBean();
                        fileBean.setFileName(imageFileName);
                        fileBean.setFileUri(fileUriForContent);

                        fileBeanListForKS = addFileBeanToList(fileBean, fileBeanListForKS);

                        kuanShiImageListRecyclerViewAdapter.updateData(fileBeanListForKS);

                        //初始化oldView
                        reSetOldView();

                        imgKsListRecyclerview.scrollToPosition(fileBeanListForKS.size() - 1);
                    }

                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {


                        List<Uri> mSelected = Matisse.obtainResult(data);

                        for (Uri u : mSelected) {

                            String sPath = PhotoUtils.getPath(NewDaHuoClothesActivity.this, u);
                            String sName = getFileNameWithSuffix(sPath);    //获取文件名

                            fileBean = new FileBean();
                            fileBean.setFileName(sName);
                            fileBean.setFileUri(u);

                            fileBeanListForKS = addFileBeanToList(fileBean, fileBeanListForKS);
                            // 将从相册中选择的款式图片添加至列表
                            fileBeanListForKSToXC = addFileBeanToList(fileBean, fileBeanListForKSToXC);

                            kuanShiImageListRecyclerViewAdapter.updateData(fileBeanListForKS);

                            //初始化oldView
                            reSetOldView();

                            imgKsListRecyclerview.scrollToPosition(fileBeanListForKS.size() - 1);
                        }


                    } else {
                        Toast.makeText(NewDaHuoClothesActivity.this, "设备没有SD卡!!", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case CODE_CAMERA_CB_REQUEST:    //款式成本拍照回调
                    showImages(fileUriCBForContent, ivCB);
                    getIntent().putExtra("SCROLL_DOWN_CB", true);
                    Handler hd = new Handler();
                    hd.postDelayed(runnable, 200);

                    //scrollto(ivCB);
                    //scrollView.smoothScrollTo(0,ivCB.getTop());
//                    scrollView = findViewById(R.id.sv_scrollView);
//                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    break;

                case CODE_CAMERA_FL_REQUEST:    //辅料图片拍照回调
                    showImages(fileUriFLForContent, ivTuPian);
                    break;
            }
        }
    }

//    private void scrollto(final View view,String sBooleanExtra){
//        final boolean scrollDown = getIntent().getBooleanExtra(sBooleanExtra,false);
//        if (scrollDown){
//            scrollView.post(new Runnable() {
//                @Override
//                public void run() {
//                    int [] localtion = new int[2];
//                    view.getLocationOnScreen(localtion);
//                    int offset = localtion[1] - scrollView.getMeasuredHeight();
//                    if (offset < 0){
//                        offset = 0;
//                    }
//                    scrollView.smoothScrollTo(0,offset);
//                }
//            });
//        }
//    }


    /**
     * 将图片显示在ImageView控件中
     *
     * @param uri
     * @param view
     */
    private void showImages(Uri uri, ImageView view) {

        Glide.with(NewDaHuoClothesActivity.this).load(uri).into(view);

    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 请求权限已经被授权
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //如果checkPerm方法，没有注解AfterPermissionGranted，也可以在这里调用该方法。
    }

    /**
     * 请求权限被拒绝
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        //这里表示拒绝权限后再点击出现的去设置对话框
        //这里需要重新设置Rationale和title，否则默认是英文格式
        //Rationale：对话框的提示内容，否则默认是英文格式
        // title：对话框的提示标题，否则默认是英文格式
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //这个方法有个前提是，用户点击了“不再询问”后，才判断权限没有被获取到

            new AppSettingsDialog.Builder(NewDaHuoClothesActivity.this)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
                    .setTitle("必需权限")
                    .build()
                    .show();
        }


    }

    /**
     * 将辅料信息用RecyclerView显示出来
     */
    private void useRecyclerViewToShowFuliaoList() {
        //设置RecyclerView的适配器
        fuLiaoAddRecyclerViewAdapter = new FuLiaoAddRecyclerViewAdapter(NewDaHuoClothesActivity.this, fuLiaoInfoBeans, 0);
        fuliaoListRecyclerview.setAdapter(fuLiaoAddRecyclerViewAdapter);
        //LayoutManager
        //new LinearLayoutManager 参数 1、上下文 2、方向 3、是否倒序
        fuliaoListRecyclerview.setLayoutManager(new LinearLayoutManager(NewDaHuoClothesActivity.this, LinearLayoutManager.VERTICAL, false));
        //倒序后设置选显示倒序第一行
        fuliaoListRecyclerview.scrollToPosition(fuLiaoInfoBeans.size() - 1);
        //添加默认分割线：高度为2px，颜色为灰色
        fuliaoListRecyclerview.addItemDecoration(new RecycleViewDivider(NewDaHuoClothesActivity.this, LinearLayoutManager.VERTICAL));


    }

    /**
     * 将款式图片用RecyclerView显示出来
     */
    private void useRecyclerViewToShow() {
        //设置RecyclerView的适配器
        kuanShiImageListRecyclerViewAdapter = new KuanShiImageListRecyclerViewAdapter(NewDaHuoClothesActivity.this, fileBeanListForKS, 1);
        imgKsListRecyclerview.setAdapter(kuanShiImageListRecyclerViewAdapter);
        //LayoutManager
        //new LinearLayoutManager 参数 1、上下文 2、方向 3、是否倒序
        imgKsListRecyclerview.setLayoutManager(new LinearLayoutManager(NewDaHuoClothesActivity.this, LinearLayoutManager.HORIZONTAL, false));
        //倒序后设置选显示倒序第一行
        imgKsListRecyclerview.scrollToPosition(fileBeanListForKS.size() - 1);
        //添加默认分割线：高度为2px，颜色为灰色
        //imgKsListRecyclerview.addItemDecoration(new RecycleViewDivider(NewDaHuoClothesActivity.this, LinearLayoutManager.HORIZONTAL));


    }

    /**
     * 查询文件名是否已存在与列表，若不存在则添加进列表
     *
     * @param list
     * @param fileName
     * @return
     */
    public ArrayList<String> addImageForList(ArrayList<String> list, String fileName) {
        boolean isHave = false;
        for (String name : list) {
            if (name.equals(fileName.trim())) {
                isHave = true;
            }
        }
        if (!isHave) {
            list.add(fileName);
        }
        return list;
    }

    /**
     * 查询文件名是否已存在与列表，若不存在则添加进列表
     *
     * @param fileBean
     * @param fileBeanList
     * @return
     */
    private ArrayList<FileBean> addFileBeanToList(FileBean fileBean, ArrayList<FileBean> fileBeanList) {
        boolean isHave = false;
        for (FileBean bean : fileBeanList) {
            if (bean.getFileName().equals(fileBean.getFileName())) {
                isHave = true;
            }
        }
        if (!isHave) {
            fileBeanList.add(fileBean);
        }
        return fileBeanList;
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

    /**
     * 确定保存按钮
     *
     * @param view
     */
    public void submitOnClick(View view) {

        String sKH = etKH.getText().toString().trim();
        String sKSMC = etKSMC.getText().toString().trim();
        String sYBH = etYBH.getText().toString().trim();
        String sBZ = etBZ.getText().toString().trim();

        if (sKH == null || sKH.equals("")) {
            Toast.makeText(NewDaHuoClothesActivity.this, "请填写款号！", Toast.LENGTH_SHORT).show();
        } else if (sKSMC == null || sKSMC.equals("")) {
            Toast.makeText(NewDaHuoClothesActivity.this, "请填写款式名称！", Toast.LENGTH_SHORT).show();
        } else if (sYBH == null || sYBH.equals("")) {
            Toast.makeText(NewDaHuoClothesActivity.this, "请填写样板号！", Toast.LENGTH_SHORT).show();
        } else {

            //从数据库中查询该款号是否存在，若不存在则可添加
            ClothesInfoDao dao = new ClothesInfoDao(NewDaHuoClothesActivity.this);
            boolean haveThisKH = dao.getInfoForYearAndKH(timeData, sKH);
            if (!haveThisKH) {

                progressDialog = new ProgressDialog(NewDaHuoClothesActivity.this);
                progressDialog.setMessage("...请您稍等...");
                progressDialog.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
                progressDialog.show();

                //拷贝相册里的照片到本应用专用的图片文件夹中
                if (fileBeanListForKSToXC != null) {
                    newPath = Environment.getExternalStorageDirectory().getPath() + "/" + folderName + "/";
                    new Thread() {
                        @Override
                        public void run() {
                            ArrayList<String> copyErrorList = new ArrayList<String>();
                            for (FileBean bean :
                                    fileBeanListForKSToXC) {
                                String sPath = PhotoUtils.getPathNoPathHead(                           //  绝对路径的URI
                                        NewDaHuoClothesActivity.this,
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
                            msg.what = COPE_FILE;
                            msg.obj = messageBean;
                            handler.sendMessage(msg);

                        }
                    }.start();
                } else {
                    copeFileIsOK = true;
                }


                //保存数据到数据库
                clothesInfoBean = new ClothesInfoBean();
                dbDaHuoInfoBean = new DBDaHuoInfoBean();
                dbDaHuoInfoBean.setYearInfo(timeData);
                dbDaHuoInfoBean.setKuanhao(sKH);
                dbDaHuoInfoBean.setKuanshimingcheng(sKSMC);
                dbDaHuoInfoBean.setYangbanhao(sYBH);
                dbDaHuoInfoBean.setBeizhu(sBZ);
                dbDaHuoInfoBean.setFengmianImg(sfengmian);
                dbDaHuoInfoBean.setTag(ClothesTagType.getTypePT());
                dbDaHuoInfoBean.setChengbenImg(fileNameForCB);
                clothesInfoBean.setDbDaHuoInfoBean(dbDaHuoInfoBean);

                fileNameForCB = "";
                dbDahuoImgBeans = new ArrayList<DBDahuoImgBean>();
                dbFuliaoInfoBeans = new ArrayList<DBFuliaoInfoBean>();


//        for (FileBean bean :
//                fileBeanListForKSToXC) {
//            Log.e("---照片名,从相册中选中的：---", bean.getFileName());             //  文件名
////            Log.e("---照片名,从相册中选中的：---", bean.getFileUri().toString());   //  content形式的URI
//
////            String sPath1 = PhotoUtils.getPath(                                     //  file:///形式的URI
////                    NewDaHuoClothesActivity.this,
////                    bean.getFileUri());
////            Log.e("---照片名,从相册中选中的：---", sPath1);
//
////            String sPath2 = PhotoUtils.getPathNoPathHead(                           //  绝对路径的URI
////                    NewDaHuoClothesActivity.this,
////                    bean.getFileUri());
////
////            Log.e("---照片名,从相册中选中的：---", sPath2);
//        }
                if (fileBeanListForKS.size() > 0) {
                    for (FileBean bean :
                            fileBeanListForKS) {
                        DBDahuoImgBean dbDahuoImgBean = new DBDahuoImgBean();
                        dbDahuoImgBean.setImgName(bean.getFileName());
                        dbDahuoImgBean.setImgType(Constants.DB_DAHUO_IMG_TYPE_KS);
                        dbDahuoImgBeans.add(dbDahuoImgBean);
//            Log.e("---照片名,所有显示的照片：---", bean.getFileName());
//            Log.e("---照片名,所有显示的照片：---", bean.getFileUri().toString());
                    }
                }
                clothesInfoBean.setDbDahuoImgBeans(dbDahuoImgBeans);

                if (fuLiaoInfoBeans.size() > 0) {
                    for (FuLiaoInfoBean bean :
                            fuLiaoInfoBeans) {
                        DBFuliaoInfoBean dbFuliaoInfoBean = new DBFuliaoInfoBean();
                        dbFuliaoInfoBean.setFuliaoName(bean.getFuliao_name());
                        dbFuliaoInfoBean.setFuliaoImg(bean.getFuliao_img_name());
                        dbFuliaoInfoBean.setJiage(bean.getJiage() + "");
                        dbFuliaoInfoBean.setGongyinshangId(bean.getGongyinshangId());
                        dbFuliaoInfoBean.setBeizhu("");
                        dbFuliaoInfoBeans.add(dbFuliaoInfoBean);
                    }
                }

                clothesInfoBean.setDbFuliaoInfoBeans(dbFuliaoInfoBeans);


                //将数据添加到数据库中
                new Thread() {
                    @Override
                    public void run() {
                        HandlerMessageBean messageBean = new HandlerMessageBean();
                        clothesInfoDao = new ClothesInfoDao(NewDaHuoClothesActivity.this);
                        boolean b = clothesInfoDao.addNewClothes(clothesInfoBean);
                        messageBean.setAddIsSuccess(b);
                        messageBean.setInsertDataBaseIsOK(true);
                        Message msg = new Message();
                        msg.what = INSERT_DATABASE;
                        msg.obj = messageBean;
                        handler.sendMessage(msg);

                    }
                }.start();

            } else {
                etKH.setFocusable(true);
                etKH.setFocusableInTouchMode(true);
                etKH.requestFocus();
                etKH.requestFocusFromTouch();
                Toast.makeText(NewDaHuoClothesActivity.this, "该款式已存在，请重新填写款号！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 用PopupWindow的方式弹出添加辅料的页面
     *
     * @param view
     */
    public void addFuLiao(View view) {
//弹出PopupWindow

        // popupWindow.showAsDropDown(view);
        initPopupWindow(view);
        //initPopupWindow(view);
    }

    private void initPopupWindow(View view) {
        initPopupWindowHaveBean(view, null, 0);
    }

    /**
     * 初始化添加辅料信息的悬浮窗
     *
     * @param view
     */
    public void initPopupWindowHaveBean(View view, final FuLiaoInfoBean fuLiaoBean, final int youbiao) {
        ApplictionWidthAndHeight widthAndHeight = new ApplictionWidthAndHeight(NewDaHuoClothesActivity.this);
        popupWindow = new CommonPopupWindow.Builder(NewDaHuoClothesActivity.this)
                //设置PopupWindow布局
                .setView(R.layout.activity_add_fu_liao)
                //设置宽高
                .setWidthAndHeight(widthAndHeight.getWidth(), widthAndHeight.getHeight())
                // .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                //         ViewGroup.LayoutParams.WRAP_CONTENT)
                //设置动画
                .setAnimationStyle(R.style.AnimVertical)
                //设置背景颜色，取值范围0.0f-1.0f 值越小越暗 1.0f为透明
                .setBackGroundLevel(0.5f)
                //设置PopupWindow里的子View及点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(final View view, int layoutResId) {
                        //获取悬浮窗中的控件
                        etName = (EditText) view.findViewById(R.id.et_name);           //辅料名称
                        etJiage = (EditText) view.findViewById(R.id.et_jiage);           //价格
                        spGongyinshang = (Spinner) view.findViewById(R.id.sp_gongyinshang); //供应商
                        ibPaiZhao = (ImageButton) view.findViewById(R.id.ib_paizhao);      //拍照按钮
                        ivTuPian = (ImageView) view.findViewById(R.id.iv_tp);         //辅料图片
                        btnOk = (Button) view.findViewById(R.id.btn_ok);               //确定按钮
                        btnQx = (Button) view.findViewById(R.id.btn_qx);               //取消按钮
                        btnOk.setText("确定");
                        radioGroup = view.findViewById(R.id.rg_type);

                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                RadioButton radioButton = (RadioButton) view.findViewById(checkedId);
                                sType = radioButton.getText().toString();

                                TbGongyinshangInfoDao gongyinshangInfoDao = new TbGongyinshangInfoDao(NewDaHuoClothesActivity.this);
                                // List<GongYinShangBean> gongyinshangInfoBeans = gongyinshangInfoDao.getGongyinshangBeans();
                                List<GongYinShangBean> gongyinshangInfoBeans = gongyinshangInfoDao.getGongyinshangBeansWithType(sType);

                                if (gongyinshangInfoBeans.size() <= 0) {
                                    GongYinShangBean bean = new GongYinShangBean();
                                    bean.setName("暂无供应商");
                                    bean.setId(-1);
                                    gongyinshangInfoBeans.add(bean);
                                }
//                        下拉列表中的值，这里暂时固定写死，以后将改为从数据库中动态获取
//
//                        List<GongYinShangBean> gongyinshangInfoBeans = new ArrayList<GongYinShangBean>();

//                        String[] gys = {
//                                "贵宜典", "姚明织带", "画龙点睛", "三鼎织带"
//                        };
//
//
//
//                        for (int i = 0; i < 4; i++) {
//                            GongYinShangBean gongYinShangBean = new GongYinShangBean();
//                            gongYinShangBean.setId(i + 1);
//                            gongYinShangBean.setName(gys[i]);
//                            gongyinshangInfoBeans.add(gongYinShangBean);
//                        }

                                //--------------------------------------------------


//                        ArrayAdapter<GongYinShangBean> adapter = new ArrayAdapter<GongYinShangBean>( view.getContext(),
//                                android.R.layout.simple_spinner_item, gongyinshangInfoBeans);

                                ArrayAdapter<GongYinShangBean> adapter = new ArrayAdapter<GongYinShangBean>(
                                        view.getContext(),
                                        android.R.layout.simple_dropdown_item_1line,
                                        android.R.id.text1,
                                        gongyinshangInfoBeans
                                );

                                spGongyinshang.setAdapter(adapter);
                            }
                        });

                        for (int i = 0; i < radioGroup.getChildCount(); i++) {
                            RadioButton rd = (RadioButton) radioGroup.getChildAt(i);
                            if (rd.isChecked()) {
                                sType = rd.getText().toString();
                            }
                        }

                        TbGongyinshangInfoDao gongyinshangInfoDao = new TbGongyinshangInfoDao(NewDaHuoClothesActivity.this);
                        // List<GongYinShangBean> gongyinshangInfoBeans = gongyinshangInfoDao.getGongyinshangBeans();
                        List<GongYinShangBean> gongyinshangInfoBeans = gongyinshangInfoDao.getGongyinshangBeansWithType(sType);

                        if (gongyinshangInfoBeans.size() <= 0) {
                            GongYinShangBean bean = new GongYinShangBean();
                            bean.setName("暂无供应商");
                            bean.setId(-1);
                            gongyinshangInfoBeans.add(bean);
                        }
//                        下拉列表中的值，这里暂时固定写死，以后将改为从数据库中动态获取
//
//                        List<GongYinShangBean> gongyinshangInfoBeans = new ArrayList<GongYinShangBean>();

//                        String[] gys = {
//                                "贵宜典", "姚明织带", "画龙点睛", "三鼎织带"
//                        };
//
//
//
//                        for (int i = 0; i < 4; i++) {
//                            GongYinShangBean gongYinShangBean = new GongYinShangBean();
//                            gongYinShangBean.setId(i + 1);
//                            gongYinShangBean.setName(gys[i]);
//                            gongyinshangInfoBeans.add(gongYinShangBean);
//                        }

                        //--------------------------------------------------


//                        ArrayAdapter<GongYinShangBean> adapter = new ArrayAdapter<GongYinShangBean>( view.getContext(),
//                                android.R.layout.simple_spinner_item, gongyinshangInfoBeans);

                        ArrayAdapter<GongYinShangBean> adapter = new ArrayAdapter<GongYinShangBean>(
                                view.getContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                android.R.id.text1,
                                gongyinshangInfoBeans
                        );

                        spGongyinshang.setAdapter(adapter);

//                        spGongyinshang.setAdapter(
//                                new ArrayAdapter<String>(
//                                        view.getContext(),
//                                        android.R.layout.simple_dropdown_item_1line,
//                                        android.R.id.text1, gys)
//                        );


                        //如果fuLiaoBean有值则初始化控件数据
                        if (fuLiaoBean != null) {
                            etName.setText(fuLiaoBean.getFuliao_name());    // 设置辅料名称
                            etJiage.setText(fuLiaoBean.getJiage() + "");    //设置辅料价格

                            int n = spGongyinshang.getAdapter().getCount();
                            for (int i = 0; i < n; i++) {
                                if (((GongYinShangBean) spGongyinshang.getAdapter().getItem(i)).getName().equals(fuLiaoBean.getGongyingshang())) {
                                    spGongyinshang.setSelection(i);         //设置供应商下拉列表选中项
                                    break;
                                }


//                                if (spGongyinshang.getAdapter().getItem(i).toString().equals(fuLiaoBean.getGongyingshang())) {
//                                    spGongyinshang.setSelection(i);         //设置供应商下拉列表选中项
//                                    break;
//                                }
                            }
                            fileNameForFL = fuLiaoBean.getFuliao_img_name();    //设置辅料图片
                            showImages(getUriForFileName(fileNameForFL), ivTuPian);  //显示辅料图片
                            btnOk.setText("修改");
                        }


                        //辅料拍照
                        ibPaiZhao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                permissions = new String[]{
                                        Manifest.permission.CAMERA,                 //使用相机的权限
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE, //写入SD卡的权限
                                        Manifest.permission.READ_EXTERNAL_STORAGE   //读取SD卡的权限
                                };

                                if (EasyPermissions.hasPermissions(v.getContext(), permissions)) {
                                    //有权限直接调用系统相机拍照
                                    if (hasSdcard()) {
                                        fileNameForFL = System.currentTimeMillis() + ".jpg";
                                        fileUriFL = new File(Environment.getExternalStorageDirectory().getPath() +
                                                "/" + folderName + "/" + fileNameForFL);


                                        fileUriFLForContent = Uri.fromFile(fileUriFL);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            //通过FileProvider创建一个content类型的Uri
                                            fileUriFLForContent = FileProvider.getUriForFile(
                                                    NewDaHuoClothesActivity.this,
                                                    "com.zz.fileprovider",
                                                    fileUriFL);
                                        }


                                        //拍完照片自动执行onActivityResult回调方法
                                        PhotoUtils.takePicture(
                                                NewDaHuoClothesActivity.this,
                                                fileUriFLForContent,
                                                CODE_CAMERA_FL_REQUEST);


                                    } else {
                                        //ToastUtils.showShort(this, "设备没有SD卡！");
                                        Toast.makeText(
                                                v.getContext(),
                                                "设备没有SD卡",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                } else {
                                    //权限还未申请，申请权限
                                    EasyPermissions.requestPermissions((Activity) v.getContext(), "需要使用到相机和读写SD卡的权限", PERMISSION_CAMERA_SDCARD,
                                            permissions);
                                }
                            }
                        });


                        //确定按钮被点击
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String fuliao_name = etName.getText().toString().trim();
                                float jiage = etJiage.getText().toString().trim().equals("") ? 0 : Float.parseFloat(etJiage.getText().toString().trim());
//                                String gongyinshang = spGongyinshang.getSelectedItem().toString();
                                Integer gongyinshangId = ((GongYinShangBean) spGongyinshang.getSelectedItem()).getId();
                                String gongyinshang = ((GongYinShangBean) spGongyinshang.getSelectedItem()).getName();
                                //判断数据是否为空
                                if (fuliao_name.equals("") || fuliao_name == null) {
                                    Toast.makeText(
                                            view.getContext(),
                                            "请填写辅料名称！",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                } else {

                                    fuLiaoInfoBean = new FuLiaoInfoBean();
                                    fuLiaoInfoBean.setFuliao_name(fuliao_name);
                                    fuLiaoInfoBean.setJiage(jiage);
                                    fuLiaoInfoBean.setFuliao_img_name(fileNameForFL);
                                    fuLiaoInfoBean.setGongyingshang(gongyinshang);
                                    fuLiaoInfoBean.setGongyinshangId(gongyinshangId);

                                    if (fuLiaoBean == null) {
                                        //添加辅料信息
                                        //将页面中的辅料信息用Bean对象的方式保存并添加至Beans列表中
                                        fuLiaoInfoBeans.add(fuLiaoInfoBean);
                                    } else {
                                        //修改辅料信息
                                        fuLiaoInfoBeans.set(youbiao, fuLiaoInfoBean);
                                    }

                                    fuLiaoInfoBean = null;
                                    fileNameForFL = "";
                                    //设置辅料信息的RecyclerView的高
                                    fuliaoListRecyclerview.getLayoutParams().height = 400;
                                    //刷新RecyclerView中的辅料信息数据
                                    fuLiaoAddRecyclerViewAdapter.updateData(fuLiaoInfoBeans);
                                    //倒序后设置选显示倒序第一行
                                    fuliaoListRecyclerview.scrollToPosition(fuLiaoInfoBeans.size() - 1);
                                    //关闭PopupWindow悬浮窗
                                    popupWindow.dismiss();
                                }


                            }
                        });

                        btnQx.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Toast.makeText(NewDaHuoClothesActivity.this, "你点击了取消", Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();
                            }
                        });


                    }
                })
                //设置外部是否可点击 默认是true
                .setOutsideTouchable(true)
                //开始构建
                .create();

        //popupWindow.showAsDropDown(view);


        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

//        showAsDropDown是显示在参照物anchor的周围
//        xoff、yoff分别是X轴、Y轴的偏移量，
//        如果不设置xoff、yoff，默认是显示在anchor的下方；
//        showAtLocation是设置在父控件的位置，
//        如设置Gravity.BOTTOM表示在父控件底部弹出，
//        xoff、yoff也是X轴、Y轴的偏移量。


//        public void showAsDropDown(View anchor)
//
//        public void showAsDropDown(View anchor, int xoff, int yoff)
//
//        public void showAtLocation(View parent, int gravity, int x, int y)

    }

    /**
     * 根据文件名获取本app相册中的URI
     *
     * @param fileName
     * @return
     */
    public Uri getUriForFileName(String fileName) {
        String folderName = this.getString(R.string.my_photo_folder_name);
        File fileUri = new File(Environment.getExternalStorageDirectory().getPath() +
                "/" + folderName + "/" + fileName);
        Uri fileUriForContent = Uri.fromFile(fileUri);
        return fileUriForContent;
    }


    /**
     * 款式成本拍照
     *
     * @param view
     */
    public void paizhaoOnClickForKSCB(View view) {
        scrollViewY = scrollView.getScrollY();
        getKscbForPZ();
    }

    //@AfterPermissionGranted(PERMISSION_CAMERA_SDCARD)
    private void getKscbForPZ() {
        permissions = new String[]{
                Manifest.permission.CAMERA,                 //使用相机的权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE, //写入SD卡的权限
                Manifest.permission.READ_EXTERNAL_STORAGE   //读取SD卡的权限
        };


        if (EasyPermissions.hasPermissions(NewDaHuoClothesActivity.this, permissions)) {
            //有权限直接调用系统相机拍照
            if (hasSdcard()) {


                fileNameForCB = System.currentTimeMillis() + ".jpg";
                fileUriCB = new File(Environment.getExternalStorageDirectory().getPath() +
                        "/" + folderName + "/" + fileNameForCB);


                fileUriCBForContent = Uri.fromFile(fileUriCB);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //通过FileProvider创建一个content类型的Uri
                    fileUriCBForContent = FileProvider.getUriForFile(
                            NewDaHuoClothesActivity.this,
                            "com.zz.fileprovider",
                            fileUriCB);
                }


                //拍完照片自动执行onActivityResult回调方法
                PhotoUtils.takePicture(
                        NewDaHuoClothesActivity.this,
                        fileUriCBForContent,
                        CODE_CAMERA_CB_REQUEST);


            } else {
                //ToastUtils.showShort(this, "设备没有SD卡！");
                Toast.makeText(
                        NewDaHuoClothesActivity.this,
                        "设备没有SD卡",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            //权限还未申请，申请权限
            EasyPermissions.requestPermissions(this,
                    "需要使用到相机和读写SD卡的权限",
                    PERMISSION_CAMERA_SDCARD,
                    permissions);
        }
    }
}
