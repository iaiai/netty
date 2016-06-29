package com.xiaozhi.netty.vo;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public class NettyDataLogin {

    public String username; //用户名

    public String password; //密码

    public Long createTime; //时间

    public String code; //设备号

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
