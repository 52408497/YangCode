package com.example.administrator.jianshang.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.administrator.jianshang.R;

import java.util.ArrayList;
import java.util.List;

import com.example.administrator.jianshang.base.BaseFragment;
import com.example.administrator.jianshang.fragment.DahuoFragment;
import com.example.administrator.jianshang.fragment.GongYingShangFragment;
import com.example.administrator.jianshang.fragment.YangYiFragment;

public class MainActivity extends FragmentActivity {
    private RadioGroup rgMain;
    private List<BaseFragment> mBaseFragment;

    private int position;   //选中的Fragment的对应的位置

    private Fragment fromFragment;//上次切换的Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化View
        initView();

        //初始化Fragement
        initFragement();

        //设置RadioGroup的监听
        setListener();
    }


    private void setListener() {
        rgMain.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rgMain.check(R.id.rb_daHuo);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch (checkedId) {
                case R.id.rb_daHuo:
                    position = 0;
                    break;
//                case R.id.rb_yangYi:
//                    position = 1;
//                    break;
                case R.id.rb_gongYingShang:
                    position = 2;
                    break;
                default:
                    position = 0;
                    break;
            }

            //根据位置得到对应的Fragment
            BaseFragment toFragment = getFragment(position);
            //替换原来的Fragment
            switchFragment(fromFragment, toFragment);
        }

        /**
         * 根据位置得到对应的Fragment
         *
         * @param position Fragment位置
         * @return
         */
        private BaseFragment getFragment(int position) {
            BaseFragment fragment = mBaseFragment.get(position);
            return fragment;
        }
    }


    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            fromFragment = to;

            //FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            //切换
            if (!to.isAdded()) {
                //没有被添加
                //隐藏from
                if (from != null) {
                    ft.hide(from);
                }

                //添加to
                if (to != null) {
                    ft.add(R.id.fl_content, to).commit();
                }

            } else {
                //已经被添加
                //隐藏from
                if (from != null) {
                    ft.hide(from);
                }
                //显示to
                ft.show(to).commit();
            }
        }
    }


    /*
        private void switchFragment(BaseFragment com.example.administrator.jianshang.fragment) {
        //得到FragmentManger
        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //替换
        transaction.replace(R.id.fl_content,com.example.administrator.jianshang.fragment);
        //提交事务
        transaction.commit();

    }
     */


    private void initFragement() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new DahuoFragment());
        mBaseFragment.add(new YangYiFragment());
        mBaseFragment.add(new GongYingShangFragment());
    }

    private void initView() {
        rgMain = findViewById(R.id.rg_main);
    }
}
