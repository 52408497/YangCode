package com.example.administrator.jianshang.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.PhotoUtils;
import com.example.administrator.jianshang.bean.DBGongyinshangInfoBean;
import com.example.administrator.jianshang.bean.FileBean;
import com.example.administrator.jianshang.sqlite.dao.TbGongyinshangInfoDao;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class NewGongYinShangActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int PERMISSION_CAMERA_SDCARD = 0xa1;
    private static final int CODE_CAMERA_ZM_REQUEST = 0xa2;
    private static final int CODE_CAMERA_FM_REQUEST = 0xa3;

    //--------------界面View控件------------------
    private EditText etName;
    private RadioGroup rgType;
    private RadioButton rbMl;
    private RadioButton rbFl;
    private EditText etDkAddress;
    private EditText etDkTel;
    private EditText etCkAddress;
    private EditText etCkTel;
    private ImageView ivMpZm;
    private ImageView ivMpFm;
    //--------------------------------

    private File file;
    private String[] permissions;
    private String folderName = "";
    private String imageFileName = "";
    private String imgZM = "";
    private String imgFM = "";

    private String zmOrFm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gong_yin_shang);

        //查看SD卡中是否存在JianShangPhoto文件夹，若不存在，则创建
        folderName = this.getString(R.string.my_photo_folder_name);
        creatFolder(folderName);

        initViews();

    }

    private void initViews() {
        etName = (EditText) findViewById(R.id.et_name);
        rgType = (RadioGroup) findViewById(R.id.rg_type);
        rbMl = (RadioButton) findViewById(R.id.rb_ml);
        rbFl = (RadioButton) findViewById(R.id.rb_fl);
        etDkAddress = (EditText) findViewById(R.id.et_dk_address);
        etDkTel = (EditText) findViewById(R.id.et_dk_tel);
        etCkAddress = (EditText) findViewById(R.id.et_ck_address);
        etCkTel = (EditText) findViewById(R.id.et_ck_tel);
        ivMpZm = (ImageView) findViewById(R.id.iv_mp_zm);
        ivMpFm = (ImageView) findViewById(R.id.iv_mp_fm);
    }

    public void addMpPhotoZM(View view) {
        zmOrFm = "正面";
        paiZhao();


    }

    public void addMpPhotoFM(View view) {
        zmOrFm = "反面";
        paiZhao();

    }

    public void btnOK(View view) {
        //判断供应商名字是否为空
        String name = etName.getText().toString().trim();
        if (name.equals("")) {
            Toast.makeText(NewGongYinShangActivity.this, "供应商名字不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            String dkTel = etDkTel.getText().toString().trim();
            String ckTel = etCkTel.getText().toString().trim();
            RadioButton rbType = findViewById(rgType.getCheckedRadioButtonId());
            DBGongyinshangInfoBean bean = new DBGongyinshangInfoBean();
            bean.setGongYinShangName(name);
            bean.setGongYinShangType(rbType.getText().toString().trim());
            bean.setDangKouAddress(etDkAddress.getText().toString().trim());
            bean.setDangKouTelephone((dkTel.equals("")||dkTel==null)?0+"":dkTel);
            bean.setCangKuTelephone((ckTel.equals("")||ckTel==null)?0+"":ckTel);
            bean.setCangKuAddress(etCkAddress.getText().toString().trim());
            bean.setMingPianImgZM(imgZM);
            bean.setMingPianImgFM(imgFM);

            //保存数据
            TbGongyinshangInfoDao dao = new TbGongyinshangInfoDao(NewGongYinShangActivity.this);
            boolean isSuccess = dao.addGongyinshangInfo(bean);

            if (isSuccess){
                Toast.makeText(NewGongYinShangActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                intent.putExtra("data","refresh");
                LocalBroadcastManager.getInstance(NewGongYinShangActivity.this).sendBroadcast(intent);
                sendBroadcast(intent);
                this.finish();

            }else {
                Toast.makeText(NewGongYinShangActivity.this, "添加失败！", Toast.LENGTH_SHORT).show();

            }


        }


    }


    @AfterPermissionGranted(PERMISSION_CAMERA_SDCARD)
    public void paiZhao() {

        permissions = new String[]{
                Manifest.permission.CAMERA,                 //使用相机的权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE, //写入SD卡的权限
                Manifest.permission.READ_EXTERNAL_STORAGE   //读取SD卡的权限
        };

        if (EasyPermissions.hasPermissions(NewGongYinShangActivity.this, permissions)) {
            //有权限直接调用系统相机拍照
            if (hasSdcard()) {

                Uri fileUriForContent = getFileUriForContent();


                switch (zmOrFm) {
                    case "正面":
                        //拍完照片自动执行onActivityResult回调方法
                        imgZM = imageFileName;
                        PhotoUtils.takePicture(
                                NewGongYinShangActivity.this,
                                fileUriForContent,
                                CODE_CAMERA_ZM_REQUEST);
                        zmOrFm = "";
                        break;
                    case "反面":
                        //拍完照片自动执行onActivityResult回调方法
                        imgFM = imageFileName;
                        PhotoUtils.takePicture(
                                NewGongYinShangActivity.this,
                                fileUriForContent,
                                CODE_CAMERA_FM_REQUEST);
                        zmOrFm = "";
                        break;
                }
            } else {
                //ToastUtils.showShort(this, "设备没有SD卡！");
                Toast.makeText(
                        NewGongYinShangActivity.this,
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
     * 根据系统时间创建一个Content类型的URI
     *
     * @return
     */
    private Uri getFileUriForContent() {

        imageFileName = System.currentTimeMillis() + ".jpg";
        file = new File(Environment.getExternalStorageDirectory().getPath() +
                "/" + folderName + "/" + imageFileName);

        Uri uri = Uri.fromFile(file);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            uri = FileProvider.getUriForFile(
                    NewGongYinShangActivity.this,
                    "com.zz.fileprovider",
                    file);
        }

        return uri;
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
     * 将图片显示在ImageView控件中
     *
     * @param uri
     * @param view
     */
    private void showImages(Uri uri, ImageView view) {

        Glide.with(NewGongYinShangActivity.this).load(uri).into(view);

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

            new AppSettingsDialog.Builder(NewGongYinShangActivity.this)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
                    .setTitle("必需权限")
                    .build()
                    .show();
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, NewGongYinShangActivity.this);

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
                case CODE_CAMERA_ZM_REQUEST://拍照完成回调

                    fileUriForFile = Uri.fromFile(file);

                    if (fileUriForFile != null) {
                        showImages(fileUriForFile, ivMpZm);
                    }
                    fileUriForFile = null;
                    break;
                case CODE_CAMERA_FM_REQUEST://拍照完成回调

                    fileUriForFile = Uri.fromFile(file);

                    if (fileUriForFile != null) {
                        showImages(fileUriForFile, ivMpFm);
                    }
                    fileUriForFile = null;
                    break;
            }
        }
    }


}
