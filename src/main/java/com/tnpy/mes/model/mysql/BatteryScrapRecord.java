package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class BatteryScrapRecord {
    private String batteryid;

    private String lineid;

    private String scraptype;

    private String scrapreason;

    private String scrapstaff;

    private String status;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date scraptime;

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

    public String getScraptype() {
        return scraptype;
    }

    public void setScraptype(String scraptype) {
        this.scraptype = scraptype == null ? null : scraptype.trim();
    }

    public String getScrapreason() {
        return scrapreason;
    }

    public void setScrapreason(String scrapreason) {
        this.scrapreason = scrapreason == null ? null : scrapreason.trim();
    }

    public String getScrapstaff() {
        return scrapstaff;
    }

    public void setScrapstaff(String scrapstaff) {
        this.scrapstaff = scrapstaff == null ? null : scrapstaff.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getScraptime() {
        return scraptime;
    }

    public void setScraptime(Date scraptime) {
        this.scraptime = scraptime;
    }
}