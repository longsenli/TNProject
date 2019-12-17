package com.tnpy.mes.model.mysql;

public class DailyProcessUsedMaterialRecord {
    private String id;

    private String plantid;

    private String processid;

    private String lineid;

    private String worklocationid;

    private String teamtype;

    private String classtype;

    private String daytime;

    private String usedmaterialid;

    private String usedmaterialname;

    private Double usedmaterialnumber1;

    private Double usedmaterialnumber2;

    private Double usedmaterialnumber3;

    private Integer lastclasssurplus;

    private Integer thisclasssurplus;

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

    public Double getUsedmaterialnumber3() {
        return usedmaterialnumber3;
    }

    public void setUsedmaterialnumber3(Double usedmaterialnumber3) {
        this.usedmaterialnumber3 = usedmaterialnumber3;
    }

    public Integer getLastclasssurplus() {
        return lastclasssurplus;
    }

    public void setLastclasssurplus(Integer lastclasssurplus) {
        this.lastclasssurplus = lastclasssurplus;
    }

    public Integer getThisclasssurplus() {
        return thisclasssurplus;
    }

    public void setThisclasssurplus(Integer thisclasssurplus) {
        this.thisclasssurplus = thisclasssurplus;
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