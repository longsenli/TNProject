package com.tnpy.mes.model.mysql;

import java.util.Date;

public class StaffAttendanceDetail {
    private String id;

    private String plantid;

    private String processid;

    private String lineid;

    private String worklocationid;

    private String staffname;

    private String staffid;

    private String classtype1;

    private String classtype2;

    private Date daytime;

    private Date cometime;

    private Date gotime;

    private String verifierid;

    private String verifiername;

    private Date verifytime;

    private String extd1;

    private String extd2;

    private String extd3;

    private String remark;

    private String status;

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

    public String getWorklocationid() {
        return worklocationid;
    }

    public void setWorklocationid(String worklocationid) {
        this.worklocationid = worklocationid == null ? null : worklocationid.trim();
    }

    public String getStaffname() {
        return staffname;
    }

    public void setStaffname(String staffname) {
        this.staffname = staffname == null ? null : staffname.trim();
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid == null ? null : staffid.trim();
    }

    public String getClasstype1() {
        return classtype1;
    }

    public void setClasstype1(String classtype1) {
        this.classtype1 = classtype1 == null ? null : classtype1.trim();
    }

    public String getClasstype2() {
        return classtype2;
    }

    public void setClasstype2(String classtype2) {
        this.classtype2 = classtype2 == null ? null : classtype2.trim();
    }

    public Date getDaytime() {
        return daytime;
    }

    public void setDaytime(Date daytime) {
        this.daytime = daytime;
    }

    public Date getCometime() {
        return cometime;
    }

    public void setCometime(Date cometime) {
        this.cometime = cometime;
    }

    public Date getGotime() {
        return gotime;
    }

    public void setGotime(Date gotime) {
        this.gotime = gotime;
    }

    public String getVerifierid() {
        return verifierid;
    }

    public void setVerifierid(String verifierid) {
        this.verifierid = verifierid == null ? null : verifierid.trim();
    }

    public String getVerifiername() {
        return verifiername;
    }

    public void setVerifiername(String verifiername) {
        this.verifiername = verifiername == null ? null : verifiername.trim();
    }

    public Date getVerifytime() {
        return verifytime;
    }

    public void setVerifytime(Date verifytime) {
        this.verifytime = verifytime;
    }

    public String getExtd1() {
        return extd1;
    }

    public void setExtd1(String extd1) {
        this.extd1 = extd1 == null ? null : extd1.trim();
    }

    public String getExtd2() {
        return extd2;
    }

    public void setExtd2(String extd2) {
        this.extd2 = extd2 == null ? null : extd2.trim();
    }

    public String getExtd3() {
        return extd3;
    }

    public void setExtd3(String extd3) {
        this.extd3 = extd3 == null ? null : extd3.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}