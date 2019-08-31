package com.example.administrator.jianshang.myview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.Constants;
import com.example.administrator.jianshang.Tools.PhotoUtils;
import com.example.administrator.jianshang.activity.NewDaHuoClothesActivity;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2019/8/27.
 */


@SuppressLint("AppCompatCustomView")
public class MyCamera extends ImageButton implements EasyPermissions.PermissionCallbacks {

    private Context context;
    private Activity activity;


    private Paint mPaint;   //画笔

    private String[] permissions;   //权限
    private String imageFileName;   //照片文件名称
    private File fileUri;           //拍照所得的照片文件
    private Uri fileUriForContent;  //照片文件的content类型的Uri
    private String folderName = ""; //照片保存的文件夹目录名称

    public MyCamera(Context context) {
        super(context);
        init(context);
    }

    public MyCamera(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyCamera(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.context = context;
    }


    /**
     * 当View需要呈现出来时自动调用此方法
     *
     * @param canvas 画布
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    //请求权限已经被授权
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //如果checkPerm方法，没有注解AfterPermissionGranted，也可以在这里调用该方法。
    }

    //请求权限被拒绝
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //这里表示拒绝权限后再点击出现的去设置对话框
        //这里需要重新设置Rationale和title，否则默认是英文格式
        //Rationale：对话框的提示内容，否则默认是英文格式
        // title：对话框的提示标题，否则默认是英文格式
        if (EasyPermissions.somePermissionPermanentlyDenied(activity, perms)) {
            //这个方法有个前提是，用户点击了“不再询问”后，才判断权限没有被获取到

            new AppSettingsDialog.Builder(activity)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
                    .setTitle("必需权限")
                    .build()
                    .show();
        }
    }

    //动态申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //权限回调交付于EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, activity);
    }

    //检测权限，若无权限则需授权
    public boolean getAndSetPermission(Activity activity) {
        boolean havePermission = false;

        this.activity = activity;

        permissions = new String[]{
                Manifest.permission.CAMERA,                 //使用相机的权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE, //写入SD卡的权限
                Manifest.permission.READ_EXTERNAL_STORAGE   //读取SD卡的权限
        };

        if (!EasyPermissions.hasPermissions(context, permissions)) {
            //权限还未申请，申请权限
            EasyPermissions.requestPermissions(activity,
                    "需要使用到相机和读写SD卡的权限",
                    Constants.PERMISSION_CAMERA_SDCARD,
                    permissions);
        }else {
            havePermission = true;
        }


        return havePermission;

    }

    //拍照
    public void takePicture(Activity activity) {
        if (this.getAndSetPermission(activity)) {
            //有权限直接调用系统相机拍照
            if (hasSdcard()) {
                //查看SD卡中是否存在JianShangPhoto文件夹，若不存在，则创建
                folderName = activity.getString(R.string.my_photo_folder_name);
                creatFolder(folderName);

                imageFileName = System.currentTimeMillis() + ".jpg";
                fileUri = new File(Environment.getExternalStorageDirectory().getPath() +
                        "/" + folderName + "/" + imageFileName);

                fileUriForContent = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //通过FileProvider创建一个content类型的Uri
                    fileUriForContent = FileProvider.getUriForFile(
                            activity,
                            "com.zz.fileprovider",
                            fileUri);
                }


                //拍完照片自动执行onActivityResult回调方法
                PhotoUtils.takePicture(
                        activity,
                        fileUriForContent,
                        Constants.CODE_CAMERA_REQUEST);

            } else {
                Toast.makeText(
                        activity,
                        "设备没有SD卡",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
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
     * 返回照片文件名称
     * @return 照片文件名称
     */
    public String getImageFileName() {
        return imageFileName;
    }

    /**
     * 返回拍照所得的照片文件
     * @return 照片文件
     */
    public File getFileUri() {
        return fileUri;
    }

    /**
     * 返回照片文件的content类型的Uri
     * @return 照片文件的content类型的Uri
     */
    public Uri getFileUriForContent() {
        return fileUriForContent;
    }


}
