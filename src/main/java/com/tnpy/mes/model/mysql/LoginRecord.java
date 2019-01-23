package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class LoginRecord {
    private String id;

    private String username;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date logintime;

    private String loginip;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date logouttime;

    private String status;

    private String userid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Date getLogintime() {
        return logintime;
    }

    public void setLogintime(Date logintime) {
        this.logintime = logintime;
    }

    public String getLoginip() {
        return loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip == null ? null : loginip.trim();
    }

    public Date getLogouttime() {
        return logouttime;
    }

    public void setLogouttime(Date logouttime) {
        this.logouttime = logouttime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }
}