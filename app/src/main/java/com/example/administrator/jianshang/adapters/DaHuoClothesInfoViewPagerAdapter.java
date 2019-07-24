package com.example.administrator.jianshang.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class DaHuoClothesInfoViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<View> viewList;

    public DaHuoClothesInfoViewPagerAdapter(Context context, ArrayList<View> viewList) {
        this.context = context;
        this.viewList = viewList;
    }


    @Override
    public int getCount() {
        return viewList.size();
    }

    //判断是否需要重新生成子视图
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // 添加界面，一般会添加当前页和左右两边的页面
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    // 去除页面
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

}
