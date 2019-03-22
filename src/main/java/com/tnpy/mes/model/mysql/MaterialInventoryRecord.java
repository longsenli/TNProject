package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class MaterialInventoryRecord {
    private String id;

    private String materialid;

    private String plantid;

    private String processid;

    private Integer currentnum;

    private Integer laststorage;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    private Integer productionnum;

    private Integer innum;

    private Integer expendnum;

    private Integer outnum;

    private String operator;

    private String status;

    public Integer getOnlineStorageInNum() {
        return onlineStorageInNum;
    }

    public void setOnlineStorageInNum(Integer onlineStorageInNum) {
        this.onlineStorageInNum = onlineStorageInNum;
    }

    private Integer onlineStorageInNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
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

    public Integer getCurrentnum() {
        return currentnum;
    }

    public void setCurrentnum(Integer currentnum) {
        this.currentnum = currentnum;
    }

    public Integer getLaststorage() {
        return laststorage;
    }

    public void setLaststorage(Integer laststorage) {
        this.laststorage = laststorage;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getProductionnum() {
        return productionnum;
    }

    public void setProductionnum(Integer productionnum) {
        this.productionnum = productionnum;
    }

    public Integer getInnum() {
        return innum;
    }

    public void setInnum(Integer innum) {
        this.innum = innum;
    }

    public Integer getExpendnum() {
        return expendnum;
    }

    public void setExpendnum(Integer expendnum) {
        this.expendnum = expendnum;
    }

    public Integer getOutnum() {
        return outnum;
    }

    public void setOutnum(Integer outnum) {
        this.outnum = outnum;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}