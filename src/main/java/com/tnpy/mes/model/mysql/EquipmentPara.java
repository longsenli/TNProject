package com.tnpy.mes.model.mysql;

public class EquipmentPara extends EquipmentParaKey {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}