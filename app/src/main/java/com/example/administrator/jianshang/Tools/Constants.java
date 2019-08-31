package com.example.administrator.jianshang.Tools;

/**
 * 常量类
 *
 * 用来保存接口地址
 * 以及一些常量
 */
public class Constants {
    public static final String ID = "ID";
    public static final String URI = "URI";
    public static final String FILENAME = "FILENAME";

    public static final int DBVERSION = 4;

    public static final int MENU_CAMERA = 111;
    public static final int MENU_XIANGCE = 112;

    //需要使用到相机和读写SD卡的权限
    public static final int PERMISSION_CAMERA_SDCARD = 0xaa1;

    //onActivityResult回调方法requestCode值
    public static final int CODE_CAMERA_REQUEST = 0xaa2;
    public static final int CODE_CAMERA_TOOL_REQUEST = 0xaa3;

    public static final int CODE_GALLERY_REQUEST = 0xaa4;
    public static final int PERMISSION_SDCARD = 0xaa5;

    public static final int COPE_FILE = 0xaa6;
    public static final int INSERT_DATABASE = 0xaa7;

    public static final String DB_DAHUO_IMG_TYPE_KS = "KS";

    public static final String TO_SEARCH_ACTIVITY = "ToSearch";

    public static final String SEARCH_CLOTHESINFO = "SearchClothes";
}
