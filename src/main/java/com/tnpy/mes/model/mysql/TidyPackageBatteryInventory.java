package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class TidyPackageBatteryInventory {
    private String id;

    private String plantid;

    private String lineid;

    private String materialid;

    private String materialname;

    private Integer currenttotalnum;

    private Integer ontidyingnum;

    private Integer backchargenum;

    private Integer piletotalnum;

    private Integer pulloffnum;

    private Integer repairbacknum;

    private Integer repairnewnum;

    private Integer backchargenewnum;

    private Integer pipenewnum;

    private Integer putonnum;

    private Integer packagenewnum;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date checktime;

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

    public Integer getCurrenttotalnum() {
        return currenttotalnum;
    }

    public void setCurrenttotalnum(Integer currenttotalnum) {
        this.currenttotalnum = currenttotalnum;
    }

    public Integer getOntidyingnum() {
        return ontidyingnum;
    }

    public void setOntidyingnum(Integer ontidyingnum) {
        this.ontidyingnum = ontidyingnum;
    }

    public Integer getBackchargenum() {
        return backchargenum;
    }

    public void setBackchargenum(Integer backchargenum) {
        this.backchargenum = backchargenum;
    }

    public Integer getPiletotalnum() {
        return piletotalnum;
    }

    public void setPiletotalnum(Integer piletotalnum) {
        this.piletotalnum = piletotalnum;
    }

    public Integer getPulloffnum() {
        return pulloffnum;
    }

    public void setPulloffnum(Integer pulloffnum) {
        this.pulloffnum = pulloffnum;
    }

    public Integer getRepairbacknum() {
        return repairbacknum;
    }

    public void setRepairbacknum(Integer repairbacknum) {
        this.repairbacknum = repairbacknum;
    }

    public Integer getRepairnewnum() {
        return repairnewnum;
    }

    public void setRepairnewnum(Integer repairnewnum) {
        this.repairnewnum = repairnewnum;
    }

    public Integer getBackchargenewnum() {
        return backchargenewnum;
    }

    public void setBackchargenewnum(Integer backchargenewnum) {
        this.backchargenewnum = backchargenewnum;
    }

    public Integer getPipenewnum() {
        return pipenewnum;
    }

    public void setPipenewnum(Integer pipenewnum) {
        this.pipenewnum = pipenewnum;
    }

    public Integer getPutonnum() {
        return putonnum;
    }

    public void setPutonnum(Integer putonnum) {
        this.putonnum = putonnum;
    }

    public Integer getPackagenewnum() {
        return packagenewnum;
    }

    public void setPackagenewnum(Integer packagenewnum) {
        this.packagenewnum = packagenewnum;
    }

    public Date getChecktime() {
        return checktime;
    }

    public void setChecktime(Date checktime) {
        this.checktime = checktime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}