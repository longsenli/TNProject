package com.tnpy.mes.model.mysql;

public class PlanProductionRecord {
    private String id;

    private String materialid;

    private String materialname;

    private String plantid;

    private String processid;

    private Integer planproduction;

    private Integer plandailyproduction;

    private Integer realproduction;

    private Double accomplishmentratio;

    private String planmonth;

    private String operator;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMaterialid() {
        return materialid;
    }

    public void setMaterialid(String materialid) {
        this.materialid = materialid == null ? null : materialid.trim();
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname == null ? null : materialname.trim();
    }

    public String getPlantid() {
        return plantid;
    }

    public void setPlantid(String plantid) {
        this.plantid = plantid == null ? null : plantid.trim();
    }

    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid == null ? null : processid.trim();
    }

    public Integer getPlanproduction() {
        return planproduction;
    }

    public void setPlanproduction(Integer planproduction) {
        this.planproduction = planproduction;
    }

    public Integer getPlandailyproduction() {
        return plandailyproduction;
    }

    public void setPlandailyproduction(Integer plandailyproduction) {
        this.plandailyproduction = plandailyproduction;
    }

    public Integer getRealproduction() {
        return realproduction;
    }

    public void setRealproduction(Integer realproduction) {
        this.realproduction = realproduction;
    }

    public Double getAccomplishmentratio() {
        return accomplishmentratio;
    }

    public void setAccomplishmentratio(Double accomplishmentratio) {
        this.accomplishmentratio = accomplishmentratio;
    }

    public String getPlanmonth() {
        return planmonth;
    }

    public void setPlanmonth(String planmonth) {
        this.planmonth = planmonth == null ? null : planmonth.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}