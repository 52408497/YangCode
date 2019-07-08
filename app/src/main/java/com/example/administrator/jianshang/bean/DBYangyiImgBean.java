package com.example.administrator.jianshang.bean;

public class DBYangyiImgBean {
    private int id;
    private int yangYiId;       //样衣ID
    private String imgType;     //图片类别
    private String imgName;     //图片文件名

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYangYiId() {
        return yangYiId;
    }

    public void setYangYiId(int yangYiId) {
        this.yangYiId = yangYiId;
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
