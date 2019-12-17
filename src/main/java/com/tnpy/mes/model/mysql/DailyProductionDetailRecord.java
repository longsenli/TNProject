package com.tnpy.mes.model.mysql;

public class DailyProductionDetailRecord {
    private String id;

    private String plantid;

    private String processid;

    private String lineid;

    private String worklocationid;

    private String materialid;

    private String materialname;

    private Integer planproduction;

    private Integer totalproduction;

    private Integer totalproductiontransition1;

    private Integer totalproductiontransition2;

    private Double accomplishmentratio;

    private String teamtype;

    private String classtype;

    private String daytime;

    private String usedmaterialid;

    private String usedmaterialname;

    private Double usedmaterialnumber1;

    private Double usedmaterialnumber2;

    private Double scrapmaterialnumber1;

    private Double scrapmaterialnumber2;

    private String extend1;

    private String extend2;

    private String extend3;

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

    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid == null ? null : lineid.trim();
    }

    public String getWorklocationid() {
        return worklocationid;
    }

    public void setWorklocationid(String worklocationid) {
        this.worklocationid = worklocationid == null ? null : worklocationid.trim();
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

    public Integer getPlanproduction() {
        return planproduction;
    }

    public void setPlanproduction(Integer planproduction) {
        this.planproduction = planproduction;
    }

    public Integer getTotalproduction() {
        return totalproduction;
    }

    public void setTotalproduction(Integer totalproduction) {
        this.totalproduction = totalproduction;
    }

    public Integer getTotalproductiontransition1() {
        return totalproductiontransition1;
    }

    public void setTotalproductiontransition1(Integer totalproductiontransition1) {
        this.totalproductiontransition1 = totalproductiontransition1;
    }

    public Integer getTotalproductiontransition2() {
        return totalproductiontransition2;
    }

    public void setTotalproductiontransition2(Integer totalproductiontransition2) {
        this.totalproductiontransition2 = totalproductiontransition2;
    }

    public Double getAccomplishmentratio() {
        return accomplishmentratio;
    }

    public void setAccomplishmentratio(Double accomplishmentratio) {
        this.accomplishmentratio = accomplishmentratio;
    }

    public String getTeamtype() {
        return teamtype;
    }

    public void setTeamtype(String teamtype) {
        this.teamtype = teamtype == null ? null : teamtype.trim();
    }

    public String getClasstype() {
        return classtype;
    }

    public void setClasstype(String classtype) {
        this.classtype = classtype == null ? null : classtype.trim();
    }

    public String getDaytime() {
        return daytime;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime == null ? null : daytime.trim();
    }

    public String getUsedmaterialid() {
        return usedmaterialid;
    }

    public void setUsedmaterialid(String usedmaterialid) {
        this.usedmaterialid = usedmaterialid == null ? null : usedmaterialid.trim();
    }

    public String getUsedmaterialname() {
        return usedmaterialname;
    }

    public void setUsedmaterialname(String usedmaterialname) {
        this.usedmaterialname = usedmaterialname == null ? null : usedmaterialname.trim();
    }

    public Double getUsedmaterialnumber1() {
        return usedmaterialnumber1;
    }

    public void setUsedmaterialnumber1(Double usedmaterialnumber1) {
        this.usedmaterialnumber1 = usedmaterialnumber1;
    }

    public Double getUsedmaterialnumber2() {
        return usedmaterialnumber2;
    }

    public void setUsedmaterialnumber2(Double usedmaterialnumber2) {
        this.usedmaterialnumber2 = usedmaterialnumber2;
    }

    public Double getScrapmaterialnumber1() {
        return scrapmaterialnumber1;
    }

    public void setScrapmaterialnumber1(Double scrapmaterialnumber1) {
        this.scrapmaterialnumber1 = scrapmaterialnumber1;
    }

    public Double getScrapmaterialnumber2() {
        return scrapmaterialnumber2;
    }

    public void setScrapmaterialnumber2(Double scrapmaterialnumber2) {
        this.scrapmaterialnumber2 = scrapmaterialnumber2;
    }

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1 == null ? null : extend1.trim();
    }

    public String getExtend2() {
        return extend2;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2 == null ? null : extend2.trim();
    }

    public String getExtend3() {
        return extend3;
    }

    public void setExtend3(String extend3) {
        this.extend3 = extend3 == null ? null : extend3.trim();
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