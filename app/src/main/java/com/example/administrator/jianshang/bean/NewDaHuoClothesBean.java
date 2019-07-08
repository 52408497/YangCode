package com.example.administrator.jianshang.bean;

import java.util.ArrayList;

public class NewDaHuoClothesBean {
    private DBDaHuoInfoBean dbDaHuoInfoBean;
    private ArrayList<DBDahuoImgBean> dbDahuoImgBeans;      //该款式的款式图片集合
    private ArrayList<DBFuliaoInfoBean> dbFuliaoInfoBeans;  //该款式的面辅料信息集合

    public DBDaHuoInfoBean getDbDaHuoInfoBean() {
        return dbDaHuoInfoBean;
    }

    public void setDbDaHuoInfoBean(DBDaHuoInfoBean dbDaHuoInfoBean) {
        this.dbDaHuoInfoBean = dbDaHuoInfoBean;
    }

    public ArrayList<DBDahuoImgBean> getDbDahuoImgBeans() {
        return dbDahuoImgBeans;
    }

    public void setDbDahuoImgBeans(ArrayList<DBDahuoImgBean> dbDahuoImgBeans) {
        this.dbDahuoImgBeans = dbDahuoImgBeans;
    }

    public ArrayList<DBFuliaoInfoBean> getDbFuliaoInfoBeans() {
        return dbFuliaoInfoBeans;
    }

    public void setDbFuliaoInfoBeans(ArrayList<DBFuliaoInfoBean> dbFuliaoInfoBeans) {
        this.dbFuliaoInfoBeans = dbFuliaoInfoBeans;
    }
}
