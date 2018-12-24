package com.tnpy.mes.model.mysql;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EquipmentParaRecord extends EquipmentParaRecordKey {
    @JsonProperty(value = "NULL")
    private String value;
    @JsonProperty(value = "NULL")
    private String recorder;
    @JsonProperty(value = "NULL")
    private String equipmenttypeid;
    @JsonProperty(value = "NULL")
    private String picturefile;

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
}