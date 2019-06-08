package com.example.administrator.jianshang.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.PhotoUtils;
import com.example.administrator.jianshang.Tools.VerifyStoragePermissions;

import java.io.File;

public class NewDaHuoClothesActivity extends AppCompatActivity {
    private ImageView photo;
    private String timeData;
    private String fileName = "";

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_da_huo_clothes);

        //动态获取SD卡权限
       // VerifyStoragePermissions.verifyStoragePermissions(NewDaHuoClothesActivity.this);
        photo = findViewById(R.id.iv_test);



        Intent intent = getIntent();
        timeData = intent.getStringExtra("yearInfo");
    }

//    public void addImages(View view) {
//        //创建弹出式菜单对象（最低版本11）
//        PopupMenu popup = new PopupMenu(this, view);
//        //获取菜单填充器
//        MenuInflater inflater = popup.getMenuInflater();
//        //填充菜单
//        inflater.inflate(R.menu.menu_get_image, popup.getMenu());
//        //绑定菜单项的点击事件
//        popup.setOnMenuItemClickListener(this);
//        //显示(这一行代码不要忘记了)
//        popup.show();
//    }

//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_item_paizhao:
//
//
//                autoObtainCameraPermission();
//
//
//                break;
//            case R.id.menu_item_xiangce:
//
//                autoObtainStoragePermission();
//
//                Toast.makeText(this, "从相册选择相片", Toast.LENGTH_SHORT).show();
//                break;
//            default:
//                break;
//        }
//        return false;
//    }



    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                //ToastUtils.showShort(this, "您已经拒绝过一次");
                Toast.makeText(NewDaHuoClothesActivity.this, "您已经拒绝过一次", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {
            //有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(NewDaHuoClothesActivity.this, "com.zz.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                //ToastUtils.showShort(this, "设备没有SD卡！");
                Toast.makeText(NewDaHuoClothesActivity.this, "设备没有SD卡", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 动态申请权限的回调方法
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(NewDaHuoClothesActivity.this, "com.zz.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        //ToastUtils.showShort(this, "设备没有SD卡！");
                        Toast.makeText(NewDaHuoClothesActivity.this, "设备没有SD卡", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    //ToastUtils.showShort(this, "请允许打开相机！！");
                    Toast.makeText(NewDaHuoClothesActivity.this, "请允许打开相机!!", Toast.LENGTH_SHORT).show();
                }
                break;


            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    //ToastUtils.showShort(this, "请允许打操作SDCard！！");
                    Toast.makeText(NewDaHuoClothesActivity.this, "请允许打操作SDCard!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private int output_X = 480;
    private int output_Y = 480;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.zz.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        //ToastUtils.showShort(this, "设备没有SD卡！");
                        Toast.makeText(NewDaHuoClothesActivity.this, "设备没有SD卡!!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showImages(bitmap);
                    }
                    break;
            }
        }
    }


    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    private void showImages(Bitmap bitmap) {
        photo.setImageBitmap(bitmap);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 点击拍照获取款式图片按钮
     * @param view
     */
    public void paiZhaoOnClickForKSTP(View view) {
        autoObtainCameraPermission();
    }

    /**
     * 点击从相册获取款式图片按钮
     * @param view
     */
    public void xiangCeOnClickForKSTP(View view) {
        autoObtainStoragePermission();
    }
}
