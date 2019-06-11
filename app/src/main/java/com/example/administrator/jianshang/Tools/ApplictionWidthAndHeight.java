package com.example.administrator.jianshang.Tools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.example.administrator.jianshang.activity.NewDaHuoClothesActivity;

/**
 * Created by Administrator on 2019/6/10.
 */

public class ApplictionWidthAndHeight {


    private Context context;
    private int width;          // 屏幕宽度（像素）
    private int height;         // 屏幕高度（像素）
    private float density;      // 屏幕密度（0.75 / 1.0 / 1.5）
    private int densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
    private int screenWidth;    // 屏幕宽度(dp)
    private int screenHeight;   // 屏幕高度(dp)

    public ApplictionWidthAndHeight(Context context) {
        this.context = context;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        density = dm.density;
        densityDpi = dm.densityDpi;

        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        screenWidth = (int) (width / density);
        screenHeight = (int) (height / density);

    }

    /**
     * 获取屏幕宽度（像素）
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * 获取屏幕高度（像素）
     *
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * 获取屏幕密度（0.75 / 1.0 / 1.5）
     *
     * @return
     */
    public float getDensity() {
        return density;
    }

    /**
     * 获取屏幕密度dpi（120 / 160 / 240）
     *
     * @return
     */
    public int getDensityDpi() {
        return densityDpi;
    }

    /**
     * 获取屏幕宽度(dp)
     *
     * @return
     */
    public int getScreenWidth() {
        return screenWidth;
    }

    /**
     * 获取屏幕高度(dp)
     *
     * @return
     */
    public int getScreenHeight() {
        return screenHeight;
    }
}
