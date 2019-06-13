package com.example.administrator.jianshang.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.ApplictionWidthAndHeight;
import com.example.administrator.jianshang.Tools.FileUtils;
import com.example.administrator.jianshang.Tools.PhotoUtils;
import com.example.administrator.jianshang.adapters.KuanShiImageListRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.RecycleViewDivider;
import com.example.administrator.jianshang.adapters.TimeRecyclerViewAdapter;
import com.example.administrator.jianshang.bean.FileBean;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class NewDaHuoClothesActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private String timeData;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int PERMISSION_CAMERA_SDCARD = 0xa2;
    private static final int PERMISSION_SDCARD = 0xa3;
    private static final int CODE_CAMERA_CB_REQUEST = 0xa4;


    private KuanShiImageListRecyclerViewAdapter kuanShiImageListRecyclerViewAdapter;
    private RecyclerView imgKsListRecyclerview;
    private View oldView = null;
    private TextView oldTvFmView = null;
    private ImageView ivCB;

    private String sfengmian = "";

    private String[] permissions;
    private String imageFileName;

    private File fileUri;
    private File fileUriCB;

    private Uri fileUriForContent;
    private Uri fileUriCBForContent;
    private FileBean fileBean;
    private FileBean beDelFileBean = null;

    private String oldPath = "";
    private String newPath = "";
    private String folderName = "";

    private ArrayList<FileBean> fileBeanListForKS;       //所有款式图片集合
    private ArrayList<FileBean> fileBeanListForKSToXC;   //相册款式图片集合

    private String fileNameForCB;               //成本图片名


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_da_huo_clothes);

        //查看SD卡中是否存在JianShangPhoto文件夹，若不存在，则创建
        folderName = this.getString(R.string.my_photo_folder_name);
        creatFolder(folderName);


        fileBeanListForKS = new ArrayList<FileBean>();
        fileBeanListForKSToXC = new ArrayList<FileBean>();

        imgKsListRecyclerview = findViewById(R.id.recyclerview_img_ks_list);
        ivCB = findViewById(R.id.iv_cb);


        //为recyclerview注册上下文菜单
        registerForContextMenu(imgKsListRecyclerview);

        //使用RecyclerView显示数据
        useRecyclerViewToShow();

        //添加每项点击事件监听
        setOnItemClickForCSListener();

        Intent intent = getIntent();
        timeData = intent.getStringExtra("yearInfo");
    }


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
    @AfterPermissionGranted(PERMISSION_CAMERA_SDCARD)
    public void paiZhaoOnClickForKSTP(View view) {

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
    @AfterPermissionGranted(PERMISSION_SDCARD)
    public void xiangCeOnClickForKSTP(View view) {

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
                    showImages(fileUriCBForContent,ivCB);
                    break;

            }
        }
    }


    private void showImages(Uri uri,ImageView view) {

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


        //拷贝相册里的照片到本应用专用的图片文件夹中


        //保存数据到数据库


        for (FileBean bean :
                fileBeanListForKSToXC) {
            Log.e("---照片名,从相册中选中的：---", bean.getFileName());             //  文件名
//            Log.e("---照片名,从相册中选中的：---", bean.getFileUri().toString());   //  content形式的URI

//            String sPath1 = PhotoUtils.getPath(                                     //  file:///形式的URI
//                    NewDaHuoClothesActivity.this,
//                    bean.getFileUri());
//            Log.e("---照片名,从相册中选中的：---", sPath1);

//            String sPath2 = PhotoUtils.getPathNoPathHead(                           //  绝对路径的URI
//                    NewDaHuoClothesActivity.this,
//                    bean.getFileUri());
//
//            Log.e("---照片名,从相册中选中的：---", sPath2);
        }


        for (FileBean bean :
                fileBeanListForKS) {
            Log.e("---照片名,所有显示的照片：---", bean.getFileName());
//            Log.e("---照片名,所有显示的照片：---", bean.getFileUri().toString());
        }
    }

    public void addFuLiao(View view) {
    }


    /**
     * 款式成本拍照
     *
     * @param view
     */
    public void paizhaoOnClickForKSCB(View view) {

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
