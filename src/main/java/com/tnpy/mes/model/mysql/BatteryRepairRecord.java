package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class BatteryRepairRecord {
    private String batteryid;

    private String lineid;

    private String reportstaff;

    private String repairreason;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date repairtime;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date backtime;

    private String repairstaff;

    private String status;

    private String plantid;

    private String batterytype;

    public String getBatteryid() {
        return batteryid;
    }

    public void setBatteryid(String batteryid) {
        this.batteryid = batteryid == null ? null : batteryid.trim();
    }

    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid == null ? null : lineid.trim();
    }

    public String getReportstaff() {
        return reportstaff;
    }

    public void setReportstaff(String reportstaff) {
        this.reportstaff = reportstaff == null ? null : reportstaff.trim();
    }

    public String getRepairreason() {
        return repairreason;
    }

    public void setRepairreason(String repairreason) {
        this.repairreason = repairreason == null ? null : repairreason.trim();
    }

    public Date getRepairtime() {
        return repairtime;
    }

    public void setRepairtime(Date repairtime) {
        this.repairtime = repairtime;
    }

    public Date getBacktime() {
        return backtime;
    }

    public void setBacktime(Date backtime) {
        this.backtime = backtime;
    }

    public String getRepairstaff() {
        return repairstaff;
    }

    public void setRepairstaff(String repairstaff) {
        this.repairstaff = repairstaff == null ? null : repairstaff.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getPlantid() {
        return plantid;
    }

    public void setPlantid(String plantid) {
        this.plantid = plantid == null ? null : plantid.trim();
    }

    public String getBatterytype() {
        return batterytype;
    }

    public void setBatterytype(String batterytype) {
        this.batterytype = batterytype == null ? null : batterytype.trim();
    }
}