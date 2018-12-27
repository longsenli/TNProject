package com.tnpy.mes.model.mysql;

import java.util.Date;

public class WorkOrder extends WorkOrderKey {
    private Integer status;

    private String units;

    private Integer batchnum;

    private Integer totalproduction;

    private String materialid;

    private Date createtime;

    private Date scheduledstarttime;

    private Date scheduledendtime;

    private Date actualstarttime;

    private Date actualendtime;

    private String openstaff;

    private String finishstaff;

    private String closestaff;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units == null ? null : units.trim();
    }

    public Integer getBatchnum() {
        return batchnum;
    }

    public void setBatchnum(Integer batchnum) {
        this.batchnum = batchnum;
    }

    public Integer getTotalproduction() {
        return totalproduction;
    }

    public void setTotalproduction(Integer totalproduction) {
        this.totalproduction = totalproduction;
    }

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getScheduledstarttime() {
        return scheduledstarttime;
    }

    public void setScheduledstarttime(Date scheduledstarttime) {
        this.scheduledstarttime = scheduledstarttime;
    }

    public Date getScheduledendtime() {
        return scheduledendtime;
    }

    public void setScheduledendtime(Date scheduledendtime) {
        this.scheduledendtime = scheduledendtime;
    }

    public Date getActualstarttime() {
        return actualstarttime;
    }

    public void setActualstarttime(Date actualstarttime) {
        this.actualstarttime = actualstarttime;
    }

    public Date getActualendtime() {
        return actualendtime;
    }

    public void setActualendtime(Date actualendtime) {
        this.actualendtime = actualendtime;
    }

    public String getOpenstaff() {
        return openstaff;
    }

    public void setOpenstaff(String openstaff) {
        this.openstaff = openstaff == null ? null : openstaff.trim();
    }

    public String getFinishstaff() {
        return finishstaff;
    }

    public void setFinishstaff(String finishstaff) {
        this.finishstaff = finishstaff == null ? null : finishstaff.trim();
    }

    public String getClosestaff() {
        return closestaff;
    }

    public void setClosestaff(String closestaff) {
        this.closestaff = closestaff == null ? null : closestaff.trim();
    }
}