package com.example.administrator.jianshang.bean;

import java.io.Serializable;

public class DBGongyinshangInfoBean  implements Serializable {
    private int id;
    private String gongYinShangType;    //供应商类别
    private String gongYinShangName;    //供应商名字
    private String dangKouAddress;      //档口地址
    private String cangKuAddress;       //仓库地址
    private long dangKouTelephone;       //档口电话
    private long cangKuTelephone;        //仓库电话
    private String mingPianImgZM;         //名片正面图片文件名
    private String mingPianImgFM;       //名片反面图片文件名


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGongYinShangType() {
        return gongYinShangType;
    }

    public void setGongYinShangType(String gongYinShangType) {
        this.gongYinShangType = gongYinShangType;
    }

    public String getGongYinShangName() {
        return gongYinShangName;
    }

    public void setGongYinShangName(String gongYinShangName) {
        this.gongYinShangName = gongYinShangName;
    }

    public String getDangKouAddress() {
        return dangKouAddress;
    }

    public void setDangKouAddress(String dangKouAddress) {
        this.dangKouAddress = dangKouAddress;
    }

    public String getCangKuAddress() {
        return cangKuAddress;
    }

    public void setCangKuAddress(String cangKuAddress) {
        this.cangKuAddress = cangKuAddress;
    }

    public long getDangKouTelephone() {
        return dangKouTelephone;
    }

    public void setDangKouTelephone(long dangKouTelephone) {
        this.dangKouTelephone = dangKouTelephone;
    }

    public long getCangKuTelephone() {
        return cangKuTelephone;
    }

    public void setCangKuTelephone(long cangKuTelephone) {
        this.cangKuTelephone = cangKuTelephone;
    }

    public String getMingPianImgZM() {
        return mingPianImgZM;
    }

    public void setMingPianImgZM(String mingPianImgZM) {
        this.mingPianImgZM = mingPianImgZM;
    }

    public String getMingPianImgFM() {
        return mingPianImgFM;
    }

    public void setMingPianImgFM(String mingPianImgFM) {
        this.mingPianImgFM = mingPianImgFM;
    }
}
