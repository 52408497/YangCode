package com.example.administrator.jianshang.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/6/4.
 */

public class DBDaHuoInfoBean implements Serializable {
    private int id;
    private String kuanhao;             //款号
    private String kuanshimingcheng;    //款式名称
    private String yangbanhao;          //样板号
    private String yearInfo;            //年份信息
    private String fengmianImg;         //封面图片
    private String chengbenImg;         //成本图片
    private String tag;                 //标记（是否为爆款等） PT/BK/..
    private String beizhu;              //备注

    public String getChengbenImg() {
        return chengbenImg;
    }

    public void setChengbenImg(String chengbenImg) {
        this.chengbenImg = chengbenImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKuanhao() {
        return kuanhao;
    }

    public void setKuanhao(String kuanhao) {
        this.kuanhao = kuanhao;
    }

    public String getKuanshimingcheng() {
        return kuanshimingcheng;
    }

    public void setKuanshimingcheng(String kuanshimingcheng) {
        this.kuanshimingcheng = kuanshimingcheng;
    }

    public String getYangbanhao() {
        return yangbanhao;
    }

    public void setYangbanhao(String yangbanhao) {
        this.yangbanhao = yangbanhao;
    }

    public String getYearInfo() {
        return yearInfo;
    }

    public void setYearInfo(String yearInfo) {
        this.yearInfo = yearInfo;
    }

    public String getFengmianImg() {
        return fengmianImg;
    }

    public void setFengmianImg(String fengmianImg) {
        this.fengmianImg = fengmianImg;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
}
