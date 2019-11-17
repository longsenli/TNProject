package com.tnpy.mes.model.mysql;

import java.util.Date;

public class DailyProductionSummaryProcess {
    private String id;

    private String plantid;

    private String processid;

    private String materialid;

    private String materialname;

    private Integer production;

    private Integer planproduction;

    private Float finishrate;

    private String classtype1;

    private String classtype2;

    private Date daytime;

    private String extd1;

    private String extd2;

    private String extd3;

    private String remark;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
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

    public Integer getProduction() {
        return production;
    }

    public void setProduction(Integer production) {
        this.production = production;
    }

    public Integer getPlanproduction() {
        return planproduction;
    }

    public void setPlanproduction(Integer planproduction) {
        this.planproduction = planproduction;
    }

    public Float getFinishrate() {
        return finishrate;
    }

    public void setFinishrate(Float finishrate) {
        this.finishrate = finishrate;
    }

    public String getClasstype1() {
        return classtype1;
    }

    public void setClasstype1(String classtype1) {
        this.classtype1 = classtype1 == null ? null : classtype1.trim();
    }

    public String getClasstype2() {
        return classtype2;
    }

    public void setClasstype2(String classtype2) {
        this.classtype2 = classtype2 == null ? null : classtype2.trim();
    }

    public Date getDaytime() {
        return daytime;
    }

    public void setDaytime(Date daytime) {
        this.daytime = daytime;
    }

    public String getExtd1() {
        return extd1;
    }

    public void setExtd1(String extd1) {
        this.extd1 = extd1 == null ? null : extd1.trim();
    }

    public String getExtd2() {
        return extd2;
    }

    public void setExtd2(String extd2) {
        this.extd2 = extd2 == null ? null : extd2.trim();
    }

    public String getExtd3() {
        return extd3;
    }

    public void setExtd3(String extd3) {
        this.extd3 = extd3 == null ? null : extd3.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}