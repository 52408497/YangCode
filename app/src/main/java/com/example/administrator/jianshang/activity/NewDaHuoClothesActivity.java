package com.example.administrator.jianshang.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.FileUtils;
import com.example.administrator.jianshang.Tools.PhotoUtils;

import java.io.File;
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


    String[] permissions;
    private String imageFileName;
    private File fileUri;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_da_huo_clothes);

        //查看SD卡中是否存在JianShangPhoto文件夹，若不存在，则创建
        String folderName = "JianShangPhoto";
        creatFolder(folderName);


        photo = findViewById(R.id.iv_test);


        Intent intent = getIntent();
        timeData = intent.getStringExtra("yearInfo");
    }

    private void creatFolder(String folderName) {
        if (hasSdcard()) {

            File appDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + folderName);
            if (!appDir.exists()) {
                appDir.mkdir();
            }

            imageFileName = System.currentTimeMillis() + ".jpg";
            fileUri = new File(Environment.getExternalStorageDirectory().getPath() +
                    "/" + folderName + "/" + imageFileName);

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


        if (EasyPermissions.hasPermissions(this, permissions)) {
            //拍照保存并显示图片

            //有权限直接调用系统相机拍照
            if (hasSdcard()) {

                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(NewDaHuoClothesActivity.this, "com.zz.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri

                //拍完照片自动执行onActivityResult回调方法
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
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

        if (EasyPermissions.hasPermissions(this, permissions)) {


            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);

        } else {
            //权限还未申请，申请权限
            EasyPermissions.requestPermissions(this,
                    "需要使用读写SD卡的权限",
                    PERMISSION_SDCARD,
                    permissions);
        }


    }


//    /**
//     * 自动获取相机权限
//     */
//    private void autoObtainCameraPermission() {
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                //ToastUtils.showShort(this, "您已经拒绝过一次");
//                Toast.makeText(NewDaHuoClothesActivity.this, "您已经拒绝过一次", Toast.LENGTH_SHORT).show();
//            }
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
//        } else {
//            //有权限直接调用系统相机拍照
//            if (hasSdcard()) {
//                imageUri = Uri.fromFile(fileUri);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                    imageUri = FileProvider.getUriForFile(NewDaHuoClothesActivity.this, "com.zz.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
//                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
//            } else {
//                //ToastUtils.showShort(this, "设备没有SD卡！");
//                Toast.makeText(NewDaHuoClothesActivity.this, "设备没有SD卡", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

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
        //EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        //Toast.makeText(NewDaHuoClothesActivity.this, ""+requestCode, Toast.LENGTH_SHORT).show();

    }

//    private int output_X = 480;
//    private int output_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = null;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调

                    uri = Uri.fromFile(fileUri);
                    if (uri != null) {
                        showImages(uri);
                    }


                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {

                        uri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            uri = FileProvider.getUriForFile(this, "com.zz.fileprovider", new File(uri.getPath()));

                        if (uri != null) {

                            //拷贝文件
                            //先判断有没有读写SD卡的权限
                            permissions = new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE, //写入SD卡的权限
                                    Manifest.permission.READ_EXTERNAL_STORAGE   //读取SD卡的权限
                            };

                            if (EasyPermissions.hasPermissions(this, permissions)) {


                                String oldPath = PhotoUtils.getPathNoPathHead(this, data.getData());
                                String newPath = Environment.getExternalStorageDirectory().getPath() + "/JianShangPhoto/";

                                boolean b = FileUtils.copyFile(oldPath, newPath);
//
//                                Log.e("test", "oldPath:" + oldPath);
//                                Log.e("test", "newPath:" + newPath);

                                //Toast.makeText(this, b+"", Toast.LENGTH_SHORT).show();


                            } else {
                                //权限还未申请，申请权限
                                EasyPermissions.requestPermissions(this,
                                        "需要使用读写SD卡的权限",
                                        PERMISSION_SDCARD,
                                        permissions);
                            }

                            showImages(uri);

                        }


                        //PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        Toast.makeText(NewDaHuoClothesActivity.this, "设备没有SD卡!!", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    }


//    /**
//     * 自动获取sdk权限
//     */
//
//    private void autoObtainStoragePermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
//        } else {
//            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
//        }
//
//    }

    private void showImages(Uri uri) {

        Glide.with(this).load(uri).into(photo);

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

            new AppSettingsDialog.Builder(this)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
                    .setTitle("必需权限")
                    .build()
                    .show();
        } else if (!EasyPermissions.hasPermissions(this, permissions)) {
            //这里响应的是除了AppSettingsDialog这个弹出框，剩下的两个弹出框被拒绝或者取消的效果
            finish();
        }


    }
}
