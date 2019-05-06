package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class ChargingRackRecord {
    private String id;

    private String plantid;

    private String processid;

    private String lineid;

    private String worklocation;

    private String staffid;

    private String staffname;

    private String materialid;

    private String materialname;

    private Float productionnumber;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date putondate;

    private Float repairnumber;

    private String repairid;

    private String repairname;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date repairtime;

    private String reason;

    private Float realnumber;

    private String materialtype;

    private String pulloffstaffid;

    private String pulloffstaffname;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date pulloffdate;

    private String repaircombine;

    private String status;

    private String remark;

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

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid == null ? null : processid.trim();
    }

    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid == null ? null : lineid.trim();
    }

    public String getWorklocation() {
        return worklocation;
    }

    public void setWorklocation(String worklocation) {
        this.worklocation = worklocation == null ? null : worklocation.trim();
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid == null ? null : staffid.trim();
    }

    public String getStaffname() {
        return staffname;
    }

    public void setStaffname(String staffname) {
        this.staffname = staffname == null ? null : staffname.trim();
    }

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname == null ? null : materialname.trim();
    }

    public Float getProductionnumber() {
        return productionnumber;
    }

    public void setProductionnumber(Float productionnumber) {
        this.productionnumber = productionnumber;
    }

    public Date getPutondate() {
        return putondate;
    }

    public void setPutondate(Date putondate) {
        this.putondate = putondate;
    }

    public Float getRepairnumber() {
        return repairnumber;
    }

    public void setRepairnumber(Float repairnumber) {
        this.repairnumber = repairnumber;
    }

    public String getRepairid() {
        return repairid;
    }

    public void setRepairid(String repairid) {
        this.repairid = repairid == null ? null : repairid.trim();
    }

    public String getRepairname() {
        return repairname;
    }

    public void setRepairname(String repairname) {
        this.repairname = repairname == null ? null : repairname.trim();
    }

    public Date getRepairtime() {
        return repairtime;
    }

    public void setRepairtime(Date repairtime) {
        this.repairtime = repairtime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Float getRealnumber() {
        return realnumber;
    }

    public void setRealnumber(Float realnumber) {
        this.realnumber = realnumber;
    }

    public String getMaterialtype() {
        return materialtype;
    }

    public void setMaterialtype(String materialtype) {
        this.materialtype = materialtype == null ? null : materialtype.trim();
    }

    public String getPulloffstaffid() {
        return pulloffstaffid;
    }

    public void setPulloffstaffid(String pulloffstaffid) {
        this.pulloffstaffid = pulloffstaffid == null ? null : pulloffstaffid.trim();
    }

    public String getPulloffstaffname() {
        return pulloffstaffname;
    }

    public void setPulloffstaffname(String pulloffstaffname) {
        this.pulloffstaffname = pulloffstaffname == null ? null : pulloffstaffname.trim();
    }

    public Date getPulloffdate() {
        return pulloffdate;
    }

    public void setPulloffdate(Date pulloffdate) {
        this.pulloffdate = pulloffdate;
    }

    public String getRepaircombine() {
        return repaircombine;
    }

    public void setRepaircombine(String repaircombine) {
        this.repaircombine = repaircombine == null ? null : repaircombine.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}