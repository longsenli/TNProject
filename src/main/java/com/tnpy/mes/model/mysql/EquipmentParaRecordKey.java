package com.tnpy.mes.model.mysql;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EquipmentParaRecordKey {
    @JsonProperty(value = "NULL")
    private String id;
    @JsonProperty(value = "NULL")
    private String equipmentid;
    @JsonProperty(value = "NULL")
    private String paramid;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid == null ? null : equipmentid.trim();
    }

    public String getParamid() {
        return paramid;
    }

    public void setParamid(String paramid) {
        this.paramid = paramid == null ? null : paramid.trim();
    }


}