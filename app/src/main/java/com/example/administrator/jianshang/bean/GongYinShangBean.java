package com.example.administrator.jianshang.bean;

import java.io.Serializable;

public class GongYinShangBean implements Serializable {
    private Integer id;
    private String name;

    public GongYinShangBean() {

    }

    public GongYinShangBean(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
