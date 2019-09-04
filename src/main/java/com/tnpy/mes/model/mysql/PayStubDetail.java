package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class PayStubDetail {
    private String id;

    private String plantid;

    private String processid;

    private String staffid;

    private String staffname;

    private Float finalwage;

    private Float productionwage;

    private Float rewardingwage;

    private Float punishmentwage;

    private Float extdwage1;

    private Float extdwage2;

    private Float extdwage3;

    private Float extdwage4;

    private String extdwage5;

    private String extdwage6;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date closingdate;

    private String updaterid;

    private String updatername;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

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

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid == null ? null : processid.trim();
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

    public Float getFinalwage() {
        return finalwage;
    }

    public void setFinalwage(Float finalwage) {
        this.finalwage = finalwage;
    }

    public Float getProductionwage() {
        return productionwage;
    }

    public void setProductionwage(Float productionwage) {
        this.productionwage = productionwage;
    }

    public Float getRewardingwage() {
        return rewardingwage;
    }

    public void setRewardingwage(Float rewardingwage) {
        this.rewardingwage = rewardingwage;
    }

    public Float getPunishmentwage() {
        return punishmentwage;
    }

    public void setPunishmentwage(Float punishmentwage) {
        this.punishmentwage = punishmentwage;
    }

    public Float getExtdwage1() {
        return extdwage1;
    }

    public void setExtdwage1(Float extdwage1) {
        this.extdwage1 = extdwage1;
    }

    public Float getExtdwage2() {
        return extdwage2;
    }

    public void setExtdwage2(Float extdwage2) {
        this.extdwage2 = extdwage2;
    }

    public Float getExtdwage3() {
        return extdwage3;
    }

    public void setExtdwage3(Float extdwage3) {
        this.extdwage3 = extdwage3;
    }

    public Float getExtdwage4() {
        return extdwage4;
    }

    public void setExtdwage4(Float extdwage4) {
        this.extdwage4 = extdwage4;
    }

    public String getExtdwage5() {
        return extdwage5;
    }

    public void setExtdwage5(String extdwage5) {
        this.extdwage5 = extdwage5 == null ? null : extdwage5.trim();
    }

    public String getExtdwage6() {
        return extdwage6;
    }

    public void setExtdwage6(String extdwage6) {
        this.extdwage6 = extdwage6 == null ? null : extdwage6.trim();
    }

    public Date getClosingdate() {
        return closingdate;
    }

    public void setClosingdate(Date closingdate) {
        this.closingdate = closingdate;
    }

    public String getUpdaterid() {
        return updaterid;
    }

    public void setUpdaterid(String updaterid) {
        this.updaterid = updaterid == null ? null : updaterid.trim();
    }

    public String getUpdatername() {
        return updatername;
    }

    public void setUpdatername(String updatername) {
        this.updatername = updatername == null ? null : updatername.trim();
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
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