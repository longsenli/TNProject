package com.tnpy.mes.model.customize;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @Description: TODO
 * @Author: LLS
 * @Date: 2019/1/5 16:27
 */
public class EquipParamLatestRecord {


    private String equipmentID;

    private String equipName;

    private String equipLocation;

    private  String value;

    private  String status;
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date recordTime;


    private String recorder;

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public String getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(String equipmentID) {
        this.equipmentID = equipmentID;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }



    public String getEquipLocation() {
        return equipLocation;
    }

    public void setEquipLocation(String equipLocation) {
        this.equipLocation = equipLocation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }


}
