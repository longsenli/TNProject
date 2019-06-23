package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class PileBatteryRecord {
    private String id;

    private String plantid;

    private String processid;

    private String lineid;

    private Float productionnumber;

    private String materialid;

    private String materialname;

    private String pilestaffid;

    private String pilestaffname;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date piletime;
    @JSONField(format ="yyyy-MM-dd")
    private Date batterydate;

    private String materialtype;

    private String location;

    private String status;

    private String remark;

    private String tidyrecordid;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date packagetime;

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

    public Float getProductionnumber() {
        return productionnumber;
    }

    public void setProductionnumber(Float productionnumber) {
        this.productionnumber = productionnumber;
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

    public String getPilestaffid() {
        return pilestaffid;
    }

    public void setPilestaffid(String pilestaffid) {
        this.pilestaffid = pilestaffid == null ? null : pilestaffid.trim();
    }

    public String getPilestaffname() {
        return pilestaffname;
    }

    public void setPilestaffname(String pilestaffname) {
        this.pilestaffname = pilestaffname == null ? null : pilestaffname.trim();
    }

    public Date getPiletime() {
        return piletime;
    }

    public void setPiletime(Date piletime) {
        this.piletime = piletime;
    }

    public Date getBatterydate() {
        return batterydate;
    }

    public void setBatterydate(Date batterydate) {
        this.batterydate = batterydate;
    }

    public String getMaterialtype() {
        return materialtype;
    }

    public void setMaterialtype(String materialtype) {
        this.materialtype = materialtype == null ? null : materialtype.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
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

    public String getTidyrecordid() {
        return tidyrecordid;
    }

    public void setTidyrecordid(String tidyrecordid) {
        this.tidyrecordid = tidyrecordid == null ? null : tidyrecordid.trim();
    }

    public Date getPackagetime() {
        return packagetime;
    }

    public void setPackagetime(Date packagetime) {
        this.packagetime = packagetime;
    }
}