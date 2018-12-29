package com.tnpy.mes.model.mysql;

public class ProcessMaterial {
    private String id;

    private String processid;

    private String materialtypeid;

    private Integer inorout;

    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid == null ? null : processid.trim();
    }

    public String getMaterialtypeid() {
        return materialtypeid;
    }

    public void setMaterialtypeid(String materialtypeid) {
        this.materialtypeid = materialtypeid == null ? null : materialtypeid.trim();
    }

    public Integer getInorout() {
        return inorout;
    }

    public void setInorout(Integer inorout) {
        this.inorout = inorout;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}