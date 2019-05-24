package com.tnpy.mes.model.mysql;

import java.util.Date;

public class BatteryGearMarkRecord {
    private String id;

    private String plantid;

    private String lineid;

    private String locationid;

    private String altitude;

    private String groupid;

    private Integer sequencenumbers;

    private Double realvoltage;

    private Integer voltagegroup;

    private Integer dischargetime;

    private Integer dischargetimegroup;

    private Integer packagegroup;

    private Date daytime;

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

    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid == null ? null : lineid.trim();
    }

    public String getLocationid() {
        return locationid;
    }

    public void setLocationid(String locationid) {
        this.locationid = locationid == null ? null : locationid.trim();
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude == null ? null : altitude.trim();
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid == null ? null : groupid.trim();
    }

    public Integer getSequencenumbers() {
        return sequencenumbers;
    }

    public void setSequencenumbers(Integer sequencenumbers) {
        this.sequencenumbers = sequencenumbers;
    }

    public Double getRealvoltage() {
        return realvoltage;
    }

    public void setRealvoltage(Double realvoltage) {
        this.realvoltage = realvoltage;
    }

    public Integer getVoltagegroup() {
        return voltagegroup;
    }

    public void setVoltagegroup(Integer voltagegroup) {
        this.voltagegroup = voltagegroup;
    }

    public Integer getDischargetime() {
        return dischargetime;
    }

    public void setDischargetime(Integer dischargetime) {
        this.dischargetime = dischargetime;
    }

    public Integer getDischargetimegroup() {
        return dischargetimegroup;
    }

    public void setDischargetimegroup(Integer dischargetimegroup) {
        this.dischargetimegroup = dischargetimegroup;
    }

    public Integer getPackagegroup() {
        return packagegroup;
    }

    public void setPackagegroup(Integer packagegroup) {
        this.packagegroup = packagegroup;
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
}