package com.tnpy.mes.model.mysql;

import java.util.Date;

public class InterphonePatrolLocation {
    private String id;

    private String plantid;

    private String processid;

    private String status;

    private Integer ordinal;

    private String locationname;

    private Date createtime;

    private String createstaff;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getLocationname() {
        return locationname;
    }

    public void setLocationname(String locationname) {
        this.locationname = locationname == null ? null : locationname.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getCreatestaff() {
        return createstaff;
    }

    public void setCreatestaff(String createstaff) {
        this.createstaff = createstaff == null ? null : createstaff.trim();
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