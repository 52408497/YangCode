package com.example.administrator.jianshang.bean;

public class DBFuliaoInfoBean {

    private int id;
    private int dahuoId;        //大货ID
    private String fuliaoName;  //辅料名称
    private String fuliaoImg;   //辅料图片文件名
    private String jiage;       //价格
    private int gongyinshangId;//供应商ID
    private String beizhu;      //备注

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

    public String getFuliaoName() {
        return fuliaoName;
    }

    public void setFuliaoName(String fuliaoName) {
        this.fuliaoName = fuliaoName;
    }

    public String getFuliaoImg() {
        return fuliaoImg;
    }

    public void setFuliaoImg(String fuliaoImg) {
        this.fuliaoImg = fuliaoImg;
    }

    public String getJiage() {
        return jiage;
    }

    public void setJiage(String jiage) {
        this.jiage = jiage;
    }

    public int getGongyinshangId() {
        return gongyinshangId;
    }

    public void setGongyinshangId(int gongyinshangId) {
        this.gongyinshangId = gongyinshangId;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
}
