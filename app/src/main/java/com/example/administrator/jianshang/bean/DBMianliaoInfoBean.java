package com.example.administrator.jianshang.bean;

public class DBMianliaoInfoBean {
    private int id;
    private int dahuoId;            //大货ID
    private String mianliaoName;    //面料名称
    private String mianliaoImg;     //面料图片文件名
    private String jiage;           //价格
    private int gongyingshangId;   //供应商ID
    private String beizhu;          //备注

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDahuoId() {
        return dahuoId;
    }

    public void setDahuoId(int dahuoId) {
        this.dahuoId = dahuoId;
    }

    public String getMianliaoName() {
        return mianliaoName;
    }

    public void setMianliaoName(String mianliaoName) {
        this.mianliaoName = mianliaoName;
    }

    public String getMianliaoImg() {
        return mianliaoImg;
    }

    public void setMianliaoImg(String mianliaoImg) {
        this.mianliaoImg = mianliaoImg;
    }

    public String getJiage() {
        return jiage;
    }

    public void setJiage(String jiage) {
        this.jiage = jiage;
    }

    public int getGongyingshangId() {
        return gongyingshangId;
    }

    public void setGongyingshangId(int gongyingshangId) {
        this.gongyingshangId = gongyingshangId;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
}
