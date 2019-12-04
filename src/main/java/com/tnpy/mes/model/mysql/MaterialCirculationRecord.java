package com.tnpy.mes.model.mysql;

import java.util.Date;

public class MaterialCirculationRecord {
    private String id;

    private String originalplantid;

    private String processid;

    private String destinationplantid;

    private String materialid;

    private String materialname;

    private Integer number;

    private String sender;

    private Date sendtime;

    private String accepter;

    private Date accepttime;

    private String circulationtype;

    private String circulationdescription;

    private String extd1;

    private String extd2;

    private String extd3;

    private String remark;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOriginalplantid() {
        return originalplantid;
    }

    public void setOriginalplantid(String originalplantid) {
        this.originalplantid = originalplantid == null ? null : originalplantid.trim();
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid == null ? null : processid.trim();
    }

    public String getDestinationplantid() {
        return destinationplantid;
    }

    public void setDestinationplantid(String destinationplantid) {
        this.destinationplantid = destinationplantid == null ? null : destinationplantid.trim();
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender == null ? null : sender.trim();
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public String getAccepter() {
        return accepter;
    }

    public void setAccepter(String accepter) {
        this.accepter = accepter == null ? null : accepter.trim();
    }

    public Date getAccepttime() {
        return accepttime;
    }

    public void setAccepttime(Date accepttime) {
        this.accepttime = accepttime;
    }

    public String getCirculationtype() {
        return circulationtype;
    }

    public void setCirculationtype(String circulationtype) {
        this.circulationtype = circulationtype == null ? null : circulationtype.trim();
    }

    public String getCirculationdescription() {
        return circulationdescription;
    }

    public void setCirculationdescription(String circulationdescription) {
        this.circulationdescription = circulationdescription == null ? null : circulationdescription.trim();
    }

    public String getExtd1() {
        return extd1;
    }

    public void setExtd1(String extd1) {
        this.extd1 = extd1 == null ? null : extd1.trim();
    }

    public String getExtd2() {
        return extd2;
    }

    public void setExtd2(String extd2) {
        this.extd2 = extd2 == null ? null : extd2.trim();
    }

    public String getExtd3() {
        return extd3;
    }

    public void setExtd3(String extd3) {
        this.extd3 = extd3 == null ? null : extd3.trim();
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