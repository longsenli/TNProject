package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class Batchrelationcontrol {
    private String id;

    private String tbbatch;

    private String relationorderid;

    private String status;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date relationtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTbbatch() {
        return tbbatch;
    }

    public void setTbbatch(String tbbatch) {
        this.tbbatch = tbbatch == null ? null : tbbatch.trim();
    }

    public String getRelationorderid() {
        return relationorderid;
    }

    public void setRelationorderid(String relationorderid) {
        this.relationorderid = relationorderid == null ? null : relationorderid.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getRelationtime() {
        return relationtime;
    }

    public void setRelationtime(Date relationtime) {
        this.relationtime = relationtime;
    }
}