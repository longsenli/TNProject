package com.tnpy.mes.model.mysql;

public class ProcessMaterial {
    private String id;

    private String processid;

    private String materialid;

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

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
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