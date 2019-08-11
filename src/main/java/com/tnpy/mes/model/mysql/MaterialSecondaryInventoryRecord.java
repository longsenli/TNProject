package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class MaterialSecondaryInventoryRecord {
    private String id;

    private String materialid;

    private String plantid;

    private String processid;

    private Integer currentnum;

    private Integer laststorage;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    private Integer gainnum;

    private Integer innum;

    private Integer expendnum;

    private Integer outnum;

    private String operator;

    private String status;

    private Integer onlinenum;

    private Integer todayrepair;

    private String remark;

    private Integer extend1;

    private Integer extend2;

    private Integer extend3;

    private Integer extend4;

    private String extend5;

    private String extend6;

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

    public Integer getGainnum() {
        return gainnum;
    }

    public void setGainnum(Integer gainnum) {
        this.gainnum = gainnum;
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

    public Integer getOnlinenum() {
        return onlinenum;
    }

    public void setOnlinenum(Integer onlinenum) {
        this.onlinenum = onlinenum;
    }

    public Integer getTodayrepair() {
        return todayrepair;
    }

    public void setTodayrepair(Integer todayrepair) {
        this.todayrepair = todayrepair;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getExtend1() {
        return extend1;
    }

    public void setExtend1(Integer extend1) {
        this.extend1 = extend1;
    }

    public Integer getExtend2() {
        return extend2;
    }

    public void setExtend2(Integer extend2) {
        this.extend2 = extend2;
    }

    public Integer getExtend3() {
        return extend3;
    }

    public void setExtend3(Integer extend3) {
        this.extend3 = extend3;
    }

    public Integer getExtend4() {
        return extend4;
    }

    public void setExtend4(Integer extend4) {
        this.extend4 = extend4;
    }

    public String getExtend5() {
        return extend5;
    }

    public void setExtend5(String extend5) {
        this.extend5 = extend5 == null ? null : extend5.trim();
    }

    public String getExtend6() {
        return extend6;
    }

    public void setExtend6(String extend6) {
        this.extend6 = extend6 == null ? null : extend6.trim();
    }
}