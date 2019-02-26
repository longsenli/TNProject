package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class BatteryShipmentNumRecord {
    private String id;

    private String materialid;

    private String batterytype;

    private String plantid;

    private Integer shipmentnum;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date shipmenttime;

    private String operator;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
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

    public Integer getShipmentnum() {
        return shipmentnum;
    }

    public void setShipmentnum(Integer shipmentnum) {
        this.shipmentnum = shipmentnum;
    }

    public Date getShipmenttime() {
        return shipmenttime;
    }

    public void setShipmenttime(Date shipmenttime) {
        this.shipmenttime = shipmenttime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}