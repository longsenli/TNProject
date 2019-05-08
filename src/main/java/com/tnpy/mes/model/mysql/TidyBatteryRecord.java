package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class TidyBatteryRecord {
    private String id;

    private String plantid;

    private String processid;

    private String lineid;

    private String worklocation;

    private Float currentnum;

    private Float lastnum;

    private String materialid;

    private String materialname;

    private Float repairnumber;

    private String repairid;

    private String repairname;

    private Date repairtime;

    private String reason;

    private String materialtype;

    private Float backtochargenum;

    private Float pilenum;

    private Float packnum;

    private Float pulloffnum;

    private Float repairbacknum;

    private String repaircombine;
    @JSONField(format ="yyyy-MM-dd")
    private Date daytime;

    private String status;

    private String remark;

    private String operatorid;

    private String operatorname;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date operatortime;

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

    public Float getCurrentnum() {
        return currentnum;
    }

    public void setCurrentnum(Float currentnum) {
        this.currentnum = currentnum;
    }

    public Float getLastnum() {
        return lastnum;
    }

    public void setLastnum(Float lastnum) {
        this.lastnum = lastnum;
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

    public String getMaterialtype() {
        return materialtype;
    }

    public void setMaterialtype(String materialtype) {
        this.materialtype = materialtype == null ? null : materialtype.trim();
    }

    public Float getBacktochargenum() {
        return backtochargenum;
    }

    public void setBacktochargenum(Float backtochargenum) {
        this.backtochargenum = backtochargenum;
    }

    public Float getPilenum() {
        return pilenum;
    }

    public void setPilenum(Float pilenum) {
        this.pilenum = pilenum;
    }

    public Float getPacknum() {
        return packnum;
    }

    public void setPacknum(Float packnum) {
        this.packnum = packnum;
    }

    public Float getPulloffnum() {
        return pulloffnum;
    }

    public void setPulloffnum(Float pulloffnum) {
        this.pulloffnum = pulloffnum;
    }

    public Float getRepairbacknum() {
        return repairbacknum;
    }

    public void setRepairbacknum(Float repairbacknum) {
        this.repairbacknum = repairbacknum;
    }

    public String getRepaircombine() {
        return repaircombine;
    }

    public void setRepaircombine(String repaircombine) {
        this.repaircombine = repaircombine == null ? null : repaircombine.trim();
    }

    public Date getDaytime() {
        return daytime;
    }

    public void setDaytime(Date daytime) {
        this.daytime = daytime;
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

    public String getOperatorid() {
        return operatorid;
    }

    public void setOperatorid(String operatorid) {
        this.operatorid = operatorid == null ? null : operatorid.trim();
    }

    public String getOperatorname() {
        return operatorname;
    }

    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname == null ? null : operatorname.trim();
    }

    public Date getOperatortime() {
        return operatortime;
    }

    public void setOperatortime(Date operatortime) {
        this.operatortime = operatortime;
    }
}