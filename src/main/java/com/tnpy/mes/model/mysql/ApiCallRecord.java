package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class ApiCallRecord {
    private String id;

    private String userid;

    private String username;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date calltime;

    private String loginip;

    private String apiroute;

    private String params;

    private String responsestatus;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Date getCalltime() {
        return calltime;
    }

    public void setCalltime(Date calltime) {
        this.calltime = calltime;
    }

    public String getLoginip() {
        return loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip == null ? null : loginip.trim();
    }

    public String getApiroute() {
        return apiroute;
    }

    public void setApiroute(String apiroute) {
        this.apiroute = apiroute == null ? null : apiroute.trim();
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    public String getResponsestatus() {
        return responsestatus;
    }

    public void setResponsestatus(String responsestatus) {
        this.responsestatus = responsestatus == null ? null : responsestatus.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}