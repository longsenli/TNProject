package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class UnqualifiedMaterialReturn {
    private String id;

    private String plantid;

    private String processid;

    private String lineid;

    private String materialrecordid;

    private String suborderid;

    private String materialid;

    private String materialname;

    private String inputer;

    private String inputerid;

    private String returner;

    private String returnerid;

    private String inputplantid;

    private String inputprocessid;

    private String inputlineid;

    private Float materialnumber;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date returntime;

    private String status;

    private Float repairnumber;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date repairtime;

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

    public String getMaterialrecordid() {
        return materialrecordid;
    }

    public void setMaterialrecordid(String materialrecordid) {
        this.materialrecordid = materialrecordid == null ? null : materialrecordid.trim();
    }

    public String getSuborderid() {
        return suborderid;
    }

    public void setSuborderid(String suborderid) {
        this.suborderid = suborderid == null ? null : suborderid.trim();
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

    public String getInputer() {
        return inputer;
    }

    public void setInputer(String inputer) {
        this.inputer = inputer == null ? null : inputer.trim();
    }

    public String getInputerid() {
        return inputerid;
    }

    public void setInputerid(String inputerid) {
        this.inputerid = inputerid == null ? null : inputerid.trim();
    }

    public String getReturner() {
        return returner;
    }

    public void setReturner(String returner) {
        this.returner = returner == null ? null : returner.trim();
    }

    public String getReturnerid() {
        return returnerid;
    }

    public void setReturnerid(String returnerid) {
        this.returnerid = returnerid == null ? null : returnerid.trim();
    }

    public String getInputplantid() {
        return inputplantid;
    }

    public void setInputplantid(String inputplantid) {
        this.inputplantid = inputplantid == null ? null : inputplantid.trim();
    }

    public String getInputprocessid() {
        return inputprocessid;
    }

    public void setInputprocessid(String inputprocessid) {
        this.inputprocessid = inputprocessid == null ? null : inputprocessid.trim();
    }

    public String getInputlineid() {
        return inputlineid;
    }

    public void setInputlineid(String inputlineid) {
        this.inputlineid = inputlineid == null ? null : inputlineid.trim();
    }

    public Float getMaterialnumber() {
        return materialnumber;
    }

    public void setMaterialnumber(Float materialnumber) {
        this.materialnumber = materialnumber;
    }

    public Date getReturntime() {
        return returntime;
    }

    public void setReturntime(Date returntime) {
        this.returntime = returntime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Float getRepairnumber() {
        return repairnumber;
    }

    public void setRepairnumber(Float repairnumber) {
        this.repairnumber = repairnumber;
    }

    public Date getRepairtime() {
        return repairtime;
    }

    public void setRepairtime(Date repairtime) {
        this.repairtime = repairtime;
    }
}