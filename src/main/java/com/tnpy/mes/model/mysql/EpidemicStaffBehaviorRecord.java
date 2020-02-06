package com.tnpy.mes.model.mysql;

import java.util.Date;

public class EpidemicStaffBehaviorRecord {
    private String id;

    private String name;

    private String identityid;

    private String department;

    private Date daytime;

    private String staylocation;

    private String contactseverity;

    private String abnormalshelf;

    private String abnormalpartner;

    private String quarantine;

    private String remark;

    private String status;

    private String extd1;

    private String extd2;

    private String extd3;

    private String processorjob;

    private Date updatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIdentityid() {
        return identityid;
    }

    public void setIdentityid(String identityid) {
        this.identityid = identityid == null ? null : identityid.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public Date getDaytime() {
        return daytime;
    }

    public void setDaytime(Date daytime) {
        this.daytime = daytime;
    }

    public String getStaylocation() {
        return staylocation;
    }

    public void setStaylocation(String staylocation) {
        this.staylocation = staylocation == null ? null : staylocation.trim();
    }

    public String getContactseverity() {
        return contactseverity;
    }

    public void setContactseverity(String contactseverity) {
        this.contactseverity = contactseverity == null ? null : contactseverity.trim();
    }

    public String getAbnormalshelf() {
        return abnormalshelf;
    }

    public void setAbnormalshelf(String abnormalshelf) {
        this.abnormalshelf = abnormalshelf == null ? null : abnormalshelf.trim();
    }

    public String getAbnormalpartner() {
        return abnormalpartner;
    }

    public void setAbnormalpartner(String abnormalpartner) {
        this.abnormalpartner = abnormalpartner == null ? null : abnormalpartner.trim();
    }

    public String getQuarantine() {
        return quarantine;
    }

    public void setQuarantine(String quarantine) {
        this.quarantine = quarantine == null ? null : quarantine.trim();
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

    public String getProcessorjob() {
        return processorjob;
    }

    public void setProcessorjob(String processorjob) {
        this.processorjob = processorjob == null ? null : processorjob.trim();
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}