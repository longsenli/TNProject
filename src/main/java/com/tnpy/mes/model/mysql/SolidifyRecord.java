package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class SolidifyRecord {
    private String id;

    private String orderid;

    private String ordersplitid;

    private String materialid;

    private String solidifyroomid;

    private String recorder1;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date starttime1;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date endtime1;

    private String recorder2;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date starttime2;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date endtime2;

    private String recorder3;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date starttime3;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date endtime3;

    private String status;

    private String ordersplitname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public String getOrdersplitid() {
        return ordersplitid;
    }

    public void setOrdersplitid(String ordersplitid) {
        this.ordersplitid = ordersplitid == null ? null : ordersplitid.trim();
    }

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
    }

    public String getSolidifyroomid() {
        return solidifyroomid;
    }

    public void setSolidifyroomid(String solidifyroomid) {
        this.solidifyroomid = solidifyroomid == null ? null : solidifyroomid.trim();
    }

    public String getRecorder1() {
        return recorder1;
    }

    public void setRecorder1(String recorder1) {
        this.recorder1 = recorder1 == null ? null : recorder1.trim();
    }

    public Date getStarttime1() {
        return starttime1;
    }

    public void setStarttime1(Date starttime1) {
        this.starttime1 = starttime1;
    }

    public Date getEndtime1() {
        return endtime1;
    }

    public void setEndtime1(Date endtime1) {
        this.endtime1 = endtime1;
    }

    public String getRecorder2() {
        return recorder2;
    }

    public void setRecorder2(String recorder2) {
        this.recorder2 = recorder2 == null ? null : recorder2.trim();
    }

    public Date getStarttime2() {
        return starttime2;
    }

    public void setStarttime2(Date starttime2) {
        this.starttime2 = starttime2;
    }

    public Date getEndtime2() {
        return endtime2;
    }

    public void setEndtime2(Date endtime2) {
        this.endtime2 = endtime2;
    }

    public String getRecorder3() {
        return recorder3;
    }

    public void setRecorder3(String recorder3) {
        this.recorder3 = recorder3 == null ? null : recorder3.trim();
    }

    public Date getStarttime3() {
        return starttime3;
    }

    public void setStarttime3(Date starttime3) {
        this.starttime3 = starttime3;
    }

    public Date getEndtime3() {
        return endtime3;
    }

    public void setEndtime3(Date endtime3) {
        this.endtime3 = endtime3;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getOrdersplitname() {
        return ordersplitname;
    }

    public void setOrdersplitname(String ordersplitname) {
        this.ordersplitname = ordersplitname == null ? null : ordersplitname.trim();
    }
}