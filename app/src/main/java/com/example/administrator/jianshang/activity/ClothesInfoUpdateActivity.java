package com.example.administrator.jianshang.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.bean.DBDaHuoInfoBean;
import com.example.administrator.jianshang.sqlite.dao.TbDahuoInfoDao;

public class ClothesInfoUpdateActivity extends AppCompatActivity {
    public static int RESULT_CODE = 1;

    private EditText etKh;
    private EditText etKsmc;
    private EditText etYbh;
    private EditText etBz;
    private Button btnOk;
    private Button btnQx;

    private DBDaHuoInfoBean daHuoInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_info_update);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        daHuoInfoBean = (DBDaHuoInfoBean) intent.getSerializableExtra("DBDaHuoInfoBean");


        etKh = findViewById(R.id.et_kh);
        etKsmc = findViewById(R.id.et_ksmc);
        etYbh = findViewById(R.id.et_ybh);
        etBz = findViewById(R.id.et_bz);
        btnOk = findViewById(R.id.btn_ok);
        btnQx = findViewById(R.id.btn_qx);


        etKh.setText(daHuoInfoBean.getKuanhao());
        etKsmc.setText(daHuoInfoBean.getKuanshimingcheng());
        etYbh.setText(daHuoInfoBean.getYangbanhao());
        etBz.setText(daHuoInfoBean.getBeizhu());


    }

    public void updateOnClick(View view) {
        DBDaHuoInfoBean bean = getNewBean();

        TbDahuoInfoDao dao = new TbDahuoInfoDao(ClothesInfoUpdateActivity.this);
        int n = dao.updateDahuoInfo(bean);

        Intent intent = new Intent();
        if (n>0) {
            intent.putExtra("result", bean);
        }else {
            intent.putExtra("result", new DBDaHuoInfoBean());
        }
        setResult(RESULT_CODE, intent);// 设置resultCode，onActivityResult()中能获取到
        finish();
    }

    private DBDaHuoInfoBean getNewBean() {
        DBDaHuoInfoBean bean = daHuoInfoBean;
        bean.setKuanhao(etKh.getText().toString().trim());
        bean.setKuanshimingcheng(etKsmc.getText().toString().trim());
        bean.setYangbanhao(etYbh.getText().toString().trim());
        bean.setBeizhu(etBz.getText().toString().trim());
        return bean;
    }

    public void qxOnClick(View view) {
        finish();
    }
}
