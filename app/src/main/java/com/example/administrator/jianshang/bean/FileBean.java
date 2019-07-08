package com.example.administrator.jianshang.bean;

import android.net.Uri;

/**
 * Created by Administrator on 2019/6/11.
 */

public class FileBean {
    private String fileName;
    private Uri fileUri;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }
}
