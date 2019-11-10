package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class PlasticUsedRecord {
    private String id;

    private String plantid;

    private String lineid;

    private String worklocation;

    private String staffid;

    private String staffname;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date usedtime;

    private String jqid;

    private String jqstaff;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date jqtime;

    private String status;

    private String usedorderid;

    private String extend1;

    private String extend2;

    private String extend3;

    private String materialid;

    private String materialname;

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

    public Date getUsedtime() {
        return usedtime;
    }

    public void setUsedtime(Date usedtime) {
        this.usedtime = usedtime;
    }

    public String getJqid() {
        return jqid;
    }

    public void setJqid(String jqid) {
        this.jqid = jqid == null ? null : jqid.trim();
    }

    public String getJqstaff() {
        return jqstaff;
    }

    public void setJqstaff(String jqstaff) {
        this.jqstaff = jqstaff == null ? null : jqstaff.trim();
    }

    public Date getJqtime() {
        return jqtime;
    }

    public void setJqtime(Date jqtime) {
        this.jqtime = jqtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getUsedorderid() {
        return usedorderid;
    }

    public void setUsedorderid(String usedorderid) {
        this.usedorderid = usedorderid == null ? null : usedorderid.trim();
    }

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1 == null ? null : extend1.trim();
    }

    public String getExtend2() {
        return extend2;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2 == null ? null : extend2.trim();
    }

    public String getExtend3() {
        return extend3;
    }

    public void setExtend3(String extend3) {
        this.extend3 = extend3 == null ? null : extend3.trim();
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
}