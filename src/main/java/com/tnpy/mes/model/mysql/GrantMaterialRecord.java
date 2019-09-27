package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class GrantMaterialRecord {
    private String id;

    private String batterytype;

    private String plantid;

    private String processid;

    private String orderid;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date granttime;

    private String operator;

    private Integer number;

    private String status;

    private String ordername;

    private String acceptplantid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBatterytype() {
        return batterytype;
    }

    public void setBatterytype(String batterytype) {
        this.batterytype = batterytype == null ? null : batterytype.trim();
    }

    public String getPlantid() {
        return plantid;
    }

    public void setPlantid(String plantid) {
        this.plantid = plantid == null ? null : plantid.trim();
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid == null ? null : processid.trim();
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public Date getGranttime() {
        return granttime;
    }

    public void setGranttime(Date granttime) {
        this.granttime = granttime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getOrdername() {
        return ordername;
    }

    public void setOrdername(String ordername) {
        this.ordername = ordername == null ? null : ordername.trim();
    }

    public String getAcceptplantid() {
        return acceptplantid;
    }

    public void setAcceptplantid(String acceptplantid) {
        this.acceptplantid = acceptplantid == null ? null : acceptplantid.trim();
    }
}