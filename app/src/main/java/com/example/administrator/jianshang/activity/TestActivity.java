package com.example.administrator.jianshang.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.myview.MyCamera;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);




    }

    public void aa(View view) {
        MyCamera myCamera = (MyCamera)view;
        myCamera.getAndSetPermission(this);

    }
}
