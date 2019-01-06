package com.tnpy.mes.model.customize;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/6 14:42
 */
public class CustomWorkOrderRecord {
    private String id;

    private String orderid;

    private String plantid;

    private String processid;

    private String lineid;

    private String status;

    private String units;

    private Integer batchnum;

    private Integer totalproduction;

    private Double scrapnum;

    private String materialid;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date scheduledstarttime;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date scheduledendtime;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date actualstarttime;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date actualendtime;

    private String openstaff;

    private String finishstaff;

    private String closestaff;

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    private String lineName;

    private String statusName;

    private String materialName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units == null ? null : units.trim();
    }

    public Integer getBatchnum() {
        return batchnum;
    }

    public void setBatchnum(Integer batchnum) {
        this.batchnum = batchnum;
    }

    public Integer getTotalproduction() {
        return totalproduction;
    }

    public void setTotalproduction(Integer totalproduction) {
        this.totalproduction = totalproduction;
    }

    public Double getScrapnum() {
        return scrapnum;
    }

    public void setScrapnum(Double scrapnum) {
        this.scrapnum = scrapnum;
    }

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getScheduledstarttime() {
        return scheduledstarttime;
    }

    public void setScheduledstarttime(Date scheduledstarttime) {
        this.scheduledstarttime = scheduledstarttime;
    }

    public Date getScheduledendtime() {
        return scheduledendtime;
    }

    public void setScheduledendtime(Date scheduledendtime) {
        this.scheduledendtime = scheduledendtime;
    }

    public Date getActualstarttime() {
        return actualstarttime;
    }

    public void setActualstarttime(Date actualstarttime) {
        this.actualstarttime = actualstarttime;
    }

    public Date getActualendtime() {
        return actualendtime;
    }

    public void setActualendtime(Date actualendtime) {
        this.actualendtime = actualendtime;
    }

    public String getOpenstaff() {
        return openstaff;
    }

    public void setOpenstaff(String openstaff) {
        this.openstaff = openstaff == null ? null : openstaff.trim();
    }

    public String getFinishstaff() {
        return finishstaff;
    }

    public void setFinishstaff(String finishstaff) {
        this.finishstaff = finishstaff == null ? null : finishstaff.trim();
    }

    public String getClosestaff() {
        return closestaff;
    }

    public void setClosestaff(String closestaff) {
        this.closestaff = closestaff == null ? null : closestaff.trim();
    }
}
