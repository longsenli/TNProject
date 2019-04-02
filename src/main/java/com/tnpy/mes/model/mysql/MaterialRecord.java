package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class MaterialRecord {
    private String id;

    private String materialid;

    private String orderid;

    private String suborderid;

    private String expendorderid;

    private Integer inorout;

    private Double number;

    private Integer status;

    private String inputer;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date inputtime;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date outputtime;

    private String outputer;

    private String inputplantid;

    private String inputprocessid;

    private String inputlineid;

    private String inputworklocationid;

    private String outputplantid;

    private String outputprocessid;

    private String outputlineid;

    private String outputworklocationid;

    private String inputerid;

    private String outputerid;

    private String materialnameinfo;

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

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public String getSuborderid() {
        return suborderid;
    }

    public void setSuborderid(String suborderid) {
        this.suborderid = suborderid == null ? null : suborderid.trim();
    }

    public String getExpendorderid() {
        return expendorderid;
    }

    public void setExpendorderid(String expendorderid) {
        this.expendorderid = expendorderid == null ? null : expendorderid.trim();
    }

    public Integer getInorout() {
        return inorout;
    }

    public void setInorout(Integer inorout) {
        this.inorout = inorout;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInputer() {
        return inputer;
    }

    public void setInputer(String inputer) {
        this.inputer = inputer == null ? null : inputer.trim();
    }

    public Date getInputtime() {
        return inputtime;
    }

    public void setInputtime(Date inputtime) {
        this.inputtime = inputtime;
    }

    public Date getOutputtime() {
        return outputtime;
    }

    public void setOutputtime(Date outputtime) {
        this.outputtime = outputtime;
    }

    public String getOutputer() {
        return outputer;
    }

    public void setOutputer(String outputer) {
        this.outputer = outputer == null ? null : outputer.trim();
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

    public String getInputworklocationid() {
        return inputworklocationid;
    }

    public void setInputworklocationid(String inputworklocationid) {
        this.inputworklocationid = inputworklocationid == null ? null : inputworklocationid.trim();
    }

    public String getOutputplantid() {
        return outputplantid;
    }

    public void setOutputplantid(String outputplantid) {
        this.outputplantid = outputplantid == null ? null : outputplantid.trim();
    }

    public String getOutputprocessid() {
        return outputprocessid;
    }

    public void setOutputprocessid(String outputprocessid) {
        this.outputprocessid = outputprocessid == null ? null : outputprocessid.trim();
    }

    public String getOutputlineid() {
        return outputlineid;
    }

    public void setOutputlineid(String outputlineid) {
        this.outputlineid = outputlineid == null ? null : outputlineid.trim();
    }

    public String getOutputworklocationid() {
        return outputworklocationid;
    }

    public void setOutputworklocationid(String outputworklocationid) {
        this.outputworklocationid = outputworklocationid == null ? null : outputworklocationid.trim();
    }

    public String getInputerid() {
        return inputerid;
    }

    public void setInputerid(String inputerid) {
        this.inputerid = inputerid == null ? null : inputerid.trim();
    }

    public String getOutputerid() {
        return outputerid;
    }

    public void setOutputerid(String outputerid) {
        this.outputerid = outputerid == null ? null : outputerid.trim();
    }

    public String getMaterialnameinfo() {
        return materialnameinfo;
    }

    public void setMaterialnameinfo(String materialnameinfo) {
        this.materialnameinfo = materialnameinfo == null ? null : materialnameinfo.trim();
    }
}