package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class PlateWeighRecord {
    private String id;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date time;

    private String balanceid;

    private String lineid;

    private String materialname;

    private String classname;

    private String operator;

    private Float weight;

    private Float centerweight;

    private String remark;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getBalanceid() {
        return balanceid;
    }

    public void setBalanceid(String balanceid) {
        this.balanceid = balanceid == null ? null : balanceid.trim();
    }

    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid == null ? null : lineid.trim();
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname == null ? null : materialname.trim();
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname == null ? null : classname.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getCenterweight() {
        return centerweight;
    }

    public void setCenterweight(Float centerweight) {
        this.centerweight = centerweight;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}