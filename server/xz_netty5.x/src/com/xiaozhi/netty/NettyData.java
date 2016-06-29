package com.xiaozhi.netty;

import java.util.List;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public class NettyData {

    public Integer code;    //根据码来做什么事

    public Object obj;  //数据(如果是错误信息之类的,此地方直接保存错误信息)

    public List list;   //数据

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
