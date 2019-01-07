package com.tnpy.mes.model.customize;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/7 8:20
 */
public class CustomMaterialRecord {
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

    private String inOrderName;

    private String inSubOrderName;

    private String outOrderName;

    private String materialName;

    public String getInOrderName() {
        return inOrderName;
    }

    public void setInOrderName(String inOrderName) {
        this.inOrderName = inOrderName;
    }

    public String getInSubOrderName() {
        return inSubOrderName;
    }

    public void setInSubOrderName(String inSubOrderName) {
        this.inSubOrderName = inSubOrderName;
    }

    public String getOutOrderName() {
        return outOrderName;
    }

    public void setOutOrderName(String outOrderName) {
        this.outOrderName = outOrderName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }




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
}
