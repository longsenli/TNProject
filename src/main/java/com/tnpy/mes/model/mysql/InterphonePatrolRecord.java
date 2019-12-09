package com.tnpy.mes.model.mysql;

import java.util.Date;

public class InterphonePatrolRecord {
    private String id;

    private String plantid;

    private String processid;

    private String patrollocationid;

    private String patrollocationname;

    private String daytime;

    private Integer ordinal;

    private String onlineflag;

    private String content;

    private String status;

    private Date patroltime;

    private String patrolstaff;

    private String patrolstaffid;

    private String extd1;

    private String extd2;

    private String extd3;

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

    public String getPatrollocationid() {
        return patrollocationid;
    }

    public void setPatrollocationid(String patrollocationid) {
        this.patrollocationid = patrollocationid == null ? null : patrollocationid.trim();
    }

    public String getPatrollocationname() {
        return patrollocationname;
    }

    public void setPatrollocationname(String patrollocationname) {
        this.patrollocationname = patrollocationname == null ? null : patrollocationname.trim();
    }

    public String getDaytime() {
        return daytime;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime == null ? null : daytime.trim();
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getOnlineflag() {
        return onlineflag;
    }

    public void setOnlineflag(String onlineflag) {
        this.onlineflag = onlineflag == null ? null : onlineflag.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getPatroltime() {
        return patroltime;
    }

    public void setPatroltime(Date patroltime) {
        this.patroltime = patroltime;
    }

    public String getPatrolstaff() {
        return patrolstaff;
    }

    public void setPatrolstaff(String patrolstaff) {
        this.patrolstaff = patrolstaff == null ? null : patrolstaff.trim();
    }

    public String getPatrolstaffid() {
        return patrolstaffid;
    }

    public void setPatrolstaffid(String patrolstaffid) {
        this.patrolstaffid = patrolstaffid == null ? null : patrolstaffid.trim();
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
}