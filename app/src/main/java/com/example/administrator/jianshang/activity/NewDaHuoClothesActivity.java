package com.example.administrator.jianshang.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.jianshang.R;

public class NewDaHuoClothesActivity extends AppCompatActivity {

    private String timeData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_da_huo_clothes);

        Intent intent = getIntent();
        timeData = intent.getStringExtra("yearInfo");
    }
}
