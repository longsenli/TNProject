package com.tnpy.mes.model.mysql;

public class ObjectRelationDict {
    private String id;

    private String previousobjectid;

    private String nextobjectid;

    private String objecttype;

    private String status;

    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPreviousobjectid() {
        return previousobjectid;
    }

    public void setPreviousobjectid(String previousobjectid) {
        this.previousobjectid = previousobjectid == null ? null : previousobjectid.trim();
    }

    public String getNextobjectid() {
        return nextobjectid;
    }

    public void setNextobjectid(String nextobjectid) {
        this.nextobjectid = nextobjectid == null ? null : nextobjectid.trim();
    }

    public String getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(String objecttype) {
        this.objecttype = objecttype == null ? null : objecttype.trim();
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