package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class WorkShiftRecord {
    private String id;

    private String plantid;

    private String workshift;

    private String daynight;

    private String abshift;

    private String processid;

    private String lineid;

    private String staffid;

    private String staffname;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date starttime;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date endtime;

    private Double timemeasure;

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

    public String getWorkshift() {
        return workshift;
    }

    public void setWorkshift(String workshift) {
        this.workshift = workshift == null ? null : workshift.trim();
    }

    public String getDaynight() {
        return daynight;
    }

    public void setDaynight(String daynight) {
        this.daynight = daynight == null ? null : daynight.trim();
    }

    public String getAbshift() {
        return abshift;
    }

    public void setAbshift(String abshift) {
        this.abshift = abshift == null ? null : abshift.trim();
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

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Double getTimemeasure() {
        return timemeasure;
    }

    public void setTimemeasure(Double timemeasure) {
        this.timemeasure = timemeasure;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}