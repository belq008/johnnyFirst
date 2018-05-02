package com.xshell.xshelllib.greendao;


import com.xshell.xshelllib.greendao.bean.XLog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

/**
 * Created by zzy on 2016/9/26.
 * httpPlugin的回调
 */
public abstract class XLogUploadCallback extends StringCallback {

    private int size;
    private List<XLog> xLogList;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<XLog> getxLogList() {
        return xLogList;
    }

    public void setxLogList(List<XLog> xLogList) {
        this.xLogList = xLogList;
    }
}
