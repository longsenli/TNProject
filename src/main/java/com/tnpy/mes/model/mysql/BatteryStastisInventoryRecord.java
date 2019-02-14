package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class BatteryStastisInventoryRecord {
    private String id;

    private String plantid;

    private Integer currentstorage;

    private Integer laststorage;

    private Integer scrapnum;

    private Integer repairnum;

    private Integer repairbacknum;

    private Integer loannum;

    private Integer borrownum;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    private String status;

    private Integer dailyproduction;

    private String batterytype;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPlantid() {
        return plantid;
    }

    public void setPlantid(String plantid) {
        this.plantid = plantid == null ? null : plantid.trim();
    }

    public Integer getCurrentstorage() {
        return currentstorage;
    }

    public void setCurrentstorage(Integer currentstorage) {
        this.currentstorage = currentstorage;
    }

    public Integer getLaststorage() {
        return laststorage;
    }

    public void setLaststorage(Integer laststorage) {
        this.laststorage = laststorage;
    }

    public Integer getScrapnum() {
        return scrapnum;
    }

    public void setScrapnum(Integer scrapnum) {
        this.scrapnum = scrapnum;
    }

    public Integer getRepairnum() {
        return repairnum;
    }

    public void setRepairnum(Integer repairnum) {
        this.repairnum = repairnum;
    }

    public Integer getRepairbacknum() {
        return repairbacknum;
    }

    public void setRepairbacknum(Integer repairbacknum) {
        this.repairbacknum = repairbacknum;
    }

    public Integer getLoannum() {
        return loannum;
    }

    public void setLoannum(Integer loannum) {
        this.loannum = loannum;
    }

    public Integer getBorrownum() {
        return borrownum;
    }

    public void setBorrownum(Integer borrownum) {
        this.borrownum = borrownum;
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

    public Integer getDailyproduction() {
        return dailyproduction;
    }

    public void setDailyproduction(Integer dailyproduction) {
        this.dailyproduction = dailyproduction;
    }

    public String getBatterytype() {
        return batterytype;
    }

    public void setBatterytype(String batterytype) {
        this.batterytype = batterytype == null ? null : batterytype.trim();
    }
}