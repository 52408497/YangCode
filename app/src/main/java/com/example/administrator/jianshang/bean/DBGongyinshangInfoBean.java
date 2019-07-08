package com.example.administrator.jianshang.bean;

public class DBGongyinshangInfoBean {
    private int id;
    private String gongYinShangType;    //供应商类别
    private String gongYinShangName;    //供应商名字
    private String dangKouAddress;      //档口地址
    private String cangKuAddress;       //仓库地址
    private int dangKouTelephone;       //档口电话
    private int cangKuTelephone;        //仓库电话
    private String mingPianImg;         //名片图片文件名

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

    public int getDangKouTelephone() {
        return dangKouTelephone;
    }

    public void setDangKouTelephone(int dangKouTelephone) {
        this.dangKouTelephone = dangKouTelephone;
    }

    public int getCangKuTelephone() {
        return cangKuTelephone;
    }

    public void setCangKuTelephone(int cangKuTelephone) {
        this.cangKuTelephone = cangKuTelephone;
    }

    public String getMingPianImg() {
        return mingPianImg;
    }

    public void setMingPianImg(String mingPianImg) {
        this.mingPianImg = mingPianImg;
    }
}
