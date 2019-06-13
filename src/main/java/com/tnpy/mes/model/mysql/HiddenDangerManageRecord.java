package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class HiddenDangerManageRecord {
    private String id;

    private String plantid;

    private String areaid;

    private String equipmentid;

    private String dangerlevel;

    private String equipmenttype;

    private String hiddendanger;

    private String hiddendangertype;

    private String hiddendangerpicture;

    private String reporter;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date reporttime;

    private String dealinfo;

    private String dealstaff;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date dealtime;

    private String dealpicture;

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

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid == null ? null : areaid.trim();
    }

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid == null ? null : equipmentid.trim();
    }

    public String getDangerlevel() {
        return dangerlevel;
    }

    public void setDangerlevel(String dangerlevel) {
        this.dangerlevel = dangerlevel == null ? null : dangerlevel.trim();
    }

    public String getEquipmenttype() {
        return equipmenttype;
    }

    public void setEquipmenttype(String equipmenttype) {
        this.equipmenttype = equipmenttype == null ? null : equipmenttype.trim();
    }

    public String getHiddendanger() {
        return hiddendanger;
    }

    public void setHiddendanger(String hiddendanger) {
        this.hiddendanger = hiddendanger == null ? null : hiddendanger.trim();
    }

    public String getHiddendangertype() {
        return hiddendangertype;
    }

    public void setHiddendangertype(String hiddendangertype) {
        this.hiddendangertype = hiddendangertype == null ? null : hiddendangertype.trim();
    }

    public String getHiddendangerpicture() {
        return hiddendangerpicture;
    }

    public void setHiddendangerpicture(String hiddendangerpicture) {
        this.hiddendangerpicture = hiddendangerpicture == null ? null : hiddendangerpicture.trim();
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter == null ? null : reporter.trim();
    }

    public Date getReporttime() {
        return reporttime;
    }

    public void setReporttime(Date reporttime) {
        this.reporttime = reporttime;
    }

    public String getDealinfo() {
        return dealinfo;
    }

    public void setDealinfo(String dealinfo) {
        this.dealinfo = dealinfo == null ? null : dealinfo.trim();
    }

    public String getDealstaff() {
        return dealstaff;
    }

    public void setDealstaff(String dealstaff) {
        this.dealstaff = dealstaff == null ? null : dealstaff.trim();
    }

    public Date getDealtime() {
        return dealtime;
    }

    public void setDealtime(Date dealtime) {
        this.dealtime = dealtime;
    }

    public String getDealpicture() {
        return dealpicture;
    }

    public void setDealpicture(String dealpicture) {
        this.dealpicture = dealpicture == null ? null : dealpicture.trim();
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