package com.example.administrator.jianshang.bean;

import java.util.ArrayList;

public class HandlerMessageBean {
    private boolean copeFileIsOK;               //拷贝文件操作状态（用来判断progressDialog是否可关闭）
    private boolean insertDataBaseIsOK;         //将数据保存到数据库的状态（用来判断progressDialog是否可关闭）
    private ArrayList<String> copyErrorList;
    private boolean addIsSuccess;

public HandlerMessageBean(){
    copeFileIsOK = false;
    insertDataBaseIsOK = false;
    copyErrorList = new ArrayList<String>();
    addIsSuccess = false;
}

    public boolean isCopeFileIsOK() {
        return copeFileIsOK;
    }

    public void setCopeFileIsOK(boolean copeFileIsOK) {
        this.copeFileIsOK = copeFileIsOK;
    }

    public boolean isInsertDataBaseIsOK() {
        return insertDataBaseIsOK;
    }

    public void setInsertDataBaseIsOK(boolean insertDataBaseIsOK) {
        this.insertDataBaseIsOK = insertDataBaseIsOK;
    }

    public ArrayList<String> getCopyErrorList() {
        return copyErrorList;
    }

    public void setCopyErrorList(ArrayList<String> copyErrorList) {
        this.copyErrorList = copyErrorList;
    }

    public boolean isAddIsSuccess() {
        return addIsSuccess;
    }

    public void setAddIsSuccess(boolean addIsSuccess) {
        this.addIsSuccess = addIsSuccess;
    }
}
