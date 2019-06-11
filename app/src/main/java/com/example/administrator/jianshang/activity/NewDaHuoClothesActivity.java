package com.example.administrator.jianshang.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.ApplictionWidthAndHeight;
import com.example.administrator.jianshang.Tools.FileUtils;
import com.example.administrator.jianshang.Tools.PhotoUtils;
import com.example.administrator.jianshang.adapters.KuanShiImageListRecyclerViewAdapter;
import com.example.administrator.jianshang.adapters.RecycleViewDivider;
import com.example.administrator.jianshang.adapters.TimeRecyclerViewAdapter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class NewDaHuoClothesActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private ImageView photo;
    private String timeData;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int PERMISSION_CAMERA_SDCARD = 0xa2;
    private static final int PERMISSION_SDCARD = 0xa3;

    private KuanShiImageListRecyclerViewAdapter kuanShiImageListRecyclerViewAdapter;
    private RecyclerView imgKsListRecyclerview;

    String[] permissions;
    private String imageFileName;
    private File fileUri;
    //private Uri imageUri;           //
    private Uri fileUriForContent;


    private String oldPath="";
    private String newPath="";
    private String folderName="";

    private ArrayList<String> fileUriForKSList;      //款式图片uri集合
    private ArrayList<String> fileNameForKSList;     //款式图片名集合
    private ArrayList<String> fileNameForFLList;     //辅料图片名集合
    private String fileNameForCB;               //成本图片名




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_da_huo_clothes);

        //查看SD卡中是否存在JianShangPhoto文件夹，若不存在，则创建
        folderName = this.getString(R.string.my_photo_folder_name);
        creatFolder(folderName);


        photo = findViewById(R.id.iv_test);

        fileUriForKSList = new ArrayList<String>();
        fileNameForKSList = new ArrayList<String>();
        fileNameForFLList = new ArrayList<String>();


        imgKsListRecyclerview = findViewById(R.id.recyclerview_img_ks_list);
        useRecyclerViewToShow();


        Intent intent = getIntent();
        timeData = intent.getStringExtra("yearInfo");
    }



    private void creatFolder(String folderName) {
        if (hasSdcard()) {

            File appDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + folderName);
            if (!appDir.exists()) {
                appDir.mkdir();
            }

//            imageFileName = System.currentTimeMillis() + ".jpg";
//            fileUri = new File(Environment.getExternalStorageDirectory().getPath() +
//                    "/" + folderName + "/" + imageFileName);

        }
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
            //拍照保存并显示图片

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
                PhotoUtils.takePicture(NewDaHuoClothesActivity.this, fileUriForContent, CODE_CAMERA_REQUEST);
            } else {
                //ToastUtils.showShort(this, "设备没有SD卡！");
                Toast.makeText(NewDaHuoClothesActivity.this, "设备没有SD卡", Toast.LENGTH_SHORT).show();
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
//            (new ApplictionWidthAndHeight(NewDaHuoClothesActivity.this)).getScreenWidth()/3
            ApplictionWidthAndHeight applictionWidthAndHeight = new ApplictionWidthAndHeight(NewDaHuoClothesActivity.this);


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



//            PhotoUtils.openPic(NewDaHuoClothesActivity.this, CODE_GALLERY_REQUEST);
//            //拷贝文件
//            boolean b = FileUtils.copyFile(oldPath, newPath);
//
//            //kuanShiImageListRecyclerViewAdapter.addData(0,imageFileName);
//            kuanShiImageListRecyclerViewAdapter.updateData(fileNameForKSList);

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

//                    Log.e("URI---imageUri:",fileUriForContent.toString());
//                    Log.e("URI---fileUri:",fileUri.toString());
//                    Log.e("URI---fileUriPath:",Uri.fromFile(fileUri).toString());

                    fileUriForFile = Uri.fromFile(fileUri);
                    if (fileUriForFile != null) {
                        //fileNameForKSList.add(imageFileName);
                        // 将款式图片名添加至款式图片名列表
                        fileNameForKSList = addImageForList(fileNameForKSList,imageFileName);

                        kuanShiImageListRecyclerViewAdapter.updateData(fileNameForKSList);

                        //kuanShiImageListRecyclerViewAdapter.addData(0,imageFileName);

                        //showImages(uri);
                    }

                    //Log.e("----MY_URI:----",uri+"");

                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {


                    List<Uri> mSelected = Matisse.obtainResult(data);

                        for (Uri u : mSelected) {

                            String sPath = PhotoUtils.getPath(NewDaHuoClothesActivity.this, u);

//                            Log.e("SelectedUir:",u.getPath().toString());
//                            Log.e("SelectedUir:---",sPath);


                            String sName = getFileNameWithSuffix(sPath);    //获取文件名

                            // 将款式图片名添加至款式图片名列表
                            fileNameForKSList = addImageForList(fileNameForKSList,imageFileName);

                        }





//                        uri = Uri.parse(PhotoUtils.getPath(NewDaHuoClothesActivity.this, data.getData()));
//
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                            uri = FileProvider.getUriForFile(NewDaHuoClothesActivity.this, "com.zz.fileprovider", new File(uri.getPath()));
//
//                        if (uri != null) {
//
//                            oldPath = PhotoUtils.getPathNoPathHead(NewDaHuoClothesActivity.this, data.getData());
//                            newPath = Environment.getExternalStorageDirectory().getPath() + "/"+folderName+"/";
//
//                            //将款式图片名添加至款式图片名列表
//                            imageFileName = getFileNameWithSuffix(oldPath);
//                            //fileNameForKSList.add(imageFileName);
//                            fileNameForKSList = addImageForList(fileNameForKSList,imageFileName);
//
//                            //kuanShiImageListRecyclerViewAdapter.addData(0,imageFileName);
//                            //showImages(uri);
//
//                        }

                    } else {
                        Toast.makeText(NewDaHuoClothesActivity.this, "设备没有SD卡!!", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    }


    private void showImages(Uri uri) {

        Glide.with(NewDaHuoClothesActivity.this).load(uri).into(photo);

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

        kuanShiImageListRecyclerViewAdapter = new KuanShiImageListRecyclerViewAdapter(NewDaHuoClothesActivity.this, fileNameForKSList,1);
        imgKsListRecyclerview.setAdapter(kuanShiImageListRecyclerViewAdapter);

        //LayoutManager
        //new LinearLayoutManager 参数 1、上下文 2、方向 3、是否倒序
        imgKsListRecyclerview.setLayoutManager(new LinearLayoutManager(NewDaHuoClothesActivity.this, LinearLayoutManager.HORIZONTAL, true));
        //倒序后设置选显示倒序第一行
        imgKsListRecyclerview.scrollToPosition(fileNameForKSList.size()-1);

        //添加默认分割线：高度为2px，颜色为灰色
        imgKsListRecyclerview.addItemDecoration(new RecycleViewDivider(NewDaHuoClothesActivity.this, LinearLayoutManager.HORIZONTAL));


    }


    /**
     * 查询文件名是否已存在与列表，若不存在则添加进列表
     * @param list
     * @param fileName
     * @return
     */
    public ArrayList<String> addImageForList(ArrayList<String> list,String fileName){
        boolean isHave = false;
        for (String name : list) {
            if (name.equals(fileName.trim())){
                isHave = true;
            }
        }
        if(!isHave){
            list.add(fileName);
        }
        return list;
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
