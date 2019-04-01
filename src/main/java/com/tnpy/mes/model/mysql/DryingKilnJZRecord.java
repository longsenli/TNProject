package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class DryingKilnJZRecord {
    private String id;

    private String dryingkilnid;

    private String dryingkilnname;

    private String plantid;

    private String lineid;

    private String worklocationid;

    private String worklocationname;

    private String suborderid;

    private String materialid;

    private String materialname;

    private Integer materialquantity;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date inputtime;

    private String inputerid;

    private String inputername;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date outputtime;

    private String outputerid;

    private String outputername;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDryingkilnid() {
        return dryingkilnid;
    }

    public void setDryingkilnid(String dryingkilnid) {
        this.dryingkilnid = dryingkilnid == null ? null : dryingkilnid.trim();
    }

    public String getDryingkilnname() {
        return dryingkilnname;
    }

    public void setDryingkilnname(String dryingkilnname) {
        this.dryingkilnname = dryingkilnname == null ? null : dryingkilnname.trim();
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

    public String getWorklocationid() {
        return worklocationid;
    }

    public void setWorklocationid(String worklocationid) {
        this.worklocationid = worklocationid == null ? null : worklocationid.trim();
    }

    public String getWorklocationname() {
        return worklocationname;
    }

    public void setWorklocationname(String worklocationname) {
        this.worklocationname = worklocationname == null ? null : worklocationname.trim();
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

    public Integer getMaterialquantity() {
        return materialquantity;
    }

    public void setMaterialquantity(Integer materialquantity) {
        this.materialquantity = materialquantity;
    }

    public Date getInputtime() {
        return inputtime;
    }

    public void setInputtime(Date inputtime) {
        this.inputtime = inputtime;
    }

    public String getInputerid() {
        return inputerid;
    }

    public void setInputerid(String inputerid) {
        this.inputerid = inputerid == null ? null : inputerid.trim();
    }

    public String getInputername() {
        return inputername;
    }

    public void setInputername(String inputername) {
        this.inputername = inputername == null ? null : inputername.trim();
    }

    public Date getOutputtime() {
        return outputtime;
    }

    public void setOutputtime(Date outputtime) {
        this.outputtime = outputtime;
    }

    public String getOutputerid() {
        return outputerid;
    }

    public void setOutputerid(String outputerid) {
        this.outputerid = outputerid == null ? null : outputerid.trim();
    }

    public String getOutputername() {
        return outputername;
    }

    public void setOutputername(String outputername) {
        this.outputername = outputername == null ? null : outputername.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}