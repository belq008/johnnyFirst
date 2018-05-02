package com.xshell.xshelllib.utils;

/**
 * Created by DELL on 2018/3/6.
 */

public class XshellEvent {
    public int what;

    public String msg;

    public XshellEvent(int tag) {
        this.what = tag;
    }
}
