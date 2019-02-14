package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class BatteryBorrowReturnRecord {
    private String id;

    private String outplantid;

    private String inplantid;

    private Integer changenum;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    private String status;

    private String batterytype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOutplantid() {
        return outplantid;
    }

    public void setOutplantid(String outplantid) {
        this.outplantid = outplantid == null ? null : outplantid.trim();
    }

    public String getInplantid() {
        return inplantid;
    }

    public void setInplantid(String inplantid) {
        this.inplantid = inplantid == null ? null : inplantid.trim();
    }

    public Integer getChangenum() {
        return changenum;
    }

    public void setChangenum(Integer changenum) {
        this.changenum = changenum;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getBatterytype() {
        return batterytype;
    }

    public void setBatterytype(String batterytype) {
        this.batterytype = batterytype == null ? null : batterytype.trim();
    }
}