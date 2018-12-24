package com.tnpy.mes.model.mysql;

public class EquipmentParaKey {
    private String equipmenttypeid;

    private String paramid;

    public String getEquipmenttypeid() {
        return equipmenttypeid;
    }

    public void setEquipmenttypeid(String equipmenttypeid) {
        this.equipmenttypeid = equipmenttypeid == null ? null : equipmenttypeid.trim();
    }

    public String getParamid() {
        return paramid;
    }

    public void setParamid(String paramid) {
        this.paramid = paramid == null ? null : paramid.trim();
    }
}