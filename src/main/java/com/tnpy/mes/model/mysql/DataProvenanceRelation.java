package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class DataProvenanceRelation {
    private String id;

    private String batchstring;

    private Double leftnumber;

    private String inorderid;

    private String insuborderid;

    private String inputername;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date inputtime;

    private String inputmaterialid;

    private String inputmaterialname;

    private Double productionnum;

    private String inplantid;

    private String inprocessid;

    private String inlineid;

    private String inworklocationid;

    private String outorderid;

    private String outsuborderid;

    private Double outputnumber;

    private String outputername;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date outputtime;

    private String outplantid;

    private String outprocessid;

    private String outlineid;

    private String outworklocationid;

    private String status;

    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBatchstring() {
        return batchstring;
    }

    public void setBatchstring(String batchstring) {
        this.batchstring = batchstring == null ? null : batchstring.trim();
    }

    public Double getLeftnumber() {
        return leftnumber;
    }

    public void setLeftnumber(Double leftnumber) {
        this.leftnumber = leftnumber;
    }

    public String getInorderid() {
        return inorderid;
    }

    public void setInorderid(String inorderid) {
        this.inorderid = inorderid == null ? null : inorderid.trim();
    }

    public String getInsuborderid() {
        return insuborderid;
    }

    public void setInsuborderid(String insuborderid) {
        this.insuborderid = insuborderid == null ? null : insuborderid.trim();
    }

    public String getInputername() {
        return inputername;
    }

    public void setInputername(String inputername) {
        this.inputername = inputername == null ? null : inputername.trim();
    }

    public Date getInputtime() {
        return inputtime;
    }

    public void setInputtime(Date inputtime) {
        this.inputtime = inputtime;
    }

    public String getInputmaterialid() {
        return inputmaterialid;
    }

    public void setInputmaterialid(String inputmaterialid) {
        this.inputmaterialid = inputmaterialid == null ? null : inputmaterialid.trim();
    }

    public String getInputmaterialname() {
        return inputmaterialname;
    }

    public void setInputmaterialname(String inputmaterialname) {
        this.inputmaterialname = inputmaterialname == null ? null : inputmaterialname.trim();
    }

    public Double getProductionnum() {
        return productionnum;
    }

    public void setProductionnum(Double productionnum) {
        this.productionnum = productionnum;
    }

    public String getInplantid() {
        return inplantid;
    }

    public void setInplantid(String inplantid) {
        this.inplantid = inplantid == null ? null : inplantid.trim();
    }

    public String getInprocessid() {
        return inprocessid;
    }

    public void setInprocessid(String inprocessid) {
        this.inprocessid = inprocessid == null ? null : inprocessid.trim();
    }

    public String getInlineid() {
        return inlineid;
    }

    public void setInlineid(String inlineid) {
        this.inlineid = inlineid == null ? null : inlineid.trim();
    }

    public String getInworklocationid() {
        return inworklocationid;
    }

    public void setInworklocationid(String inworklocationid) {
        this.inworklocationid = inworklocationid == null ? null : inworklocationid.trim();
    }

    public String getOutorderid() {
        return outorderid;
    }

    public void setOutorderid(String outorderid) {
        this.outorderid = outorderid == null ? null : outorderid.trim();
    }

    public String getOutsuborderid() {
        return outsuborderid;
    }

    public void setOutsuborderid(String outsuborderid) {
        this.outsuborderid = outsuborderid == null ? null : outsuborderid.trim();
    }

    public Double getOutputnumber() {
        return outputnumber;
    }

    public void setOutputnumber(Double outputnumber) {
        this.outputnumber = outputnumber;
    }

    public String getOutputername() {
        return outputername;
    }

    public void setOutputername(String outputername) {
        this.outputername = outputername == null ? null : outputername.trim();
    }

    public Date getOutputtime() {
        return outputtime;
    }

    public void setOutputtime(Date outputtime) {
        this.outputtime = outputtime;
    }

    public String getOutplantid() {
        return outplantid;
    }

    public void setOutplantid(String outplantid) {
        this.outplantid = outplantid == null ? null : outplantid.trim();
    }

    public String getOutprocessid() {
        return outprocessid;
    }

    public void setOutprocessid(String outprocessid) {
        this.outprocessid = outprocessid == null ? null : outprocessid.trim();
    }

    public String getOutlineid() {
        return outlineid;
    }

    public void setOutlineid(String outlineid) {
        this.outlineid = outlineid == null ? null : outlineid.trim();
    }

    public String getOutworklocationid() {
        return outworklocationid;
    }

    public void setOutworklocationid(String outworklocationid) {
        this.outworklocationid = outworklocationid == null ? null : outworklocationid.trim();
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