package com.example.administrator.jianshang.activity;

import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.Constants;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        PhotoView pv = findViewById(R.id.pv);

        //获取传递过来的参数
        String url = getIntent().getStringExtra(Constants.ID);



        Glide.with(this)
                .load(url)
                /*.placeholder(R.mipmap.ic_default_man)*/
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(pv);

//        Glide.with(ImageDetailActivity.this)
//                .load(url)
//                .into(pv);
    }
}
