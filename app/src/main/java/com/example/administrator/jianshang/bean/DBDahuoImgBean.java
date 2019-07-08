package com.example.administrator.jianshang.bean;

public class DBDahuoImgBean {
    private int id;
    private int dahuoId;    //大货信息表中对应的ID
    private String imgType; //图片类型
    private String imgName; //图片文件名


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

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }



}
