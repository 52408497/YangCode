package com.example.administrator.jianshang.bean;

public class DBYangyiInfoBean {
    private int id;
    private String kuanShiMingCheng;    //款式名称
    private String yangBanHao;          //样板编号
    private String fengMianImg;         //封面图片名称
    private String creatTime;           //创建时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKuanShiMingCheng() {
        return kuanShiMingCheng;
    }

    public void setKuanShiMingCheng(String kuanShiMingCheng) {
        this.kuanShiMingCheng = kuanShiMingCheng;
    }

    public String getYangBanHao() {
        return yangBanHao;
    }

    public void setYangBanHao(String yangBanHao) {
        this.yangBanHao = yangBanHao;
    }

    public String getFengMianImg() {
        return fengMianImg;
    }

    public void setFengMianImg(String fengMianImg) {
        this.fengMianImg = fengMianImg;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getBeiZhu() {
        return beiZhu;
    }

    public void setBeiZhu(String beiZhu) {
        this.beiZhu = beiZhu;
    }

    private String beiZhu;              //备注


}
