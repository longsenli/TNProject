package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class EquipmentParaRecordKey {
    @JsonProperty(value = "NULL")
    private String equipmentid;
    @JsonProperty(value = "NULL")
    private String paramid;
    @JsonProperty(value = "NULL")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date recordtime;

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

    public Date getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(Date recordtime) {
        this.recordtime = recordtime;
    }
}