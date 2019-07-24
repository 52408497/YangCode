package com.example.administrator.jianshang.bean;

public class FuLiaoInfoBean {
    private String fuliao_name;
    private String fuliao_img_name;
    private int jiage;
    private String gongyingshang;
    private int gongyinshangId;

    private int id;
    private int dahuoId;        //大货ID
    private String beizhu;      //备注


    public int getGongyinshangId() {
        return gongyinshangId;
    }

    public void setGongyinshangId(int gongyinshangId) {
        this.gongyinshangId = gongyinshangId;
    }

    public String getFuliao_name() {
        return fuliao_name;
    }

    public void setFuliao_name(String fuliao_name) {
        this.fuliao_name = fuliao_name;
    }

    public String getFuliao_img_name() {
        return fuliao_img_name;
    }

    public void setFuliao_img_name(String fuliao_img_name) {
        this.fuliao_img_name = fuliao_img_name;
    }

    public int getJiage() {
        return jiage;
    }

    public void setJiage(int jiage) {
        this.jiage = jiage;
    }

    public String getGongyingshang() {
        return gongyingshang;
    }

    public void setGongyingshang(String gongyingshang) {
        this.gongyingshang = gongyingshang;
    }


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

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
}

