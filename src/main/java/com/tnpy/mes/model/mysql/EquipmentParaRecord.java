package com.tnpy.mes.model.mysql;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class EquipmentParaRecord extends EquipmentParaRecordKey {
    @JsonProperty(value = "NULL")
    private String value;
    @JsonProperty(value = "NULL")
    private String recorder;
    @JsonProperty(value = "NULL")
    private String equipmenttypeid;
    @JsonProperty(value = "NULL")
    private String picturefile;
    @JsonProperty(value = "NULL")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date recordtime;

    private String status;
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder == null ? null : recorder.trim();
    }

    public String getEquipmenttypeid() {
        return equipmenttypeid;
    }

    public void setEquipmenttypeid(String equipmenttypeid) {
        this.equipmenttypeid = equipmenttypeid == null ? null : equipmenttypeid.trim();
    }

    public String getPicturefile() {
        return picturefile;
    }

    public void setPicturefile(String picturefile) {
        this.picturefile = picturefile == null ? null : picturefile.trim();
    }
    public Date getRecordtime() {
        return recordtime;
    }

    public void setRecordtime(Date recordtime) {
        this.recordtime = recordtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}