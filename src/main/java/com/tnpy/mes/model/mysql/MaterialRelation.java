package com.tnpy.mes.model.mysql;

public class MaterialRelation {
    private String id;

    private String inmaterialid;

    private String outmaterialid;

    private String proportionality;

    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getInmaterialid() {
        return inmaterialid;
    }

    public void setInmaterialid(String inmaterialid) {
        this.inmaterialid = inmaterialid == null ? null : inmaterialid.trim();
    }

    public String getOutmaterialid() {
        return outmaterialid;
    }

    public void setOutmaterialid(String outmaterialid) {
        this.outmaterialid = outmaterialid == null ? null : outmaterialid.trim();
    }

    public String getProportionality() {
        return proportionality;
    }

    public void setProportionality(String proportionality) {
        this.proportionality = proportionality == null ? null : proportionality.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}