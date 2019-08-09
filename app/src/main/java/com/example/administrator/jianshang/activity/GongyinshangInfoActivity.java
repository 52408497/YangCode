package com.example.administrator.jianshang.activity;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.bean.DBGongyinshangInfoBean;

import java.io.File;

public class GongyinshangInfoActivity extends AppCompatActivity {
private  DBGongyinshangInfoBean gongyinshangInfoBean = null;
    private TextView tvId;
    private TextView tvGongYinShangName;
    private TextView tvGongYinShangType;
    private TextView tvDangKouAddress;
    private TextView tvDangKouTelephone;
    private TextView tvCangKuAddress;
    private TextView tvCangKuTelephone;
    private ImageView ivMingPianImgZm;
    private ImageView ivMingPianImgFm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gongyinshang_info);

        gongyinshangInfoBean = (DBGongyinshangInfoBean)getIntent().getSerializableExtra("gongyinshangInfoBean");

        initView();
        initDatas();

    }

    private void initDatas() {
        tvId.setText(gongyinshangInfoBean.getId()+"");
        tvGongYinShangName.setText(gongyinshangInfoBean.getGongYinShangName());
        tvGongYinShangType.setText(gongyinshangInfoBean.getGongYinShangType());
        tvDangKouAddress.setText(gongyinshangInfoBean.getDangKouAddress());
        tvDangKouTelephone.setText(gongyinshangInfoBean.getDangKouTelephone()+"");
        tvCangKuAddress.setText(gongyinshangInfoBean.getCangKuAddress());
        tvCangKuTelephone.setText(gongyinshangInfoBean.getCangKuTelephone()+"");

        showImg(ivMingPianImgZm,gongyinshangInfoBean.getMingPianImgZM());
        showImg(ivMingPianImgFm,gongyinshangInfoBean.getMingPianImgFM());

    }

    private void showImg(ImageView ivMingPianImg, String imgName) {
        String imageFileName = imgName;
        String folderName = GongyinshangInfoActivity.this.getString(R.string.my_photo_folder_name);
        File fileUri = new File(Environment.getExternalStorageDirectory().getPath() +
                "/" + folderName + "/" + imageFileName);
        Uri fileUriForContent = Uri.fromFile(fileUri);

        Glide.with(GongyinshangInfoActivity.this)
                .load(fileUriForContent)
                .placeholder(R.drawable.default_no_img)     //占位图
                .error(R.drawable.default_no_img)           //出错的占位图
//                .override(width, height)                    //图片显示的分辨率，像素值，可转化为dp再设
//              .animate(R.anim.glide_anim)                 //动画
//                .centerCrop()                               //图片显示样式
//                .fitCenter()                                //图片显示样式
                .dontAnimate()
                .into(ivMingPianImg);
    }

    private void initView() {
        tvId = (TextView) findViewById(R.id.tv_id);
        tvGongYinShangName = (TextView) findViewById(R.id.tv_gongYinShangName);
        tvGongYinShangType = (TextView) findViewById(R.id.tv_gongYinShangType);
        tvDangKouAddress = (TextView) findViewById(R.id.tv_dangKouAddress);
        tvDangKouTelephone = (TextView) findViewById(R.id.tv_dangKouTelephone);
        tvCangKuAddress = (TextView) findViewById(R.id.tv_cangKuAddress);
        tvCangKuTelephone = (TextView) findViewById(R.id.tv_cangKuTelephone);
        ivMingPianImgZm = (ImageView) findViewById(R.id.iv_mingPian_img_zm);
        ivMingPianImgFm = (ImageView) findViewById(R.id.iv_mingPian_img_fm);
    }
}
