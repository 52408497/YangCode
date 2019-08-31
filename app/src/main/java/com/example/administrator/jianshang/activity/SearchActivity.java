package com.example.administrator.jianshang.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.Tools.Constants;
import com.example.administrator.jianshang.myview.SearchView;

public class SearchActivity extends AppCompatActivity {
    private TextView tvTitle;
    private SearchView svInput;
    private LinearLayout llSearchList;
    private RecyclerView rvSearch;
    private View vSearchListEmpty;

    private String sToSearchActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        sToSearchActivity = intent.getStringExtra(Constants.TO_SEARCH_ACTIVITY);

        initView();
        initDatas();
    }

    private void initDatas() {
        if (sToSearchActivity.equals(Constants.SEARCH_CLOTHESINFO)){
            tvTitle.setText("搜索款式信息");
        }
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        svInput = findViewById(R.id.sv_input);
        llSearchList = findViewById(R.id.ll_search_list);
        rvSearch = findViewById(R.id.rv_search);
        vSearchListEmpty = findViewById(R.id.v_search_list_empty);

    }
}
